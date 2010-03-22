package es.engade.thearsmonsters.model.entities.egg.dao;

import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import org.springframework.transaction.annotation.Transactional;

import com.google.appengine.api.datastore.Key;

import es.engade.thearsmonsters.model.entities.common.dao.GenericDaoJdo;
import es.engade.thearsmonsters.model.entities.egg.MonsterEgg;
import es.engade.thearsmonsters.model.entities.lair.Lair;

@Transactional
public class MonsterEggDaoJdo extends GenericDaoJdo<MonsterEgg, Key> implements MonsterEggDao {

    @SuppressWarnings("unchecked")
    public List<MonsterEgg> findEggsByLair(Lair lair) {
        
        PersistenceManager pm = getPersistenceManager();
        
        Query query = pm.newQuery(MonsterEgg.class);
        query.declareImports("import " + lair.getClass().getName());
        query.setFilter("lair == lairParam");
        query.declareParameters(lair.getClass().getName() + " lairParam");
        
        List<MonsterEgg> results = null;
        try {

            results = (List<MonsterEgg>) query.execute(lair);
            // Carga Lazy --> Accedemos al resultado dentro de la transacción
            results.size();

        } finally {
            query.closeAll();
        }

        return results;
    }
    
    public int getNumberOfEggsByLair(Lair lair) {
        PersistenceManager pm = getPersistenceManager();

        Query query = pm.newQuery(MonsterEgg.class);
        query.setResult("count(this)");
        
        String numberOfEggsStr = "";
        
        try {
            numberOfEggsStr = query.execute().toString();
        } finally {
            query.closeAll();
        }
        int numberOfEggs = Integer.parseInt(numberOfEggsStr);
        return numberOfEggs;
    }

}