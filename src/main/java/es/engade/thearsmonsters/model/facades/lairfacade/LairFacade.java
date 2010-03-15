package es.engade.thearsmonsters.model.facades.lairfacade;


import es.engade.thearsmonsters.model.entities.common.Id;
import es.engade.thearsmonsters.model.entities.lair.Lair;
import es.engade.thearsmonsters.model.entities.lair.exceptions.NoRoomsLoadedException;
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
    public Lair findLair(Id lairId) 
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
     * Get a building set of some lairs, without their rooms.
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
    	throws InWorksActionException, InternalErrorException, NoRoomsLoadedException, InsuficientGarbageException;
    	    
    /**
     * Set the room's state to upgrading level and store the new state in the database.
     * Then the player will can put builders inside the room.
     * @param lair may be the session myLair LairVO instance.
     * 		Must contains the rooms and its state will be updated.
     * @param roomType reference the room included in lair.getRooms.
     * @throws InWorksActionException if the room is in enlarging size state or
     * 		if simply that room can not be upgraded.
     * @throws InstanceNotFoundException if the lair do not contains the room of the specified RoomType.
     * @throws NoRoomsLoadedException if the lair have not its rooms loaded
     */
    public void setRoomUpgradingInWorksState(Lair lair, RoomType roomType)
		throws InWorksActionException, InternalErrorException, InstanceNotFoundException, NoRoomsLoadedException, InsuficientGarbageException;
    
    /**
     * Similar to setRoomUpgradingInWorksState.
     * @see setRoomUpgradingInWorksState(LairVO lair, RoomType roomType)
     */
    public void setRoomEnlargingInWorksState(Lair lair, RoomType roomType)
		throws InWorksActionException, InternalErrorException, InstanceNotFoundException, NoRoomsLoadedException, InsuficientGarbageException;
    
    /**
     * Similar to setRoomUpgradingInWorksState. The effort done will be lost, but the garbage
     * will be restored.
     * @see setRoomUpgradingInWorksState(LairVO lair, RoomType roomType)
     */
    public void cancelWorks(Lair lair, RoomType roomType)
		throws InWorksActionException, InternalErrorException, InstanceNotFoundException, NoRoomsLoadedException;
    
    /**
     * changeResources("money", amount, lair) spent the specified amount of money and charge
     * the corresponding garbage in the lair (less the commission, depending on TradeOffice level).<br/>
     * With "garbage" do de same but changing garbage for money.
     * @return the amount of the other resource gained (is the arg amount less the commission).
     * @throws WarehouseFullStorageException if the garbage obtained on the change is greather than the Warehouse storage capacity
     * @throws TradeOfficeFullStorageException if the money obtained on the change is greather than the TradeOffice storage capacity
     * @throws InsuficientGarbageException if money_or_garbage=="garbage" and amount is greather than current garbage in the lair
     * @throws InsuficientMoneyException if money_or_garbage=="money" and amount is greather than current money in the lair
     */
    public int changeResources(String money_or_garbage, int amount, Lair lair)
    	throws NoRoomsLoadedException, WarehouseFullStorageException, TradeOfficeFullStorageException, InsuficientGarbageException, InsuficientMoneyException, OnlyOneChangePerGameDayException, InternalErrorException;
    

}
