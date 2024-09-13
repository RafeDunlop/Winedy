Feature: Login
  Scenario: User trys to login using a username and password that was previously registered
    Given registered username is "Username" and password is "Password123"
    When I login using "Username" and "Password123"
    Then I should be logged in

  Scenario: User trys to login using a username and password that has not been registered
    Given There are no registered users
    When I login using "Username" and "Password123"
    Then I should get an error message