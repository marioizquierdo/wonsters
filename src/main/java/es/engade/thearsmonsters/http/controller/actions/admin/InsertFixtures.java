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

import es.engade.thearsmonsters.http.controller.session.SessionManager;
import es.engade.thearsmonsters.test.facade.TestFacade;
import es.engade.thearsmonsters.util.exceptions.InternalErrorException;
import es.engade.thearsmonsters.util.struts.action.DefaultAction;
/**
 * Action for insert testing data into de game. Only rules when the DB table UserProfile is empty.
 * There is no view links to this action, it can only be activated by the url  
 * No login is required.
 */
public class InsertFixtures extends DefaultAction {
	
    @Override
    public ActionForward doExecute(ActionMapping mapping,
            ActionForm form, HttpServletRequest request,
            HttpServletResponse response)
        throws IOException, ServletException, InternalErrorException {

    	TestFacade testFacade = new TestFacade();
    	
        /* Create fixtures */
    	try {
    		testFacade.addFixtureDatas(loginNames());
    		
			SessionManager.login(request, response, "a", "a", true, false); // se supone que se inserta el usuario a, contraseña a
			return mapping.findForward("GameStart");
			
		} catch (Exception e) {
			System.out.println(e.toString());
			System.out.println(e.getMessage());
	    	return mapping.findForward("InternalError");
		}
        
    }
    
    private List<String> loginNames() {
		List<String> loginNames = new ArrayList<String>();
		loginNames.add("a"); // a is the admin user by default
		loginNames.add("b");
		loginNames.add("c");
		loginNames.add("d");
		return loginNames;
    }

}
