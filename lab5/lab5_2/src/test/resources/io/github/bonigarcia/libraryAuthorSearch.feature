Feature: Author search
  Background: A library with books
    Given the library is initialized with the following data
        | title                | author          | published |
        | One good book        | Anonymous       | 14-03-2013 | 
        | Some other book      | Tim Tomson      | 23-03-2014 |
        | How to cook a dino   | Fred Flintstone | 01-01-2012 | 
        | How to cook a dino 2 | Fred Flintstone | 01-01-2016 | 

  Scenario Outline: Search books by author
    When the customer searches for books published by '<author>'
    Then <publications> books should have been found
      And Book <index> should have the title '<title>'

   Examples: 
    | author         | publications | index | title              |
    | Tim Tomson     | 1            | 1     | Some other book    |
    | Fred Flintstone | 2            | 1     | How to cook a dino |
