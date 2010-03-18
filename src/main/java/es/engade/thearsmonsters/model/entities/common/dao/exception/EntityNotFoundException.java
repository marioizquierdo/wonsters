package es.engade.thearsmonsters.model.entities.common.dao.exception;

public class EntityNotFoundException extends RuntimeException {
    
    private static final long serialVersionUID = 201003111540L;

    @SuppressWarnings("unchecked")
    public EntityNotFoundException(Class entityClass, Object id) {
        super("Cannot retrieve " + entityClass.getName() + " with key " + id);
    }
    
    @SuppressWarnings("unchecked")
    public EntityNotFoundException(Class entityClass, String message) {
        super("Cannot retrieve " + entityClass.getName() + ": " + message);
    }

}
