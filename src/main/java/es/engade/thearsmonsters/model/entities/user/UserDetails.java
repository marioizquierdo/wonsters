package es.engade.thearsmonsters.model.entities.user;

import java.io.Serializable;

public class UserDetails implements Serializable {

	private static final long serialVersionUID = 200911261607L;
	
	private String firstName;
    private String surname;
    private String email;
    private String language;
    
    public String getFirstName() {
		return firstName;
	}


	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}


	public String getSurname() {
		return surname;
	}


	public void setSurname(String surname) {
		this.surname = surname;
	}


	public String getEmail() {
		return email;
	}


	public void setEmail(String email) {
		this.email = email;
	}


	public String getLanguage() {
		return language;
	}


	public void setLanguage(String language) {
		this.language = language;
	}


	public UserDetails(String firstName, String surname, String email,
        String language) {
        
        this.firstName = firstName;
        this.surname = surname;
        this.email = email;
        this.language = language;
        
    }
    
    
    public boolean equals(UserDetails userDetails) {
    	return (
    	userDetails.getFirstName().equals(firstName)	&&
    	userDetails.getSurname().equals(surname)		&&
    	userDetails.getEmail().equals(email)			&&
    	userDetails.getLanguage().equals(language)
    	);
    }
    
    public String toString() {
        return new String("firstName = " + firstName + " | " +
            "surname = " + surname + " | " +
            "email = " + email + " | " +
            "language = " + language);
    }
}
