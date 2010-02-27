package es.engade.thearsmonsters.model.entities.lair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import es.engade.thearsmonsters.model.entities.common.Id;
import es.engade.thearsmonsters.model.entities.egg.MonsterEgg;
import es.engade.thearsmonsters.model.entities.lair.exceptions.NoRoomsLoadedException;
import es.engade.thearsmonsters.model.entities.monster.Monster;
import es.engade.thearsmonsters.model.entities.room.Room;
import es.engade.thearsmonsters.model.entities.room.enums.RoomType;
import es.engade.thearsmonsters.model.entities.room.types.TradeOffice;
import es.engade.thearsmonsters.model.entities.room.types.Warehouse;
import es.engade.thearsmonsters.model.entities.user.User;
import es.engade.thearsmonsters.model.util.Format;

public class Lair {

	private Id id;
    private int money;
    private int garbage;
    private int vitalSpaceOccupied;
    private User user;
    private RoomData roomData;
    private Address address;
    private List<Room> rooms;
    
    private Set<Monster> monsters;
    private Set<MonsterEgg> monsterEggs;

    public Lair() {}
    
	public Lair(Id lairId, User user, int money, int garbage, int vitalSpaceOccupied,
    		RoomData roomData, Address address) {
        
		this.id = lairId;
        this.user = user;
        this.money = money;
        this.garbage = garbage;
        this.vitalSpaceOccupied = vitalSpaceOccupied;
        this.roomData = roomData;
        this.roomData.setLair(this);
        this.address = address;
        this.rooms = null;
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

		for(RoomType roomType : RoomType.values()) { // En esta implementacion, se a–aden todas las que hay menos las construidas
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
					"TradeOffice. At lair of '"+user.getLoginName()+"'");
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
					"TradeOffice. At lair of '"+user.getLoginName()+"'");
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

	public Id getId() { return id; }
	public void setId(Id lairId) { this.id = lairId; }
	public int getMoney() { return money; }
	public void setMoney(int money) { this.money = money; }
	public int getGarbage() { return garbage; }
	public void setGarbage(int garbage) { this.garbage = garbage; }
	public Address getAddress() { return address; }
	public User getUser() { return user; }
	public void setUser(User user) { this.user = user; }
	public void setAddress(Address address) { this.address = address; }

	
	
	/**** Monsters and eggs ****/
	
	public Set<Monster> getMonsters() { return monsters; }
	public void setMonsters(Set<Monster> monsters) { this.monsters = monsters; }
	public Set<MonsterEgg> getMonsterEggs() { return monsterEggs; }
	public void setMonsterEggs(Set<MonsterEgg> monsterEggs) { this.monsterEggs = monsterEggs; }
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
			"user", user.getLoginName(),
			"money", money,
			"garbage", garbage,
			"vitalSpaceOccupied", vitalSpaceOccupied,
			"address", address,
		});
	}
	
	public boolean equals(Lair lair) {
		return 
		    this.user.equals(lair.user) &&
		    this.getMoney() == lair.getMoney() &&
		    this.getGarbage() == lair.getGarbage() &&
		    this.getVitalSpaceOccupied() == lair.getVitalSpaceOccupied() &&
		    this.getAddress().equals(lair.getAddress()) &&
		    roomsEquals(lair);
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
