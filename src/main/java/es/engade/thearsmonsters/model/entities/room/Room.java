package es.engade.thearsmonsters.model.entities.room;

import java.io.Serializable;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;

import es.engade.thearsmonsters.model.entities.common.ThearsmonstersEntity;
import es.engade.thearsmonsters.model.entities.lair.Lair;
import es.engade.thearsmonsters.model.entities.room.enums.RoomType;
import es.engade.thearsmonsters.model.entities.room.exceptions.InWorksException;
import es.engade.thearsmonsters.model.entities.room.exceptions.PublicAccessException;
import es.engade.thearsmonsters.model.entities.room.publicaccess.RoomPublicAccess;
import es.engade.thearsmonsters.model.entities.room.state.RoomNormalState;
import es.engade.thearsmonsters.model.entities.room.state.RoomState;
import es.engade.thearsmonsters.model.entities.room.state.RoomUpgradingState;
import es.engade.thearsmonsters.model.util.Format;

@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class Room extends ThearsmonstersEntity implements Serializable {
    
    private static final long serialVersionUID = 20100305L;

    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    protected Key id;
    
    @Persistent//(defaultFetchGroup="true")
    protected Lair lair;
    
    @Persistent
    protected int level;
    
    @Persistent(serialized="true",defaultFetchGroup="true")
    protected RoomPublicAccess publicAccess;
    
    @Persistent(serialized="true",defaultFetchGroup="true")
    protected RoomState state;
    
    @Persistent(serialized="true",defaultFetchGroup="true")
    protected RoomType type;
    
    /* ----------------- Basic Methods ----------------- */
    
    public Room() {}
    
    /**
     * Constructor
     */
    public Room(Lair lair, RoomType type, int level, 
    		RoomPublicAccess publicAccess, RoomState state) {
        this.lair = lair;
        this.type = type;
        this.level = level;
        
        this.publicAccess = publicAccess;
        this.state = state;
    }
    
	public Key getIdKey() {
		return id;
	}
    
    public void setIdKey(Key roomId) {
        this.id = roomId;
    }

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
		this.getLair().updateScore(); // al modificar el nivel de una sala, cambia la puntuación del usuario.
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
	
	
	/* ---------------- State (inWorks) Methods ---------------- */

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
	 * Change the state of the room to normal, canceling the effortDone
	 * in the current works.
	 * @return true if the state was changed or false if the room is not currentlly inWorks.
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
	
	public int getEffortDone() {
		return state.getEffortDone();
	}
	
	/**
	 * @return integer between 0 and 100. Is the percentage of works done to reach room's next level. 
	 */
	public int getEffortDonePercentage() {
		if(!isInWorks()) return 0;
		int effortToNextLevel = isInInitialState() ? getEffortBuild() : getEffortUpgrade();
		return 100 * getEffortDone() / effortToNextLevel;
	}
	
	
	/* ---------------- Room Type Methods ---------------- */
	
	/**
	 * @return true if this room is in works for build at level 1, size 0 (generally)
	 */
	public boolean isInInitialState() {
		// Compare this with a new defined room in its initial state.
		return this.equals(type.build(lair));
	}
	
	/**
	 * RoomType in terms of RoomType enumeration
	 */
	public  RoomType getRoomType(){
		return this.type;
	}
	
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
	public int getGarbageUpgrade() {return this.getLevel() == 0 ? getGarbageBuild() : getGarbageUpgradeWhenLevel(this.getLevel());}
	/**
	 * @return Effort needed for Next Level Upgrading (-1 if cannot be upgraded)
	 */
	public int getEffortUpgrade() {return this.getLevel() == 0  ? getEffortBuild() : getEffortUpgradeWhenLevel(this.getLevel());}

	/**
	 * Garbage needed for upgrading.
	 * gUpg(level) = gUpg * fgUpg^level, level in [1 .. maxLevel-1]
	 * @return -1 if cannot be upgraded, the garbage needed otherwise
	 */
	public int getGarbageUpgradeWhenLevel(int level) {
		if(level<1 || (getMaxLevel() >= 0 && level >= getMaxLevel())) return -1;
		return Format.roundValue(type.getGarbageUpgrade(level));
	}

	/**
	 * Effort needed for upgrading.
	 * eUpg(level) = eUpg * feUpg^level, level in [1 .. maxLevel-1]
	 * @return -1 if cannot be upgraded, the effort needed otherwise
	 */
	public int getEffortUpgradeWhenLevel(int level) {
		if(level<1 || (getMaxLevel() >= 0 && level >= getMaxLevel())) return -1;
		return Format.roundValue(type.getEffortUpgrade(level));	
	}
	/**
	 * If this room is in works, returns the amount of garbage returned to the player
	 * when cancel the in-works state. Which is the 90 percent of the garbage upgrade.
	 */
	public int getGarbageReturnedWhenCancelWorks() {
		return (int) (getGarbageUpgrade() * 0.9); // 0.9 es el 90% que devuelven, debido al coste del 10% sobre el total al cancelar las obras
	}
		
	/**
	 * Weather this room type may be published (true) or not (false)
	 */
	public boolean isPublicable() { return this.getLevel() > 0 && this.getRoomType().isPublicable(); }
	
	/**
	 * Max size allowed for this room type
	 * @return if return -1 means there is no max size (inf)
	 */
	public int getMaxSize() {return 1; /* type.getMaxSize(); */} // NOT SUPPORTED IN THIS VERSION
	
	/**
	 * Max level allowed for this room type
	 * @return if return -1 means there is no max level (inf)
	 */
	public int getMaxLevel() {return this.getRoomType().getMaxLevel();}
	

	/* ------------- Object Override Methods ------------- */
	
	@Override
    public String toString() {
		return Format.p(this.getClass(), new Object[]{
			"login", lair.getUser().getLogin(),
			"type", type,
			"level", level, 
			"state", state,
			"publicAccess", publicAccess
		});
	}
	
	@Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((lair == null) ? 0 : lair.hashCode());
        result = prime * result + level;
        result = prime * result
                + ((publicAccess == null) ? 0 : publicAccess.hashCode());
        result = prime * result + ((state == null) ? 0 : state.hashCode());
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
        Room other = (Room) obj;
        if (lair == null) {
            if (other.lair != null)
                return false;
        } else if (!lair.equals(other.lair))
            return false;
        if (level != other.level)
            return false;
        if (publicAccess == null) {
            if (other.publicAccess != null)
                return false;
        } else if (!publicAccess.equals(other.publicAccess))
            return false;
        if (state == null) {
            if (other.state != null)
                return false;
        } else if (!state.equals(other.state))
            return false;
        return true;
    }
        
    public void touch() {
    	lair.touchInstance();
    }
    
}

