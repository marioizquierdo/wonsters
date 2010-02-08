package es.engade.thearsmonsters.model.entities.lair.exceptions;

public class NoRoomsLoadedException extends RuntimeException {
    
	private static final long serialVersionUID = 200911261650L;

	public NoRoomsLoadedException() {
        super("This lair has no rooms loaded. Please execute the setRooms(List<Room> rooms) method," +
        		"the rooms can be loaded from the SQLRoomDAO.findLairRooms().");
    }
    
}
