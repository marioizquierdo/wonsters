package es.engade.thearsmonsters.http.view.actionforms;

import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

import es.engade.thearsmonsters.util.struts.action.DefaultActionForm;
import es.engade.thearsmonsters.util.struts.action.PropertyValidator;

public class LoginForm extends DefaultActionForm {

    private String login;
    private String password;
    private boolean rememberMyPassword;
    
    public LoginForm() {
        reset();
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
    
    public boolean getRememberMyPassword() {
        return rememberMyPassword;
    }
    
    public void setRememberMyPassword(boolean rememberMyPassword) {
        this.rememberMyPassword = rememberMyPassword;
    }
    
    @Override
    public void reset(ActionMapping mapping, HttpServletRequest request) {
        reset();
    }
    
    @Override
    public ActionErrors validate(ActionMapping mapping,
        HttpServletRequest request) {
        
        ActionErrors errors = new ActionErrors();

        PropertyValidator.validateMandatory(errors, "login", login);
        PropertyValidator.validateMandatory(errors, "password", password);
        
        reportErrors(request, errors, "Error");
        
        return errors;
    }
    
    private void reset() {
        login = null;
        password = null;
        rememberMyPassword = false;
    }

}
