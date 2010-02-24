package es.engade.thearsmonsters.model.entities.room.state;

import es.engade.thearsmonsters.model.entities.room.enums.WorksType;
import es.engade.thearsmonsters.model.entities.room.exceptions.InWorksException;

public class RoomEnlargingState extends RoomInWorksState {
	
	private static final long serialVersionUID = 200911261633L;

	public RoomEnlargingState(int effortDone) {
		super(effortDone);
	}
	
	@Override
    public WorksType getWorksType() throws InWorksException {
		return WorksType.Enlarging;
	}
	
	@Override
    public boolean equals(Object theObject) {
		try {
			RoomEnlargingState state = (RoomEnlargingState) theObject;
			return this.getEffortDone() == state.getEffortDone();
		} catch (Exception e) {
			return false;
		}
	}

}
