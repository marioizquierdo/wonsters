package es.engade.thearsmonsters.model.entities.common.dao;

import java.io.Serializable;

public interface GenericDao<T, PK extends Serializable> {

    public T makePersistent(final T gaeObject);
    public T get(PK id);
    public T save(T obj);
    public void remove(T obj);

}