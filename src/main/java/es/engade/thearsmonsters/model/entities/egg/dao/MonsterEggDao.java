package es.engade.thearsmonsters.model.entities.egg.dao;

import java.util.List;

import com.google.appengine.api.datastore.Key;

import es.engade.thearsmonsters.model.entities.common.dao.GenericDao;
import es.engade.thearsmonsters.model.entities.egg.MonsterEgg;
import es.engade.thearsmonsters.model.entities.lair.Lair;

public interface MonsterEggDao extends GenericDao<MonsterEgg, Key> {

    public List<MonsterEgg> findEggsByLair(Lair lair);
    
    public int getNumberOfEggsByLair(Lair lair);
}
