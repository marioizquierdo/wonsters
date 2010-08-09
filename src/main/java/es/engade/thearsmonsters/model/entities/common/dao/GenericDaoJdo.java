package es.engade.thearsmonsters.model.entities.common.dao;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;

import javax.jdo.JDOObjectNotFoundException;
import javax.jdo.PersistenceManager;

import org.springframework.orm.jdo.JdoObjectRetrievalFailureException;
import org.springframework.orm.jdo.support.JdoDaoSupport;

import es.engade.thearsmonsters.model.entities.common.ThearsmonstersEntity;
import es.engade.thearsmonsters.model.entities.common.dao.exception.JDOConstraintException;
import es.engade.thearsmonsters.model.entities.common.dao.pmfprovider.PMFProvider;
import es.engade.thearsmonsters.util.exceptions.InstanceNotFoundException;

public class GenericDaoJdo<T extends ThearsmonstersEntity, PK extends Serializable> extends JdoDaoSupport
        implements GenericDao<T, PK> {

    private Class<T> persistentClass;

    @SuppressWarnings("unchecked")
    public GenericDaoJdo() {
        this.persistentClass = (Class<T>) ((ParameterizedType) getClass()
                .getGenericSuperclass()).getActualTypeArguments()[0];
    }

    public boolean exists(PK id) {
        T entity = (T) getJdoTemplate().getObjectById(this.persistentClass, id);
        return entity != null;
    }

    public T get(PK id) throws InstanceNotFoundException {

        if (id == null)
            throw new InstanceNotFoundException(id, this.persistentClass
                    .getName());
        T entity;
        try {
            entity = (T) getJdoTemplate().getObjectById(this.persistentClass,
                    id);
        } catch (JdoObjectRetrievalFailureException e) {
            if (e.contains(JDOObjectNotFoundException.class)) {
                throw new InstanceNotFoundException(id, this.persistentClass
                        .getName());
            } else
                throw e;
        }

        if (entity == null) {
            throw new InstanceNotFoundException(id, this.persistentClass
                    .getName());
            // ObjectRetrievalFailureException(this.persistentClass, id);
        }

        return entity;
    }

    public List<T> getAll() {
        return new ArrayList<T>(getJdoTemplate().find(persistentClass));
    }

    public List<T> getAllDistinct() {
        Collection<T> result = new LinkedHashSet<T>(getAll());
        return new ArrayList<T>(result);
    }

    public void remove(PK id) throws InstanceNotFoundException {
        PersistenceManager pm = this.getPersistenceManager();
        try {
            T objToDelete = pm.getObjectById(this.persistentClass, id);
            pm.deletePersistent(objToDelete);
            // getJdoTemplate().deletePersistent(this.get(id));
        } catch (JDOObjectNotFoundException ex) {
                throw new InstanceNotFoundException(id, this.persistentClass
                        .getName());
        }
    }

    public T save(T object) {
        return (T) getJdoTemplate().makePersistent(object);
    }

    public T update(T object) {
    	if (object.getIdKey() == null) {
    		throw new JDOConstraintException(object.getClass(), "NOT_PERSISTENT", object.getId());
    	}
        return (T) getJdoTemplate().makePersistent(object);
    }

    public void setPmfProvider(PMFProvider pmfp) {
        this.setPersistenceManagerFactory(pmfp.getPmf());
    }

}