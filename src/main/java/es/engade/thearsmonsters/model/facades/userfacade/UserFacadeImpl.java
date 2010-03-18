package es.engade.thearsmonsters.model.facades.userfacade;

import es.engade.thearsmonsters.model.entities.user.User;
import es.engade.thearsmonsters.model.entities.user.UserDetails;
import es.engade.thearsmonsters.model.entities.user.dao.UserDao;
import es.engade.thearsmonsters.model.facades.userfacade.exceptions.FullPlacesException;
import es.engade.thearsmonsters.model.facades.userfacade.exceptions.IncorrectPasswordException;
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
        // TODO Auto-generated method stub

    }

    @Override
    public int countUsers() throws InternalErrorException {
        return userDao.getNumberOfUsers();
    }

    @Override
    public User findUserProfile() throws InternalErrorException {
        // TODO Auto-generated method stub
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
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void registerUser(String login, String clearPassword,
            UserDetails userDetails) throws FullPlacesException,
            DuplicateInstanceException, InternalErrorException {
        // TODO Auto-generated method stub

    }

    @Override
    public void removeUserProfile(String login) throws InternalErrorException,
            InstanceNotFoundException {
        // TODO Auto-generated method stub

    }

    @Override
    public void updateUserProfileDetails(UserDetails userProfileDetailsVO)
            throws InternalErrorException {
        // TODO Auto-generated method stub

    }

}
