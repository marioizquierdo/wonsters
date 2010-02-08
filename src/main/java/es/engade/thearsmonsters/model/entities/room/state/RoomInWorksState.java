package es.engade.thearsmonsters.model.entities.room.state;

import es.engade.thearsmonsters.model.entities.room.exceptions.InWorksException;

public abstract class RoomInWorksState  extends RoomState {
	
	private static final long serialVersionUID = 200911261635L;
	
	private int effortDone;
	
	public RoomInWorksState(int effortDone) {
		this.effortDone = effortDone;
	}

	public int getEffortDone() throws InWorksException {
		return effortDone;
	}

	public void setEffortDone(int effortDone) {
		this.effortDone = effortDone;
	}
	
	public boolean isInWorks() {
		return true;
	}
	
	public String toString() {
        try {
			return new String(
					getWorksType() + "(effortDone = " + effortDone + ")");
		} catch (InWorksException impossible) {
			impossible.printStackTrace();
			return "in works";
		}
    }

}
