package es.engade.thearsmonsters.http.view.applicationobjects;


/**
 * A class implementing this interface may be localized using for example the JSTL
 * fmt:message tag because it has a key and a sorted list of params.
 *
 */
public interface LocalizableMessage {
	
	/**
	 * Key for localize the message in the Message.properties file.
	 */
    public String getMessageKey();
    
    /**
     * Params needed to fill {hooks} in the localized message.<br/>
     * The list must be sorted as the hook's order.<br/>
     * @return a List of values of the params or null if there is no params.
     * 		(null is equivalent to an empty list). 
     */
    public Object[] getMessageParams();
}
