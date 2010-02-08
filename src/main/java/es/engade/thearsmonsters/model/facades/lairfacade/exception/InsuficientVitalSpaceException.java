package es.engade.thearsmonsters.model.facades.lairfacade.exception;

import es.engade.thearsmonsters.http.view.applicationobjects.LocalizableMessage;
import es.engade.thearsmonsters.util.exceptions.ModelException;

public class InsuficientVitalSpaceException  extends ModelException implements LocalizableMessage {
    
	private static final long serialVersionUID = 200912142039L;
	
	private int vitalSpaceNeeded;
	private int vitalSpaceAvaliable;
	private String messageKey;
	private String[] messageParams;
	
    public InsuficientVitalSpaceException(int vitalSpaceNeeded, int vitalSpaceAvaliable) {
        super("Insuficient vital space: you need "+vitalSpaceNeeded+" but you only have "+vitalSpaceAvaliable);
        this.vitalSpaceNeeded = vitalSpaceNeeded;
        this.vitalSpaceAvaliable = vitalSpaceAvaliable;
        
        this.messageKey = "FlashMessage.InsuficientVitalSpaceException";
        this.messageParams = new String[]{vitalSpaceNeeded+"", vitalSpaceAvaliable+""};
    }

	public int getVitalSpaceNeeded() {
		return vitalSpaceNeeded;
	}

	public int getVitalSpaceAvaliable() {
		return vitalSpaceAvaliable;
	}

	public String getMessageKey() {
		return messageKey;
	}

	public String[] getMessageParams() {
		return messageParams;
	} 

}
