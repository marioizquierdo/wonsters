package es.engade.thearsmonsters.model.entities.lair;

import java.io.Serializable;
import java.util.Date;

import es.engade.thearsmonsters.model.entities.monster.Monster;
import es.engade.thearsmonsters.model.entities.room.Room;
import es.engade.thearsmonsters.model.entities.room.enums.RoomType;
import es.engade.thearsmonsters.model.facades.lairfacade.exception.OnlyOneChangePerGameDayException;
import es.engade.thearsmonsters.model.util.DateTools;
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

	private Date lastChangeResourcesDate;
    private int vitalSpaceOccupied;
	private Lair lair;
    
    public RoomData(Date lastChangeResourcesDate, int vitalSpaceOccupied) {
    	this.lair = null; // hay que fijarlo con setLair();
        this.lastChangeResourcesDate = lastChangeResourcesDate;
        this.vitalSpaceOccupied = vitalSpaceOccupied;
    }
    
    /**
     * Constructor with default values (when the lair is first time created for a new user).
     */
    public RoomData() {
    	this(DateTools.yesterday(), 0);
    }
    
    
	public void setLair(Lair lair) {
    	this.lair = lair;
    }
	

	
	
	/* ---- TradeOffice dependent methods ---- */
    
	private Room getTradeOffice() {
		return lair.getRoom(RoomType.TradeOffice);
	}
    
	public Date getLastChangeResourcesDate() {
		return lastChangeResourcesDate;
	}

	private void setLastChangeResourcesDate(Date newDate) {
		lastChangeResourcesDate = newDate;
	}

	public void setLastChangeResourcesDateToNow() {
		setLastChangeResourcesDate(DateTools.now());
	}
	
	public boolean isReadyToChangeResources() {
		// Esta preparado para hacer otro cambio de recursos si
		// el último cambio se hizo ayer o antes.
		return lastChangeResourcesDate.before(DateTools.yesterday());
	}
	
	/**
	 * When change garbage for money, the max quantity allowed.
	 * @throws NullPointerException if the TradeOffice is not in the lair (take care).
	 */
	public int getChangeResourcesMaxGarbageAmountEnabled() {
		Room tradeOffice = getTradeOffice();
		if(tradeOffice != null) {
			int freeMoneyStorageCapacity = getMoneyStorageCapacity() - lair.getMoney();
			int garbageNeededToFillTheTradeOffice = (freeMoneyStorageCapacity * 100 / (100 - getPercentageCommision())) + 1;
			return Math.min(lair.getGarbage(), garbageNeededToFillTheTradeOffice);
			
		} else {
			throw new RuntimeException("Change resources is not possible without " +
					"TradeOffice. At lair of '"+ lair.getUser().getLogin() +"'");
		}
	}
	
	/**
	 * When change money for garbage, the max quantity allowed.
	 * @throws NullPointerException if the TradeOffice is not in the lair (take care).
	 */
    public int getChangeResourcesMaxMoneyAmountEnabled() {
		Room tradeOffice = getTradeOffice();
		if(tradeOffice != null) {
			int freeGarbageStorageCapacity = getGarbageStorageCapacity() - lair.getGarbage();
			int moneyNeededToFillTheWarehouse = (freeGarbageStorageCapacity * 100 / (100 - getPercentageCommision())) + 1;
			return Math.min(lair.getMoney(), moneyNeededToFillTheWarehouse);
			
		} else {
			throw new RuntimeException("Change resources is not possible without " +
					"TradeOffice. At lair of '"+ lair.getUser().getLogin() +"'");
		}
    }
	
	
	/**
	 * Little hard to read, but this method is useful in the view because
	 * the exception can create its own localized message (implements LocalizableMessage),
	 * and is better to call this and check if the returned value is null, otherwise
	 * print the error message, than simply look at isReadyToChangeResources().
	 * @return the exception if the change can not be made or null otherwise
	 */
	public OnlyOneChangePerGameDayException getOnlyOneChangePerGameDayException() {
		if(!isReadyToChangeResources()) {
			return new OnlyOneChangePerGameDayException(lastChangeResourcesDate, lair.getUser().getLogin());
		} else {
			return null;
		}
	}
	
	/**
	 * Max amount of money that this lair can store.
	 */
	public int getMoneyStorageCapacity() {
		return getMoneyStorageCapacityByLevel(getTradeOffice());
	}
	
	private int getMoneyStorageCapacityByLevel(Room tradeOffice) {
		if(tradeOffice != null) {
			return tradeOffice.getGarbageUpgrade() * 4; // need always to be more than garbageUpgrade
		} else {
			return 0; // While there is no tradeOffice, no money can be stored
		}
	}
	
	/**
	 * Percentage commission (a number between 0 and 100)
	 * depends on the level of the trade Office.
	 */
	public int getPercentageCommision() {
		return getPercentageCommisionByLevel(getTradeOffice()); 
	}
	
	/**
	 * Percentage commission for each level of the TradeOffice.
	 */
	private int getPercentageCommisionByLevel(Room tradeOffice) {
		if(tradeOffice == null) return 100;
		switch (tradeOffice.getLevel()) {
		case 1: return 50;
		case 2: return 45;
		case 3: return 35;
		case 4: return 30;
		case 5: return 25;
		case 6: return 20;
		case 7: return 15;
		case 8: return 10;
		case 9: return 5;
		default: return 0;
		}
	}
	
	
	/* ---- Warehouse dependent methods ---- */
	
	private Room getWarehouse() {
		return lair.getRoom(RoomType.Warehouse);
	}
	
	/**
	 * Amount of garbage that can be stored in this lair.
	 * Depends on the Warehouse level.
	 */
	public int getGarbageStorageCapacity() {
		return getGarbageStorageCapacityByLevel(getWarehouse());
	}
	
	private int getGarbageStorageCapacityByLevel(Room warehouse) {
		if(warehouse == null || warehouse.getLevel() <= 0) {
			return 0; // While there is no warehouse, no garbage can be stored
		} else {
			return warehouse.getGarbageUpgradeWhenLevel(warehouse.getLevel() + 1) * 10; // need always to be more than garbageUpgrade
		}
	}
	
	
	/* ---- Dormitories dependent methods -----*/
	
	private Room getDormitories() {
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
	public int getVitalSpace() {
		Room dormitories = getDormitories(); 
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
	public boolean refreshVitalSpaceOccupied() {
		int newOccupiedVitalSpace = 0;
		for(Monster m : lair.getMonsters()) {
			newOccupiedVitalSpace += m.getRace().getVitalSpace();
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
	public int getVitalSpaceFree() {
		return getVitalSpace() - getVitalSpaceOccupied();
	}

	
	
	
	//---- Object overrides ----//
	
	@Override
    public String toString() {
		return Format.p(this.getClass(), new Object[]{
		    "lastChangeResourcesDate", lastChangeResourcesDate,
			"vitalSpaceOccupied", vitalSpaceOccupied
		});
	}
	
	@Override
    public int hashCode() {
	    final int prime = 31;
	    int result = 1;
	    result = prime * result + ((lair == null) ? 0 : lair.hashCode());
	    result = prime
	            * result
	            + ((lastChangeResourcesDate == null) ? 0
	                    : lastChangeResourcesDate.hashCode());
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
	    if (lair == null) {
		    if (other.lair != null)
			    return false;
	    } else if (!lair.equals(other.lair))
		    return false;
	    if (lastChangeResourcesDate == null) {
		    if (other.lastChangeResourcesDate != null)
			    return false;
	    } else if (!lastChangeResourcesDate
	            .equals(other.lastChangeResourcesDate))
		    return false;
	    if (vitalSpaceOccupied != other.vitalSpaceOccupied)
		    return false;
	    return true;
    }
	

}
