package es.engade.thearsmonsters.model.entities.common.dao;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;

import javax.jdo.JDOObjectNotFoundException;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;

import org.springframework.orm.ObjectRetrievalFailureException;
import org.springframework.orm.jdo.JdoObjectRetrievalFailureException;
import org.springframework.orm.jdo.support.JdoDaoSupport;

import es.engade.thearsmonsters.model.entities.common.dao.exception.EntityNotFoundException;

public class GenericDaoJdo <T, PK extends Serializable>  extends JdoDaoSupport implements GenericDao<T, PK> {

    private Class<T> persistentClass;

    @SuppressWarnings("unchecked")
    public GenericDaoJdo() {
        this.persistentClass = (Class<T>) ((ParameterizedType) getClass().
                getGenericSuperclass()).getActualTypeArguments()[0];
    }

     public boolean exists(PK id) {
            T entity = (T) getJdoTemplate().getObjectById(this.persistentClass, id);
            return entity != null;
     }
    
     public T get(PK id) {
         T entity;
         try {
            entity = (T) getJdoTemplate().getObjectById(this.persistentClass, id);
         } catch (JdoObjectRetrievalFailureException e) {
             if (e.contains(JDOObjectNotFoundException.class)) {
                 throw new EntityNotFoundException(this.persistentClass, id);
             } else
                 throw e;
         }
    
            if (entity == null) {
                throw new EntityNotFoundException(this.persistentClass, id); 
//                ObjectRetrievalFailureException(this.persistentClass, id);
            }
    
            return entity;
     }
     
//     public T getByKey(Key id) {
//            T entity = (T) getJdoTemplate().getObjectById(this.persistentClass, id);
//    
//            if (entity == null) {
//                throw new ObjectRetrievalFailureException(this.persistentClass, id);
//            }
//    
//            return entity;
//     }
    
     public List<T> getAll() {
      return new ArrayList<T>(getJdoTemplate().find(persistentClass));
     }
    
     public List<T> getAllDistinct() {
            Collection<T> result = new LinkedHashSet<T>(getAll());
            return new ArrayList<T>(result);
     }
    
     public void remove(PK id) {
         PersistenceManager pm = this.getPersistenceManager();
         T objToDelete = pm.getObjectById(this.persistentClass, id);
         pm.deletePersistent(objToDelete);
//         getJdoTemplate().deletePersistent(this.get(id)); 
     }
    
     public T save(T object) {
      return (T) getJdoTemplate().makePersistent(object);
     }
     
     public T update(T object) {
         return (T) getJdoTemplate().makePersistent(object);
        }
    
     public void setPmf(PersistenceManagerFactory pmf) {
         this.setPersistenceManagerFactory(pmf);
     }

}