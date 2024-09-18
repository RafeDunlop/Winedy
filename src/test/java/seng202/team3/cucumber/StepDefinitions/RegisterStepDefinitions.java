package seng202.team3.cucumber.StepDefinitions;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import seng202.team3.exceptions.IllegalWineDrinkerException;
import seng202.team3.repository.WineDrinkerDAO;
import seng202.team3.services.SignInScreenService;

public class RegisterStepDefinitions {
    private String mockUsernameField;
    private String mockPasswordField;
    private String mockRePasswordField;
    private IllegalWineDrinkerException exception;
    private SignInScreenService signInScreenService = new SignInScreenService();

    String DATABASE_PATH = "jdbc:sqlite:./src/test/resources/test_database.db";
    private WineDrinkerDAO wineDrinkerDAO;
    @Given("The Wine Drinker is on the register page")
    public void theWineDrinkerIsOnTheRegisterPageWithDatabaseLoaded(){
        wineDrinkerDAO = new WineDrinkerDAO(DATABASE_PATH);
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
    @When("user clicks create account button")
    public void userClicksCreateAccountButton(){
        exception = assertThrows(IllegalWineDrinkerException.class, () -> signInScreenService.validateRegisteringUsername(mockUsernameField));
        signInScreenService.validateRegisteringPasswords(mockPasswordField, mockRePasswordField);
        signInScreenService.registerUser(mockUsernameField, mockPasswordField, null, null, null, null, 0);
    }

    @Then("user is prompted that their username must contain 5-16 alphanumeric characters and user is not registered in the database")
    public void userGetsUsernameErrorMessage(){
        assertEquals(exception.getMessage(),"Username must be between 5 and 16 characters and must be alpha-numeric");
    }
}
