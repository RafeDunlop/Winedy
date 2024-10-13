Feature: Adding a wine to list
  Scenario: Wine Drinker adds a wine to an already existing list
    Given Wine Drinker is logged in with username "testing" and password "password"
    And Wine Drinker is viewing a wine on the search screen and they click add wine to list
    When Wine Drinker clicks "Summer Wines" on the add wine to list pop up to add wine to this list
    Then A wine is added to "Summer Wines"

  Scenario: Wine Drinker creates a new list to add the wine into
    Given Wine Drinker is logged in with username "testing" and password "password"
    And Wine Drinker is viewing a wine on the search screen and they click add wine to list
    And The Wine Drinker clicks the create new list method on the add wine to list pop up
    And Wine Drinker enters "testList" into the name field
    And Wine Drinker clicks the create list button
    When Wine Drinker clicks "testList" on the add wine to list pop up to add wine to this list
    Then A wine is added to "testList"

    Scenario: Wine Drinker removes wine from list
      Given Wine Drinker is logged in with username "testing" and password "password"
      And Wine Drinker is viewing a wine on the search screen and they click add wine to list
      And Wine already in "Summer Wines"
      When Wine Drinker clicks "Summer Wines" on the add wine to list pop up to add wine to this list, unselecting it
      Then A wine is removed from "Summer Wines"

