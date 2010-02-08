package es.engade.thearsmonsters.http.view.actionforms;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMessage;

import es.engade.thearsmonsters.util.struts.action.PropertyValidator;

public final class ThearsmonstersPropertyValidator {

    private final static String INCORRECT_EMAIL_ADDRESS = 
        "ErrorMessages.emailAddress.incorrect";
        
    private ThearsmonstersPropertyValidator() {}

    public final static void validateEmailAddress(ActionErrors errors,
        String propertyName, String propertyValue, boolean mandatory) {

        if (mandatory) {
        	PropertyValidator.validateMandatory(errors, propertyName, propertyValue);
        }
        
        if(propertyValue != null && propertyValue != "") {
            
            /**
             * Check that "propertyValue" contains the sign "@" surrounded by
             * at least one character.
             */
            int atSignIndex = propertyValue.indexOf("@");
            
            if ( (atSignIndex < 1) || 
                 (atSignIndex == propertyValue.length() - 1) ) {
                 errors.add(propertyName, 
                     new ActionMessage(INCORRECT_EMAIL_ADDRESS));
            }
            
        }
        
    }

}
