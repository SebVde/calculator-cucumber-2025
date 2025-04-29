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
    Then the operation evaluates to rational "15/24"

  Scenario: Dividing two rational numbers
    Given a rational operation '/'
    And the following list of rational numbers
      | 3/4 | 5/6 |
    Then the operation evaluates to rational "18/20"

  # Complex arithmetic scenarios
  Scenario: Adding two complex numbers
    Given a rational operation '+'
    And the following list of rational numbers
      | 3/4 + 5/6i | 1/2 + 1/3i |
    Then the operation evaluates to rational "5/4 + 3/2i"

  Scenario: Subtracting two complex numbers
    Given a rational operation '-'
    And the following list of rational numbers
      | 3/4 + 5/6i | 1/2 + 1/3i |
    Then the operation evaluates to rational "1/4 + 1/2i"

  Scenario: Multiplying two complex numbers
    Given a rational operation '*'
    And the following list of rational numbers
      | 3/4 + 5/6i | 1/2 + 1/3i |
    Then the operation evaluates to rational "-1/36 + 29/36i"

  Scenario: Dividing two complex numbers
    Given a rational operation '/'
    And the following list of rational numbers
      | 3/4 + 5/6i | 1/2 + 1/3i |
    Then the operation evaluates to rational "19/25 + 2/25i"

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
