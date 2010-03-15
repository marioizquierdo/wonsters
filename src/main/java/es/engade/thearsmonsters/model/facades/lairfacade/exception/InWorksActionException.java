package es.engade.thearsmonsters.model.facades.lairfacade.exception;

import es.engade.thearsmonsters.model.entities.room.enums.RoomType;
import es.engade.thearsmonsters.util.exceptions.ModelException;

public class InWorksActionException  extends ModelException {

	private static final long serialVersionUID = 200912142039L;
	
    public InWorksActionException(String message) {
        super(message);
    }

    public InWorksActionException(String message, String login, RoomType roomType) {
        super(message+ " - in room "+ roomType +" of the "+ login +"'s lair.");
    }
    
    public InWorksActionException(String login, RoomType roomType, boolean isInWorks) {
        super("Action not bound: The room "+ roomType +
        		" from "+ login +"'s lair "+
        		(isInWorks? "is in works yet" : "is not in works"));
    }
    
    /* Test code. Uncomment for testing. */
//    public static void main(String[] args) {
//    
//        try {
//            throw new InWorksActionException("there is a error in the room");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    
//    }    

}
