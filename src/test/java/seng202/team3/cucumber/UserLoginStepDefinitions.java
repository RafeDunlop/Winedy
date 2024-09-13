package seng202.team3.cucumber;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import seng202.team3.WineDrinkerManager;

public class UserLoginStepDefinitions {

    WineDrinkerManager wineDrinkerManager = WineDrinkerManager.getInstance();
    @Given("registered username is {string} and password is {string}")
    public void registeredUsernameAndPasswordIs(String username, String Password) {

    }

    @When("I login using {string} and {string}")
    public void loginUser(String username, String password) {

    }

    @Then("I should be logged in")
    public void checkCurrentUser() {

    }
}
