package es.engade.thearsmonsters.model.entities.user;

import java.io.Serializable;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;

import es.engade.thearsmonsters.model.entities.lair.Lair;
import es.engade.thearsmonsters.model.util.Format;

@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class User implements Serializable {

    private static final long serialVersionUID = 20100305L;
    
    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Key id;
    
    @Persistent
    private String loginName;
    
    @Persistent
    private String encryptedPassword;
    
    @Persistent(mappedBy = "user")
    private Lair lair;
    
    @Persistent(serialized = "true", defaultFetchGroup="true")
    private UserDetails userDetails;
    
    public Key getId() {
		return id;
	}

	public void setId(Key userId) {
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
    
	@Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((loginName == null) ? 0 : loginName.hashCode());
        return result;
    }

	/**
     * Compara este usuario con otro
     * Como el loginName es unico, basta con comparar el loginName para saber si es el mismo usuario.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        User other = (User) obj;
        if (loginName == null) {
            if (other.loginName != null)
                return false;
        } else if (!loginName.equals(other.loginName))
            return false;
        return true;
    }
    
}
