package es.engade.thearsmonsters.model.entities.room.publicaccess;

public class RoomPublicAccessClose  extends RoomPublicAccess {
	
	private static final long serialVersionUID = 200911261639L;
	
	@Override
    public boolean isPublished() {
		return false;
	}
	
	@Override
    public boolean equals(Object theObject) {
		try {
			RoomPublicAccessClose access = (RoomPublicAccessClose) theObject;
			return !access.isPublished();
		} catch (Exception e) {
			return false;
		}
	}

}
