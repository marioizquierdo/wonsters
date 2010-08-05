package es.engade.thearsmonsters.model.entities.common.dao.exception;

public class ConstraintException extends RuntimeException {

	private static final long serialVersionUID = 20100805L;
	
	@SuppressWarnings("rawtypes")
    public ConstraintException(Class classValue, String constraint) {
        super("Violated " + constraint + " constraint in class " + classValue.getName());
    }
    
    @SuppressWarnings("rawtypes")
    public ConstraintException(Class classValue, String constraint, String field) {
        super("Violated " + constraint + " (field "+ field + " constraint in class " + classValue.getName());
    }
}
