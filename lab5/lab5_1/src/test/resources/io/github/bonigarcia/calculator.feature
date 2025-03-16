Feature: Basic Arithmetic

  Background: A Calculator
    Given a calculator I just turned on

  Scenario: Addition
    When I add 4 and 5
    Then the result is 9

  Scenario: Substraction
    When I substract 7 to 2 
    Then the result is 5


  Scenario Outline: Several additions
    When I add <a> and <b>
    Then the result is <c>
  
  Examples: Single digits
    | a | b | c  |
    | 1 | 2 | 3  |
    | 3 | 7 | 10 |


  Scenario Outline: Multiplication
    When I multiply <d> and <e>
    Then the result is <f>

  Examples: Multiplication examples
    | d | e | f  |
    | 1 | 4 | 4  |
    | 0 | 4 | 0  |
    | 4 | 0 | 0  |
    | 4 | 1 | 4  |
    | 8 | 6 | 48 |


  Scenario Outline: Division 
    When I divide <g> by <h>
    Then the result is <i>

  Examples: Multiplication examples
    | g | h | i  |
    | 4 | 1 | 4 |
    | 4 | 2 | 2 |
    | 60 | 1 | 60 |
    | 60 | 3 | 20 |

  Scenario: Zero division 
    When I divide 7 by zero
    Then division by zero exception is thrown
