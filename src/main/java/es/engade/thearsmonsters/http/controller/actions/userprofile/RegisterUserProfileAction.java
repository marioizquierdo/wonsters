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
import es.engade.thearsmonsters.http.view.actionforms.UserProfileForm;
import es.engade.thearsmonsters.model.entities.user.UserDetails;
import es.engade.thearsmonsters.model.facades.userfacade.exceptions.FullPlacesException;
import es.engade.thearsmonsters.model.facades.userfacade.exceptions.IncorrectPasswordException;
import es.engade.thearsmonsters.model.facades.userfacade.exceptions.InvalidInvitationCodeException;
import es.engade.thearsmonsters.util.exceptions.DuplicateInstanceException;
import es.engade.thearsmonsters.util.exceptions.InstanceNotFoundException;
import es.engade.thearsmonsters.util.exceptions.InternalErrorException;
    
public class RegisterUserProfileAction extends ThearsmonstersDefaultAction {

    @Override
    public ActionForward doExecuteGameAction(ActionMapping mapping,
        ActionForm form, HttpServletRequest request,
        HttpServletResponse response)
        throws IOException, ServletException, InternalErrorException {
            
        /* Get data. */
        UserProfileForm userProfileForm = (UserProfileForm) form;
        String login = userProfileForm.getLogin();
        String clearPassword = userProfileForm.getPassword();
        String invitationCode = userProfileForm.getInvitationCode();

        UserDetails userProfileDetails = new UserDetails(
        		userProfileForm.getFirstName(),userProfileForm.getSurname(),
        		userProfileForm.getEmail(),userProfileForm.getLanguage());
                                                                    
        
        /* Register user. */            
        ActionMessages errors = new ActionMessages();
          
        try {
        	
            SessionManager.registerUser(request, login, clearPassword,
                userProfileDetails, invitationCode);

        } catch (DuplicateInstanceException e) {
            errors.add("login", new ActionMessage("ErrorMessages.login.alreadyExists"));
        } catch (FullPlacesException e) {
        	FlashMessage.showError(request, e);
		} catch (InvalidInvitationCodeException e){
			errors.add("invitationCode", new ActionMessage("ErrorMessages.validationCode.invalid"));
		}
        
        /* Return ActionForward. */
        if (errors.isEmpty()) {
        	try {
				SessionManager.login(request, response, login, clearPassword, true, false);
			} catch (InstanceNotFoundException e) {
				throw new InternalErrorException(e);
			} catch (IncorrectPasswordException e) {
				throw new InternalErrorException(e);
			}
			FlashMessage.show(request, "FlashMessage.WelcomeToTheGame", login, FlashMessage.Status.GOOD_NEW, null);
			FlashMessage.show(request, "FlashMessage.GameStartInfo", "", FlashMessage.Status.INFO, null);
			FlashMessage.show(request, "FlashMessage.GameStartWarning", "", FlashMessage.Status.ERROR, null);
            return mapping.findForward("GameStart");
        } else {
        	reportErrors(request, errors, "RegError");
            saveErrors(request, errors);
            return new ActionForward(mapping.getInput());
        }
        
    }

}
