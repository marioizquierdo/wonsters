package es.engade.thearsmonsters.http.view.actionforms;

import java.util.Arrays;
import java.util.Collection;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

import es.engade.thearsmonsters.http.view.applicationobjects.Languages;
import es.engade.thearsmonsters.http.view.applicationobjects.PromotionalValidation;
import es.engade.thearsmonsters.util.struts.action.DefaultActionForm;
import es.engade.thearsmonsters.util.struts.action.PropertyValidator;

public class UserProfileForm extends DefaultActionForm {

	private static final long serialVersionUID = 7348409560759602652L;

	public final static String REGISTER_ACTION = "REGISTER";
    public final static String UPDATE_ACTION = "UPDATE";
    
    private final static Collection<String> ACTION_TYPES = Arrays.asList(
        new String[] {REGISTER_ACTION, UPDATE_ACTION});

    private String action;
    private String login;
    private String password;
    private String retypePassword;
    private String invitationCode;
    private String firstName;
    private String surname;
    private String email;
    private String language;
    
    public UserProfileForm() {
        reset();
    }
    
    public String getAction() {
        return action;
    }
    
    public void setAction(String action) {
        this.action = action;
    }
    
    public String getLogin() {
        return login;
    }
    
    public void setLogin(String login) {
        this.login = login.trim();
    }
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    
    public String getRetypePassword() {
        return retypePassword;
    }
    
    public void setRetypePassword(String retypePassword) {
        this.retypePassword = retypePassword;
    }
    
    public String getFirstName() {
        return firstName;
    }
    
    public void setFirstName(String firstName) {
		this.firstName = trim(firstName);
    }
    
    public String getSurname() {
        return surname;
    }
    
    public void setSurname(String surname) {
        this.surname = trim(surname);
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = trim(email);
    }
    
    public String getLanguage() {
        return language;
    }
    
    public void setLanguage(String language) {
        this.language = language;
    }

    public String getInvitationCode() {
		return invitationCode;
	}

	public void setInvitationCode(String invitationCode) {
		this.invitationCode = invitationCode;
	}

	@Override
    public void reset(ActionMapping mapping, HttpServletRequest request) {
        reset();
    }

    @Override
    public ActionErrors validate(ActionMapping mapping,
        HttpServletRequest request) {
        
        ActionErrors errors = new ActionErrors();
        
        PropertyValidator.validateString(errors, "action", action, true, 
            ACTION_TYPES);

        if (REGISTER_ACTION.equals(action)) {
            PropertyValidator.validateMandatory(errors, "login", login);
            PropertyValidator.validateMandatory(errors, "email", email);
            boolean validatePassword = PropertyValidator.validateMandatory(
                errors, "password", password);
            validatePassword = validatePassword &&
                PropertyValidator.validateMandatory(errors, "retypePassword", 
                    retypePassword);
            if (validatePassword && !password.equals(retypePassword)) {
                errors.add("password", 
                    new ActionMessage("ErrorMessages.password.doNotMatch"));
            }
            if (!PromotionalValidation.validate(invitationCode, true)) {
            	errors.add("invitationCode", 
                        new ActionMessage("ErrorMessages.validationCode.invalid"));
            }
            
        }
            
        ThearsmonstersPropertyValidator.validateEmailAddress(errors, "email", email, false);
        PropertyValidator.validateString(errors, "language", language, true, 
            Languages.getLanguageCodes());
        
        saveErrorsFixed(request, errors, "RegError");

        return errors;
        
    }
    
    private void reset() {
        action = null;
        login = null;
        password = null;
        retypePassword = null;
        invitationCode = null;
        firstName = null;
        surname = null;
        email = null;
        language = null;
    }
    
    private String trim(String param) {
    	if(param == null) {
    		return null;
    	} else {
    		return param.trim();
    	}
    }

}
