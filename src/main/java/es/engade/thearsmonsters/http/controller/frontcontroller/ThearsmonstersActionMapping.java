package es.engade.thearsmonsters.http.controller.frontcontroller;

import org.apache.struts.action.ActionMapping;

public class ThearsmonstersActionMapping extends ActionMapping {

    private boolean authenticationRequired;
    private boolean adminRequired;

    public ThearsmonstersActionMapping() {
    	authenticationRequired = false;
    }
    
    public boolean getAuthenticationRequired() {
        return authenticationRequired;
    }
    
    public void setAuthenticationRequired(boolean authenticationRequired) {
        this.authenticationRequired = authenticationRequired;
    }

	public boolean getAdminRequired() {
		return adminRequired;
	}

	public void setAdminRequired(boolean adminRequired) {
		this.adminRequired = adminRequired;
	}

}
