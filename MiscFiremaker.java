package misc.scripts.firemaker;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.util.LinkedList;

import misc.scripts.firemaker.workers.DoBank;
import misc.scripts.firemaker.workers.DoBurn;
import misc.scripts.firemaker.workers.ToBurnTile;

import org.hexbot.api.listeners.MessageEvent;
import org.hexbot.api.listeners.MessageListener;
import org.hexbot.api.listeners.Paintable;
import org.hexbot.api.methods.input.Mouse;
import org.hexbot.api.methods.input.Mouse.Speed;
import org.hexbot.api.util.Time;
import org.hexbot.core.concurrent.script.Condition;
import org.hexbot.core.concurrent.script.Info;
import org.hexbot.core.concurrent.script.TaskScript;
import org.hexbot.core.concurrent.script.Type;

@Info(
		name = "MiscFiremaker",
		author = "Misc",
		description = "v1",
		type = Type.FIREMAKING
		)
public class MiscFiremaker extends TaskScript implements Paintable{

	long timeStart, timeLastRemovePoint, timeNextRemovePoint;
	int innerOrbitAngle = 0, innerOrbitAngle2 = 180, outerOrbitAngle = 90, outerOrbitAngle2 = 270;
	
	private static LinkedList<Point> points = new LinkedList<Point>();
	
	public MiscFiremaker(){
		timeStart = timeLastRemovePoint = timeNextRemovePoint = System.currentTimeMillis();
		Mouse.setSpeed(Speed.FAST);
		Mouse.setSpeed(Speed.NORMAL);
		submit(new ToBurnTile(), new DoBurn(), new DoBank());
	}
	
	public static void waitFor(Condition condition, int timeout){
		for(int i = 0; i < timeout; i += 100){
			if(condition.validate())
				return;
			Time.sleep(100);
		}
	}
	
	private void updatePoints(){
		if(points.isEmpty()){
			points.add(0,Mouse.getLocation());
			timeNextRemovePoint = System.currentTimeMillis() + 2000;
		}
		else if(!Mouse.getLocation().equals(points.get(0))){
			points.add(0,Mouse.getLocation());
		}
		if(!points.isEmpty() && System.currentTimeMillis() > timeNextRemovePoint){
			timeLastRemovePoint = timeNextRemovePoint;
			timeNextRemovePoint = System.currentTimeMillis() + 30;
			points.removeLast();
		}
	}
	
	private void drawPoints(Graphics g){
		if(points.size() > 1){
			Point prev = points.getFirst();
			for(Point p : points){
				if(!prev.equals(p))
					g.drawLine(prev.x, prev.y, p.x, p.y);
				prev = p;
			}
		}
	}
	
	private void updateOrbits(){
		if(innerOrbitAngle >= 360)
			innerOrbitAngle = 0;
		if(innerOrbitAngle2 >= 360)
			innerOrbitAngle2 = 0;
		if(outerOrbitAngle >= 360)
			outerOrbitAngle = 0;
		if(outerOrbitAngle2 >= 360)
			outerOrbitAngle2 = 0;
		innerOrbitAngle+=2;
		innerOrbitAngle2+=2;
		outerOrbitAngle-=2;
		outerOrbitAngle2-=2;
	}
	
	private void drawCursorOrbit(Graphics g){
		g.setColor(Color.green);
		g.drawArc(Mouse.getX() - 10, Mouse.getY() - 10, 20, 20, innerOrbitAngle, 135);
		g.drawArc(Mouse.getX() - 10, Mouse.getY() - 10, 20, 20, innerOrbitAngle2, 135);
		g.drawArc(Mouse.getX() - 15, Mouse.getY() - 15, 30, 30, outerOrbitAngle, 135);
		g.drawArc(Mouse.getX() - 15, Mouse.getY() - 15, 30, 30, outerOrbitAngle2, 135);
		
	}

	private void drawMouse(Graphics g){
		g.setColor(Color.red);
		g.fillRect(Mouse.getX() - 1, Mouse.getY() - 5, 2, 10);
		g.fillRect(Mouse.getX() - 5, Mouse.getY() - 1, 10, 2);
	}
	
	@Override
	public void paint(Graphics g) {
		drawMouse(g);
		
		updatePoints();
		drawPoints(g);
		
		updateOrbits();
		drawCursorOrbit(g);
		
	}

}
