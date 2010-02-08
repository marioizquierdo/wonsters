package es.engade.thearsmonsters.model.facades.userfacade.exceptions;

import es.engade.thearsmonsters.http.view.applicationobjects.LocalizableMessage;
import es.engade.thearsmonsters.util.exceptions.ModelException;

public class FullPlacesException extends ModelException implements LocalizableMessage {
    
	private static final long serialVersionUID = 200912142050L;
	
	private String messageKey;
	private String[] messageParams;
	
    public FullPlacesException() {
        super("There are no free space in the monsters city. Please try to register later");
        this.messageKey = "FlashMessage.FullPlacesException";
    	this.messageParams = new String[]{};
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
//            throw new FullPlacesException();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    
//    }    

}
