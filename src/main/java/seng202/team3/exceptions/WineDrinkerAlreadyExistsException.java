package seng202.team3.exceptions;

/** WineDrinkerAlreadyExistsException to be thrown if a wine drinker tries to create an account with a username
 * that already exists
 */
public class WineDrinkerAlreadyExistsException extends DuplicateEntryException {

    public WineDrinkerAlreadyExistsException(String message) { super(message); }

}
