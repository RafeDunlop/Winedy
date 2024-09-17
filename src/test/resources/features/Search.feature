Feature: Wine search

  Scenario: Search for red, medium wines from New Zealand with "Marlborough Martinborough" in name or long description
    Given The Wine Drinker is in the search wine page
    When search with the phrase "Marlborough Martinborough"
    And enters the filter Colour: "Red"
    And enters the filter Fullness: "MEDIUM"
    And enters the filter Country: "New Zealand"
    Then the search returns wines with either Phrase: "Marlborough Martinborough", Colour: "Red", Fullness: "MEDIUM", Country: "New Zealand"