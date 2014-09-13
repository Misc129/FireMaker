package misc.scripts.firemaker.workers;

import misc.scripts.firemaker.MiscFiremaker;

import org.hexbot.api.methods.interactable.GameObjects;
import org.hexbot.api.methods.interactable.Players;
import org.hexbot.api.methods.node.Inventory;
import org.hexbot.api.util.Filter;
import org.hexbot.api.util.Time;
import org.hexbot.api.wrapper.Tile;
import org.hexbot.api.wrapper.interactable.GameObject;
import org.hexbot.api.wrapper.node.InventoryItem;
import org.hexbot.core.concurrent.script.Condition;
import org.hexbot.core.concurrent.script.Worker;

public class DoBurn extends Worker{

	public static final int ID_LOG = 1519;
	public static final int ID_TINDERBOX = 590;
	public static final int ID_FIRE = 24919;
	public static final int ANIMATION_FIREMAKING = 733;
	
	@Override
	public void run() {
		System.out.println("Do burn");
		
		InventoryItem logs = Inventory.getItem(ID_LOG);
		InventoryItem tinderbox = Inventory.getItem(ID_TINDERBOX);
		
		final Tile start = Players.getLocal().getLocation();
		if(logs != null && tinderbox != null){
			if(logs.interact("Use")){
				Time.sleep(1000);
				while(Players.getLocal().getAnimation() == ANIMATION_FIREMAKING){
					Time.sleep(1500);
				}
				if(tinderbox.interact("Use")){
					Time.sleep(2000);
				}
			}
		}
	}

	@Override
	public boolean validate() {
		return canBurn() && Inventory.getCount(ID_LOG) > 0;
	}

	public static boolean canBurn(){
		if(hasFire(Players.getLocal().getLocation()))
			return false;
		return Players.getLocal().getY() >= ToBurnTile.MIN_Y && Players.getLocal().getLocation().getY() <= ToBurnTile.MAX_Y;
	}
	
	public static boolean hasFire(final Tile t){
		GameObject[] objects = GameObjects.getLoaded(new Filter<GameObject>(){
			@Override
			public boolean accept(GameObject arg0) {
				return arg0.getLocation().equals(t);
			}});
		for(GameObject object : objects){
			if(object.getId() == ID_FIRE)
				return true;
		}
		return false;
	}
	
}
