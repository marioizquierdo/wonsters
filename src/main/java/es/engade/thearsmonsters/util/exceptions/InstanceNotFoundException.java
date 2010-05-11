package es.engade.thearsmonsters.util.exceptions;

public class InstanceNotFoundException extends InstanceException {

    private static final long serialVersionUID = -5517845719446393579L;

	public InstanceNotFoundException(Object key, String className) {
        super("Instance not found", key, className);
    }
    
    
    /* Test code. Uncomment for testing. */    
//    public final static void main(String[] args) {
//    
//        try {
//            throw new InstanceNotFoundException("TestKey",            
//                String.class.getName());
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    
//    }

}
