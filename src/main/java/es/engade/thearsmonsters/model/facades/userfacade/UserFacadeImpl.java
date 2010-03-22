package es.engade.thearsmonsters.model.facades.userfacade;

import javax.jdo.annotations.Transactional;

import es.engade.thearsmonsters.model.entities.user.User;
import es.engade.thearsmonsters.model.entities.user.UserDetails;
import es.engade.thearsmonsters.model.entities.user.dao.UserDao;
import es.engade.thearsmonsters.model.facades.userfacade.exceptions.FullPlacesException;
import es.engade.thearsmonsters.model.facades.userfacade.exceptions.IncorrectPasswordException;
import es.engade.thearsmonsters.model.facades.userfacade.util.PasswordEncrypter;
import es.engade.thearsmonsters.util.exceptions.DuplicateInstanceException;
import es.engade.thearsmonsters.util.exceptions.InstanceNotFoundException;
import es.engade.thearsmonsters.util.exceptions.InternalErrorException;

public class UserFacadeImpl implements UserFacade {

    private UserDao userDao;
    
    // Internal state
    private String login = null;
    
    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public void changePassword(String oldClearPassword, String newClearPassword)
            throws IncorrectPasswordException, InternalErrorException {
        // TODO Pillar el login de la sesión?

    }

    @Override
    public int countUsers() throws InternalErrorException {
        return userDao.getNumberOfUsers();
    }

    @Override
    public User findUserProfile() throws InternalErrorException {

        if (login != null)
            try {
                return findUserProfile(login);
            } catch (InstanceNotFoundException e) {
                // Internal Error: A logged user should exist
                throw new InternalErrorException(e);
            }
        return null;
    }

    @Override
    public User findUserProfile(String login) throws InstanceNotFoundException,
            InternalErrorException {
        
        return userDao.findUserByLogin(login);
        
    }

    @Override
    public LoginResult login(String login, String password,
            boolean passwordIsEncrypted, boolean loginAsAdmin)
            throws InstanceNotFoundException, IncorrectPasswordException,
            InternalErrorException {

            LoginResult loginResult = userDao.login(login, password, passwordIsEncrypted, loginAsAdmin);
            this.login = login;
            
            return loginResult;
    }

    @Transactional
    public void registerUser(String login, String clearPassword,
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
            userDao.save(newUser);
            this.login = login;
        }

    }

    @Transactional
    public void removeUserProfile(String login) throws InternalErrorException,
            InstanceNotFoundException {

        User user = userDao.findUserByLogin(login);
        
        userDao.remove(user.getId());
        
        this.login = null;

    }

    @Transactional
    public void updateUserProfileDetails(UserDetails userProfileDetailsVO)
            throws InternalErrorException {
        
        if (login == null) {
            throw new InternalErrorException(new Exception("Unexistent user"));
        }

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
