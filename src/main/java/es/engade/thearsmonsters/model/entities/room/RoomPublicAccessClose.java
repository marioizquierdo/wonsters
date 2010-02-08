package es.engade.thearsmonsters.model.entities.room;

public class RoomPublicAccessClose  extends RoomPublicAccess {
	
	private static final long serialVersionUID = 200911261639L;
	
	public boolean isPublished() {
		return false;
	}
	
	public boolean equals(Object theObject) {
		try {
			RoomPublicAccessClose access = (RoomPublicAccessClose) theObject;
			return !access.isPublished();
		} catch (Exception e) {
			return false;
		}
	}

}
