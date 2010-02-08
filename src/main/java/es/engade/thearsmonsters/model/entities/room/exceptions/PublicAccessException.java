package es.engade.thearsmonsters.model.entities.room.exceptions;

import es.engade.thearsmonsters.util.exceptions.ModelException;

public class PublicAccessException extends ModelException {

	private static final long serialVersionUID = 200911261635L;
	
    public PublicAccessException(String method) {
        super("The room is not public. Method "+method+" not avaliable");
    }

}
