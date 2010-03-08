package es.engade.thearsmonsters.model.entities.user.dao;

import org.springframework.transaction.annotation.Transactional;

import com.google.appengine.api.datastore.Key;

import es.engade.thearsmonsters.model.entities.common.dao.GenericDaoJdo;
import es.engade.thearsmonsters.model.entities.user.User;

@Transactional
public class UserDaoJdo extends GenericDaoJdo<User, Key> implements UserDao {

    public UserDaoJdo() {
    }

}