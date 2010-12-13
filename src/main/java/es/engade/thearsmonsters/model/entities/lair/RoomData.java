package es.engade.thearsmonsters.model.entities.lair;

import java.io.Serializable;

import es.engade.thearsmonsters.model.entities.monster.Monster;
import es.engade.thearsmonsters.model.entities.monster.enums.MonsterAge;
import es.engade.thearsmonsters.model.entities.room.Room;
import es.engade.thearsmonsters.model.entities.room.enums.RoomType;
import es.engade.thearsmonsters.model.util.Format;

/**
 * Encapsulate specific rooms data.
 * It is attributes that depends on concrete room actions, but in the 
 * database are stored in the Lair table because they are needed
 * only for one specific purpose.
 * An example is the lastChangeResourcesTurn, used for check that
 * only one resources (garbage-money) change can be made per day.
 *
 */
public class RoomData implements Serializable {

	private static final long serialVersionUID = 200911261651L;

    private int vitalSpaceOccupied;
    
    public RoomData(int vitalSpaceOccupied) {
        this.vitalSpaceOccupied = vitalSpaceOccupied;
    }
    
    /**
     * Constructor with default values (when the lair is first time created for a new user).
     */
    public RoomData() {
    	this(0);
    }
	

	
	
	/* ---- TradeOffice dependent methods ---- */
    
	private Room getTradeOffice(Lair lair) {
		return lair.getRoom(RoomType.TradeOffice);
	}
	
	/**
	 * When change garbage for money, the max quantity allowed.
	 * @throws RuntimeException if the TradeOffice is not in the lair (take care).
	 */
	public int getChangeResourcesMaxGarbageAmountEnabled(Lair lair) {
		Room tradeOffice = getTradeOffice(lair);
		if(tradeOffice != null) {
			int freeMoneyStorageCapacity = getMoneyStorageCapacity(lair) - lair.getMoney();
			int garbageNeededToFillTheTradeOffice = (freeMoneyStorageCapacity * 100 / (100 - getPercentageCommision(lair))) + 1;
			return Math.min(lair.getGarbage(), garbageNeededToFillTheTradeOffice);
			
		} else {
			throw new RuntimeException("Change resources is not possible without " +
					"TradeOffice. At lair of '"+ lair.getUser().getLogin() +"'");
		}
	}
	
	/**
	 * When change money for garbage, the max quantity allowed.
	 * @throws RuntimeException if the TradeOffice is not in the lair (take care).
	 */
    public int getChangeResourcesMaxMoneyAmountEnabled(Lair lair) {
		Room tradeOffice = getTradeOffice(lair);
		if(tradeOffice != null) {
			int freeGarbageStorageCapacity = getGarbageStorageCapacity(lair) - lair.getGarbage();
			int moneyNeededToFillTheWarehouse = (freeGarbageStorageCapacity * 100 / (100 - getPercentageCommision(lair))) + 1;
			return Math.min(lair.getMoney(), moneyNeededToFillTheWarehouse);
			
		} else {
			throw new RuntimeException("Change resources is not possible without " +
					"TradeOffice. At lair of '"+ lair.getUser().getLogin() +"'");
		}
    }
	
	/**
	 * Max amount of money that this lair can store.
	 */
	public int getMoneyStorageCapacity(Lair lair) {
		Room tradeOffice = getTradeOffice(lair);
		return getMoneyStorageCapacityByLevel(lair, (tradeOffice == null) ? 0 : tradeOffice.getLevel());
	}
	
	public int getMoneyStorageCapacityByLevel(Lair lair, int tradeOfficeLevel) {
		Room tradeOffice = getTradeOffice(lair);
		if((tradeOffice == null) || tradeOfficeLevel <= 0) {
			return 0; // While there is no warehouse, no garbage can be stored
		} else {
			switch (tradeOfficeLevel) {
				case 1: return 50;
				case 2: return 100;
				case 3: return 200;
				case 4: return 800;
				case 5: return 1600;
				case 6: return 3200;
				case 7: return 4000;
				case 8: return 5000;
				case 9: return 10000;
				case 10: return 15000;
				default: return 0; // should never happen
			}
		}
	}
	
