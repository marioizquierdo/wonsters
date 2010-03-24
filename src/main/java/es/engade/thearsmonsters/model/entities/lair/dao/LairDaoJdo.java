package es.engade.thearsmonsters.model.entities.lair.dao;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import org.springframework.transaction.annotation.Transactional;

import com.google.appengine.api.datastore.Key;

import es.engade.thearsmonsters.model.entities.common.dao.GenericDaoJdo;
import es.engade.thearsmonsters.model.entities.lair.Address;
import es.engade.thearsmonsters.model.entities.lair.Lair;
import es.engade.thearsmonsters.model.entities.user.User;
import es.engade.thearsmonsters.util.exceptions.InstanceNotFoundException;

@Transactional
public class LairDaoJdo extends GenericDaoJdo<Lair, Key> implements LairDao {

    public Lair findLairByAddress(Address address)
        throws InstanceNotFoundException {
        
        PersistenceManager pm = getPersistenceManager();
        
        Query query = pm.newQuery(Lair.class);
        query.declareImports("import " + address.getClass().getName());
        query.setFilter("address == addressParam");
        query.declareParameters("Address addressParam");
        query.setUnique(true);
        
        Lair lair = null;
        try {
            lair = (Lair) query.execute(address);
        } finally {
            query.closeAll();
        }
        if (lair == null)
            throw new InstanceNotFoundException(address, Lair.class.getName());
        return lair;
        
    }

    public Lair findLairByUser(User user)
        throws InstanceNotFoundException {
        
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
            throw new InstanceNotFoundException(user.getLogin(), Lair.class.getName());
        return lair;
        
    }
}