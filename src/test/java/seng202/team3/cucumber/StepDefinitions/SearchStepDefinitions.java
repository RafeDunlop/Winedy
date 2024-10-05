package seng202.team3.cucumber.StepDefinitions;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import seng202.team3.models.SearchWineList;
import seng202.team3.models.Wine;
import seng202.team3.repository.WineDAO;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class SearchStepDefinitions {
    List<String> keywords;
    private String colour;
    private String fullness;
    private String country;
    private WineDAO wineDAO;
    private final SearchWineList searchedWines = new SearchWineList();

    @Given("The Wine Drinker is in the search wine page")
    public void userIsOnSearchScreenWithDatabaseLoaded() {
        wineDAO = new WineDAO("jdbc:sqlite:./src/test/resources/test_database.db");
    }

    private void addWines(SearchWineList searchWineList) {
        for (Wine wine : searchWineList.getWineList()) {
            boolean existing = false;
            for (Wine existingWine : searchedWines.getWineList()) {
                if (wine.getUniqueWineID() == existingWine.getUniqueWineID()) {
                    existing = true;
                    break;
                }
            }
            if (!existing) {
                searchedWines.addWineToList(wine);
            }
        }
    }

    @When("search with the phrase {string}")
    public void searchWithPhrase(String phrase) {
        this.keywords = List.of(phrase.split(" "));
        SearchWineList searchWineList = wineDAO.searchWines(keywords, null, null, null, null, null, null, null, null);
        addWines(searchWineList);
    }
    @When("enters the filter Colour: {string}")
    public void searchWithColourFilter(String colour) {
        this.colour = colour;
        SearchWineList searchWineList = wineDAO.searchWines(null, null, null, null, null, null, colour, null, null);
        addWines(searchWineList);
    }

    @When("enters the filter Fullness: {string}")
    public void searchWithFullnessFilter(String fullness) {
        this.fullness = fullness;
        SearchWineList searchWineList = wineDAO.searchWines(null, null, null, null, null, null, null, fullness, null);
        addWines(searchWineList);
    }

    @When("enters the filter Country: {string}")
    public void searchWithCountryFilter(String country) {
        this.country = country;
        SearchWineList searchWineList = wineDAO.searchWines(null, null, null, null, null, country, null, null, null);
        addWines(searchWineList);
    }

    @Then("the search returns wines with either Phrase: {string}, Colour: {string}, Fullness: {string}, Country: {string}")
    public void theSearchedWinesShouldMatchPhraseAndFilters(String phrase, String colour, String fullness, String country) {
        List<String> keywords = List.of(phrase.split(" "));
        SearchWineList searchWineList = wineDAO.searchWines(keywords, null, null, null, null, country, colour, fullness, null);
        int matching = getMatching(keywords);
        assertEquals(searchWineList.getWineList().size(), matching);
    }

    private int getMatching(List<String> keywords) {
        int matching = 0;
        boolean wordMatch;
        for (Wine wine : searchedWines.getWineList()) {
            wordMatch = false;
            for (String word : keywords) {
                if (wine.getName().contains(word) || wine.getLongDescription().contains(word)) {
                    wordMatch = true;
                    break;
                }
            }
            if (wine.getColour().equals(this.colour) && wine.getFullness().equals(this.fullness) && wine.getCountry().equals(this.country) && wordMatch) {
                matching++;
            }
        }
        return matching;
    }
}