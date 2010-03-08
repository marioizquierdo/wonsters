package es.engade.thearsmonsters.model.entities.monster.dao;

import com.google.appengine.api.datastore.Key;

import es.engade.thearsmonsters.model.entities.common.dao.GenericDao;
import es.engade.thearsmonsters.model.entities.monster.Monster;

public interface MonsterDao extends GenericDao<Monster, Key> {

}
