package es.engade.thearsmonsters.http.view.actionforms;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;

import es.engade.thearsmonsters.util.struts.action.DefaultActionForm;
import es.engade.thearsmonsters.util.struts.action.PropertyValidator;

public class LoginForm extends DefaultActionForm {

    private String loginName;
    private String password;
    private boolean rememberMyPassword;
    
    public LoginForm() {
        reset();
    }
    
    public String getLoginName() {
        return loginName;
    }
    
    public void setLoginName(String loginName) {
        this.loginName = loginName.trim();
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

        PropertyValidator.validateMandatory(errors, "loginName", loginName);
        PropertyValidator.validateMandatory(errors, "password", password);
        
        return errors;
        
    }
    
    private void reset() {
        loginName = null;
        password = null;
        rememberMyPassword = false;
    }

}
