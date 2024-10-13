package seng202.team3.cucumber.StepDefinitions;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.After;
import seng202.team3.models.UserWineList;
import seng202.team3.models.Wine;
import seng202.team3.models.WineDrinker;
import seng202.team3.repository.DatabaseManager;
import seng202.team3.services.ProfileScreenService;
import seng202.team3.services.SignInScreenService;
import seng202.team3.services.WineDrinkerManager;
import seng202.team3.services.WineListManager;

import java.io.File;

import static org.junit.Assert.assertTrue;

public class AddToListStepDefinitions {
    private String mockListNameTextField = "";
    private String mockDescriptionTextField = "";
    private WineListManager wineListManager;

    private WineDrinkerManager wineDrinkerManager;
    private ProfileScreenService profileScreenService;
    private String DATABASE_PATH = "jdbc:sqlite:./src/test/resources/test_database.db";

    private final Wine WINE_1 = new Wine(
            4,
            "Bread & Butter Chardonnay 2017, California",
            "USA",
            "White",
            "Rich",
            new String[]{"Chardonnay"},
            "OFF DRY",
            "Complex and layered, with distinctive notes of vanilla bean, almond husk and tropical fruit - it's reminiscent of a decadent crème brûlée! Californian Chardonnay at it's very best. This Chardonnay opens delicately with rich notes of vanilla bean and almond husk, which reminds us of a decadent crème brûlée. The creamy custard notes are balanced by a soft minerality and a hint of worn leather Any seafood dish that features butter or brown butter sauce... baked chicken, creamy pastas or soups, squash and winter vegetables... You're staying for dinner, right?",
            (float) 15.99,
            new String[]{"IWC 2019 - Commended Award, IWC 2018 - Bronze Award", "Decanter 2018 - Silver Award"},
            (float) 13.5,
            750,
            2017);
    private Wine wineToAdd;


    @Given("Wine Drinker is logged in with username {string} and password {string}")
    public void wineDrinkerIsLoggedIn(String username, String password) {
        DatabaseManager.REMOVE_INSTANCE();
        WineDrinkerManager.REMOVE_INSTANCE();
        WineListManager.REMOVE_INSTANCE();
        DatabaseManager.getInstance(DATABASE_PATH);
        wineListManager = WineListManager.getInstance(DATABASE_PATH);
        wineDrinkerManager = WineDrinkerManager.getInstance(DATABASE_PATH);
        wineDrinkerManager.setCurrentUser(new WineDrinker(username, password, null, null, null, null, 0));
        SignInScreenService.registerUser(username, password, null, null, null, null, 0);
        wineListManager.newList("Summer Wines", "");
        profileScreenService = new ProfileScreenService();
    }

    @Given("Wine Drinker is viewing a wine on the search screen and they click add wine to list")
    public void wineDrinkerIsViewingAWine() {
        wineToAdd = WINE_1;
    }

    @Given("The Wine Drinker clicks the create new list method on the add wine to list pop up")
    public void wineDrinkerClicksCreateListToAddWineTo() {
        mockListNameTextField = "";
        mockDescriptionTextField = "";
    }

    @Given("Wine already in {string}")
    public void wineAlreadyInList(String listName) {
        UserWineList wineList = wineListManager.getAllUserWineLists().stream().filter(list -> list.getWineListName().equals(listName)).toList().getFirst();
        wineList.addWineToList(wineToAdd);
        wineListManager.update(wineList);
    }
    @Given("Wine Drinker enters {string} into the name field")
    public void wineDrinkerEntersNameIntoTextField(String listName) {
        mockListNameTextField = listName;
    }
    @Given("Wine Drinker clicks the create list button")
    public void wineDrinkerClicksCreateList() {
        wineListManager.newList(mockListNameTextField, mockDescriptionTextField);
    }
    @When("Wine Drinker clicks {string} on the add wine to list pop up to add wine to this list")
    public void wineDrinkerClickOnListToAddWineTo(String listName) {
        UserWineList wineList = wineListManager.getAllUserWineLists().stream().filter(list -> list.getWineListName().equals(listName)).toList().getFirst();
        wineList.addWineToList(wineToAdd);
        wineListManager.update(wineList);
    }

    @When("Wine Drinker clicks {string} on the add wine to list pop up to add wine to this list, unselecting it")
    public void wineDrinkerUnselectsListRemovingTheWine(String listName) {
        UserWineList wineList = wineListManager.getAllUserWineLists().stream().filter(list -> list.getWineListName().equals(listName)).toList().getFirst();
        wineList.removeWineFromList(wineToAdd);
        wineListManager.update(wineList);
    }
    @Then("A wine is added to {string}")
    public void wineAddedToList(String listName) {
        UserWineList wineList = wineListManager.getAllUserWineLists().stream().filter(list -> list.getWineListName().equals(listName)).toList().getFirst();
        boolean found = wineList.getWineList().stream().anyMatch(wine -> wine.getUniqueWineID() == wineToAdd.getUniqueWineID());
        assertTrue(found);
    }

    @Then("A wine is removed from {string}")
    public void wineRemovedFromList(String listName) {
        UserWineList wineList = wineListManager.getAllUserWineLists().stream().filter(list -> list.getWineListName().equals(listName)).toList().getFirst();
        boolean found = wineList.getWineList().stream().anyMatch(wine -> wine.getUniqueWineID() == wineToAdd.getUniqueWineID());
        assertTrue(!found);

    }
    @After
    public void cleanUp() {
        File file = new File("./src/test/resources/test_database.db");
        file.delete();
    }

}
