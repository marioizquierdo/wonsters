package es.engade.thearsmonsters.model.entities.lair;

import java.io.Serializable;

import es.engade.thearsmonsters.model.util.GameConf;

/**
 * Encapsulate and store specific rooms data.
 * It is attributes that depends on concrete room actions, but in the 
 * database are stored in the Lair table because they are needed
 * only for one specific purpose.
 * An example is the lastChangeResourcesTurn, used for check that
 * only one resources (garbage-money) change can be made per day.
 *
 */
public class RoomData implements Serializable {

	private static final long serialVersionUID = 200911261651L;

	private long lastChangeResourcesTurn;
    private Lair lair;
    
    public RoomData(long lastChangeResourcesTurn) {
        this.lastChangeResourcesTurn = lastChangeResourcesTurn;
    	this.lair = null;
    }
    
    /**
     * Constructor with default values (when the lair is first time created
     * for a new user in the database).
     */
    public RoomData() {
    	this(0);
    }

	public Lair getLair() {
		return lair;
	}

	public void setLair(Lair lair) {
		this.lair = lair;
	}

	public long getLastChangeResourcesTurn() {
		return lastChangeResourcesTurn;
	}

	public void setLastChangeResourcesTurn(long newTurn) {
		this.lastChangeResourcesTurn = newTurn;
	}

	public void setLastChangeResourcesTurnToNow() {
		this.lastChangeResourcesTurn = GameConf.getCurrentTurn();
	}
	
	public boolean isReadyToChangeResources() {
		return this.lastChangeResourcesTurn <= GameConf.getCurrentTurn() - GameConf.getTurnsPerDay();
	}
	
//	/**
//	 * Little hard to read, but this method is usefull in the view because
//	 * the exception can create its own localized message (implements LocalizableMessage),
//	 * and is better to call this and check if the returned value is null, otherwise
//	 * print the error message, than simply look at isReadyToChangeResources().
//	 * @return the exception if the change can not be made or null otherwise
//	 */
//	public OnlyOneChangePerGameDayException getOnlyOneChangePerGameDayException() {
//		if(!isReadyToChangeResources()) {
//			return new OnlyOneChangePerGameDayException(this.lastChangeResourcesTurn, this.lair.getLoginName());
//		} else {
//			return null;
//		}
//	}

    
    @Override
    public String toString() {
        return 
        	"lastChangeResourcesTurn = "+lastChangeResourcesTurn;
    }
	
	@Override
    public boolean equals(Object o) {
		RoomData roomData = (RoomData) o;
		return
			this.getLastChangeResourcesTurn() == roomData.getLastChangeResourcesTurn();
	}
    

}
