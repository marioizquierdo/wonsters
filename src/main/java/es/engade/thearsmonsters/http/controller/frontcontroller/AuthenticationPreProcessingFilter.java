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
 * A filter to check if the action to be executed requires that the user had 
 * been authenticated. If the user has not been authenticated and the action 
 * requires it, <code>doProcess</code> returns the <code>ActionForward</code>
 * returned by <code>mapping.findForward("AuthenticationPage")</code>.
 */
public class AuthenticationPreProcessingFilter extends PreProcessingFilter {

    public AuthenticationPreProcessingFilter(PreProcessingFilter nextFilter) {
        super(nextFilter);
    }

    @Override
    protected ActionForward doProcess(HttpServletRequest request, 
            HttpServletResponse response, Action action, ActionForm form,
            ActionMapping mapping) throws IOException, ServletException,
                InternalErrorException {
        
//TODO: Código temporal para poder acceder a partes que requieran autentificación
        return null;

//**************************************************************************
//**********        ANTIGUO CODIGO DEL PREPROCESSING FILTER      ***********
//**************************************************************************
//    	ThearsmonstersActionMapping thearsmonstersActionMapping =
//	        (ThearsmonstersActionMapping) mapping;
//
//	
//	    if (thearsmonstersActionMapping.getAuthenticationRequired()) {
//	    	
//	        if (SessionManager.isUserAuthenticated(request)) {
//	            return null;
//	        } else {    	
//		        return mapping.findForward("AuthenticationPage");
//		    }
//        
//        } else {
//            return null;
//        }
//

    }
}
