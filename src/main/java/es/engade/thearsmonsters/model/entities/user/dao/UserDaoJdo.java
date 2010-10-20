package es.engade.thearsmonsters.model.entities.user.dao;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import com.google.appengine.api.datastore.Key;

import es.engade.thearsmonsters.model.entities.common.dao.GenericDaoJdo;
import es.engade.thearsmonsters.model.entities.common.dao.exception.EntityNotFoundException;
import es.engade.thearsmonsters.model.entities.common.dao.exception.JDOConstraintException.JDOUniqueConstraintException;
import es.engade.thearsmonsters.model.entities.common.dao.exception.JDOConstraintException.JDOUnmodificableConstraintException;
import es.engade.thearsmonsters.model.entities.lair.Lair;
import es.engade.thearsmonsters.model.entities.user.User;
import es.engade.thearsmonsters.model.facades.userfacade.LoginResult;
import es.engade.thearsmonsters.model.facades.userfacade.exceptions.IncorrectPasswordException;
import es.engade.thearsmonsters.model.facades.userfacade.util.PasswordEncrypter;
import es.engade.thearsmonsters.util.exceptions.InstanceNotFoundException;

public class UserDaoJdo extends GenericDaoJdo<User, Key> implements UserDao {

    public User findUserByLogin(String login) throws InstanceNotFoundException {
        PersistenceManager pm = getPersistenceManager();

        String upperLogin = login.toUpperCase();
        Query query = pm.newQuery(User.class);
        query.setFilter("loginUppercase == loginParam");
        query.declareParameters("String loginParam");
        query.setUnique(true);

        User user = null;
        try {
            // List<User> results = (List<User>) query.execute(login);
            // user = results.get(0);
            user = (User) query.execute(upperLogin);
        } finally {
            query.closeAll();
        }
        if (user == null)
            throw new InstanceNotFoundException(login, User.class.getName());
        // throw new EntityNotFoundException(User.class, "Unexistent login " + login);
        return user;
    }

    public int getNumberOfUsers() {
        PersistenceManager pm = getPersistenceManager();

        Query query = pm.newQuery(User.class);
        query.setResult("count(this)");

        String numberOfUsersStr = "";

        try {
            numberOfUsersStr = query.execute().toString();
        } finally {
            query.closeAll();
        }
        int numberOfUsers = Integer.parseInt(numberOfUsersStr);
        return numberOfUsers;
    }

    public LoginResult login(String login, String password,
            boolean passwordIsEncrypted, boolean loginAsAdmin)
            throws IncorrectPasswordException, InstanceNotFoundException {

        User user = findUserByLogin(login);
        String encryptedPassword = user.getEncryptedPassword();

        if (!passwordIsEncrypted) {
            if (!PasswordEncrypter.isClearPasswordCorrect(password,
                    encryptedPassword))
                throw new IncorrectPasswordException(login);
        } else {
            if (!encryptedPassword.equals(password))
                throw new IncorrectPasswordException(login);
        }

        Lair lair = user.getLair();

        lair.touch();
        
        String firstName = user.getUserDetails().getFirstName();
        String language = user.getUserDetails().getLanguage();

        LoginResult lr = new LoginResult(lair, login, firstName,
                encryptedPassword, language);
        return lr;
    }

    // ****************************************************************************
    // Dado que el datastore no soporta restricciones de unicidad, se
    // reimplementan
    // estos métodos para controlar que loginName es único, y que no se modifica
    // en una actualización.
    // ****************************************************************************
    public User save(User user) {
        try {
            findUserByLogin(user.getLogin());
        } catch (InstanceNotFoundException e) {
            return super.save(user);
        }
        throw new JDOUniqueConstraintException(user
                .getClass(), "login");
    }

    public User update(User user) {
        try {
            User persistedUser = this.get(user.getIdKey());
            if (!persistedUser.getLogin().equals(user.getLogin()))
                throw new JDOUnmodificableConstraintException(
                        user.getClass(), "login");
        } catch (InstanceNotFoundException e) {
            throw new EntityNotFoundException(user.getClass(), user.getIdKey());
        }
        return (User) getJdoTemplate().makePersistent(user);
    }
}