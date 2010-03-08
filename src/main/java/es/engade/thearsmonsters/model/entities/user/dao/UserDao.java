package es.engade.thearsmonsters.model.entities.user.dao;

import com.google.appengine.api.datastore.Key;

import es.engade.thearsmonsters.model.entities.common.dao.GenericDao;
import es.engade.thearsmonsters.model.entities.user.User;

public interface UserDao extends GenericDao<User, Key> {

}
