Feature: Search functionality for the online library
  Scenario: User searches for a book by title
    Given the user is on the homepage of the online library
    When the user enters "Harry Potter" into the search bar
    And the user clicks on the search button
    Then the user should see "Harry Potter and the Sorcerer's Stone" in the search results

  Scenario: User searches for a book by author
    Given the user is on the homepage of the online library
    When the user enters "George" into the search bar
    And the user clicks on the search button
    Then the user should see books written by "George R.R. Martin" in the search results

  Scenario: User searches for a book by genre
    Given the user is on the homepage of the online library
    When the user selects "Children's" from the genres
    Then the user should see the book "The Tower of Nero" in the book list

  Scenario: User searches for a non-existent book
    Given the user is on the homepage of the online library
    When the user enters "Non Existent Book" into the search bar
    And the user clicks on the search button
    Then the user should see no books 

