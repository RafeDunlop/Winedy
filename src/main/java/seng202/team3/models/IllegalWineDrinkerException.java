package seng202.team3.models;

/**
 * exception thrown when user attempts to register a wine drinker which does not conform, likely because of
 * erroneous input or missing fields
 *
 * @author Rafe Dunlop (rdu46)
 */
public class IllegalWineDrinkerException extends IllegalArgumentException {

    /**
     * trivial constructor using IllegalArgumentException String input constructor
     * @param cause String, a sentence describing what about the wine drinker is malformed
     */
    public IllegalWineDrinkerException(String cause) {
        super(cause);
    }
}