	/**
	 * Percentage commission (a number between 0 and 100)
	 * depends on the level of the trade Office.
	 */
	public int getPercentageCommision(Lair lair) {
		Room tradeOffice = getTradeOffice(lair);
		return getPercentageCommisionByLevel(lair, (tradeOffice == null) ? 0 : tradeOffice.getLevel());
	}
	
	/**
	 * Percentage commission for each level of the TradeOffice.
	 */
	public int getPercentageCommisionByLevel(Lair lair, int tradeOfficeLevel) {
		Room tradeOffice = getTradeOffice(lair);
		if(tradeOffice == null) return 100;
		switch (tradeOfficeLevel) {
			case 1: return 50;
			case 2: return 45;
			case 3: return 35;
			case 4: return 30;
			case 5: return 25;
			case 6: return 20;
			case 7: return 15;
			case 8: return 10;
			case 9: return 5;
			default: return 1;
		}
	}
	
	
	/* ---- Warehouse dependent methods ---- */
	
	private Room getWarehouse(Lair lair) {
		return lair.getRoom(RoomType.Warehouse);
	}
	
	/**
	 * Amount of garbage that can be stored in this lair.
	 * Depends on the Warehouse level.
	 */
	public int getGarbageStorageCapacity(Lair lair) {
		Room warehouse = getWarehouse(lair);
		return getGarbageStorageCapacityByLevel(lair, (warehouse == null) ? 0 : warehouse.getLevel());
	}
	public int getGarbageStorageCapacityByLevel(Lair lair, int warehouseLevel) {
		Room warehouse = getWarehouse(lair);
		if((warehouse == null) || warehouseLevel <= 0) {
			return 0; // While there is no warehouse, no garbage can be stored
		} else {
			switch(warehouseLevel) {
				case 1: return 50;
				case 2: return 150;
				default: return warehouse.getGarbageUpgradeWhenLevel(warehouseLevel) * 8; // need always to be more than garbageUpgrade
			}
		}
	}
	
	
	/* ---- Dormitories dependent methods -----*/
	
	private Room getDormitories(Lair lair) {
		return lair.getRoom(RoomType.Dormitories);
	}
	

	/**
	 * Amount of vital space used by monsters.
	 */
    public int getVitalSpaceOccupied() {
    	return vitalSpaceOccupied;
    }
    
	/**
	 * Total amount of vital space: Dormitories.level * 10
	 */
	public int getVitalSpace(Lair lair) {
		Room dormitories = getDormitories(lair); 
		if(dormitories != null) {
			return dormitories.getLevel() * 10;
		} else {
			return 0; // While there is no dormitories, there is no vitalSpace too
		}
	}

	/**
	 * Given all the monsters of this lair, recalculate the vital Space.
	 * @return true if the occupied vital space was changed, false if still be the same.
	 */
	public boolean refreshVitalSpaceOccupied(Lair lair) {
		int newOccupiedVitalSpace = 0;
		for(Monster m : lair.getMonsters()) {
			if (!m.getAge().equals(MonsterAge.Dead)) {
				newOccupiedVitalSpace += m.getRace().getVitalSpace();
			}
		}
		if(newOccupiedVitalSpace == this.vitalSpaceOccupied) {
			return false;
		} else {
			this.vitalSpaceOccupied = newOccupiedVitalSpace;
			return true;
		}
    }
	
	/**
	 * The rest of vital space available (vitalSpace - vitalSpaceOcuppied)
	 */
	public int getVitalSpaceFree(Lair lair) {
		return getVitalSpace(lair) - getVitalSpaceOccupied();
	}

	
	
	
	//---- Object overrides ----//
	
	@Override
    public String toString() {
		return Format.p(this.getClass(), new Object[]{
			"vitalSpaceOccupied", vitalSpaceOccupied
		});
	}

	@Override
    public int hashCode() {
	    final int prime = 31;
	    int result = 1;
	    result = prime * result + vitalSpaceOccupied;
	    return result;
    }

	@Override
    public boolean equals(Object obj) {
	    if (this == obj)
		    return true;
	    if (obj == null)
		    return false;
	    if (getClass() != obj.getClass())
		    return false;
	    RoomData other = (RoomData) obj;
	    if (vitalSpaceOccupied != other.vitalSpaceOccupied)
		    return false;
	    return true;
    }
	

}
