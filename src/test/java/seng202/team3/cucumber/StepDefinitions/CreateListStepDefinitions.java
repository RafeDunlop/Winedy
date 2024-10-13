package seng202.team3.cucumber.StepDefinitions;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.After;
import org.junit.jupiter.api.Assertions;
import seng202.team3.models.UserWineList;
import seng202.team3.models.Wine;
import seng202.team3.models.WineDrinker;
import seng202.team3.repository.DatabaseManager;
import seng202.team3.repository.WineDrinkerDAO;
import seng202.team3.services.ProfileScreenService;
import seng202.team3.services.SignInScreenService;
import seng202.team3.services.WineDrinkerManager;
import seng202.team3.services.WineListManager;
import java.io.File;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class CreateListStepDefinitions {

    private String mockListNameTextField = "";
    private String mockDescriptionTextField = "";
    private WineListManager wineListManager;

    private WineDrinkerManager wineDrinkerManager;
    private ProfileScreenService profileScreenService;
    private String DATABASE_PATH = "jdbc:sqlite:./src/test/resources/test_database.db";


    /**
     * Step definitions for the cucumber tests related to the create wine list use case
     *
     * @author Sophia Copley (sco207)
     */
    @Given("The Wine Drinker is viewing their profile and clicks create new wine list")
    public void theWineDrinkerIsOnTheProfileScreen() {
        mockListNameTextField = "";
        mockDescriptionTextField = "";
        DatabaseManager.REMOVE_INSTANCE();
        WineDrinkerManager.REMOVE_INSTANCE();
        WineListManager.REMOVE_INSTANCE();
        DatabaseManager.getInstance(DATABASE_PATH);
        wineListManager = WineListManager.getInstance(DATABASE_PATH);
        wineDrinkerManager = WineDrinkerManager.getInstance(DATABASE_PATH);
        wineDrinkerManager.setCurrentUser(new WineDrinker("username", "password", null, null, null, null, 0));
        SignInScreenService.registerUser("username", "password", null, null, null, null, 0);
        profileScreenService = new ProfileScreenService();
    }

    @Given("The Wine Drinker enters {string} into the name field")
    public void theWineDrinkerEntersSomethingIntoNameField(String name) {
        mockListNameTextField = name;
    }

    @When("The Wine Drinker clicks the create list button")
    public void createListButtonClicked() {
        wineListManager.newList(mockListNameTextField, mockDescriptionTextField);
    }

    @Given("The Wine Drinker enters {string} into the description field")
    public void addListDescription(String description) {
        mockDescriptionTextField = description;
    }

    @Then("A new list is created called {string} with description {string}")
    public void newListCreated(String name, String description) {
        List<UserWineList> allLists = wineListManager.getAllUserWineLists();
        boolean found = allLists.size() == 2;
        assertTrue(found);
    }

    @Then("An error message is created that says {string}")
    public void errorMessageHasBeenDisplayed(String errorMessage) {
        assertEquals(errorMessage, profileScreenService.getCreateListErrorMessage(mockListNameTextField));
    }


    @After
    public void cleanUp() {
        File file = new File("./src/test/resources/test_database.db");
        file.delete();
    }

}
