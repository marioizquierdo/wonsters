package es.engade.thearsmonsters.util.exceptions;

import java.io.Serializable;

public class DuplicateInstanceException extends InstanceException implements Serializable {

    private static final long serialVersionUID = -835633942686570096L;

	public DuplicateInstanceException(Object key, String className) {
        super("Duplicate instance", key, className);
    }


}
