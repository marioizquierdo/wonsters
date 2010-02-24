package es.engade.thearsmonsters.http.controller.frontcontroller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import es.engade.thearsmonsters.http.controller.session.SessionManager;
import es.engade.thearsmonsters.util.exceptions.InternalErrorException;

/**
 * A filter to guarantee that the session will have the necessary objects 
 * if the user has been authenticated or had selected "remember my password" 
 * in the past.
 */
public class SessionPreProcessingFilter extends PreProcessingFilter {

    public SessionPreProcessingFilter(PreProcessingFilter nextFilter) {
        super(nextFilter);
    }

    @Override
    protected ActionForward doProcess(HttpServletRequest request, 
        HttpServletResponse response, Action action, ActionForm form,
        ActionMapping mapping) throws IOException, ServletException,
            InternalErrorException {
            
        SessionManager.touchSession(request);

        return null;
            
    }

}
