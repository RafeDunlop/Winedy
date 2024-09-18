Feature: Wine search

  Scenario: AT_01 Search for red, medium wines from New Zealand with "Marlborough Martinborough" in the name or long description
    Given The Wine Drinker is in the search wine page
    When search with the phrase "Marlborough Martinborough"
    And enters the filter Colour: "Red"
    And enters the filter Fullness: "MEDIUM"
    And enters the filter Country: "New Zealand"
    Then the search returns wines with either Phrase: "Marlborough Martinborough", Colour: "Red", Fullness: "MEDIUM", Country: "New Zealand"

  Scenario: AT_02 Search for rose, medium wines from New Zealand
    Given The Wine Drinker is in the search wine page
    When enters the filter Colour: "Rose"
    And enters the filter Fullness: "MEDIUM"
    And enters the filter Country: "New Zealand"
    Then the search returns wines with either Phrase: "", Colour: "Rose", Fullness: "MEDIUM", Country: "New Zealand"

  Scenario: AT_03 Search for wines with "Marlborough Martinborough" in the name or long description
    Given The Wine Drinker is in the search wine page
    When search with the phrase "Marlborough Martinborough"
    Then the search returns wines with either Phrase: "Marlborough Martinborough", Colour: "", Fullness: "", Country: ""

  Scenario: AT_04 Search for white, full wines from New Zealand
    Given The Wine Drinker is in the search wine page
    When enters the filter Colour: "White"
    And enters the filter Fullness: "FULL"
    Then the search returns wines with either Phrase: "", Colour: "White", Fullness: "FULL", Country: ""