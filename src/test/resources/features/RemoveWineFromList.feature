Feature: Removing wine from list
  Scenario:
    Given Wine Drinker logged in with username "testing" and the password "password"
    And Wine Drinker is viewing their "testList" and has selected a wine to delete
    When Confirm delete button is clicked
    Then The Wine is deleted from the list



