package es.engade.thearsmonsters.util.exceptions;

/**
 * The root exception of all exceptions in the "Model".
 */
public abstract class ModelException extends Exception {

    protected ModelException() {}

    protected ModelException(String message) {
        super(message);
    }

}
