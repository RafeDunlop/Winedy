package seng202.team3.services;

import seng202.team3.models.WineDrinker;
import seng202.team3.repository.WineDrinkerDAO;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service class for the profile screen
 * @author Yuvraj (yfa50)
 */
public class ProfileScreenService {
    /**
     * WineDrinkerManager to handle wine drinker related tasks
     */
    private WineDrinkerManager wineDrinkerManager;

    private WineListManager wineListManager;
    public ProfileScreenService() {
        this.wineDrinkerManager = WineDrinkerManager.getInstance();
        this.wineListManager = WineListManager.getInstance();
    }

    public void savePreferences(String colour, String fullness, String grapeVariety, double abvLimit){
        WineDrinker wineDrinker = wineDrinkerManager.getCurrentUser();
        wineDrinker.setColourPreference(colour);
        wineDrinker.setFullnessPreference(fullness);
        wineDrinker.setGrapePreference(grapeVariety);
        wineDrinker.setAbvLimit(abvLimit);
        wineDrinkerManager.updateWineDrinker(wineDrinker);
    }

    public boolean isValidListName(String listName) {
        List<String> currentListNames = wineListManager.getAllUserWineLists().stream().map(list -> list.getWineListName()).collect(Collectors.toList());
        return !listName.trim().isEmpty() && !currentListNames.contains(listName);
    }
}
