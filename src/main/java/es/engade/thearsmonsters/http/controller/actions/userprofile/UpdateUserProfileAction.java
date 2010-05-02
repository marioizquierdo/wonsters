package es.engade.thearsmonsters.http.controller.actions.userprofile;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import es.engade.thearsmonsters.http.controller.actions.ThearsmonstersDefaultAction;
import es.engade.thearsmonsters.http.controller.session.SessionManager;
import es.engade.thearsmonsters.http.controller.util.FlashMessage;
import es.engade.thearsmonsters.http.view.actionforms.UserProfileForm;
import es.engade.thearsmonsters.model.entities.user.UserDetails;
import es.engade.thearsmonsters.util.exceptions.InternalErrorException;
    
public class UpdateUserProfileAction extends ThearsmonstersDefaultAction {

    @Override
    public ActionForward doExecuteGameAction(ActionMapping mapping,
        ActionForm form, HttpServletRequest request,
        HttpServletResponse response)
        throws IOException, ServletException, InternalErrorException {
        
        /* Get data. */
        UserProfileForm userProfileForm = (UserProfileForm) form;
        UserDetails userDetails = new UserDetails(
            userProfileForm.getFirstName(), userProfileForm.getSurname(),
            userProfileForm.getEmail(), userProfileForm.getLanguage());
            
        /* Update user profile details. */                    
        SessionManager.updateUserProfileDetails(request, userDetails);
        
        /* Notify success message */
        FlashMessage.show(request, null);
        
        /* Return ActionForward. */
        return mapping.findForward("EditUserProfile");
        
    }

}
