package es.engade.thearsmonsters.model.entities.user;

import es.engade.thearsmonsters.model.entities.common.Id;
import es.engade.thearsmonsters.model.entities.lair.Lair;
import es.engade.thearsmonsters.model.util.Format;

public class User {

	private Id id;
	private String loginName;
    private String encryptedPassword;
    private Lair lair;
    private UserDetails userDetails;
    
    public Id getId() {
		return id;
	}

	public void setId(Id userId) {
		this.id = userId;
	}

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public String getEncryptedPassword() {
		return encryptedPassword;
	}

	public void setEncryptedPassword(String encryptedPassword) {
		this.encryptedPassword = encryptedPassword;
	}

	public Lair getLair() {
		return lair;
	}

	public void setLair(Lair lair) {
		this.lair = lair;
	}

	public UserDetails getUserDetails() {
		return userDetails;
	}

	public void setUserDetails(UserDetails userDetails) {
		this.userDetails = userDetails;
	}

	public User() {}
	
	public User(String loginName, String encryptedPassword,
        UserDetails userDetails) {
        
        this.loginName = loginName;
        this.encryptedPassword = encryptedPassword;
        this.userDetails = userDetails;
        
    }
	
	
	
	@Override
    public String toString() {
		return Format.p(this.getClass(), new Object[]{
			"loginName", loginName,
			"encryptedPassword", encryptedPassword,
			"userDetails", userDetails,
		});
	}
    
	/**
	 * Compara este usuario con otro
	 * Como el loginName es œnico, basta con comparar el loginName para saber si es el mismo usuario.
	 */
    public boolean equals(User user) {
    	return user.getLoginName().equals(loginName);
    }
    
}
