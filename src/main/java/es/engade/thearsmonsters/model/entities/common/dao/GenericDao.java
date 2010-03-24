package es.engade.thearsmonsters.model.entities.common.dao;

import java.io.Serializable;
import java.util.List;

import es.engade.thearsmonsters.util.exceptions.InstanceNotFoundException;

public interface GenericDao<T, PK extends Serializable> {

    public boolean exists(PK id);
    public T save(T obj);
    
    public T get(PK id)
        throws InstanceNotFoundException;
    
    public List<T> getAll();
    public List<T> getAllDistinct();
    public T update(final T gaeObject);
    public void remove(PK id);

}