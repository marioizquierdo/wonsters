package es.engade.thearsmonsters.model.entities.lair.dao;

import java.util.ArrayList;
import java.util.List;

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

        Address minAddress = new Address(street, building, Address.MIN_FLOOR);
        Address maxAddress = new Address(street, building, Address.MAX_FLOOR);

        Query query = pm.newQuery(Lair.class);
        query.declareImports("import " + Address.class.getName());
        query
                .setFilter("address >= minAddressParam && address <= maxAddressParam");
        query.declareParameters("Address minAddressParam, maxAddressParam");
        query.setUnique(true);

        List<Lair> lairs = null;
        try {
            lairs = (List<Lair>) query.execute(minAddress, maxAddress);
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