package es.engade.thearsmonsters.model.facades.userfacade;

import java.io.Serializable;

import es.engade.thearsmonsters.model.entities.lair.Lair;

public class LoginResult implements Serializable {

	private static final long serialVersionUID = 200912142051L;
	
	private Lair lair;
	private String firstName;
    private String encryptedPassword;
    private String language;

    public LoginResult(Lair lair, String firstName, String encryptedPassword,
        String language) {
        
    	this.lair = lair;
        this.firstName = firstName;
        this.encryptedPassword = encryptedPassword;
        this.language = language;

    }
    
    public Lair getLair() {
        return lair;
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
		loginResult.getFirstName().equals(firstName)		&&
		loginResult.getLanguage().equals(language)
		);
    }
    
    public String toString() {
        return new String("firstName = " + firstName + " | " +
            "encryptedPassword = " + encryptedPassword + " | " +
            "language = " + language);
    }  

}
