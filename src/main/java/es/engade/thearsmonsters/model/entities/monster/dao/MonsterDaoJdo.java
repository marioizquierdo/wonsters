package es.engade.thearsmonsters.model.entities.monster.dao;

import org.springframework.transaction.annotation.Transactional;

import com.google.appengine.api.datastore.Key;

import es.engade.thearsmonsters.model.entities.common.dao.GenericDaoJdo;
import es.engade.thearsmonsters.model.entities.monster.Monster;

@Transactional
public class MonsterDaoJdo extends GenericDaoJdo<Monster, Key> implements MonsterDao {

    public MonsterDaoJdo() {
    }

}