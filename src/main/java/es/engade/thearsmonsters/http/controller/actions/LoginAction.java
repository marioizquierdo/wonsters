package es.engade.thearsmonsters.http.controller.actions;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import es.engade.thearsmonsters.http.controller.session.SessionManager;
import es.engade.thearsmonsters.http.view.actionforms.LoginForm;
import es.engade.thearsmonsters.model.facades.userfacade.exceptions.IncorrectPasswordException;
import es.engade.thearsmonsters.util.exceptions.InstanceNotFoundException;
import es.engade.thearsmonsters.util.exceptions.InternalErrorException;

public class LoginAction extends AThearsmonstersDefaultAction {

    public ActionForward doExecute(ActionMapping mapping,
        ActionForm form, HttpServletRequest request,
        HttpServletResponse response)
        throws IOException, ServletException, InternalErrorException {
        
        /* Get data. */
        LoginForm loginForm = (LoginForm) form;
        String loginName = loginForm.getLoginName();
        String password = loginForm.getPassword();
        boolean rememberMyPassword = loginForm.getRememberMyPassword();

        /* Do login. */
        ActionMessages errors = new ActionMessages();
        
        try {

            SessionManager.login(request, response, loginName, password,
                rememberMyPassword, false);
                
        } catch (InstanceNotFoundException e) {
            errors.add("loginName", new ActionMessage(
                "ErrorMessages.loginName.notFound"));
        } catch (IncorrectPasswordException e) {
            errors.add("password", new ActionMessage(
                "ErrorMessages.password.incorrect"));
        }
        
        /* Return ActionForward. */
        if (errors.isEmpty()) {
            return mapping.findForward("GameStart");
        } else {
            saveErrors(request, errors);            
            return new ActionForward(mapping.getInput());
        }
     
    }
    
}
