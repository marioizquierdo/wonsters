package es.engade.thearsmonsters.model.entities.user.dao;

import com.google.appengine.api.datastore.Key;

import es.engade.thearsmonsters.model.entities.common.dao.GenericDao;
import es.engade.thearsmonsters.model.entities.user.User;
import es.engade.thearsmonsters.model.facades.userfacade.LoginResult;
import es.engade.thearsmonsters.model.facades.userfacade.exceptions.IncorrectPasswordException;
import es.engade.thearsmonsters.util.exceptions.InstanceNotFoundException;

public interface UserDao extends GenericDao<User, Key> {

    public User findUserByLogin(String login)
        throws InstanceNotFoundException;
    
    public boolean isValidationCodeUsed(String code);
    
    public int getNumberOfUsers();
    
    public LoginResult login(String login, String password,
            boolean passwordIsEncrypted, boolean loginAsAdmin)
            throws IncorrectPasswordException,
                InstanceNotFoundException;
    
}
