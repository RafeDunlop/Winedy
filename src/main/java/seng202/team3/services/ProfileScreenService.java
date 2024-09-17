package seng202.team3.services;

import seng202.team3.models.WineDrinker;
import seng202.team3.repository.WineDrinkerDAO;

/**
 * Service class for the profile screen
 * @author Yuvraj (yfa50)
 */
public class ProfileScreenService {
    /**
     * WineDrinkerManager to handle wine drinker related tasks
     */
    private WineDrinkerManager wineDrinkerManager;

    public ProfileScreenService() {
        this.wineDrinkerManager = WineDrinkerManager.getInstance();
    }

    public void savePreferences(String colour, String fullness, String grapeVariety, double abvLimit){
        WineDrinker wineDrinker = wineDrinkerManager.getCurrentUser();
        wineDrinker.setColourPreference(colour);
        wineDrinker.setFullnessPreference(fullness);
        wineDrinker.setGrapePreference(grapeVariety);
        wineDrinker.setAbvLimit(abvLimit);
        wineDrinkerManager.updateWineDrinker(wineDrinker);
    }
}
