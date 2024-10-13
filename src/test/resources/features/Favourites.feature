Feature: Favourites list
  Scenario: Wine Drinker likes a wine
    Given Wine Drinker logged in with username "testing and password "password"
    And Wine Drinker is viewing "Stoneleigh 2008 Sauvignon Blanc"
    And "Stoneleigh 2008 Sauvignon Blanc" is not in the favourites list
    When Wine Drinker clicks the empty heart icon
    Then "Stoneleigh 2008 Sauvignon Blanc" is added to the favourites list

    Scenario: Wine Drinker has already liked the wine
      Given Wine Drinker logged in with username "testing and password "password"
      And Wine Drinker is viewing "Stoneleigh 2008 Sauvignon Blanc"
      And "Stoneleigh 2008 Sauvignon Blanc" is in the favourites list
      When Wine Drinker clicks the pink heart icon
      Then "Stoneleigh 2008 Sauvignon Blanc" is added to the favourites list