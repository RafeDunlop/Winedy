package seng202.team3.cucumber.StepDefinitions;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.After;
import seng202.team3.models.UserWineList;
import seng202.team3.models.WineDrinker;
import seng202.team3.repository.DatabaseManager;
import seng202.team3.services.ProfileScreenService;
import seng202.team3.services.SignInScreenService;
import seng202.team3.services.WineDrinkerManager;
import seng202.team3.services.WineListManager;

import java.io.File;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class RenamingListStepDefinitions {
    private String mockListNameTextField = "";

    private WineListManager wineListManager;

    private WineDrinkerManager wineDrinkerManager;
    private ProfileScreenService profileScreenService;
    private String DATABASE_PATH = "jdbc:sqlite:./src/test/resources/test_database.db";
    private UserWineList currentList;
    @Given("Wine Drinker is viewing {string} and clicks rename list button")
    public void theWineDrinkerIsViewingList(String listName) {
        mockListNameTextField = "";
        DatabaseManager.REMOVE_INSTANCE();
        WineDrinkerManager.REMOVE_INSTANCE();
        WineListManager.REMOVE_INSTANCE();
        DatabaseManager.getInstance(DATABASE_PATH);
        this.wineListManager = WineListManager.getInstance(DATABASE_PATH);
        wineDrinkerManager = WineDrinkerManager.getInstance(DATABASE_PATH);
        wineDrinkerManager.setCurrentUser(new WineDrinker("username", "password", null, null, null, null, 0));
        SignInScreenService.registerUser("username", "password", null, null, null, null, 0);
        profileScreenService = new ProfileScreenService();
        currentList = wineListManager.newList(listName, "");
    }


    @Given("Wine Drinker enters {string} into the displayed text field")
    public void wineDrinkerEntersNewListName(String newListName) {
        mockListNameTextField = newListName;
    }

    @When("Wine Drinker enters {string} into the text field")
    public void wineDrinkerEntersInvalidListName(String newListName) {
        mockListNameTextField = newListName;
    }

    @When("Wine Drinker clicks save changes")
    public void wineDrinkerSavesChanges() {
        wineListManager.rename(currentList, mockListNameTextField);
    }

    @Then("the list is renamed to {string}")
    public void listIsRenamed(String newListName) {
        boolean found = wineListManager.getAllUserWineLists().stream().anyMatch(list -> list.getWineListName().equals(newListName));
        assertTrue(found);
    }

    @Then("Error message is created that says {string}")
    public void errorMessageDisplayed(String errorMessage) {
        assertEquals(errorMessage, profileScreenService.getCreateListErrorMessage(mockListNameTextField));
    }
    @After
    public void cleanUp() {
        File file = new File("./src/test/resources/test_database.db");
        file.delete();
    }
}
