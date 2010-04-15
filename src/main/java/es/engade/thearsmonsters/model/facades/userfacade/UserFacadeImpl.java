package es.engade.thearsmonsters.model.facades.userfacade;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.engade.thearsmonsters.model.entities.lair.Lair;
import es.engade.thearsmonsters.model.entities.user.User;
import es.engade.thearsmonsters.model.entities.user.UserDetails;
import es.engade.thearsmonsters.model.entities.user.dao.UserDao;
import es.engade.thearsmonsters.model.facades.userfacade.exceptions.FullPlacesException;
import es.engade.thearsmonsters.model.facades.userfacade.exceptions.IncorrectPasswordException;
import es.engade.thearsmonsters.model.facades.userfacade.util.PasswordEncrypter;
import es.engade.thearsmonsters.util.exceptions.DuplicateInstanceException;
import es.engade.thearsmonsters.util.exceptions.InstanceNotFoundException;
import es.engade.thearsmonsters.util.exceptions.InternalErrorException;
import es.engade.thearsmonsters.util.factory.FactoryData;

@Service("userFacade")
@Transactional
public class UserFacadeImpl implements UserFacade {

    @Autowired
    private UserDao userDao;
    
    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    public void changePassword(String login, String oldClearPassword, String newClearPassword)
            throws IncorrectPasswordException, InternalErrorException {
        
        try {
            User user = userDao.findUserByLogin(login);
            
            if (!PasswordEncrypter.isClearPasswordCorrect(oldClearPassword, user.getEncryptedPassword()))
                throw new IncorrectPasswordException(login);
                
            user.setEncryptedPassword(PasswordEncrypter.crypt(newClearPassword));
            userDao.update(user);
                
        } catch (InstanceNotFoundException e) {
            throw new InternalErrorException(e);
        }

    }

    @Transactional(readOnly=true)
    public int countUsers() throws InternalErrorException {
        return userDao.getNumberOfUsers();
    }

    @Transactional(readOnly=true)
    public User findUserProfile(String login) throws InstanceNotFoundException,
            InternalErrorException {
        
        return userDao.findUserByLogin(login);
        
    }

    @Transactional(readOnly=true)
    public LoginResult login(String login, String password,
            boolean passwordIsEncrypted, boolean loginAsAdmin)
            throws InstanceNotFoundException, IncorrectPasswordException,
            InternalErrorException {

            LoginResult loginResult = userDao.login(login, password, passwordIsEncrypted, loginAsAdmin);
            
            return loginResult;
    }

    public LoginResult registerUser(String login, String clearPassword,
            UserDetails userDetails) throws FullPlacesException,
            DuplicateInstanceException, InternalErrorException {
        
        try {
            User user = userDao.findUserByLogin(login);
            if (user != null)
                throw new DuplicateInstanceException(user.getId(), User.class.getName());
            else
                throw new InternalErrorException(new Exception("Expected InstanceNotFoundException, but null value was found"));
        } catch (InstanceNotFoundException e) {

            User newUser = new User(login, PasswordEncrypter.crypt(clearPassword), userDetails);
            Lair newLair = FactoryData.LairWhatIs.InInitialState.build(newUser);
           
            userDao.save(newUser);
            
            return new LoginResult(newLair, login, userDetails.getFirstName(), 
                    newUser.getEncryptedPassword(), userDetails.getLanguage());

        }

    }

    public void removeUserProfile(String login) throws InternalErrorException,
            InstanceNotFoundException {

        User user = userDao.findUserByLogin(login);
        
        userDao.remove(user.getId());

    }

    public void updateUserProfileDetails(String login, UserDetails userProfileDetailsVO)
            throws InternalErrorException {

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
