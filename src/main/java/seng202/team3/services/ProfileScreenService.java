package seng202.team3.services;

import seng202.team3.models.WineDrinker;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service class for the profile screen
 * @author Yuvraj Fagotra (yfa50)
 */
public class ProfileScreenService {
    /**
     * WineDrinkerManager to handle wine drinker related tasks
     */
    private final WineDrinkerManager wineDrinkerManager;

    /**
     * WineListManager to handle wine list related tasks
     */
    private WineListManager wineListManager;

    /**
     * Constructor for profile screen service initialised the wineDrinkerManager and wineListManager
     */
    public ProfileScreenService() {
        this.wineDrinkerManager = WineDrinkerManager.getInstance();
        this.wineListManager = WineListManager.getInstance();
    }

    /**
     * Saves the users preferences when they are changed on the profile screen
     * @param colour wine colour preference
     * @param fullness wine fullness preference
     * @param grapeVariety wine variety preference
     * @param abvLimit alcohol by volume preference
     */
    public void savePreferences(String colour, String fullness, String grapeVariety, double abvLimit){
        WineDrinker wineDrinker = wineDrinkerManager.getCurrentUser();
        wineDrinker.setColourPreference(colour);
        wineDrinker.setFullnessPreference(fullness);
        wineDrinker.setGrapePreference(grapeVariety);
        wineDrinker.setAbvLimit(abvLimit);
        wineDrinkerManager.updateWineDrinker(wineDrinker);
    }

    /**
     * Checks if a new list name is valid
     * Checks if it has the same name as another list, whether it has the right number of characters and whether it is empty
     * @param listName name of the list to be validated
     * @return boolean of whether it is valid
     */
    public boolean isValidListName(String listName) {
        List<String> currentListNames = wineListManager.getAllUserWineLists().stream().map(list -> list.getWineListName()).collect(Collectors.toList());
        return !listName.trim().isEmpty() && !currentListNames.contains(listName);
    }
}
