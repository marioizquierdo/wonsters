package es.engade.thearsmonsters.model.entities.monster.dao;

import java.util.List;

import com.google.appengine.api.datastore.Key;

import es.engade.thearsmonsters.model.entities.common.dao.GenericDao;
import es.engade.thearsmonsters.model.entities.lair.Lair;
import es.engade.thearsmonsters.model.entities.monster.Monster;
import es.engade.thearsmonsters.util.exceptions.InstanceNotFoundException;

public interface MonsterDao extends GenericDao<Monster, Key> {

    public Monster get(String id) throws InstanceNotFoundException;
    public List<Monster> findMonstersByLair(Lair lair);
}
