package es.engade.thearsmonsters.model.facades.userfacade;

import org.springframework.stereotype.Service;

import es.engade.thearsmonsters.model.entities.lair.Lair;
import es.engade.thearsmonsters.model.entities.user.User;
import es.engade.thearsmonsters.model.entities.user.UserDetails;
import es.engade.thearsmonsters.model.facades.userfacade.exceptions.FullPlacesException;
import es.engade.thearsmonsters.model.facades.userfacade.exceptions.IncorrectPasswordException;
import es.engade.thearsmonsters.util.exceptions.DuplicateInstanceException;
import es.engade.thearsmonsters.util.exceptions.InstanceNotFoundException;
import es.engade.thearsmonsters.util.exceptions.InternalErrorException;
import es.engade.thearsmonsters.util.factory.FactoryData;

@Service("userFacade")
public class UserFacadeMock implements UserFacade {

	private User user;
	private Lair lair;
	
	public UserFacadeMock() {
		lair = FactoryData.LairWhatIs.Default.build();
		user = lair.getUser();
	}
	
	public void changePassword(String login, String oldClearPassword, String newClearPassword)
			throws IncorrectPasswordException, InternalErrorException {
	}

	public int countUsers() throws InternalErrorException {
		return 1;
	}

	public User findUserProfile(String login)
			throws InstanceNotFoundException, InternalErrorException {
		return user;
	}

	public LoginResult login(String login, String password,
			boolean passwordIsEncrypted, boolean loginAsAdmin)
			throws InstanceNotFoundException, IncorrectPasswordException,
			InternalErrorException {
		return new LoginResult(lair, user.getLogin(), "testName", "encryptedPass", "es");
	}

	public void logout(Lair lair) {}
	
	public LoginResult registerUser(String login, String clearPassword,
			UserDetails userDetails, String validationCode) throws FullPlacesException,
			DuplicateInstanceException, InternalErrorException {
	    return new LoginResult(lair, user.getLogin(), 
	            user.getUserDetails().getFirstName(), 
	            user.getEncryptedPassword(), 
	            user.getUserDetails().getLanguage());
	}

	public void removeUserProfile(String login)
			throws InternalErrorException, InstanceNotFoundException {
	}

	public void updateUserProfileDetails(String login, UserDetails userProfileDetailsVO)
			throws InternalErrorException {
	}

}
