package es.engade.thearsmonsters.http.controller.actions.userprofile;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import es.engade.thearsmonsters.http.controller.actions.ThearsmonstersDefaultAction;
import es.engade.thearsmonsters.http.controller.session.SessionManager;
import es.engade.thearsmonsters.http.controller.util.FlashMessage;
import es.engade.thearsmonsters.http.view.actionforms.ChangePasswordForm;
import es.engade.thearsmonsters.model.facades.userfacade.exceptions.IncorrectPasswordException;
import es.engade.thearsmonsters.util.exceptions.InternalErrorException;

public class ChangePasswordAction extends ThearsmonstersDefaultAction {

    @Override
    public ActionForward doExecuteGameAction(ActionMapping mapping,
        ActionForm form, HttpServletRequest request,
        HttpServletResponse response)
        throws IOException, ServletException, InternalErrorException {
        
        /* Get data. */
        ChangePasswordForm changePasswordForm = (ChangePasswordForm) form;
        String oldPassword = changePasswordForm.getOldPassword();
        String newPassword = changePasswordForm.getNewPassword();

        /* Do login. */
        ActionMessages errors = new ActionMessages();        
            
        try {
            SessionManager.changePassword(request, response, oldPassword, newPassword);
        } catch (IncorrectPasswordException e) {
            errors.add("oldPassword", new ActionMessage(
                "ErrorMessages.password.incorrect"));
        } 
        
        /* Return ActionForward. */
        if (errors.isEmpty()) {
        	FlashMessage.show(request, FlashMessage.Status.INFO);
        	return mapping.findForward("GameStart");
        } else {
        	saveErrorsFixed(request, errors);
            saveErrors(request, errors);
            return new ActionForward(mapping.getInput());
        }
        
    }
    
}
