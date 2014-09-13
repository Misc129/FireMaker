package misc.scripts.firemaker.workers;

import org.hexbot.api.methods.Calculations;
import org.hexbot.api.methods.Walking;
import org.hexbot.api.methods.helper.Bank;
import org.hexbot.api.methods.node.Inventory;
import org.hexbot.api.util.Time;
import org.hexbot.api.wrapper.Tile;
import org.hexbot.api.wrapper.node.InventoryItem;
import org.hexbot.core.concurrent.script.Worker;

public class DoBank extends Worker {

	public static final Tile TILE_BANK = new Tile(3185,3436,0);
	
	@Override
	public void run() {
		System.out.println("Do bank");
		if(Calculations.distanceTo(TILE_BANK) > 5){
			Walking.walk(TILE_BANK);
			Time.sleep(1000);
			return;
		}
		
		if(!Bank.isOpen()){
			Bank.open();
			Time.sleep(1000);
		}
		
		if(hasOtherItems() && Bank.depositAll()){
			Time.sleep(1500);
		}
		
		if(Inventory.getCount(DoBurn.ID_TINDERBOX) < 1 && Bank.withdraw(DoBurn.ID_TINDERBOX, 1)){
			Time.sleep(1500);
		}
		
		if(Inventory.getCount(DoBurn.ID_TINDERBOX) > 0 && Bank.withdraw(DoBurn.ID_LOG, 27)){
			Time.sleep(1500);
		}
		
	}

	@Override
	public boolean validate() {
		return Inventory.getCount(DoBurn.ID_LOG) < 1;
	}
	
	private boolean hasOtherItems(){
		for(InventoryItem item : Inventory.getAll()){
			if(item.getId() != DoBurn.ID_LOG && item.getId() != DoBurn.ID_TINDERBOX){
				return true;
			}
		}
		return false;
	}
	
	private boolean validateInventory(){
		return Inventory.getCount(DoBurn.ID_LOG) > 0 && Inventory.getCount(DoBurn.ID_TINDERBOX) == 1;
	}

}
