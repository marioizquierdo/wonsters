package es.engade.thearsmonsters.model.entities.lair;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.jdo.annotations.Element;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;

import es.engade.thearsmonsters.model.entities.egg.MonsterEgg;
import es.engade.thearsmonsters.model.entities.monster.Monster;
import es.engade.thearsmonsters.model.entities.room.Room;
import es.engade.thearsmonsters.model.entities.room.enums.RoomType;
import es.engade.thearsmonsters.model.entities.user.User;
import es.engade.thearsmonsters.model.facades.lairfacade.exception.IncorrectAddressException;
import es.engade.thearsmonsters.model.facades.lairfacade.exception.OnlyOneChangePerGameDayException;
import es.engade.thearsmonsters.model.util.Format;
import es.engade.thearsmonsters.model.util.GameConf;

@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class Lair implements Serializable {

    private static final long serialVersionUID = 20100305L;
    
    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Key id;
    
    @Persistent
    private int money;
    
    @Persistent
    private int garbage;
    
    @Persistent
    private int addressStreet;
    
    @Persistent
    private int addressBuilding;
    
    @Persistent
    private int addressFloor;
    
    @Persistent(mappedBy = "lair",defaultFetchGroup="true")
    private User user;
    
    @Persistent(serialized="true",defaultFetchGroup="true")
    private RoomData roomData;
    
    @Persistent(mappedBy = "lair",defaultFetchGroup="true")
    @Element(dependent = "true")
    private List<Room> rooms;
    
    @Persistent(mappedBy = "lair")
    @Element(dependent = "true")
    private List<Monster> monsters;
    
    @Persistent(mappedBy = "lair")
    @Element(dependent = "true")
    private List<MonsterEgg> monsterEggs;

    public Lair() {
        this.rooms = new ArrayList<Room>();
        this.monsters = new ArrayList<Monster>();
        this.monsterEggs = new ArrayList<MonsterEgg>();
    }
    
	public Lair(User user, int money, int garbage,
    		RoomData roomData, int street, int building, int floor) {
        this.user = user;
        this.money = money;
        this.garbage = garbage;
        this.roomData = roomData;
        this.roomData.setLair(this);
        this.setAddressStreet(street);
        this.setAddressBuilding(building);
        this.setAddressFloor(floor);
        this.rooms = new ArrayList<Room>();
        this.monsters = new ArrayList<Monster>();
        this.monsterEggs = new ArrayList<MonsterEgg>();
    }
	
	
	
	//------ Rooms ------//

    /**
     * @return the rooms of this lair.
     */
    public List<Room> getRooms() {
		return rooms;
	}
    
    /**
     * @param roomType identify the room
     * @return a specific Room in this lair or null if that room is not present.
     */
    public Room getRoom(RoomType roomType) {
    	for(Room room : rooms) {
    		if(room.getType().equals(roomType)) {
    			return room;
    		}
    	}
		return null;
	}
    
    /**
     * Get room by String (representing a RoomType).
     * Useful for JSTL access to Rooms.
     */
    public Room getRoom(String roomType) {
    	return getRoom(RoomType.valueOf(roomType));
    }

    /**
     * Set or refresh the list of rooms of this lair.
     * The order of the rooms will be the order shown in the view.
     */
	public void setRooms(List<Room> newRooms) {
		rooms.clear(); // vacía la lista
		if (newRooms != null)
    		for(Room room : newRooms) {
    			room.setLair(this); // establece el doble enlace.
    			rooms.add(room); // y añade la nueva Room
    		}
	}
	
	/**
	 * Add a new room to the lair in its initial state.
	 * Sets the relation between the lair and the room.
	 * Used for build a new Room.
	 * If that roomType exists yet in the lair, returns that room (doesn't build a new room).
	 */
	public Room buildRoom(RoomType roomType) {
		Room room = this.getRoom(roomType);
		if(room == null) {
			room = roomType.build(this);
			rooms.add(room);
		} 
		return room;
	}
	
	/**
	 * List of roomTypes available to build within the current lair state.
	 */
	public List<RoomType> getNewRoomsAvaliable() {
		List<RoomType> newRoomsAvaliable = new ArrayList<RoomType>();
		Set<RoomType> buildedRooms = new HashSet<RoomType>();
		for(Room room : rooms) {
			buildedRooms.add(room.getType());
		}

		for(RoomType roomType : RoomType.values()) { // En esta implementacion, se añaden todas las que hay menos las construidas
			if(!buildedRooms.contains(roomType)) {
				newRoomsAvaliable.add(roomType);
			}
		}
		return newRoomsAvaliable;
	}
	

	

	//------ Common getters and setters ------//

	public Key getId() { return id; }
	public void setId(Key lairId) { this.id = lairId; }
	public int getMoney() { return money; }
	public void setMoney(int money) { this.money = money; }
	public int getGarbage() { return garbage; }
	public void setGarbage(int garbage) { this.garbage = garbage; }
	public User getUser() { return user; }
	public void setUser(User user) { this.user = user; }
	public int getAddressStreet() { return addressStreet; }
	public void setAddressStreet(int street) { 
	    if (street > GameConf.getMaxNumberOfStreets() || street < 0)
            throw new IncorrectAddressException(street, addressBuilding, addressFloor);
	    this.addressStreet = street; }
	public int getAddressBuilding() { return addressBuilding; }
	public void setAddressBuilding(int building) {
	    if (building > GameConf.getMaxNumberOfBuildings() || building < 0)
            throw new IncorrectAddressException(addressStreet, building, addressFloor);
	    this.addressBuilding = building; 
	}
	public int getAddressFloor() { return addressFloor; }
	public void setAddressFloor(int floor) { 
	    if (floor > GameConf.getMaxNumberOfFloors() || floor < 0)
            throw new IncorrectAddressException(addressStreet, addressBuilding, floor);
	    this.addressFloor = floor; 
	}
    public int getVitalSpaceOccupied() { return roomData.getVitalSpaceOccupied(); }
	public int getVitalSpace() { return roomData.getVitalSpace(); }
	public boolean setVitalSpaceOccupied() { return roomData.setVitalSpaceOccupied(); }
	public int getVitalSpaceFree() { return roomData.getVitalSpaceFree(); }
	public void setLastChangeResourcesTurnToNow() { roomData.setLastChangeResourcesTurnToNow(); }
	public boolean isReadyToChangeResources() { return roomData.isReadyToChangeResources(); }
	public int getChangeResourcesMaxGarbageAmountEnabled() { return roomData.getChangeResourcesMaxGarbageAmountEnabled(); }
	public int getChangeResourcesMaxMoneyAmountEnabled() { return roomData.getChangeResourcesMaxMoneyAmountEnabled(); }
	public OnlyOneChangePerGameDayException getOnlyOneChangePerGameDayException() { return roomData.getOnlyOneChangePerGameDayException(); }
	public int getMoneyStorageCapacity() { return roomData.getMoneyStorageCapacity(); }
	public int getPercentageCommision() { return roomData.getPercentageCommision(); }
	public int getGarbageStorageCapacity() { return roomData.getGarbageStorageCapacity(); }
	public RoomData getRoomData() { return roomData; }
	public void setRoomData(RoomData roomData) { this.roomData = roomData; }
	
	//------ Monsters and eggs ------//
	
	public List<Monster> getMonsters() { return monsters; }
	public void setMonsters(List<Monster> monsters) { this.monsters = monsters; }
	public List<MonsterEgg> getMonsterEggs() { return monsterEggs; }
	public void setMonsterEggs(List<MonsterEgg> monsterEggs) { this.monsterEggs = monsterEggs; }
	public Lair addMonsterEgg(MonsterEgg monsterEgg) {
	    monsterEgg.setLair(this);
		this.monsterEggs.add(monsterEgg);
		return this;
	}
	public Lair removeMonsterEgg(MonsterEgg monsterEgg) {
	    monsterEgg.setLair(null);
	    this.monsterEggs.remove(monsterEgg);
	    return this;
	}
	public Lair addMonster(Monster monster) {
	    monster.setLair(this);
		this.monsters.add(monster);
		this.setVitalSpaceOccupied();
		return this;
	}
	public Lair removeMonster(Monster monster) {
	    monster.setLair(null);
	    this.monsters.remove(monster);
	    this.setVitalSpaceOccupied();
	    return this;
	}

	
	//------ Object overrides ------//
	
	@Override
    public String toString() {
//	    String strUser;
//	    if (user == null)
//	        strUser = "";
//	    else
//	        strUser = user.getLogin();
	    
		return Format.p(this.getClass(), new Object[]{
//		    "user", strUser,
			"money", money,
			"garbage", garbage,
			"address", "("+this.addressStreet+","+this.addressBuilding+","+this.addressFloor+")",
		});
	}
	
	@Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        //TODO: REHACER
//        result = prime * result + ((address == null) ? 0 : address.hashCode());
        result = prime * result + garbage;
        result = prime * result + money;
        result = prime * result + ((user == null) ? 0 : user.hashCode());
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
        Lair other = (Lair) obj;
        if (garbage != other.garbage)
            return false;
        if (money != other.money)
            return false;
        //TODO: Añadir direccion
        if (user != null && other.user != null && !user.getId().equals(other.user.getId()))
            return false;
        if (!roomsEquals((Lair)obj))
            return false;
        return true;
    }

	/**
	 * Compare this lair rooms with another one. Return true if both have no rooms loaded yet, or if
	 * both contains the same rooms. 
	 */
	private boolean roomsEquals(Lair lair) {
		if(this.rooms == null && lair.rooms == null) {
			return true;
		} else if((this.rooms == null && lair.rooms != null) || (this.rooms != null && lair.rooms == null)) {
			return false;
		} else {
			return Format.set(rooms.toArray()).equals(Format.set(lair.rooms.toArray()));
		}
	}
    
}
