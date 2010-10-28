package es.engade.thearsmonsters.model.facades.userfacade.exceptions;

import java.io.Serializable;

import es.engade.thearsmonsters.http.view.applicationobjects.LocalizableMessage;
import es.engade.thearsmonsters.util.exceptions.ModelException;

public class InvalidInvitationCodeException extends ModelException implements LocalizableMessage, Serializable {
    
	private static final long serialVersionUID = 20101023L;
	
	private String messageKey;
	private String[] messageParams;
	
    public InvalidInvitationCodeException() {
        super("The invitation code used is not valid");
        this.messageKey = "ErrorMessages.validationCode.invalid";
    	this.messageParams = new String[]{};
    }

	public String getMessageKey() {
		return messageKey;
	}

	public String[] getMessageParams() {
		return messageParams;
	}
    
}
