Feature: Register Wine Drinker
  Scenario: AT_5 User registers with invalid username
    Given The Wine Drinker is on the register page
    And Wine Drinker inputs "testing%" in the username field
    And Wine Drinker inputs "password" in the password field
    And Wine Drinker inputs "password" in the re-enter password field
    When user clicks create account button
    Then user is prompted that their username must contain 5-16 alphanumeric characters and user is not registered in the database