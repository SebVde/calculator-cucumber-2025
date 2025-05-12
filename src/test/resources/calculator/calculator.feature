Feature: Arithmetic Expressions
  This feature provides a range of scenarios corresponding to the
  intended external behaviour of arithmetic expressions on integers, rational numbers, and complex numbers.

  Background:
    Given I initialise a calculator

  # Integer arithmetic scenarios
  Scenario: Adding two integer numbers
    Given an integer operation '+'
    When I provide a first number 4
    And I provide a second number 5
    Then the operation evaluates to 9

  Scenario: Subtracting two integer numbers
    Given an integer operation '-'
    When I provide a first number 7
    And I provide a second number 5
    Then the operation evaluates to 2

  # Real arithmetic scenarios
  Scenario: Adding two real numbers
    Given a real operation '+'
    When I provide a first number 4.5
    And I provide a second number 5.5
    Then the operation evaluates to 10.0

   Scenario: Subtracting two real numbers
    Given a real operation '-'
    When I provide a first number 7.5
    And I provide a second number 5.5
    Then the operation evaluates to 2.0

  Scenario: Multiplying two real numbers
    Given a real operation '*'
    When I provide a first number 3.5
    And I provide a second number 2.0
    Then the operation evaluates to 7.0

  Scenario: Dividing two real numbers
    Given a real operation '/'
    When I provide a first number 7.5
    And I provide a second number 2.5
    Then the operation evaluates to 3.0

  # Rational arithmetic scenarios
  Scenario: Adding two rational numbers
    Given a rational operation '+'
    And the following list of rational numbers
      | 3/4 | 5/6 |
    Then the operation evaluates to rational "19/12"

  Scenario: Subtracting two rational numbers
    Given a rational operation '-'
    And the following list of rational numbers
      | 7/6 | 5/6 |
    Then the operation evaluates to rational "1/3"

  Scenario: Multiplying two rational numbers
    Given a rational operation '*'
    And the following list of rational numbers
      | 3/4 | 5/6 |
    Then the operation evaluates to rational "5/8"

  Scenario: Dividing two rational numbers
    Given a rational operation '/'
    And the following list of rational numbers
      | 3/4 | 5/6 |
    Then the operation evaluates to rational "9/10"

  # Complex arithmetic scenarios
  Scenario: Adding two complex numbers
    Given a complex operation '+'
    And the following list of complex numbers
      | 3/4 + 5/6i | 1/2 + 1/3i |
    Then the operation evaluates to complex "5/4 + 7/6i"

  Scenario: Subtracting two complex numbers
    Given a complex operation '-'
    And the following list of complex numbers
      | 3/4 + 5/6i | 1/2 + 1/3i |
    Then the operation evaluates to complex "1/4 + 1/2i"

  Scenario: Multiplying two complex numbers
    Given a complex operation '*'
    And the following list of complex numbers
      | 3/4 + 5/6i | 1/2 + 1/3i |
    Then the operation evaluates to complex "7/72 + 2/3i"

  Scenario: Dividing two complex numbers
    Given a complex operation '/'
    And the following list of complex numbers
      | 3/4 + 5/6i | 1/2 + 1/3i |
    Then the operation evaluates to complex "47/26 + 6/13i"

  # Edge cases
  Scenario: Adding an empty list of numbers
    Given an integer operation '+'
    Then the operation evaluates to 0

  Scenario: Dividing by zero
    Given an integer operation '/'
    When I provide a first number 7
    And I provide a second number 0
    Then the operation returns NaN

  Scenario: Undefined operation
    Given an integer operation '/'
    When I provide a first number 0
    And I provide a second number 0
    Then the operation returns NaN
