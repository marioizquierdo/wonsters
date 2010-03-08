package es.engade.thearsmonsters.model.entities.common.dao;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;

import javax.jdo.JDOObjectNotFoundException;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;

import org.springframework.orm.jdo.PersistenceManagerFactoryUtils;

public class GenericDaoJdo<T, PK extends Serializable> implements GenericDao<T, PK>{

    protected PersistenceManagerFactory pmf;

    protected Class<T> persistentClass;
    
    /** Constructor, defining the PersistenceManagerFactory to use. */
    @SuppressWarnings("unchecked")
    public GenericDaoJdo()
    {
        this.persistentClass = (Class<T>) ((ParameterizedType) getClass().
                getGenericSuperclass()).getActualTypeArguments()[0]; 
    }

    public T makePersistent(final T gaeObject) {
        return getPersistenceManager().makePersistent(gaeObject);
  }  
    
    public T get(PK id) {
        PersistenceManager pm = getPersistenceManager();
        T obj = null;
        //T detached = null;

        try {
                obj = pm.getObjectById(persistentClass, id);
                // If you're using transactions, you can call
                // pm.setDetachAllOnCommit(true) before committing to automatically
                // detach all objects without calls to detachCopy or detachCopyAll.
                //detached = pm.detachCopy(obj);
                return obj;
                //System.out.println("__Generic__DaoImpl:get(detached) - " + detached);
        } catch (JDOObjectNotFoundException e) {
                // e.printStackTrace();
                System.err.println(e.getMessage());
                // ignore
                // could not retrieve entity of kind __Index__ with key __Index__("xyz")
                return null;
        } finally {
                pm.close();
        }
        //return detached;
    }

    public T save(T obj) {
        PersistenceManager pm = getPersistenceManager();
        try {
                pm.makePersistent(obj);
        } finally {
                pm.close();
        }
        return obj;
    }

    public void remove(T obj) {
        PersistenceManager pm = getPersistenceManager();
        try {
                pm.deletePersistent(obj);
        } finally {
                pm.close();
        }
    }

    public void setPmf(PersistenceManagerFactory pmf) {
        this.pmf = pmf;
    }
    
    private PersistenceManager getPersistenceManager() {  
        return PersistenceManagerFactoryUtils  
            .getPersistenceManager(pmf, true);
    }  
}