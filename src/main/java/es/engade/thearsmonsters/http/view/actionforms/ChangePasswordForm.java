package es.engade.thearsmonsters.http.view.actionforms;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

import es.engade.thearsmonsters.util.struts.action.DefaultActionForm;
import es.engade.thearsmonsters.util.struts.action.PropertyValidator;

public class ChangePasswordForm extends DefaultActionForm {

    private String oldPassword;
    private String newPassword;
    private String retypeNewPassword;
        
    public ChangePasswordForm() {
        reset();
    }
    
    public String getOldPassword() {
        return oldPassword;
    }
    
    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }
    
    public String getNewPassword() {
        return newPassword;
    }
    
    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
    
    public String getRetypeNewPassword() {
        return retypeNewPassword;
    }
    
    public void setRetypeNewPassword(String retypeNewPassword) {
        this.retypeNewPassword = retypeNewPassword;
    }
    
    @Override
    public void reset(ActionMapping mapping, HttpServletRequest request) {
        reset();
    }

    @Override
    public ActionErrors validate(ActionMapping mapping,
        HttpServletRequest request) {
        
        ActionErrors errors = new ActionErrors();

        PropertyValidator.validateMandatory(errors, "oldPassword", oldPassword);
        boolean validatePassword = PropertyValidator.validateMandatory(errors,
            "newPassword", newPassword);
        validatePassword = validatePassword && 
            PropertyValidator.validateMandatory(errors, "retypeNewPassword", 
                retypeNewPassword);

        if (validatePassword && !newPassword.equals(retypeNewPassword)) {
            errors.add("newPassword", 
                new ActionMessage("ErrorMessages.newPassword.doNotMatch"));
        }

        saveErrorsFixed(request, errors);
        return errors;
        
    }
    
    private void reset() {
        oldPassword = null;
        newPassword = null;
        retypeNewPassword = null;
    }
    
}
