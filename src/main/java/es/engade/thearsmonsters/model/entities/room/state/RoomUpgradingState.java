package es.engade.thearsmonsters.model.entities.room.state;

import es.engade.thearsmonsters.model.entities.room.enums.WorksType;
import es.engade.thearsmonsters.model.entities.room.exceptions.InWorksException;

public class RoomUpgradingState extends RoomInWorksState {

	private static final long serialVersionUID = 200911261635L;
	
	public RoomUpgradingState(int effortDone) {
		super(effortDone);
	}
	
	@Override
    public WorksType getWorksType() throws InWorksException {
		return WorksType.Upgrading;
	}
	
	@Override
    public boolean equals(Object theObject) {
		try {
			RoomUpgradingState state = (RoomUpgradingState) theObject;
			return this.getEffortDone() == state.getEffortDone();
		} catch (Exception e) {
			return false;
		}
	}

}
