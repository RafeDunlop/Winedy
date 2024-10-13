package seng202.team3.cucumber.StepDefinitions;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import seng202.team3.exceptions.IllegalWineDrinkerException;
import seng202.team3.models.WineDrinker;
import seng202.team3.repository.DatabaseManager;
import seng202.team3.repository.WineDrinkerDAO;
import seng202.team3.services.SignInScreenService;
import seng202.team3.services.WineDrinkerManager;

import java.io.File;

public class LoginStepDefinitions {

    private String mockUsernameField = "";
    private String mockPasswordField = "";
    private IllegalWineDrinkerException userException = null;
    private IllegalWineDrinkerException passwordException = null;
    private WineDrinkerManager wineDrinkerManager;
    private SignInScreenService signInScreenService;
    final String DATABASE_PATH = "jdbc:sqlite:./src/test/resources/test_database.db";

    @BeforeAll
    public static void deleteTestDB() {
        File file = new File("./src/test/resources/test_database.db");
        file.delete();
    }

    @BeforeEach
    public void setup() {
        DatabaseManager.REMOVE_INSTANCE();
        DatabaseManager.getInstance(DATABASE_PATH);
    }

    @AfterEach
    public void cleanUp() {
        File file = new File("./src/test/resources/test_database.db");
        file.delete();
    }

    @Given("The Wine Drinker is on the login page")
    public void theWineDrinkerIsOnTheLoginPageWithDatabaseLoaded(){
        wineDrinkerManager = WineDrinkerManager.getInstance(DATABASE_PATH);
        wineDrinkerManager.setWineDrinkerDAO( new WineDrinkerDAO(DATABASE_PATH));
        signInScreenService = new SignInScreenService();
        signInScreenService.setWineDrinkerManager(wineDrinkerManager);
    }

    @Given("{string} is a registered user with password {string}")
    public void setupPreregisteredUser(String existingUsername, String existingPassword) {
        //ensure not already in database from previous tests
        wineDrinkerManager.deleteWineDrinker(new WineDrinker(existingUsername, existingPassword, null,null,null, null, 0));
        //put in database for this test
        signInScreenService.validateAndRegisterUser(existingUsername, existingPassword, existingPassword, null, null, null, null, 0);
        //logout
        wineDrinkerManager.setCurrentUser(null);
    }
    @Given("{string} is not a registered user")
    public void setupPreregisteredUser(String nonExistingUsername) {
        wineDrinkerManager.deleteWineDrinker(new WineDrinker(nonExistingUsername, "passing", null,null, null,null, 0));
        wineDrinkerManager.setCurrentUser(null);
    }

    @Given("Wine Drinker inputs {string} into username field")
    public void wineDrinkerInputsInUsernameField(String string) {
        mockUsernameField = string;
    }

    @Given("Wine Drinker inputs {string} into password field")
    public void wineDrinkerInputsInPasswordField(String string) {
        mockPasswordField = string;
    }
    @When("Wine Drinker clicks login button with valid data")
    public void userClicksLoginBtn(){
        assertDoesNotThrow(() -> signInScreenService.validateAndLoginUser(mockUsernameField, mockPasswordField));
    }

    @When("Wine Drinker clicks login button with invalid user")
    public void userClicksLoginBtnInvalid(){
         userException = assertThrows(IllegalWineDrinkerException.class, () -> signInScreenService.validateAndLoginUser(mockUsernameField, mockPasswordField));
    }

    @When("Wine Drinker clicks login button with invalid password")
    public void userClicksLoginBtnIncorrectPassword(){
        passwordException = assertThrows(IllegalWineDrinkerException.class, () -> signInScreenService.validateAndLoginUser(mockUsernameField, mockPasswordField));
    }
    @Then("user is logged in and moved to the profile screen")
    public void checkUserIsLoggedIn(){
        assertEquals(mockUsernameField, wineDrinkerManager.getCurrentUser().getUsername());
    }

    @Then("user is not logged in and prompted with user does not exist")
    public void checkUserPromptedDoesNotExist(){
        assertEquals("User does not exist.", userException.getMessage());
    }

    @Then("user is prompted with password incorrect")
    public void checkUserPromptedPasswordIncorrect(){
        //check if message matches prompt user will get
        assertEquals("Password Incorrect", passwordException.getMessage());
    }
}
