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
import es.engade.thearsmonsters.util.configuration.AppContext;
import es.engade.thearsmonsters.util.exceptions.InstanceNotFoundException;
import es.engade.thearsmonsters.util.exceptions.InternalErrorException;

public class FindUser extends DefaultAdminAction {

    @Override
    public ActionForward doExecute(ActionMapping mapping,
        ActionForm form, HttpServletRequest request,
        HttpServletResponse response)
        throws IOException, ServletException, InternalErrorException {
        
        ActionMessages errors = new ActionMessages();
    	
    	/* Get Data */
        String login = request.getParameter("login");
		if(login=="" || login==null) {
			errors.add("login",
	                new ActionMessage("ErrorMessages.login.notFound"));
			saveErrors(request, errors);            
            return new ActionForward(mapping.getInput());
		}

        /* Find UserProfile. */
    	try {
    		
    	    UserFacade userFacade = (UserFacade) AppContext.getInstance().getAppContext().getBean("userFacade");
    		
			User userProfile = userFacade.findUserProfile(login);
			   
			/* Set attribute login on Request */
    		request.setAttribute("userProfile", userProfile);
			
		} catch (InstanceNotFoundException e) {
            errors.add("login",
                new ActionMessage("ErrorMessages.login.notFound"));
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
