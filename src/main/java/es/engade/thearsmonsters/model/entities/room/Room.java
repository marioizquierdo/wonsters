package es.engade.thearsmonsters.model.entities.room;

import java.util.List;

import es.engade.thearsmonsters.model.entities.common.Id;
import es.engade.thearsmonsters.model.entities.lair.Lair;
import es.engade.thearsmonsters.model.entities.monster.enums.MonsterAge;
import es.engade.thearsmonsters.model.entities.room.enums.RoomType;
import es.engade.thearsmonsters.model.entities.room.enums.WorksType;
import es.engade.thearsmonsters.model.entities.room.exceptions.InWorksException;
import es.engade.thearsmonsters.model.entities.room.exceptions.PublicAccessException;
import es.engade.thearsmonsters.model.entities.room.state.RoomEnlargingState;
import es.engade.thearsmonsters.model.entities.room.state.RoomInWorksState;
import es.engade.thearsmonsters.model.entities.room.state.RoomNormalState;
import es.engade.thearsmonsters.model.entities.room.state.RoomState;
import es.engade.thearsmonsters.model.entities.room.state.RoomUpgradingState;

public abstract class Room {

    protected Id id;
    protected Lair lair;
    protected int level;
    protected int size;
    protected RoomPublicAccess publicAccess;
    protected RoomState state; 
    
    /* ----------------- Basic Methods ----------------- */
    
    /**
     * Basic VO constructor
     * @param roomId Room identifier
     * @param lair Name of the room's owner lair
     * @param level Current level of the room
     * @param size Current number of places
     * @param publicAccess State Pattern to manage the public access operations
     * @param state State Pattern to manage the room state operations
     */
    public Room(Id roomId, Lair lair, int level, int size, 
    		RoomPublicAccess publicAccess, RoomState state) {
        this.id = roomId;
        this.lair = lair;
        this.level = level;
        this.size = size;
        this.publicAccess = publicAccess;
        this.state = state;
//        removeTasks();
    }
    
    /**
     * Default initial state when the room is created
     * Subclasses must override this constructor for special initial state.
     * @param lair: the owner lair of the room
     */
    public Room(Lair lair) {
    	// Set with default values.
    	this(null, lair, 1, 0, new RoomPublicAccessClose(), new RoomUpgradingState(0));
    }
    
	public Id getId() {
		return id;
	}
    
    public void setId(Id roomId) {
        this.id = roomId;
    }

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public RoomPublicAccess getPublicAccess() {
		return publicAccess;
	}

	public void setPublicAccess(RoomPublicAccess publicAccess) {
		this.publicAccess = publicAccess;
	}

	public RoomState getState() {
		return state;
	}

	public void setState(RoomState state) {
		this.state = state;
	}
	
	public Lair getLair() {
		return lair;
	}

	public void setLair(Lair lair) {
		this.lair = lair;
	}

	/**
	 * Change the state of the room to RoomUpgradingState,
	 * at the begining of works (effortDone = 0).
	 * @return true if the state was changed, false weather
	 * 		the room is inWorks yet or weather the room can not
	 * 		be upgraded one level more.
	 */
	public boolean setStateStartUpgrading() {
        if(!isInWorks() && isUpgradable()) {
        	setState(new RoomUpgradingState(0));
        	return true;
        } else {
        	return false;
        }
	}
	
	/**
	 * Change the state of the room to RoomEnlargingState,
	 * at the begining of works (effortDone = 0).
	 * @return true if the state was changed, false weather
	 * 		the room is inWorks yet or weather the room can not
	 * 		be enlarged one place more.
	 */
	public boolean setStateStartEnlarging() {
        if(!isInWorks() && isEnlargable()) {
        	setState(new RoomEnlargingState(0));
        	return true;
        } else {
        	return false;
        }
	}
	
	/**
	 * Change the state of the room to normal, canceling the effortDone
	 * in the current works.<br>
	 * NOTE: The tasks will not be changed here. The CancelWorksAction
	 * of the facade will cancel the current builder jobs.
	 * @return true if the state was changed or 
	 * 		false if the room is not inWorks.
	 */
	public boolean setStateCancelWorks() {
        if(isInWorks() && getLevel() > 0) {
        	setState(new RoomNormalState());
        	return true;
        } else {
        	return false;
        }
	}
	
