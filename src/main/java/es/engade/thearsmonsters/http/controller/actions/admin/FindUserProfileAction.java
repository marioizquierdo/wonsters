package es.engade.thearsmonsters.http.controller.actions.admin;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import es.engade.thearsmonsters.model.entities.user.User;
import es.engade.thearsmonsters.model.facades.userfacade.UserFacade;
import es.engade.thearsmonsters.model.facades.userfacade.UserFacadeMock;
import es.engade.thearsmonsters.util.exceptions.InstanceNotFoundException;
import es.engade.thearsmonsters.util.exceptions.InternalErrorException;

public class FindUserProfileAction extends DefaultAdminAction {

    public ActionForward doExecute(ActionMapping mapping,
        ActionForm form, HttpServletRequest request,
        HttpServletResponse response)
        throws IOException, ServletException, InternalErrorException {
        
        ActionMessages errors = new ActionMessages();
    	
    	/* Get Data */
        String loginName = request.getParameter("loginName");
		if(loginName=="" || loginName==null) {
			errors.add("loginName",
	                new ActionMessage("ErrorMessages.loginName.notFound"));
			saveErrors(request, errors);            
            return new ActionForward(mapping.getInput());
		}

        /* Find UserProfile. */
    	try {
    		
    		UserFacade userFacade = new UserFacadeMock();
    		
			User userProfile = userFacade.findUserProfile(loginName);
			   
			/* Set attribute loginName on Request */
    		request.setAttribute("userProfile", userProfile);
			
		} catch (InstanceNotFoundException e) {
            errors.add("loginName",
                new ActionMessage("ErrorMessages.loginName.notFound"));
		}
        
        /* Return ActionForward.*/
        if (errors.isEmpty()) {
            return mapping.findForward("AdminShowUserProfile");
        } else {
            saveErrors(request, errors);            
            return new ActionForward(mapping.getInput());
        }
        
    }
	
}
