package es.engade.thearsmonsters.model.facades.userfacade.exceptions;

import es.engade.thearsmonsters.util.exceptions.ModelException;

public class IncorrectPasswordException extends ModelException {

	private static final long serialVersionUID = 200912142050L;
	
    private String loginName;
    
    public IncorrectPasswordException(String loginName) {
        super("Incorrect password exception => loginName = " + loginName);
        this.loginName = loginName;
    }
    
    public String getLoginName() {
        return loginName;
    }
    
    /* Test code. Uncomment for testing. */
//    public static void main(String[] args) {
//    
//        try {
//            throw new IncorrectPasswordException("mizquierdo");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    
//    }    

}