	/**
	 * @return True if this room can be upgraded one more level. False otherwise.
	 */
	public boolean isUpgradable() {
		return this.getGarbageUpgrade() >= 0;
	}
	
	/**
	 * @return True if this room can be enlarged one more place. False otherwise.
	 */
	public boolean isEnlargable() {
		return this.getGarbageEnlarge() >= 0;
	}

	public boolean isPublished() {
		return publicAccess.isPublished();
	}
	
	public int getPublicPrice() throws PublicAccessException {
		return publicAccess.getPublicPrice();
	}
	
	public int getPublicGuildPrice() throws PublicAccessException {
		return publicAccess.getPublicGuildPrice();
	}
	
	public String getPublicMarketingText() throws PublicAccessException {
		return publicAccess.getPublicMarketingText();
	}
	
	public boolean isInWorks() {
		return state.isInWorks();
	}
	
	public int getEffortDone() throws InWorksException {
		return state.getEffortDone();
	}
	
	public WorksType getWorksType() throws InWorksException {
		return state.getWorksType();
	}
	
	/* ---------------- Room Type Methods ---------------- */
	/* Override depending on the roomType implementation */
	
	/**
	 * @return true if this room is in works for build at level 1, size 0 (generally)
	 */
	public boolean isInInitialState() {
		// Compare this with a new defined room in its initial state.
		return this.equals(
				RoomType.newRoom(
						this.getRoomType().code(), this.getLair()));
	}
	
	/**
	 * RoomType in terms of RoomType enumeration
	 */
	public abstract RoomType getRoomType();
	
	/**
	 * Alias of getRoomType()
	 */
	public RoomType getType() { return getRoomType(); }
	
	/**
	 * Garbage needed for build this room type.
	 */
	public int getGarbageBuild() {return this.getRoomType().getGarbageBuild();}
	
	/**
	 * Effort needed for build this room type.
	 */
	public int getEffortBuild() {return this.getRoomType().getEffortBuild();}
	/**
	 * @return Garbage needed for Next Level Upgrading (-1 if cannot be upgraded)
	 */
	public int getGarbageUpgrade() {return gUpg(this.getLevel());}
	/**
	 * @return Effort needed for Next Level Upgrading (-1 if cannot be upgraded)
	 */
	public int getEffortUpgrade() {return eUpg(this.getLevel());}
	/**
	 * @return Garbage needed for Enlarging one more place (-1 if cannot be enlarged)
	 */
	public int getGarbageEnlarge() {return gEnl(this.getSize());}
	/**
	 * @return Effort needed for Enlarging one more place (-1 if cannot be enlarged)
	 */
	public int getEffortEnlarge() {return eEnl(this.getSize());}

	/**
	 * Garbage needed for upgrading.
	 * gUpg(level) = gUpg * fgUpg^level, level in [1 .. maxLevel-1]
	 * @return -1 if cannot be upgraded, the garbage needed otherwise
	 */
	public int gUpg(int level) {
		if(level<1 || (getMaxLevel()>=0 && level>=getMaxLevel())) return -1;
		return roundValue(_gUpg(level));
	}
	protected double _gUpg(int level) {return -1;}; // template to extend only if needed

	/**
	 * Effort needed for upgrading.
	 * eUpg(level) = eUpg * feUpg^level, level in [1 .. maxLevel-1]
	 * @return -1 if cannot be upgraded, the effort needed otherwise
	 */
	public int eUpg(int level) {
		if(level<1 || (getMaxLevel()>=0 && level>=getMaxLevel())) return -1;
		return roundValue(_eUpg(level));
		
	}
	protected double _eUpg(int level) {return -1;}; // template to extend only if needed

	/**
	 * Garbage needed for enlarging.
	 * gEnl(level) = gEnl * fgEnl^size, size in [0 .. maxSize-1]
	 * @return -1 if cannot be enlarged, the garbage needed otherwise
	 */
	public int gEnl(int size) {
		if(size<0 || (getMaxSize()>=0 && size>=getMaxSize())) return -1;
		return roundValue(_gEnl(size));
	}
	protected double _gEnl(int size) {return -1;}; // template to extend only if needed
	
