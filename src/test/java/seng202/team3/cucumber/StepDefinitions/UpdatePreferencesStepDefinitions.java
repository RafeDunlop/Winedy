package seng202.team3.cucumber.StepDefinitions;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.jupiter.api.BeforeEach;
import seng202.team3.models.WineDrinker;
import seng202.team3.services.ProfileScreenService;
import seng202.team3.services.SignInScreenService;
import seng202.team3.services.WineDrinkerManager;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Cucumber tests for AT_26 and AT_27 (updating user preferences)
 * @author Krishna Sridhar
 */

public class UpdatePreferencesStepDefinitions {
    private String mockUsername;
    private String colourPreference;
    private String fullnessPreference;
    private String varietyPreference;
    private double abvLimitPreference;
    private WineDrinkerManager wineDrinkerManager;
    private SignInScreenService signInScreenService = new SignInScreenService();
    private ProfileScreenService profileScreenService = new ProfileScreenService();

    @Given("Wine Drinker is logged in as {string}")
    public void wineDrinkLoggedIn(String mockUsername) {
        this.mockUsername = mockUsername;
    }

    @Given("has wine preferences Colour: {string}, Fullness: {string}, Variety: {string} and ABV Limit: {double}%")
    public void existingPreferences(String colourPreference, String fullnessPreference, String varietyPreference, double abvLimitPreference) {
        this.colourPreference = colourPreference;
        this.fullnessPreference = fullnessPreference;
        this.varietyPreference = varietyPreference;
        this.abvLimitPreference = abvLimitPreference;
    }

    @Given("is on the profile screen")
    public void userIsOnSearchScreenWithDatabaseLoaded() {
        wineDrinkerManager = WineDrinkerManager.getInstance();
        WineDrinker wineDrinker = new WineDrinker(mockUsername, "password", null, colourPreference, fullnessPreference, varietyPreference, abvLimitPreference);
        wineDrinkerManager.setCurrentUser(wineDrinker);
    }

    @When("Wine Drinker changes Colour to {string}, Fullness to {string}, Variety to {string} and AVB Limit to {double}%")
    public void changePreferences(String newColourPreference, String newFullnessPreference, String newVarietyPreference, double newABVLimitPreference) {
        this.colourPreference = newColourPreference;
        this.fullnessPreference = newFullnessPreference;
        this.varietyPreference = newVarietyPreference;
        this.abvLimitPreference = newABVLimitPreference;
    }

    @When("clicks save preferences")
    public void savePreferences() {
        profileScreenService.savePreferences(colourPreference, fullnessPreference, varietyPreference, abvLimitPreference);
    }

    @Then("Wine Drinker new preference is stored as Colour: {string}")
    public void storedPreferences(String colourPreference) {
        WineDrinker wineDrinker = wineDrinkerManager.getCurrentUser();
        assertEquals(wineDrinker.getColourPreference(), colourPreference);
    }
}
