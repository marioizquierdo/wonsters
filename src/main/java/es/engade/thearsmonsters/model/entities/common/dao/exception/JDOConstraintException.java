package es.engade.thearsmonsters.model.entities.common.dao.exception;

public class JDOConstraintException extends RuntimeException {

    private static final long serialVersionUID = 20100430L;
    
    @SuppressWarnings("unchecked")
    public JDOConstraintException(Class classValue, String constraint) {
        super("Violated " + constraint + " constraint in class " + classValue.getName());
    }
    
    @SuppressWarnings("unchecked")
    public JDOConstraintException(Class classValue, String constraint, String field) {
        super("Violated " + constraint + " (field "+ field + " constraint in class " + classValue.getName());
    }

    public static class JDOUniqueConstraintException extends JDOConstraintException {

        private static final long serialVersionUID = 20100430L;

        @SuppressWarnings("unchecked")
        public JDOUniqueConstraintException(Class classValue, String field) {
            super(classValue, "UNIQUE", field);
        }
        
    }
    
    public static class JDOUnmodificableConstraintException extends JDOConstraintException {

        private static final long serialVersionUID = 20100430L;

        @SuppressWarnings("unchecked")
        public JDOUnmodificableConstraintException(Class classValue, String field) {
            super(classValue, "UNMODIFICABLE", field);
        }
        
    }
}
