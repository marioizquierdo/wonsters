package es.engade.thearsmonsters.util.exceptions;

import java.io.PrintStream;
import java.io.PrintWriter;

public class InternalErrorException extends Exception {

    private static final long serialVersionUID = 6419142514334183227L;
	private Exception encapsulatedException;

    public InternalErrorException(Exception exception) {
        encapsulatedException = exception;
    }
    
    public InternalErrorException(String message) {
    	this(new Exception(message));
    }

    @Override
    public String getMessage() {
        return encapsulatedException.getMessage();
    }

    public Exception getEncapsulatedException() {
        return encapsulatedException;
    }
    
    @Override
    public void printStackTrace() {
        printStackTrace(System.err);
    }
    
    @Override
    public void printStackTrace(PrintStream printStream) {
        super.printStackTrace(printStream);
        printStream.println("***Information about encapsulated exception***");
        encapsulatedException.printStackTrace(printStream);
    }
    
    @Override
    public void printStackTrace(PrintWriter printWriter) {
        super.printStackTrace(printWriter);
        printWriter.println("***Information about encapsulated exception***");
        encapsulatedException.printStackTrace(printWriter);
    }
    
}
