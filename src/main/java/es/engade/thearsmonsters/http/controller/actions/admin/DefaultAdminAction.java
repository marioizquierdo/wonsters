package es.engade.thearsmonsters.http.controller.actions.admin;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import es.engade.thearsmonsters.util.struts.action.DefaultAction;
/**
 * Action for insert testing data into de game. Only alloowed for admin users.
 */
public abstract class DefaultAdminAction extends DefaultAction {
	
	/**
	 *  Guarda los errores que se hayan producido y devuelve el findForward
	 */
	protected ActionForward saveMessagesAndForward(List<String> success, 
    		List<String> errors, ActionMapping mapping, HttpServletRequest request) {
    	request.setAttribute("successMessages", success);
    	request.setAttribute("errorMessages", errors);
    	return mapping.findForward("AdminShowActionResults");
    }

}
