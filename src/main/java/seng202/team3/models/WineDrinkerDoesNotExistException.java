package seng202.team3.models;

public class WineDrinkerDoesNotExistException extends Exception {
    public WineDrinkerDoesNotExistException() {}
    public WineDrinkerDoesNotExistException(String message) {
        super(message);
    }
    public WineDrinkerDoesNotExistException (Throwable cause){
        super(cause);
    }
    public WineDrinkerDoesNotExistException(String message, Throwable cause) {
        super(message, cause);
    }
}
