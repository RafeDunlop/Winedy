Feature: Login Wine Drinker
  Scenario: AT_9 Valid Login
    Given The Wine Drinker is on the login page
    And "testing" is a registered user with password "password"
    And Wine Drinker inputs "testing" into username field
    And Wine Drinker inputs "password" into password field
    When Wine Drinker clicks login button with valid data
    Then user is logged in and moved to the profile screen

  Scenario: AT_10 invalid login user does not exist
    Given The Wine Drinker is on the login page
    And "testing" is not a registered user
    And Wine Drinker inputs "testing" into username field
    And Wine Drinker inputs "password" into password field
    When Wine Drinker clicks login button with invalid user
    Then user is not logged in and prompted with user does not exist

    Scenario: AT_11
      Given The Wine Drinker is on the login page
      And "testing" is a registered user with password "clever"
      And Wine Drinker inputs "testing" into username field
      And Wine Drinker inputs "password" into password field
      When Wine Drinker clicks login button with invalid password
      Then user is prompted with password incorrect
