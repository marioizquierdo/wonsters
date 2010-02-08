package es.engade.thearsmonsters.model.facades.monsterfacade.exceptions;

import es.engade.thearsmonsters.util.exceptions.ModelException;

public class MonsterGrowException extends ModelException {
    
	private static final long serialVersionUID = 200912142100L;

	public MonsterGrowException(long monsterId) {
        super("The monster with id "+monsterId+" cannot grow to its next age");
    }
    
    /* Test code. Uncomment for testing. */
//    public static void main(String[] args) {
//    
//        try {
//            throw new MonsterGrowException(1);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    
//    }    

}
