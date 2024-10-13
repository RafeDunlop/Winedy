package seng202.team3.cucumber.StepDefinitions;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import seng202.team3.models.UserWineList;
import seng202.team3.models.Wine;
import seng202.team3.models.WineDrinker;
import seng202.team3.repository.DatabaseManager;
import seng202.team3.services.ProfileScreenService;
import seng202.team3.services.SignInScreenService;
import seng202.team3.services.WineDrinkerManager;
import seng202.team3.services.WineListManager;

import static org.junit.Assert.assertEquals;

public class RemoveWineFromListStepDefinitions {

    private WineListManager wineListManager;

    private WineDrinkerManager wineDrinkerManager;
    private String DATABASE_PATH = "jdbc:sqlite:./src/test/resources/test_database.db";
    private String mockListNameTextField = "";
    private String mockDescriptionTextField = "";
    private UserWineList currentList;

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


    @Given("Wine Drinker logged in with username {string} and the password {string}")
    public void wineDrinkerIsLoggedIn(String username, String password) {
        DatabaseManager.REMOVE_INSTANCE();
        WineDrinkerManager.REMOVE_INSTANCE();
        WineListManager.REMOVE_INSTANCE();
        DatabaseManager.getInstance(DATABASE_PATH);
        wineListManager = WineListManager.getInstance(DATABASE_PATH);
        wineDrinkerManager = WineDrinkerManager.getInstance(DATABASE_PATH);
        wineDrinkerManager.setCurrentUser(new WineDrinker(username, password, null, null, null, null, 0));
        SignInScreenService.registerUser(username, password, null, null, null, null, 0);
    }

    @Given("Wine Drinker is viewing their {string} and has selected a wine to delete")
    public void wineDrinkerViewingList(String listName) {
        currentList = wineListManager.newList(listName, "");
    }

    @When("Confirm delete button is clicked")
    public void confirmDeleteButtonClicked() {
        wineListManager.remove(currentList);
    }

    @Then("The Wine is deleted from the list")
    public void wineIsDeleted() {
        assertEquals(0, wineListManager.getAllUserWineLists().getLast().getWineList().size());
    }
}

