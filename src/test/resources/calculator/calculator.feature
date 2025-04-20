Feature: Integer Arithmetic Expressions
  This feature provides a range of scenarios corresponding to the
  intended external behaviour of arithmetic expressions on integers.

  # This is just a comment.
  # You can start with a Background: that will be run before executing each scenario.

  Background:
    Given I initialise a calculator

  # Each scenario can be seen as a test that can be executed with JUnit,
  # provided that each of the steps (Given, When, And and Then) are
  # implemented in a Java mapping file (CalculatorSteps.Java)

  Scenario: Adding two integer numbers
    Given an operation '+'
    When I provide a first number 4
    And I provide a second number 5
    Then the operation evaluates to 9

  Scenario: Adding two decimal numbers
    Given an operation '+'
    When I provide a first number 4.0
    And I provide a second number 5.1
    Then the operation evaluates to 9.1

  Scenario: Adding two complex numbers
    Given an operation '+'
    When I provide a first number 2+3i
    And I provide a second number 6+1i
    Then the operation evaluates to 8+4i

  Scenario: Subtracting two integer numbers
    Given an operation '-'
    When I provide a first number 7
    And I provide a second number 5
    Then the operation evaluates to 2

  Scenario: Subtracting two decimal numbers
    Given an operation '-'
    When I provide a first number 7.0
    And I provide a second number 5.0
    Then the operation evaluates to 2.0

  Scenario: Subtracting two complex numbers
    Given an operation '-'
    When I provide a first number 6+1i
    And I provide a second number 2+6i
    Then the operation evaluates to 4-5i

  Scenario: Multiplying two integer numbers
    Given an operation '*'
    When I provide a first number 7
    And I provide a second number 5
    Then the operation evaluates to 35

  Scenario: Multiplying two decimal numbers
    Given an operation '*'
    When I provide a first number 7.0
    And I provide a second number 5.0
    Then the operation evaluates to 35.0

  Scenario: Multiplying two complex numbers
    Given an operation '*'
    When I provide a first number 2+3i
    And I provide a second number 6+1i
    Then the operation evaluates to 9+20i

  Scenario: Dividing two integer numbers
    Given an operation '/'
    When I provide a first number 7
    And I provide a second number 5
    Then the operation evaluates to 1

  Scenario: Dividing two decimal numbers
    Given an operation '/'
    When I provide a first number 5.0
    And I provide a second number 2.0
    Then the operation evaluates to 2.5

  Scenario: Dividing two complex numbers
    Given an operation '/'
    When I provide a first number 20-4i
    And I provide a second number 3+2i
    Then the operation evaluates to 4-4i

  Scenario: Dividing an integer by zero
    Given an operation '/'
    When I provide a first number 7
    And I provide a second number 0
    Then the operation returns infinity

  Scenario: Dividing zero by zero
    Given an operation '/'
    When I provide a first number 0
    And I provide a second number 0
    Then the undefined operation is equal to 0

  Scenario: Printing the sum of two integer numbers
    Given the sum of two numbers 8 and 6
    Then its INFIX notation is ( 8 + 6 )
    And its PREFIX notation is + (8, 6)
    And its POSTFIX notation is (8, 6) +

  Scenario: Printing the sum of two decimal numbers
    Given the sum of two numbers 8.0 and 6.0
    Then its INFIX notation is ( 8.0 + 6.0 )
    And its PREFIX notation is + (8.0, 6.0)
    And its POSTFIX notation is (8.0, 6.0) +

  # This is an example of a scenario in which we provide a list of numbers as input.
  # (In fact, this is not entirely true, since what is given as input is a table of
  # strings. In this case, the table is of dimension 1 * 3 (1 line and three columns).
  Scenario: Evaluation arithmetic operations over a list of integer numbers
    Given the following list of numbers
      | 8 | 2 | 2 |
    Then the sum is 12
    And the product is 32
    And the difference is 4
    And the quotient is 2

  # A scenario outline (or template) is a scenario that is parameterised
  # with different values. The outline comes with a set of examples.
  # The scenario will be executed with each of the provided inputs.
  Scenario Outline: Adding two numbers
    Given an operation '+'
    When I provide a first number <n1>
    And I provide a second number <n2>
    Then the operation evaluates to <result>

    Examples:
      | n1    | n2 | result |
      | 4     | 5  | 9      |
      | 5     | 3  | 8      |
      | 4.0E2 | 5  | 405.0  |
      | 5E-2  | 3  | 3.05   |

  Scenario Outline: Dividing two numbers
    Given an operation '/'
    When I provide a first number <n1>
    And I provide a second number <n2>
    Then the operation evaluates to <result>

    Examples:
      | n1 | n2 | result |
      | 35 | 5  | 7      |
      | 7  | 5  | 1      |
      | 5  | 7  | 0      |
      | 7. | 2  | 3.5    |
      | .1 | 2  | 0.05   |

  Scenario Outline: Evaluating arithmetic operations with two integer parameters
    Given an operation <op>
    When I provide a first number <n1>
    And I provide a second number <n2>
    Then the operation evaluates to <result>

    Examples:
      | op  | n1  | n2  | result |
      | "+" | 4   | 5   | 9      |
      | "-" | 8   | 5   | 3      |
      | "*" | 7   | 2   | 14     |
      | "/" | 6   | 2   | 3      |
      | "+" | 4.0 | 5.0 | 9.0    |
      | "-" | 8.0 | 5   | 3.0    |
      | "*" | 7   | 2.0 | 14.0   |
      | "/" | 6.0 | 2.0 | 3.0    |

  Scenario Outline: Testing different notations for arithmetic operations
    Given an operation <op>
    When I provide a first number <n1>
    And I provide a second number <n2>
    Then its INFIX notation is <infix>
    And its PREFIX notation is <prefix>
    And its POSTFIX notation is <postfix>

    Examples:
      | op  | n1   | n2  | infix           | prefix         | postfix        |
      | "+" | 8    | 6   | ( 8 + 6 )       | + (8, 6)       | (8, 6) +       |
      | "-" | 8    | 6   | ( 8 - 6 )       | - (8, 6)       | (8, 6) -       |
      | "*" | 8    | 6   | ( 8 * 6 )       | * (8, 6)       | (8, 6) *       |
      | "/" | 8    | 6   | ( 8 / 6 )       | / (8, 6)       | (8, 6) /       |
      | "*" | 8E2  | 6.0 | ( 800.0 * 6.0 ) | * (800.0, 6.0) | (800.0, 6.0) * |
      | "/" | 8E-2 | 6.1 | ( 0.08 / 6.1 )  | / (0.08, 6.1)  | (0.08, 6.1) /  |
