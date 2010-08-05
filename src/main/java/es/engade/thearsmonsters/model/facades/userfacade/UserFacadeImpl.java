package es.engade.thearsmonsters.model.facades.userfacade;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.engade.thearsmonsters.model.entities.common.dao.exception.ConstraintException;
import es.engade.thearsmonsters.model.entities.lair.Address;
import es.engade.thearsmonsters.model.entities.lair.Lair;
import es.engade.thearsmonsters.model.entities.lair.dao.LairDao;
import es.engade.thearsmonsters.model.entities.user.User;
import es.engade.thearsmonsters.model.entities.user.UserDetails;
import es.engade.thearsmonsters.model.entities.user.dao.UserDao;
import es.engade.thearsmonsters.model.facades.common.ThearsmonstersFacade;
import es.engade.thearsmonsters.model.facades.userfacade.exceptions.FullPlacesException;
import es.engade.thearsmonsters.model.facades.userfacade.exceptions.IncorrectPasswordException;
import es.engade.thearsmonsters.model.facades.userfacade.util.PasswordEncrypter;
import es.engade.thearsmonsters.util.exceptions.DuplicateInstanceException;
import es.engade.thearsmonsters.util.exceptions.InstanceNotFoundException;
import es.engade.thearsmonsters.util.exceptions.InternalErrorException;
import es.engade.thearsmonsters.util.factory.FactoryData;

@Service("userFacade")
@Transactional
public class UserFacadeImpl extends ThearsmonstersFacade implements UserFacade {

	@Autowired
	private UserDao userDao;
	@Autowired
	private LairDao lairDao;

	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}

	public void setLairDao(LairDao lairDao) {
		this.lairDao = lairDao;
	}

	public void changePassword(String login, String oldClearPassword,
			String newClearPassword) throws IncorrectPasswordException,
			InternalErrorException {

		try {
			User user = userDao.findUserByLogin(login);

			if (!PasswordEncrypter.isClearPasswordCorrect(oldClearPassword,
					user.getEncryptedPassword()))
				throw new IncorrectPasswordException(login);

			user.setEncryptedPassword(PasswordEncrypter.crypt(newClearPassword));
			userDao.update(user);

		} catch (InstanceNotFoundException e) {
			throw new InternalErrorException(e);
		}

	}

	@Transactional(readOnly = true)
	public int countUsers() throws InternalErrorException {
		return userDao.getNumberOfUsers();
	}

	@Transactional(readOnly = true)
	public User findUserProfile(String login) throws InstanceNotFoundException,
			InternalErrorException {

		return userDao.findUserByLogin(login);

	}

	@Transactional(readOnly = true)
	public LoginResult login(String login, String password,
			boolean passwordIsEncrypted, boolean loginAsAdmin)
			throws InstanceNotFoundException, IncorrectPasswordException,
			InternalErrorException {

		LoginResult loginResult = userDao.login(login, password,
				passwordIsEncrypted, loginAsAdmin);

		return loginResult;
	}

	public LoginResult registerUser(String login, String clearPassword,
			UserDetails userDetails) throws FullPlacesException,
			DuplicateInstanceException, InternalErrorException {

		try {
			User newUser = new User(login,
					PasswordEncrypter.crypt(clearPassword), userDetails);
			Lair newLair = FactoryData.LairWhatIs.InInitialState.build(newUser);
			Address nextAddress = lairDao.findNextFreeAddress();
			newLair.setAddress(nextAddress);

			userDao.save(newUser);
			return new LoginResult(newLair, login, userDetails.getFirstName(),
					newUser.getEncryptedPassword(), userDetails.getLanguage());
		} catch (ConstraintException ex) {
			throw new DuplicateInstanceException(login, User.class.getName());
		}

	}

	public void removeUserProfile(String login) throws InternalErrorException,
			InstanceNotFoundException {

		User user = userDao.findUserByLogin(login);

		userDao.remove(user.getIdKey());

	}

	public void updateUserProfileDetails(String login,
			UserDetails userProfileDetailsVO) throws InternalErrorException {

		try {
			User user = userDao.findUserByLogin(login);

			user.setUserDetails(userProfileDetailsVO);
			userDao.update(user);
		} catch (InstanceNotFoundException e) {
			// Internal Error: A logged user should exist
			throw new InternalErrorException(e);
		}
	}

}
