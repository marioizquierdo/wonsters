package es.engade.thearsmonsters.model.facades.lairfacade.exception;

import es.engade.thearsmonsters.http.view.applicationobjects.LocalizableMessage;
import es.engade.thearsmonsters.model.util.GameConf;
import es.engade.thearsmonsters.util.exceptions.ModelException;

public class MaxEggsException  extends ModelException implements LocalizableMessage {
	
	private static final long serialVersionUID = 200912142039L;
	
	private String messageKey;
	private String[] messageParams;
    
    public MaxEggsException() {
        super("You can't store more eggs. Please, sell another egg before or" +
        		"enlarge the GameOpt/maxEggs parameter in the game configuration (web.xml)");
        this.messageKey = "FlashMessage.MaxEggsException";
        
        this.messageParams = new String[]{GameConf.getMaxEggs()+""};
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
//            throw new MaxEggsException();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    
//    }    

}
