package es.engade.thearsmonsters.model.entities.room.exceptions;

public class InWorksException extends RuntimeException {
    
	private static final long serialVersionUID = 200911261635L;

	public InWorksException(String method) {
        super("The room is not in works state. Method "+method+" not allowed.");
    }

}
