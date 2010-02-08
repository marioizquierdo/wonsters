package es.engade.thearsmonsters.model.entities.room.exceptions;


public class NoTasksLoadedException extends RuntimeException {
    
	private static final long serialVersionUID = 200911261635L;

	public NoTasksLoadedException() {
        super("This room has no task loaded. Please execute the setTasks(List<List<TaskVO>> tasks) method first, " +
        		"or better just invoke SQLTaskDAO.setRoomTasks(connection, room).");
    }
}
