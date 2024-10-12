package seng202.team3.exceptions;

/** WineDrinkerAlreadyExistsException to be thrown if a wine drinker tries to create an account with a username
 * that already exists
 *
 * @author Steven Leishman (sle159)
 */
public class WineDrinkerAlreadyExistsException extends Exception {

    /**
     * constructor for the WineDrinkerAlreadyExistsException.
     * sets the message of the exception to be the given string via the super method
     *
     * @param message the message to be set as the message of the exception
     */
    public WineDrinkerAlreadyExistsException(String message) {
        super(message);
    }
}
