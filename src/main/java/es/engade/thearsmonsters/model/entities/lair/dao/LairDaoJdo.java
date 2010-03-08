package es.engade.thearsmonsters.model.entities.lair.dao;

import org.springframework.transaction.annotation.Transactional;

import com.google.appengine.api.datastore.Key;

import es.engade.thearsmonsters.model.entities.common.dao.GenericDaoJdo;
import es.engade.thearsmonsters.model.entities.lair.Lair;

@Transactional
public class LairDaoJdo extends GenericDaoJdo<Lair, Key> implements LairDao {

    public LairDaoJdo() {
    }

}