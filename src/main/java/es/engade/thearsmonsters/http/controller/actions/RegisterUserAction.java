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
import es.engade.thearsmonsters.http.controller.util.FlashMessage;
import es.engade.thearsmonsters.http.view.actionforms.UserProfileForm;
import es.engade.thearsmonsters.model.entities.user.UserDetails;
import es.engade.thearsmonsters.model.facades.userfacade.exceptions.FullPlacesException;
import es.engade.thearsmonsters.model.facades.userfacade.exceptions.IncorrectPasswordException;
import es.engade.thearsmonsters.util.exceptions.DuplicateInstanceException;
import es.engade.thearsmonsters.util.exceptions.InstanceNotFoundException;
import es.engade.thearsmonsters.util.exceptions.InternalErrorException;
    
public class RegisterUserAction extends AThearsmonstersDefaultAction {

    @Override
    public ActionForward doExecuteGameAction(ActionMapping mapping,
        ActionForm form, HttpServletRequest request,
        HttpServletResponse response)
        throws IOException, ServletException, InternalErrorException {
            
        /* Get data. */
        UserProfileForm userProfileForm = (UserProfileForm) form;
        String loginName = userProfileForm.getLoginName();
        String clearPassword = userProfileForm.getPassword();
        UserDetails userProfileDetails = new UserDetails(
            userProfileForm.getFirstName(), userProfileForm.getSurname(),
            userProfileForm.getEmail(), userProfileForm.getLanguage());
                                                                    
        
        /* Register user. */            
        ActionMessages errors = new ActionMessages();
          
        try {
            SessionManager.registerUser(request, loginName, clearPassword,
                userProfileDetails);

        } catch (DuplicateInstanceException e) {
            errors.add("loginName",
                new ActionMessage("ErrorMessages.loginName.alreadyExists"));
        } catch (FullPlacesException e) {
        	FlashMessage.showError(request, e);
		}            
        
        /* Return ActionForward. */
        if (errors.isEmpty()) {
        	try {
				SessionManager.login(request, response, loginName, clearPassword, true, false);
			} catch (InstanceNotFoundException e) {
				throw new InternalErrorException(e);
			} catch (IncorrectPasswordException e) {
				throw new InternalErrorException(e);
			}
			FlashMessage.show(request, "FlashMessage.WelcomeToTheGame", loginName, FlashMessage.Status.GOOD_NEW, null);
			FlashMessage.show(request, "FlashMessage.GameStartInfo", "", FlashMessage.Status.INFO, null);
			FlashMessage.show(request, "FlashMessage.GameStartWarning", "", FlashMessage.Status.ERROR, null);
            return mapping.findForward("GameStart");
        } else {
            saveErrors(request, errors);
            return new ActionForward(mapping.getInput());
        }
        
    }

}
