Feature: Creating a new wine list
  Scenario: Wine Drinker creates a new wine list
    Given The Wine Drinker is viewing their profile and clicks create new wine list
    And The Wine Drinker enters "testList" into the name field
    When The Wine Drinker clicks the create list button
    Then A new list is created called "testList" with description ""

  Scenario: Wine drinker creates a new list with the max character length
    Given The Wine Drinker is viewing their profile and clicks create new wine list
    And The Wine Drinker enters "123451234512345123451234512345" into the name field
    When The Wine Drinker clicks the create list button
    Then A new list is created called "123451234512345123451234512345" with description ""


  Scenario: Wine drinker creates a new list with the max description length
    Given The Wine Drinker is viewing their profile and clicks create new wine list
    And The Wine Drinker enters "testList" into the name field
    And The Wine Drinker enters "Cabernet Sauvignon, one of the most popular red wines globally, is known for its deep, bold flavors and strong tannins. It often carries notes of black currant, dark cherries, and hints of vanilla or cedar, making it a complex and rich choice. Grown in regions like Bordeaux, California, and Chile, this wine pairs beautifully with hearty dishes like grilled meats or aged cheeses. Its full body and high tannin content allow it to age well, further developing nuanced flavors over time." into the description field
    When The Wine Drinker clicks the create list button
    Then A new list is created called "testList" with description "Cabernet Sauvignon, one of the most popular red wines globally, is known for its deep, bold flavors and strong tannins. It often carries notes of black currant, dark cherries, and hints of vanilla or cedar, making it a complex and rich choice. Grown in regions like Bordeaux, California, and Chile, this wine pairs beautifully with hearty dishes like grilled meats or aged cheeses. Its full body and high tannin content allow it to age well, further developing nuanced flavors over time."


  Scenario: Wine Drinker attempts to create a new list with no list name
    Given The Wine Drinker is viewing their profile and clicks create new wine list
    When The Wine Drinker enters "" into the name field
    Then An error message is created that says "List name is required to create a list"


  Scenario: Wine Drinker attempts to create a list with a name that already exists
    Given The Wine Drinker is viewing their profile and clicks create new wine list
    When The Wine Drinker enters "Favourites" into the name field
    Then An error message is created that says "A list with this name already exists!"

