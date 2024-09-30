package seng202.team3.cucumber.StepDefinitions;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import seng202.team3.exceptions.IllegalWineDrinkerException;
import seng202.team3.models.WineDrinker;
import seng202.team3.repository.WineDrinkerDAO;
import seng202.team3.services.SignInScreenService;
import seng202.team3.services.WineDrinkerManager;


public class RegisterStepDefinitions {
    private String mockUsernameField = "";
    private String mockPasswordField = "";
    private String mockRePasswordField = "";
    private IllegalWineDrinkerException passwordException = null;
    private IllegalWineDrinkerException usernameException = null;
    private SignInScreenService signInScreenService;
    private WineDrinkerManager wineDrinkerManager;

    private final String DATABASE_PATH = "jdbc:sqlite:./src/test/resources/test_database.db";

    @Given("The Wine Drinker is on the register page")
    public void theWineDrinkerIsOnTheRegisterPageWithDatabaseLoaded(){
        wineDrinkerManager = WineDrinkerManager.getInstance();
        wineDrinkerManager.setWineDrinkerDAO( new WineDrinkerDAO(DATABASE_PATH));
        signInScreenService = new SignInScreenService();
        signInScreenService.setWineDrinkerManager(wineDrinkerManager);
    }

    @Given("user {string} already exists in database")
    public void addExistingUserToDatabase (String existingUser) {
        signInScreenService.validateAndRegisterUser(existingUser, "tests", "tests", null, null, null, null, 0);
    }

    @Given("Wine Drinker inputs {string} in the username field")
    public void wineDrinkerInputsInUsernameField(String string) {
        mockUsernameField = string;
    }

    @Given("Wine Drinker inputs {string} in the password field")
    public void wineDrinkerInputsInPasswordField(String string) {
        mockPasswordField = string;
    }

    @Given("Wine Drinker inputs {string} in the re-enter password field")
    public void wineDrinkerInputsInRePasswordField(String string) {
        mockRePasswordField = string;
    }

    @Given("Wine Drinker {string} does not exist")
    public void ensureWineDrinkerDoesntExist(String string) {
        WineDrinker mockDrinker = new WineDrinker(string, null, null, null, null, null, 0);
        wineDrinkerManager.deleteWineDrinker(mockDrinker);
    }

    @When("user clicks create account button with invalid username")
    public void userClicksCreateAccountButtonWithWrongUsername(){
        usernameException = assertThrows(IllegalWineDrinkerException.class, () ->
                                        signInScreenService.validateAndRegisterUser(mockUsernameField, mockPasswordField,
                                        mockRePasswordField, null, null, null, null, 0));
    }

    @When("user clicks create account button with invalid password")
    public void userClicksCreateAccountButtonWithMismatchingPasswords(){
        passwordException = assertThrows(IllegalWineDrinkerException.class, () -> signInScreenService.validateAndRegisterUser(mockUsernameField, mockPasswordField,
                mockRePasswordField, null, null, null, null, 0));
    }

    @Then("user is prompted that their username must contain 5-16 alphanumeric characters and user is not registered in the database")
    public void userGetsUsernameMismatchErrorMessage(){
        assertEquals(usernameException.getMessage(),"Username must be between 5 and 16 characters and must be alpha-numeric");
    }

    @Then("user is prompted that their Username is already taken and user is not registered in the database")
    public void userGetsUsernameTakenErrorMessage(){
        assertEquals(usernameException.getMessage(),"Username is already taken");
    }
    @Then("user is prompted that their passwords do not match and user is not registered in the database")
    public void userGetsPasswordsDontMatchErrorMessage(){
        assertEquals(passwordException.getMessage(),"Passwords do not match");
    }
}
