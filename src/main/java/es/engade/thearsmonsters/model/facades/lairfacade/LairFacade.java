package es.engade.thearsmonsters.model.facades.lairfacade;

import es.engade.thearsmonsters.model.entities.lair.Lair;
import es.engade.thearsmonsters.model.entities.room.enums.RoomType;
import es.engade.thearsmonsters.model.facades.lairfacade.exception.InWorksActionException;
import es.engade.thearsmonsters.model.facades.lairfacade.exception.IncorrectAddressException;
import es.engade.thearsmonsters.model.facades.lairfacade.exception.InsuficientGarbageException;
import es.engade.thearsmonsters.model.facades.lairfacade.exception.InsuficientMoneyException;
import es.engade.thearsmonsters.model.facades.lairfacade.exception.OnlyOneChangePerGameDayException;
import es.engade.thearsmonsters.model.facades.lairfacade.exception.TradeOfficeFullStorageException;
import es.engade.thearsmonsters.model.facades.lairfacade.exception.WarehouseFullStorageException;
import es.engade.thearsmonsters.util.exceptions.InstanceNotFoundException;
import es.engade.thearsmonsters.util.exceptions.InternalErrorException;

/**
 * A facade to model the interaction of the web application with the lair.
 */
public interface LairFacade {
    
	/**
	 * Find a lair with all its rooms by lair Id.
	 */
    public Lair findLair(String lairId) 
    	throws InstanceNotFoundException, InternalErrorException;
    
	/**
	 * Find a lair with all its rooms by login.
	 */
    public Lair findLairByLogin(String login) 
    	throws InstanceNotFoundException, InternalErrorException;   
    
	/**
	 * Find a lair with all its rooms by address.
	 */
    public Lair findLairByAddress(int street, int building, int floor) 
		throws InstanceNotFoundException, InternalErrorException, IncorrectAddressException;
    
    /**
     * Get a block of lairs, localized at (street, building) coordinates.
     */
    public BuildingChunk findBuilding(int street, int building) 
		throws InternalErrorException, IncorrectAddressException;
    
    /**
     * Builds a new Room in the lair at initial state (level 0, size 1, EnlargingState)
     * @param lair may be the session myLair LairVO instance.
     * 		Must contains the rooms and its state will be updated.
     * @param roomType reference the room included in lair.getRooms.
     * @throws InWorksActionException if the roomType is not allowed for build in this lair.
     * @throws NoRoomsLoadedException if the lair have not its rooms loaded
     */
    public void createNewRoom(Lair lair, RoomType roomType)
    	throws InWorksActionException, InternalErrorException, InsuficientGarbageException;
    	    
    /**
     * Set the room's state to upgrading level and store the new state in the database.
     * Then the player will can put builders inside the room.
     * @param lair may be the session myLair Lair instance.
     * 		Must contains the rooms and its state will be updated.
     * @param roomType reference the room included in lair.getRooms.
     * @throws InWorksActionException if the room can not be upgraded.
     * @throws InstanceNotFoundException if the lair do not contains the room of the specified RoomType.
     */
    public void setRoomUpgradingInWorksState(Lair lair, RoomType roomType)
		throws InWorksActionException, InstanceNotFoundException, InsuficientGarbageException, InternalErrorException;
    
    /**
     * Similar to setRoomUpgradingInWorksState. The effort done will be lost, but the garbage
     * will be restored.
     * @see setRoomUpgradingInWorksState(Lair lair, RoomType roomType)
     */
    public void cancelWorks(Lair lair, RoomType roomType)
		throws InWorksActionException, InternalErrorException, InstanceNotFoundException;
    
    /**
     * Change resources: garbage for money or viceversa.
     * @param money_or_garbage type of change, can be "money" (spent money and change it for garbage)
     * 		or "garbage" (the reverse)
     * @return the amount of the other resource gained (is the arg amount less the commission).
     * @throws WarehouseFullStorageException if the garbage obtained on the change is greather than the Warehouse storage capacity
     * @throws TradeOfficeFullStorageException if the money obtained on the change is greather than the TradeOffice storage capacity
     * @throws InsuficientGarbageException if money_or_garbage=="garbage" and amount is greather than current garbage in the lair
     * @throws InsuficientMoneyException if money_or_garbage=="money" and amount is greather than current money in the lair
     */
    public int changeResources(Lair lair, String money_or_garbage, int amount)
    	throws WarehouseFullStorageException, TradeOfficeFullStorageException, InsuficientGarbageException, InsuficientMoneyException, OnlyOneChangePerGameDayException, InternalErrorException;
    
    /**
     * Gets a sorted block of Lair info objects
     * 
     * @param startIndex the first element to retrieve
     * @param count the number of elements
     * @return
     */
    public LairRankingInfoChunk getLairsRanking(int startIndex, int count);

}
