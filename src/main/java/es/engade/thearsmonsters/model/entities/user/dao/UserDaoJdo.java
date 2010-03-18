package es.engade.thearsmonsters.model.entities.user.dao;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import com.google.appengine.api.datastore.Key;

import es.engade.thearsmonsters.model.entities.common.dao.GenericDaoJdo;
import es.engade.thearsmonsters.model.entities.common.dao.exception.EntityNotFoundException;
import es.engade.thearsmonsters.model.entities.user.User;

public class UserDaoJdo extends GenericDaoJdo<User, Key> implements UserDao {
    
    public User findUserByLogin(String login) {
        PersistenceManager pm = getPersistenceManager();
        
        Query query = pm.newQuery(User.class);
        query.setFilter("login == loginParam");
        query.declareParameters("String loginParam");
        query.setUnique(true);
        
        User user = null;
        try {
//            List<User> results = (List<User>) query.execute(login);
//            if (results.size() == 1)
//                user = results.get(0);
            user = (User) query.execute(login);
        } finally {
            query.closeAll();
        }
        if (user == null)
            throw new EntityNotFoundException(User.class, "Unexistent login " + login);
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
}