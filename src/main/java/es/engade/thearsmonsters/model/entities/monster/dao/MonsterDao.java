package es.engade.thearsmonsters.model.entities.monster.dao;

import java.util.List;

import com.google.appengine.api.datastore.Key;

import es.engade.thearsmonsters.model.entities.common.dao.GenericDao;
import es.engade.thearsmonsters.model.entities.lair.Lair;
import es.engade.thearsmonsters.model.entities.monster.Monster;

public interface MonsterDao extends GenericDao<Monster, Key> {

    public List<Monster> findMonstersByLair(Lair lair);
}
