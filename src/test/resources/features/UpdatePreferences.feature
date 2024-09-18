Feature: Update Preferences of Wine Drinker

  Scenario: AT_26 Change user preferences and save them
    Given Wine Drinker is logged in as "testing"
    And has wine preferences Colour: "", Fullness: "", Variety: "" and ABV Limit: 25%
    And is on the profile screen
    When Wine Drinker changes Colour to "White", Fullness to "", Variety to "" and AVB Limit to 14%
    And clicks save preferences
    Then Wine Drinker’s new preference is stored as Colour: "White", Fullness: "", Variety: "" and ABV Limit: 14%