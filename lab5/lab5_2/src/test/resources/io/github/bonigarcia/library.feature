Feature: Book search
  To allow a customer to find his favourite books quickly, the library must offer multiple ways to search for a book.

  Scenario: Search books by publication year
    Given a book with the title 'One good book', written by 'Anonymous', published in 14 March 2013
      And another book with the title 'Some other book', written by 'Tim Tomson', published in 23 August 2014
      And another book with the title 'How to cook a dino', written by 'Fred Flintstone', published in 01 January 2012
    When the customer searches for books published between 2013 and 2014
    Then 2 books should have been found
      And Book 1 should have the title 'Some other book'
      And Book 2 should have the title 'One good book'

    
  Scenario Outline: Search books by author
    Given the library is initialized with the following data
        | title                | author          | day | month | year |
        | One good book        | Anonymous       | 14  | 3     | 2013 | 
        | Some other book      | Tim Tomson      | 23  | 3     | 2014 |
        | How to cook a dino   | Fred Flintstone | 01  | 1     | 2012 | 
        | How to cook a dino 2 | Fred Flintstone | 01  | 1     | 2016 | 

    When the customer searches for books published by <author>
    Then <publications> books should have been found
      And Book <index> should have the title <title>

   Examples: 
    | author         | publications | index | title              |
    | Tim Tomson     | 1            | 1     | Some other book    |
    | Fred Flinstone | 2            | 1     | How to cook a dino |
