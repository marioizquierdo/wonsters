package es.engade.thearsmonsters.util.exceptions;

public class DuplicateInstanceException extends InstanceException {

    public DuplicateInstanceException(Object key, String className) {
        super("Duplicate instance", key, className);
    }
    
    /* Test code. Uncomment for testing. */
//    public static void main(String[] args) {
//    
//        try {
//            throw new DuplicateInstanceException("TestKey",            
//                String.class.getName());
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    
//    }

}
