package es.engade.thearsmonsters.model.entities.lair;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;

import es.engade.thearsmonsters.model.entities.egg.MonsterEgg;
import es.engade.thearsmonsters.model.entities.lair.exceptions.NoRoomsLoadedException;
import es.engade.thearsmonsters.model.entities.monster.Monster;
import es.engade.thearsmonsters.model.entities.room.Room;
import es.engade.thearsmonsters.model.entities.room.enums.RoomType;
import es.engade.thearsmonsters.model.entities.room.types.TradeOffice;
import es.engade.thearsmonsters.model.entities.room.types.Warehouse;
import es.engade.thearsmonsters.model.entities.user.User;
import es.engade.thearsmonsters.model.util.Format;

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
    private int vitalSpaceOccupied;
    
    @Persistent(defaultFetchGroup="true", mappedBy = "lair")
    private User user;
    
    @Persistent(serialized="true",defaultFetchGroup="true")
    private RoomData roomData;
    
    @Persistent(serialized="true",defaultFetchGroup="true")
    private Address address;
    
    @Persistent
    private List<Room> rooms;
    
    @Persistent(mappedBy = "lair")
    private List<Monster> monsters;
    
    @Persistent(mappedBy = "lair")
    private List<MonsterEgg> monsterEggs;

    public Lair() {
        this.rooms = new ArrayList<Room>();
        this.monsters = new ArrayList<Monster>();
        this.monsterEggs = new ArrayList<MonsterEgg>();
    }
    
	public Lair(User user, int money, int garbage, int vitalSpaceOccupied,
    		RoomData roomData, Address address) {
        
        this.user = user;
        this.money = money;
        this.garbage = garbage;
        this.vitalSpaceOccupied = vitalSpaceOccupied;
        this.roomData = roomData;
//        this.roomData.setLair(this);
        this.address = address;
        this.rooms = new ArrayList<Room>();
        this.monsters = new ArrayList<Monster>();
        this.monsterEggs = new ArrayList<MonsterEgg>();
    }
	
	
	/****** Rooms ******/

    /**
     * @return the rooms of this lair.
     */
    public List<Room> getRooms() throws NoRoomsLoadedException {
    	if(rooms == null) throw new NoRoomsLoadedException();
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
     * Set or refresh the list (or map) of rooms of this lair.
     * The order of the rooms may be the order shown in the view.
     */
	public void setRooms(List<Room> rooms) {
		this.rooms = rooms;
	}
	
	/**
	 * Add a new room to the lair.
	 * Example: lair.addRoom(warehause).addRoom(truffleFarm);
	 */
	public Lair addRoom(Room room) {
		this.getRooms().add(room);
		return this;
	}
	
	/**
	 * List of roomTypes available to build with the current lair state.
	 */
	public List<RoomType> getNewRoomsAvaliable() throws  NoRoomsLoadedException {
		List<RoomType> newRoomsAvaliable = new ArrayList<RoomType>();
		Set<RoomType> buildedRooms = new HashSet<RoomType>();
		for(Room room : rooms) {
			buildedRooms.add(room.getType());
		}

		for(RoomType roomType : RoomType.values()) { // En esta implementacion, se a�aden todas las que hay menos las construidas
			if(!buildedRooms.contains(roomType.toString())) {
				newRoomsAvaliable.add(roomType);
			}
		}
		return newRoomsAvaliable;
	}
	
	
	/****** RoomData and specific Room methods ******/
	
	public RoomData getRoomData() {
		return this.roomData;
	}
	
	public int getGarbageStorageCapacity() throws NoRoomsLoadedException {
		Warehouse warehouse = (Warehouse) getRoom(RoomType.Warehouse);
		if(warehouse != null) {
			return warehouse.getGarbageStorageCapacity();
		} else {
			return 0; // If there is no warehouse, no garbage can be stored
		}
	}
	
	public int getMoneyStorageCapacity() throws NoRoomsLoadedException {
		TradeOffice tradeOffice = (TradeOffice) getRoom(RoomType.TradeOffice);
		if(tradeOffice != null) {
			return tradeOffice.getMoneyStorageCapacity();
		} else {
			return 0; // If there is no tradeOffice, no money can be stored
		}
	}
	
	/**
	 * When change garbage for money, which is the max cuantity allowed.
	 * @trows NullPointerException if the TradeOffice is not in the lair (take care).
	 */
	public int getChangeResourcesMaxGarbageAmountEnabled() {
		TradeOffice tradeOffice = (TradeOffice) getRoom(RoomType.TradeOffice);
		if(tradeOffice != null) {
			int freeMoneyStorageCapacity = getMoneyStorageCapacity() - getMoney();
			int garbageNeededForFillTheTradeOffice = (freeMoneyStorageCapacity * 
				100 / (100 - tradeOffice.getPercentageCommision())) + 1;
			return Math.min(getGarbage(), garbageNeededForFillTheTradeOffice);
		} else {
			throw new RuntimeException("Change resources is not possible without " +
					"TradeOffice. At lair of '"+user.getLogin()+"'");
		}
	}
	
	/**
	 * When change money for garbage, which is the max cuantity allowed.
	 * @trows NullPointerException if the TradeOffice is not in the lair (take care).
	 */
	
    public int getChangeResourcesMaxMoneyAmountEnabled() {
		TradeOffice tradeOffice = (TradeOffice) getRoom(RoomType.TradeOffice);
		if(tradeOffice != null) {
			int freeGarbageStorageCapacity = getGarbageStorageCapacity() - getGarbage();
			int moneyNeededForFillTheWarehouse = (freeGarbageStorageCapacity * 
				100 / (100 - tradeOffice.getPercentageCommision())) + 1;
			return Math.min(getMoney(), moneyNeededForFillTheWarehouse);
		} else {
			throw new RuntimeException("Change resources is not possible without " +
					"TradeOffice. At lair of '"+user.getLogin()+"'");
		}
    }
	
	
	/****** Vital Space ******/

	/**
	 * Amount of vital space used by the monsters.
	 */
	public int getVitalSpaceOccupied() {
		return vitalSpaceOccupied;
	}
	
	/**
	 * Total amount of vital space. Is dormitories.size * 10
	 */
	public int getVitalSpace() throws NoRoomsLoadedException {
		Room dormitories = getRoom(RoomType.Dormitories); 
		if(dormitories != null) {
			return dormitories.getSize() * 10;
		} else {
			return 0;
		}
	}
	
	public void setVitalSpaceOccupied(int occupiedVitalSpace) {
		this.vitalSpaceOccupied = occupiedVitalSpace;
	}
	
//	/**
//	 * Given all the monsters of this lair, recalculate the vital Space
//	 * @return true if the occupied vital space was changed, false if still be the same.
//	 */
//	public boolean setVitalSpaceOccupied(List<Monster> lairMonsters) {
//		int newOccupiedVitalSpace = 0;
//		for(Monster m : lairMonsters) {
//			newOccupiedVitalSpace += m.getRace().getVitalSpace();
//		}
//		if(newOccupiedVitalSpace == this.vitalSpaceOccupied) {
//			return false;
//		} else {
//			this.vitalSpaceOccupied = newOccupiedVitalSpace;
//			return true;
//		}
//	}
	
	/**
	 * The rest of vital space avaliable (vitalSpace - vitalSpaceOcuppied)
	 */
	public int getVitalSpaceFree() throws NoRoomsLoadedException {
		return getVitalSpace() - getVitalSpaceOccupied();
	}

	

	/***** Common getters and setters *****/

	public Key getId() { return id; }
	public void setId(Key lairId) { this.id = lairId; }
	public int getMoney() { return money; }
	public void setMoney(int money) { this.money = money; }
	public int getGarbage() { return garbage; }
	public void setGarbage(int garbage) { this.garbage = garbage; }
	public Address getAddress() { return address; }
	public User getUser() { return user; }
	public void setUser(User user) { this.user = user; }
	public void setAddress(Address address) { this.address = address; }

	
	
	/**** Monsters and eggs ****/
	
	public List<Monster> getMonsters() { return monsters; }
	public void setMonsters(List<Monster> monsters) { this.monsters = monsters; }
	public List<MonsterEgg> getMonsterEggs() { return monsterEggs; }
	public void setMonsterEggs(List<MonsterEgg> monsterEggs) { this.monsterEggs = monsterEggs; }
	public Lair addMonsterEgg(MonsterEgg monsterEgg) {
		this.monsterEggs.add(monsterEgg);
		return this;
	}
	public Lair addMonster(Monster monster) {
		this.monsters.add(monster);
		return this;
	}

	
	@Override
    public String toString() {
		return Format.p(this.getClass(), new Object[]{
		    "user", user,
			"money", money,
			"garbage", garbage,
			"vitalSpaceOccupied", vitalSpaceOccupied,
			"address", address,
		});
	}
	
	@Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((address == null) ? 0 : address.hashCode());
        result = prime * result + garbage;
        result = prime * result + money;
        result = prime * result + ((user == null) ? 0 : user.hashCode());
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
        Lair other = (Lair) obj;
        if (address == null) {
            if (other.address != null)
                return false;
        } else if (!address.equals(other.address))
            return false;
        if (garbage != other.garbage)
            return false;
        if (money != other.money)
            return false;
        if (user == null) {
            if (other.user != null)
                return false;
        } else if (!user.equals(other.user))
            return false;
        if (vitalSpaceOccupied != other.vitalSpaceOccupied)
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
