package es.engade.thearsmonsters.util.struts.action;

import java.io.Serializable;
import java.util.Locale;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.Globals;

import es.engade.thearsmonsters.http.controller.util.SaveErrorsFixed;

/**
 * Provides convenience methods to facilitate the implementation of concrete
 * <code>ActionForm</code>s.
 */
public abstract class DefaultActionForm extends ActionForm implements Serializable {
    private static final long serialVersionUID = -3602428798493449883L;

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
    
    protected boolean saveErrorsFixed(HttpServletRequest request, ActionErrors errors, String suffix) {
    	return SaveErrorsFixed.saveErrors(request, errors, suffix);
    }
    protected boolean saveErrorsFixed(HttpServletRequest request, ActionErrors errors) {
    	return SaveErrorsFixed.saveErrors(request, errors);
    }

}
