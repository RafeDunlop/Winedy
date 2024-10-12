package seng202.team3.services;

import seng202.team3.models.Wine;

/**
 * Service class for the add personal wine popup
 * @author Krishna Sridhar (nsr36)
 */
public class PersonalWinePopupService {

    public boolean validatePersonalWineName(Wine personalWine) {
        if (personalWine.getName() != null) {
            return true;
        }
        return false;
    }
}
