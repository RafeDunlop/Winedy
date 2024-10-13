package seng202.team3.services;

import seng202.team3.models.UserWineList;
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
     * Constructor for testing with the test database url
     */
    public ProfileScreenService(String url) {
        this.wineDrinkerManager = WineDrinkerManager.getInstance(url);
        this.wineListManager = WineListManager.getInstance(url);
    }

    /**
     * Saves the users preferences when they are changed on the profile screen
     *
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
     * Checks if it has the same name as another list, whether it has the right number of characters and whether it is
     * empty
     *
     * @param listName name of the list to be validated
     * @return boolean of whether it is valid name for a new list
     */
    public Boolean isValidNewListName(String listName) {
        List<String> currentListNames = getCurrentListNames();
        return !listName.trim().isEmpty() && !currentListNames.contains(listName);
    }

    /**
     * Checks if a new name for an already existing list is valid
     *
     * @param oldListName the old name of the list
     * @param newListName the new name for the list
     * @return whether the list is a valid new name for the list
     */
    public Boolean isValidRenamedListName(String oldListName, String newListName) {
        List<String> currentListNames = getCurrentListNames();
        currentListNames.remove(oldListName);
        return !newListName.trim().isEmpty() && !currentListNames.contains(newListName);
    }

    /**
     * Creates error messages for errors encountered when creating a list
     *
     * @param listName name of list to be validated
     * @return the error message for the list name
     */
    public String getCreateListErrorMessage(String listName) {
        List<String> currentListNames = getCurrentListNames();
        if (currentListNames.contains(listName)) {
            return "A list with this name already exists!";
        } else if (listName.trim().isEmpty()) {
            return "List name is required to create a list";
        }
        return "";
    }

    /**
     * Method which indicates is some text has reached a given character limit
     *
     * @param text the text to be validated
     * @param charLimit the character limit
     * @return boolean of whether the text has reached the character limit
     */
    public boolean reachedCharLimit(String text, int charLimit) {
        return text.length() >= charLimit;
    }

    /**
     * Private method to get all the names of the lists that a user currently has
     *
     * @return list of the names of a user's lists
     */
    private List<String> getCurrentListNames() {
        return wineListManager.getAllUserWineLists().stream().map(UserWineList::getWineListName).collect(Collectors.toList());
    }

    /**
     * Checks if there are unsaved changes by comparing the previous string to a new string
     *
     * @param oldString old string
     * @param newString newly entered string
     * @return true if there are unsaved changes and false if there are no changes.
     */
    public boolean unsavedChanges(String oldString, String newString) {
        return !oldString.equals(newString);
    }

}
