package es.engade.thearsmonsters.model.entities.room.exceptions;

import es.engade.thearsmonsters.util.exceptions.ModelException;

public class InWorksException extends ModelException {
    
	private static final long serialVersionUID = 200911261635L;

	public InWorksException(String method) {
        super("The room is not in works. Method "+method+" not avaliable");
    }

}
