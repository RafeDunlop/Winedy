Feature: Renaming a list
  Scenario: Wine Drinker renames their list
    Given Wine Drinker is viewing "testList" and clicks rename list button
    And Wine Drinker enters "Software Vibes" into the displayed text field
    When Wine Drinker clicks save changes
    Then the list is renamed to "Software Vibes"

  Scenario: Wine drinker renames their list to be empty
    Given Wine Drinker is viewing "testList" and clicks rename list button
    When Wine Drinker enters "" into the displayed text field
    Then  Error message is created that says "List name is required to create a list"

  Scenario: Wine Drinker renames their list to be the same as another list
    Given Wine Drinker is viewing "testList" and clicks rename list button
    When Wine Drinker enters "Favourites" into the text field
    Then Error message is created that says "A list with this name already exists!"