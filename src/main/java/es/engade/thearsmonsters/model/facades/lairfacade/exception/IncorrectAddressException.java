package es.engade.thearsmonsters.model.facades.lairfacade.exception;

import es.engade.thearsmonsters.http.view.applicationobjects.LocalizableMessage;
import es.engade.thearsmonsters.model.entities.lair.Address;
import es.engade.thearsmonsters.model.util.GameConf;

public class IncorrectAddressException extends IndexOutOfBoundsException implements LocalizableMessage {
    
	private static final long serialVersionUID = 200912142039L;
	
	private String messageKey;
    private String[] messageParams;
	
    public IncorrectAddressException() {
    	
        super("Address out of bounds. Allowed values: " +
        		"street ["+ Address.FIRST_STREET+", "+ Address.LAST_STREET +"], " +
        		"building ["+ Address.FIRST_BUILDING+", "+ Address.LAST_BUILDING +"], " +
        		"floor ["+ Address.FIRST_FLOOR+", "+ Address.LAST_FLOOR +"].");
        
        this.messageKey = "FlashMessages.IncorrectAddressException";
        this.messageParams = new String[]{
        		GameConf.getMaxNumberOfStreets()+"", 
        		GameConf.getMaxNumberOfBuildings()+"",
        		GameConf.getMaxNumberOfFloors()+""};
    }
    
    public IncorrectAddressException(int street, int building, int floor) {
        
        super("Address out of bounds ("+
                street +","+ building +","+ floor +
                "). Allowed values: " +
                "street ["+ Address.FIRST_STREET+", "+ Address.LAST_STREET +"], " +
                "building ["+ Address.FIRST_BUILDING+", "+ Address.LAST_BUILDING +"], " +
                "floor ["+ Address.FIRST_FLOOR+", "+ Address.LAST_FLOOR +"].");
        
        this.messageKey = "FlashMessages.IncorrectAddressException";
        this.messageParams = new String[]{
                GameConf.getMaxNumberOfStreets()+"", 
                GameConf.getMaxNumberOfBuildings()+"",
                GameConf.getMaxNumberOfFloors()+""};
    }
    
    public IncorrectAddressException(int street, int building) {
    	this(street, building, 0);
    }

	public String getMessageKey() {
		return messageKey;
	}

	public String[] getMessageParams() {
		return messageParams;
	}
    
    /* Test code. Uncomment for testing. */
//    public static void main(String[] args) {
//    
//        try {
//            throw new IncorrectAddressException();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    
//    }    

}
