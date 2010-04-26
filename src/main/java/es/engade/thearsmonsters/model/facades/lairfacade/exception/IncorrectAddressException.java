package es.engade.thearsmonsters.model.facades.lairfacade.exception;

import es.engade.thearsmonsters.http.view.applicationobjects.LocalizableMessage;
import es.engade.thearsmonsters.model.util.GameConf;

public class IncorrectAddressException extends IndexOutOfBoundsException implements LocalizableMessage {
    
	private static final long serialVersionUID = 200912142039L;
	
	private String messageKey;
    private String[] messageParams;
	
    public IncorrectAddressException() {
    	
        super("Address out of bounds. Allowed values: " +
        		"street [0, "+   GameConf.getMaxNumberOfStreets()    +"], " +
        		"building [0, "+ GameConf.getMaxNumberOfBuildings() +"], " +
        		"floor [0, "+    GameConf.getMaxNumberOfFloors()   +"].");
        
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
                "street [0, "+   GameConf.getMaxNumberOfStreets()    +"], " +
                "building [0, "+ GameConf.getMaxNumberOfBuildings() +"], " +
                "floor [0, "+    GameConf.getMaxNumberOfFloors()   +"].");
        
        this.messageKey = "FlashMessages.IncorrectAddressException";
        this.messageParams = new String[]{
                GameConf.getMaxNumberOfStreets()+"", 
                GameConf.getMaxNumberOfBuildings()+"",
                GameConf.getMaxNumberOfFloors()+""};
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
