package misc.scripts.firemaker.workers;

import org.hexbot.api.methods.Calculations;
import org.hexbot.api.methods.Walking;
import org.hexbot.api.methods.node.Inventory;
import org.hexbot.api.util.Time;
import org.hexbot.api.wrapper.Tile;
import org.hexbot.core.concurrent.script.Worker;

public class ToBurnTile extends Worker{

	public static final int MAX_Y = 3432;
	public static final int MIN_Y = 3428;
	
	public static final Tile[] TILES_START_BURNING = {
		new Tile(3198,3432,0),
		new Tile(3198,3431,0),
		new Tile(3198,3430,0),
		new Tile(3198,3429,0),
		new Tile(3198,3428,0),
	};

	@Override
	public void run() {
		System.out.println("To burn tile");
		if(Inventory.getCount(DoBurn.ID_LOG) == 27 && Calculations.distanceTo(TILES_START_BURNING[0]) > 10){
			Walking.walk(TILES_START_BURNING[0]);
			Time.sleep(1000);
			return;
		}
		
		// TODO use array copy
		Tile tile1 = TILES_START_BURNING[0];
		Tile tile2 = TILES_START_BURNING[1];
		Tile tile3 = TILES_START_BURNING[2];
		Tile tile4 = TILES_START_BURNING[3];
		Tile result = null;
		while(true){
			System.out.println(tile1.toString());
			System.out.println(tile2.toString());
			System.out.println(tile3.toString());
			System.out.println(tile4.toString());
			if(!DoBurn.hasFire(tile1) && tile1.canReach()){
				result = tile1;
				break;
			}
			if(!DoBurn.hasFire(tile2) && tile2.canReach()){
				result = tile2;
				break;
			}
			if(!DoBurn.hasFire(tile3) && tile3.canReach()){
				result = tile3;
				break;
			}
			if(!DoBurn.hasFire(tile4) && tile4.canReach()){
				result = tile4;
				break;
			}
			tile1 = tile1.derive(-1, 0);
			tile2 = tile2.derive(-1, 0);
			tile3 = tile3.derive(-1, 0);
			tile4 = tile4.derive(-1, 0);
		}
		if(result != null && Walking.walk(result)){
			Time.sleep(1000);	
		}
	}

	@Override
	public boolean validate() {
		return !DoBurn.canBurn() && Inventory.getCount(DoBurn.ID_LOG) > 0;
	}

	
	
}
