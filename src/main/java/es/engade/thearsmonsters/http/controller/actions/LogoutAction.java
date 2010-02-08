package es.engade.thearsmonsters.http.controller.actions;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import es.engade.thearsmonsters.http.controller.session.SessionManager;
import es.engade.thearsmonsters.util.exceptions.InternalErrorException;

public class LogoutAction extends AThearsmonstersDefaultAction {

    public ActionForward doExecuteGameAction(ActionMapping mapping,
        ActionForm form, HttpServletRequest request,
        HttpServletResponse response)
        throws IOException, ServletException, InternalErrorException {
        
        /* Do logout. */
        SessionManager.logout(request, response);
        
        /* Return ActionForward. */    
        return mapping.findForward("AuthenticationPage");
    
    }
    
}
