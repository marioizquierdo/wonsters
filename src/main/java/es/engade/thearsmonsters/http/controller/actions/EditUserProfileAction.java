package es.engade.thearsmonsters.http.controller.actions;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.Globals;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import es.engade.thearsmonsters.http.view.actionforms.UserProfileForm;
import es.engade.thearsmonsters.http.view.applicationobjects.Languages;
import es.engade.thearsmonsters.model.entities.user.User;
import es.engade.thearsmonsters.model.entities.user.UserDetails;
import es.engade.thearsmonsters.model.facades.userfacade.UserFacade;
import es.engade.thearsmonsters.model.facades.userfacade.UserFacadeMock;
import es.engade.thearsmonsters.util.exceptions.InternalErrorException;
    
public class EditUserProfileAction extends AThearsmonstersDefaultAction {

    @Override
    public ActionForward doExecuteGameAction(ActionMapping mapping,
        ActionForm form, HttpServletRequest request,
        HttpServletResponse response)
        throws IOException, ServletException, InternalErrorException {
        
        /* 
         * Insert list of languages, ordered by current language.
         */
        request.setAttribute("languages", 
            Languages.getLanguages(getLocale(request).getLanguage()));
        
        /* Fill "form". */
        UserProfileForm userProfileForm = (UserProfileForm) form;
        
        /*
         * If the request is to allow the user to correct errors in the form,
         * "userProfileForm" must not be modified.
         */         
        String action = userProfileForm.getAction();
                
        if (request.getAttribute(Globals.ERROR_KEY) == null) {
            
            /* 
             * Set "language" to his/her current selection. 
             */
            userProfileForm.setLanguage(getLocale(request).getLanguage());

            /*
             * If the user is updating his/her profile, set the rest of
             * attributes.
             */           
            if ("UPDATE".equals(action)) {
            
            	UserFacade userFacade = new UserFacadeMock();
                User userProfile = userFacade.
                        findUserProfile();
                UserDetails userProfileDetails = 
                    userProfile.getUserDetails();

                userProfileForm.setFirstName(
                    userProfileDetails.getFirstName());
                userProfileForm.setSurname(userProfileDetails.getSurname());
                userProfileForm.setEmail(userProfileDetails.getEmail());
                                                                   
            }
            
        }
        
        /* Return ActionForward. */
        if ("UPDATE".equals(action)) {
            return mapping.findForward("UpdateUserProfileDetailsForm");
        } else {
            return mapping.findForward("RegisterUserForm");
        }
        
    }

}
