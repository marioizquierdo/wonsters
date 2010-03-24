package es.engade.thearsmonsters.http.controller.actions.admin;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import es.engade.thearsmonsters.model.facades.userfacade.UserFacade;
import es.engade.thearsmonsters.util.configuration.AppContext;
import es.engade.thearsmonsters.util.exceptions.InstanceNotFoundException;
import es.engade.thearsmonsters.util.exceptions.InternalErrorException;

public class ToBanUser extends DefaultAdminAction {
	
    @Override
    public ActionForward doExecute(ActionMapping mapping,
            ActionForm form, HttpServletRequest request,
            HttpServletResponse response)
        throws IOException, ServletException, InternalErrorException {
    	
    	/* List of messages to put in the request 
    	 * (expected by AdminShowActionResultsContent.jspx) */
    	List<String> success = new ArrayList<String>();
    	List<String> errors = new ArrayList<String>();
        	
    	/* Get Data */
        String login = request.getParameter("login");
		if(login=="" || login==null) {
			errors.add("AdminShowActionResults.ToBanUser.loginFail");
	        return  saveMessagesAndForward(success, errors, mapping, request);
		}

        /* Delete UserProfile (and his Lair, monsters, etc). */
    	try {
    	    ClassPathXmlApplicationContext appContext = AppContext.getInstance().getAppContext();
    	    UserFacade userFacade = (UserFacade) appContext.getBean("userFacade");

    		userFacade.removeUserProfile(login);
			success.add("AdminShowActionResults.ToBanUser.Success");
			
		} catch (InstanceNotFoundException e) {
			errors.add("ErrorMessages.login.notFound");
		}
        
        /* Return ActionForward.*/
        return saveMessagesAndForward(success, errors, mapping, request);
        
    }

}