	/**
	 * Effort needed for enlarging.
	 * eEnl(level) = eEnl * feEnl^size, size in [0 .. maxSize-1]
	 * @return -1 if cannot be enlarged, the effort needed otherwise
	 */
	public int eEnl(int size) {
		if(size<0 || (getMaxSize()>=0 && size>=getMaxSize())) return -1;
		return roundValue(_eEnl(size));
	}
	protected double _eEnl(int size) {return -1;}; // template to extend only if needed
	
	/**
	 * Max number of Employees allowed
	 * @return <ul>
	 *   <li>[0..N], exactly from 0 to N monsters (0 means nobody can enter the room).</li>
	 *   <li>-1, free size, only until there are more free size.</li>
	 *   <li>-2, there is no maxEmployees, any number is allowed.</li>
	 *   </ul>
	 */
	public int getMaxWorkers() {return this.getRoomType().getMaxWorkers();}
	
	/**
	 * Max number of Customers allowed
	 * @return <ul>
	 *   <li>[0..N], exactly from 0 to N monsters.</li>
	 *   <li>-1, free size, only until there are more free size</li>
	 *   <li>-2, there is no maxCustomers, any number is allowed.</li>
	 *   </ul>
	 */
	public int getMaxCustomers() {return this.getRoomType().getMaxCustomers();}
	
	/**
	 * Allowed ages of Employees to work in this room type
	 * @return AgeState subset List [Child, Adult or Old]
	 */
	public List<MonsterAge> getAgeWorker() {return this.getRoomType().getAgeWorker();}
	
	/**
	 * Allowed ages of Customers to work in this room type
	 * @return AgeState subset List  [Child, Adult or Old]
	 */
	public List<MonsterAge> getAgeCustomer() {return this.getRoomType().getAgeCustomer();}
	
	/**
	 * Weather this room type may be published (true) or not (false)
	 */
	public boolean isPublicable() {return this.getRoomType().isPublicable();}
	
	/**
	 * Max size allowed for this room type
	 * @return if return -1 means there is no max size (inf)
	 */
	public int getMaxSize() {return this.getRoomType().getMaxSize();}
	
	/**
	 * Max level allowed for this room type
	 * @return if return -1 means there is no max level (inf)
	 */
	public int getMaxLevel() {return this.getRoomType().getMaxLevel();}
	

	/* ------------- Object Override Methods ------------- */
	
	@Override
    public String toString() {
        return new String(
                "roomId = " + id + " | " +
        		"lairId = " + lair.getId() + " | " +
                "level = " + level + " | " +
                "size = " + size + " | " +
                publicAccess + " | " +
                state + " | ");
//                getTasksToString());
    }
	
	@Override
    public boolean equals(Object theObject) {
		Room room = (Room)theObject;
		return 
			//this.roomId == room.getRoomId() &&
		    this.lair.equals(room.lair) &&
		    this.level == room.getLevel() &&
		    this.size == room.getSize() &&
		    this.publicAccess.equals(room.getPublicAccess()) &&
		    this.state.equals(room.getState());
	}
	
	
	/*----------- Helper methods -----------*/
	
//	/**
//	 * Makes a copy of the original list but the elements still be the same.
//	 * @return Another list pointing to the same elements in the same order.
//	 */
//	private List<TaskVO> keepReferences(List<TaskVO> list) {
//		List<TaskVO> clone = new ArrayList<TaskVO>(list.size());
//		for(TaskVO task : list) {
//			clone.add(task);
//		}
//		return clone;
//	}
	
	/**
	 * Cast a double value into a integer and
	 * round to zero the less significant digits.
	 */
	protected int roundValue(double value) {
		int v = (int) value;
		if(v > 100000000) {
			v = (v/1000000) * 1000000;
		} else if(v > 100000) {
			v = (v/1000) * 1000;
		} else if(v > 10000) {
			v = (v/100) * 100;
		} else if(v > 1000) {
			v = (v/10) * 10;
		}
		return v;
	}


        
    
}

