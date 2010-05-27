package es.engade.thearsmonsters.model.facades.monsterfacade.exceptions;

import com.google.appengine.api.datastore.Key;

import es.engade.thearsmonsters.model.entities.monster.enums.MonsterAge;

public class MonsterGrowException extends RuntimeException {
    
	private static final long serialVersionUID = 200912142100L;

	public MonsterGrowException(Key monsterId, MonsterAge age) {
        super("The monster with id "+monsterId+" cannot grow to its next age: " + age);
    }

}
