package es.engade.thearsmonsters.http.controller.actions.admin;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import es.engade.thearsmonsters.http.controller.actions.ThearsmonstersDefaultAction;
import es.engade.thearsmonsters.http.controller.session.SessionManager;
import es.engade.thearsmonsters.test.facade.TestFacade;
import es.engade.thearsmonsters.util.exceptions.InternalErrorException;
/**
 * Action for insert testing data into the game. Only rules when the DB table UserProfile is empty.
 * There is no view links to this action, it can only be activated by the url.
 * No login is required.
 */
public class InsertFixtures extends ThearsmonstersDefaultAction {
	
    @Override
    public ActionForward doExecute(ActionMapping mapping,
            ActionForm form, HttpServletRequest request,
            HttpServletResponse response)
        throws IOException, ServletException, InternalErrorException {

    	TestFacade testFacade = new TestFacade();
    	
        /* Create fixtures */
    	try {
    		testFacade.addFixtureDatas(logins());
    		
			SessionManager.login(request, response, "a", "a", true, false); // se supone que se inserta el usuario a, contraseña a
			return mapping.findForward("GameStart");
			
		} catch (Exception e) {
			e.printStackTrace();
	    	return mapping.findForward("InternalError");
		}
        
    }
    
    private List<String> logins() {
    	// a is the admin user by default
		return Arrays.asList(new String[]{"a", "b", "c", "d"});
    }

}
