Feature: Register Wine Drinker
  Scenario: AT_5 User registers with invalid username with special character
    Given The Wine Drinker is on the register page
    And Wine Drinker "testing%" does not exist
    And Wine Drinker inputs "testing%" in the username field
    And Wine Drinker inputs "password" in the password field
    And Wine Drinker inputs "password" in the re-enter password field
    When user clicks create account button with invalid username
    Then user is prompted that their username must contain 5-16 alphanumeric characters and user is not registered in the database

  Scenario: AT_6 User registers with invalid username too short
    Given The Wine Drinker is on the register page
    And Wine Drinker "adm" does not exist
    And Wine Drinker inputs "adm" in the username field
    And Wine Drinker inputs "password" in the password field
    And Wine Drinker inputs "password" in the re-enter password field
    When user clicks create account button with invalid username
    Then user is prompted that their username must contain 5-16 alphanumeric characters and user is not registered in the database

  Scenario: AT_8 User registers without second password
    Given The Wine Drinker is on the register page
    And Wine Drinker "testing" does not exist
    And Wine Drinker inputs "testing" in the username field
    And Wine Drinker inputs "password" in the password field
    When user clicks create account button with invalid password
    Then user is prompted that their passwords do not match and user is not registered in the database

  Scenario: AT_7 User registers with valid username already taken
    Given The Wine Drinker is on the register page
    And user "testing" already exists in database
    And Wine Drinker inputs "testing" in the username field
    And Wine Drinker inputs "password" in the password field
    And Wine Drinker inputs "password" in the re-enter password field
    When user clicks create account button with invalid username
    Then user is prompted that their Username is already taken and user is not registered in the database