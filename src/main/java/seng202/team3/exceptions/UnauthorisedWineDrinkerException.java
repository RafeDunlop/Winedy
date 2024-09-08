package seng202.team3.exceptions;

/**
 * Custom UnauthorisedWineDrinkerException to be thrown if the user has an invalid password
 */
public class UnauthorisedWineDrinkerException  extends Exception {
    /**
     * UnauthorisedWineDrinkerException constructor that passes a messaged to the parent exception class
     * @param message error message
     */
    public UnauthorisedWineDrinkerException(String message) {super(message); }
}
