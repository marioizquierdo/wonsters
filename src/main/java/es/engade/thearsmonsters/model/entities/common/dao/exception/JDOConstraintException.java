package es.engade.thearsmonsters.model.entities.common.dao.exception;

public class JDOConstraintException extends ConstraintException {

	private static final long serialVersionUID = 20100805L;
    
    @SuppressWarnings("rawtypes")
    public JDOConstraintException(Class classValue, String constraint) {
        super(classValue, constraint);
    }
    
    @SuppressWarnings("rawtypes")
    public JDOConstraintException(Class classValue, String constraint, String field) {
        super(classValue, constraint, field);
    }

    public static class JDOUniqueConstraintException extends JDOConstraintException {

        private static final long serialVersionUID = 20100430L;

        @SuppressWarnings("rawtypes")
        public JDOUniqueConstraintException(Class classValue, String field) {
            super(classValue, "UNIQUE", field);
        }
        
    }
    
    public static class JDOUnmodificableConstraintException extends JDOConstraintException {

        private static final long serialVersionUID = 20100430L;

        @SuppressWarnings("rawtypes")
        public JDOUnmodificableConstraintException(Class classValue, String field) {
            super(classValue, "UNMODIFICABLE", field);
        }
        
    }
}
