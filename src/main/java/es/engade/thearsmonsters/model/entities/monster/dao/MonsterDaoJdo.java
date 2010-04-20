package es.engade.thearsmonsters.model.entities.monster.dao;

import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import com.google.appengine.api.datastore.Key;

import es.engade.thearsmonsters.model.entities.common.KeyUtils;
import es.engade.thearsmonsters.model.entities.common.dao.GenericDaoJdo;
import es.engade.thearsmonsters.model.entities.lair.Lair;
import es.engade.thearsmonsters.model.entities.monster.Monster;
import es.engade.thearsmonsters.util.exceptions.InstanceNotFoundException;

public class MonsterDaoJdo extends GenericDaoJdo<Monster, Key> implements MonsterDao {

    @SuppressWarnings("unchecked")
    public List<Monster> findMonstersByLair(Lair lair) {
        
        PersistenceManager pm = getPersistenceManager();
        
        Query query = pm.newQuery(Monster.class);
        query.declareImports("import " + lair.getClass().getName());
        query.setFilter("lair == lairParam");
        query.declareParameters(lair.getClass().getName() + " lairParam");
        
        List<Monster> results = null;
        try {

            results = (List<Monster>) query.execute(lair);
            // Carga Lazy --> Accedemos al resultado dentro de la transacción
            results.size();

        } finally {
            query.closeAll();
        }

        return results;
    }

    public Monster get(String id) throws InstanceNotFoundException {
        return get(KeyUtils.fromString(id));
    }

}