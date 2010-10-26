package es.engade.thearsmonsters.http.controller.actions;

import java.io.IOException;
import java.util.Iterator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import es.engade.thearsmonsters.http.controller.session.SessionManager;
import es.engade.thearsmonsters.util.exceptions.InternalErrorException;
import es.engade.thearsmonsters.util.struts.action.DefaultAction;


/**
 * A base in game Action. Be sure that myLair is in the session when the user
 * is logged and clear session cached data in case of exception.<br/>
 * Subclasses just need to redefine doExecuteGameAction.<br/>
 * If a subclass needs to redefine doOnException, must call the extraDoOnException method inside.
 * A Struts default org.apache.struts.actions.ForwardAction may be changed for this one if
 * 	the view uses the myLair session property.
 */
public class ThearsmonstersDefaultAction extends DefaultAction {
	
    @Override
    protected ActionForward doExecute(ActionMapping mapping,
            ActionForm form, HttpServletRequest request,
            HttpServletResponse response)
            throws IOException, ServletException, InternalErrorException {
    	
    	// Common stuff here (actions that appear on all thearsmonsters requests)
    	
    	return doExecuteGameAction(mapping, form, request, response);
    }
    
    /**
     * Extra specific on error stuff for Thearsmonsters Actions 
     */
    @Override
    protected void extraDoOnException(ActionMapping mapping,
        ActionForm form, HttpServletRequest request,
        HttpServletResponse response, Exception exception)
        throws IOException, ServletException {
    	
    	// Some session data may be unstable. Delete it.
    	SessionManager.removeMyLair(request);
    	
    }
    
    /**
     * Normal Thearsmonsters actions must only extend that method.
     * If an action reimplements doExecute, this one is not necesary. 
     */
    @SuppressWarnings("deprecation")
    protected ActionForward doExecuteGameAction(ActionMapping mapping,
            ActionForm form, HttpServletRequest request,
            HttpServletResponse response)
            throws IOException, ServletException, InternalErrorException {
    	// By default, acts like the standard org.apache.struts.actions.ForwardAction
    	ActionForward retVal = new ActionForward(mapping.getParameter());
        retVal.setContextRelative(true);
        return retVal;
    }

    protected boolean reportErrors(HttpServletRequest request, ActionMessages errors, String suffix) {
    	
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
