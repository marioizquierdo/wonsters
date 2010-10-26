package es.engade.thearsmonsters.util.struts.action;

import java.util.Iterator;
import java.util.Locale;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.Globals;

/**
 * Provides convenience methods to facilitate the implementation of concrete
 * <code>ActionForm</code>s.
 */
public abstract class DefaultActionForm extends ActionForm {

    /**
     * Same as 
     * <code>org.apache.struts.action.Action.getLocale(HttpServletRequest)
     * </code>
     */
    protected Locale getLocale(HttpServletRequest request) {
    
        HttpSession session = request.getSession(false);
        
        if (session == null) {
            return Locale.getDefault();
        }
        
        Locale locale = (Locale) session.getAttribute(Globals.LOCALE_KEY);
        
        if (locale == null) {
            return Locale.getDefault();
        } else {
            return locale;
        }
    
    }
    
    protected boolean reportErrors(HttpServletRequest request, ActionErrors errors, String suffix) {
    	
    	@SuppressWarnings("unchecked")
		Iterator<String> itProp = errors.properties();
    	boolean hasErrors = itProp.hasNext();
        while (itProp.hasNext()) {
        	String prop = itProp.next();
        	ActionMessage am = (ActionMessage)errors.get(prop).next();
        	request.setAttribute(prop + suffix, am.getKey());
        }
        return hasErrors;
        
    }

}
