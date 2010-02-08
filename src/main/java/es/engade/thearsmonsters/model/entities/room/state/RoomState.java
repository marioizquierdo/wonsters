package es.engade.thearsmonsters.model.entities.room.state;

import java.io.Serializable;

import es.engade.thearsmonsters.model.entities.room.enums.WorksType;
import es.engade.thearsmonsters.model.entities.room.exceptions.InWorksException;

public abstract class RoomState implements Serializable {
	
	private static final long serialVersionUID = 200911261632L;

	public abstract boolean isInWorks();
	
	public int getEffortDone() throws InWorksException {
		throw new InWorksException("getEffortDone");
	}
	
	public WorksType getWorksType() throws InWorksException {
		throw new InWorksException("getWorksType");
	}
}
