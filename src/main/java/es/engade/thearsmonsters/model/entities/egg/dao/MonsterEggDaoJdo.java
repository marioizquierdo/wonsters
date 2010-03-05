package es.engade.thearsmonsters.model.entities.egg.dao;

import com.google.appengine.api.datastore.Key;

import es.engade.thearsmonsters.model.entities.common.dao.GenericDaoJdo;
import es.engade.thearsmonsters.model.entities.egg.MonsterEgg;


public class MonsterEggDaoJdo extends GenericDaoJdo<MonsterEgg, Key> implements MonsterEggDao {

    public MonsterEggDaoJdo() {
    }

}