package es.engade.thearsmonsters.model.entities.room.state;

public class RoomNormalState  extends RoomState {

	private static final long serialVersionUID = 200911261635L;
	
	public RoomNormalState() {}

	public boolean isInWorks() {
		return false;
	}
	
	public boolean equals(Object theObject) {
		try {
			RoomNormalState state = (RoomNormalState) theObject;
			return !state.isInWorks();
		} catch (Exception e) {
			return false;
		}
	}
	
	public String toString() {
        return new String("Normal State");
    }
}
