package es.engade.thearsmonsters.model.facades.lairfacade.exception;

import es.engade.thearsmonsters.http.view.applicationobjects.LocalizableMessage;
import es.engade.thearsmonsters.util.exceptions.ModelException;

public class InsuficientGarbageException  extends ModelException  implements LocalizableMessage {
    
	private static final long serialVersionUID = 200912142039L;
	
	private int garbageNeeded;
	private int garbageAvaliable;
	private String messageKey;
	private String[] messageParams;
	
    public InsuficientGarbageException(int garbageNeeded, int garbageAvaliable) {
        super("Insuficient garbage: you need "+garbageNeeded+" but you only have "+garbageAvaliable);
        this.garbageNeeded = garbageNeeded;
        this.garbageAvaliable = garbageAvaliable;
        
        this.messageKey = "FlashMessage.InsuficientGarbageException";
        this.messageParams = new String[]{garbageNeeded+"", garbageAvaliable+""};
    }

	public int getGarbageNeeded() {
		return garbageNeeded;
	}

	public int getGarbageAvaliable() {
		return garbageAvaliable;
	}  

	public String getMessageKey() {
		return messageKey;
	}

	public String[] getMessageParams() {
		return messageParams;
	}

}
