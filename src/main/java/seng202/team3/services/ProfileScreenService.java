package seng202.team3.services;

import seng202.team3.models.WineDrinker;
import seng202.team3.repository.WineDrinkerDAO;

/**
 * Service class for the profile screen
 * @author Yuvraj (yfa50)
 */
public class ProfileScreenService {

    private WineDrinkerManager wineDrinkerManager;
    private WineDrinkerDAO wineDrinkerDAO;

    public ProfileScreenService() {
        this.wineDrinkerManager = WineDrinkerManager.getInstance();
        this.wineDrinkerDAO = new WineDrinkerDAO();
    }

    public void savePreferences(String colour, String fullness, String grapeVariety, double abvLimit){
        WineDrinker wineDrinker = wineDrinkerManager.getCurrentUser();
        wineDrinker.setColourPreference(colour);
        wineDrinker.setFullnessPreference(fullness);
        wineDrinker.setGrapePreference(grapeVariety);
        wineDrinker.setAbvLimit(abvLimit);
        wineDrinkerDAO.update(wineDrinker);
    }
}
