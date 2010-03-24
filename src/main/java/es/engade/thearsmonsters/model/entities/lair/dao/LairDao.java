package es.engade.thearsmonsters.model.entities.lair.dao;

import com.google.appengine.api.datastore.Key;

import es.engade.thearsmonsters.model.entities.common.dao.GenericDao;
import es.engade.thearsmonsters.model.entities.lair.Address;
import es.engade.thearsmonsters.model.entities.lair.Lair;
import es.engade.thearsmonsters.model.entities.user.User;
import es.engade.thearsmonsters.util.exceptions.InstanceNotFoundException;

public interface LairDao extends GenericDao<Lair, Key> {

    /**
     * Find a lair with all its rooms by address.
     */
    public Lair findLairByAddress(Address address)
        throws InstanceNotFoundException;
    
    /**
     * Find a lair with all its rooms by user.
     */
    public Lair findLairByUser(User user)
        throws InstanceNotFoundException;
}
