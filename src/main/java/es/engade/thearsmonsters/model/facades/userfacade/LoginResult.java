package es.engade.thearsmonsters.model.facades.userfacade;

import java.io.Serializable;

import es.engade.thearsmonsters.model.entities.lair.Lair;

public class LoginResult implements Serializable {

	private static final long serialVersionUID = 200912142051L;
	
	private Lair lair;
	private String login;
	private String firstName;
    private String encryptedPassword;
    private String language;
    
    private boolean lairPersistentState;

    public LoginResult(Lair lair, String login, String firstName, String encryptedPassword,
        String language) {
        
    	this.lair = lair;
    	this.login = login;
    	this.lairPersistentState = true;
    	this.firstName = firstName;
        this.encryptedPassword = encryptedPassword;
        this.language = language;

    }
    
    public Lair getLair() {
        return lair;
    }

    public void setLair(Lair lair) {
        this.lair = lair;
        lairPersistentState = true;
    }
    
    public boolean detachLair() {
        boolean oldState = lairPersistentState;
        lairPersistentState = false;
        return oldState;
    }
    
    public boolean isPersistentLair() { 
        return lairPersistentState; 
    }
    
    public String getLogin() {
        return login;
    }
    
    public String getFirstName() {
        return firstName;
    }
    
    public String getEncryptedPassword() {
        return encryptedPassword;
    }
    
    public String getLanguage() {
        return language;
    }
    
    public boolean equals(LoginResult loginResult) {
    	return(
    	        loginResult.getLogin().equals(login) &&
    	        loginResult.getFirstName().equals(firstName) &&
    	        loginResult.getLanguage().equals(language)
		);
    }
    
    @Override
    public String toString() {
        return new String("login = " + login + " | " + 
            " firstName = " + firstName + " | " +
            "encryptedPassword = " + encryptedPassword + " | " +
            "language = " + language);
    }  

}
