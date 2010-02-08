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
     * Params needed for fill the {hooks} in the localized message. <br/>
     * The list must be sorted in the same order than the hooks. <br/>
     * @return a List of values of the params or null if there is no params.
     * 		(null is equivalent to an empty list). 
     */
    public String[] getMessageParams();
}
