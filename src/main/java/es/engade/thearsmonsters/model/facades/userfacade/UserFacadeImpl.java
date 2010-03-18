package es.engade.thearsmonsters.model.facades.userfacade;

import javax.jdo.annotations.Transactional;

import es.engade.thearsmonsters.http.controller.session.SessionManager;
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
        // TODO Pillar el login de la sesión?
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

            return userDao.login(login, password, passwordIsEncrypted, loginAsAdmin);
    }

    @Transactional
    public void registerUser(String login, String clearPassword,
            UserDetails userDetails) throws FullPlacesException,
            DuplicateInstanceException, InternalErrorException {

        //TODO: Como se testea/lanza FullPlacesException???
        
        try {
            User user = userDao.findUserByLogin(login);
            if (user != null)
                throw new DuplicateInstanceException(user.getId(), User.class.getName());
            else
                throw new InternalErrorException(new Exception("Expected InstanceNotFoundException, but null value was found"));
        } catch (InstanceNotFoundException e) {

            User newUser = new User(login, PasswordEncrypter.crypt(clearPassword), userDetails);
            userDao.save(newUser);
        }

    }

    @Transactional
    public void removeUserProfile(String login) throws InternalErrorException,
            InstanceNotFoundException {

        User user = userDao.findUserByLogin(login);
        
        userDao.remove(user.getId());

    }

    @Override
    public void updateUserProfileDetails(UserDetails userProfileDetailsVO)
            throws InternalErrorException {
        // TODO Auto-generated method stub

    }

}
