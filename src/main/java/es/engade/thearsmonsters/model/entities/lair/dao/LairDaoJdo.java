package es.engade.thearsmonsters.model.entities.lair.dao;

import java.util.ArrayList;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import org.springframework.transaction.annotation.Transactional;

import com.google.appengine.api.datastore.Key;

import es.engade.thearsmonsters.model.entities.common.dao.GenericDaoJdo;
import es.engade.thearsmonsters.model.entities.lair.Lair;
import es.engade.thearsmonsters.model.entities.user.User;
import es.engade.thearsmonsters.util.exceptions.InstanceNotFoundException;

@Transactional
public class LairDaoJdo extends GenericDaoJdo<Lair, Key> implements LairDao {

    public Lair findLairByAddress(int street, int building, int floor)
            throws InstanceNotFoundException {

        PersistenceManager pm = getPersistenceManager();

        Query query = pm.newQuery(Lair.class);
        query.setFilter("addressStreet == streetP && addressBuilding == buildingP && addressFloor == floorP");
        query.declareParameters("int streetP, int buildingP, int floorP");
        query.setUnique(true);

        Lair lair = null;
        try {
            lair = (Lair) query.execute(street, building, floor);
        } finally {
            query.closeAll();
        }
        if (lair == null)
            throw new InstanceNotFoundException(null, Lair.class.getName());
        return lair;

    }

    public Lair findLairByUser(User user) throws InstanceNotFoundException {

        PersistenceManager pm = getPersistenceManager();

        Query query = pm.newQuery(Lair.class);
        query.declareImports("import " + user.getClass().getName());
        query.setFilter("user == userParam");
        query.declareParameters("User userParam");
        query.setUnique(true);

        Lair lair = null;
        try {
            lair = (Lair) query.execute(user);
        } finally {
            query.closeAll();
        }
        if (lair == null)
            throw new InstanceNotFoundException(user.getLogin(), Lair.class
                    .getName());
        return lair;

    }
    
    @SuppressWarnings("unchecked")
    public List<Lair> findLairsByBuilding(int street, int building) {
        PersistenceManager pm = getPersistenceManager();

        Query query = pm.newQuery(Lair.class);
        query.setFilter("addressStreet == streetParam && addressBuilding == buildingParam");// && address <= maxAddressParam");
        query.declareParameters("int streetParam, int buildingParam");//, maxAddressParam");
        query.setUnique(false);

        List<Lair> lairs = null;
        try {
            lairs = (List<Lair>) query.execute(street, building);
         // Carga Lazy --> Accedemos al resultado dentro de la transacción
            lairs.size();
        } finally {
            query.closeAll();
        }
        if (lairs == null)
            lairs = new ArrayList<Lair>();
        
        return lairs;
    }
    
}