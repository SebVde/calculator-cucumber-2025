package calculator;

import static org.junit.jupiter.api.Assertions.*;

import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.util.ArrayList;
import java.util.List;

/**
 * This class defines the step definitions used by Cucumber to test arithmetic
 * operations in the calculator. It covers real, rational, and complex number cases,
 * supports multiple notations, and performs both setup and assertion steps.
 */
public class CalculatorSteps {

	private ArrayList<Expression> params;
	private Operation op;
	private Calculator c;

	/**
	 * Initializes calculator and resets internal state before each scenario.
	 */
	@Before
	public void resetMemoryBeforeEachScenario() {
		params = new ArrayList<>();
		op = null;
		c = new Calculator();
	}

	/**
	 * Initializes a new calculator instance.
	 */
	@Given("I initialise a calculator")
	public void givenIInitialiseACalculator() {
		c = new Calculator();
	}

	/**
	 * Creates a basic arithmetic operation for integer numbers.
	 */
	@Given("an integer operation {string}")
	public void givenAnIntegerOperation(String s) {
		params = new ArrayList<>();
		try {
			switch (s) {
				case "+" -> op = new Plus(params);
				case "-" -> op = new Minus(params);
				case "*" -> op = new Times(params);
				case "/" -> op = new Divides(params);
				default -> fail("Unsupported operation: " + s);
			}
		} catch (IllegalConstruction e) {
			fail("Failed to create operation: " + e.getMessage());
		}
	}

	/**
	 * Creates a rational arithmetic operation.
	 */
	@Given("a rational operation {string}")
	public void givenARationalOperation(String s) {
		givenAnIntegerOperation(s);
	}

	/**
	 * Creates a real arithmetic operation.
	 */
	@Given("a real operation {string}")
	public void givenARealOperation(String s) {
		givenAnIntegerOperation(s);
	}

	/**
	 * Creates a complex arithmetic operation.
	 */
	@Given("a complex operation {string}")
	public void givenAComplexOperation(String s) {
		givenAnIntegerOperation(s);
	}

	/**
	 * Adds a list of integer numbers to the operation.
	 */
	@Given("the following list of integer numbers")
	public void givenTheFollowingListOfNumbers(List<List<String>> numbers) {
		if (params == null) params = new ArrayList<>();
		numbers.getFirst().forEach(n -> params.add(new RealNumber(Double.parseDouble(n))));
		if (op != null) try { op.addMoreParams(params); } catch (Exception e) { fail(e.getMessage()); }
	}

	/**
	 * Adds a list of rational numbers to the operation.
	 */
	@Given("the following list of rational numbers")
	public void givenTheFollowingListOfRationalNumbers(List<List<String>> numbers) {
		if (params == null) params = new ArrayList<>();
		numbers.getFirst().forEach(n -> {
			String[] fraction = n.split("/");
			if (fraction.length == 2) {
				RealNumber num = new RealNumber(Double.parseDouble(fraction[0]));
				RealNumber den = new RealNumber(Double.parseDouble(fraction[1]));
				params.add(new RationalNumber(num, den));
			} else fail("Invalid rational number: " + n);
		});
		if (op != null) try { op.addMoreParams(params); } catch (Exception e) { fail(e.getMessage()); }
	}

	/**
	 * Adds a list of complex numbers (as strings) to the operation.
	 */
	@Given("the following list of complex numbers")
	public void givenTheFollowingListOfComplexNumbers(List<List<String>> numbers) {
		if (params == null) params = new ArrayList<>();
		numbers.getFirst().forEach(n -> {
			try {
				String[] parts;
				boolean negative = n.contains("-");
				if (negative) {
					parts = n.split("-");
					if (parts[0].isEmpty()) {
						parts[0] = "-" + parts[1];
						parts[1] = parts[2];
					}
				} else {
					parts = n.split("\\+");
				}
				String real = parts[0].trim();
				String imag = parts[1].replace("i", "").trim();
				if (negative) imag = "-" + imag;
				params.add(new ComplexNumber(parseRational(real), parseRational(imag)));
			} catch (Exception _) {
				fail("Failed to parse complex number: " + n);
			}
		});
		if (op != null) try { op.addMoreParams(params); } catch (Exception e) { fail(e.getMessage()); }
	}

	/**
	 * Adds a list of real numbers to the operation.
	 */
	@Given("the following list of real numbers")
	public void givenTheFollowingListOfRealNumbers(List<List<String>> numbers) {
		givenTheFollowingListOfNumbers(numbers);
	}

	/**
	 * Parses a rational number from a string.
	 */
	private RationalNumber parseRational(String fraction) {
		try {
			String[] parts = fraction.split("/");
			if (parts.length == 2) return new RationalNumber(new RealNumber(Double.parseDouble(parts[0])), new RealNumber(Double.parseDouble(parts[1])));
			else if (parts.length == 1) return new RationalNumber(new RealNumber(Double.parseDouble(parts[0])), new RealNumber(1.0));
			else throw new IllegalArgumentException("Invalid rational: " + fraction);
		} catch (NumberFormatException _) {
			throw new IllegalArgumentException("Invalid number: " + fraction);
		}
	}

	/**
	 * Initializes a sum operation between two integer values.
	 *
	 * @param n1 the first integer
	 * @param n2 the second integer
	 */
	@Given("^the sum of two numbers (\\d+) and (\\d+)$")
	public void givenTheSum(int n1, int n2) {
		try {
			params = new ArrayList<>();
			params.add(new RealNumber(Double.parseDouble(String.valueOf(n1))));
			params.add(new RealNumber(Double.parseDouble(String.valueOf(n2))));
			op = new Plus(params);
		} catch (IllegalConstruction e) {
			fail("Failed to create sum operation: " + e.getMessage());
		}
	}

	/**
	 * Initializes a sum operation between two rational numbers.
	 *
	 * @param r1 the first rational number (e.g. "1/2")
	 * @param r2 the second rational number (e.g. "2/3")
	 */
	@Given("^the sum of two rational numbers (\\d+/\\d+) and (\\d+/\\d+)$")
	public void givenTheSumOfTwoRationalNumbers(String r1, String r2) {
		try {
			params = new ArrayList<>();
			params.add(parseRational(r1));
			params.add(parseRational(r2));
			op = new Plus(params);
		} catch (IllegalConstruction e) {
			fail("Failed to create rational sum operation: " + e.getMessage());
		}
	}

	/**
	 * Adds a single number (integer) to the list of parameters during the scenario.
	 *
	 * @param s  description label (ignored)
	 * @param val the integer value
	 */
	@When("^I provide a (.*) number (\\d+)$")
	public void whenIProvideANumber(String s, int val) {
		if (params == null) {
			params = new ArrayList<>();
		}

		ArrayList<Expression> newParams = new ArrayList<>();
		newParams.add(new RealNumber(Double.parseDouble(String.valueOf(val))));

		try {
			if (op != null) {
				op.addMoreParams(newParams);
			} else {
				params.addAll(newParams);
			}
		} catch (Exception e) {
			fail("Failed to add parameter to operation: " + e.getMessage());
		}
	}

	/**
	 * Adds a first real number value to the parameters.
	 *
	 * @param val the real value
	 */
	@When("^I provide a first number (\\d+\\.\\d+)$")
	public void whenIProvideAFirstRealNumber(double val) {
		if (params == null) {
			params = new ArrayList<>();
		}
		ArrayList<Expression> newParams = new ArrayList<>();
		newParams.add(new RealNumber(val));

		try {
			if (op != null) {
				op.addMoreParams(newParams);
			} else {
				params.addAll(newParams);
			}
		} catch (Exception e) {
			fail("Failed to add parameter to operation: " + e.getMessage());
		}
	}

	/**
	 * Adds a second real number value to the parameters.
	 *
	 * @param val the real value
	 */
	@When("^I provide a second number (\\d+\\.\\d+)$")
	public void whenIProvideASecondRealNumber(double val) {
		whenIProvideAFirstRealNumber(val);
	}

	/**
	 * Asserts that the result of the operation matches an expected integer result.
	 *
	 * @param s operation type (e.g., sum, product)
	 * @param val expected result
	 */
	@Then("^the (.*) is (\\d+)$")
	public void thenTheOperationIs(String s, int val) {
		try {
			switch (s) {
				case "sum" -> op = new Plus(params);
				case "product" -> op = new Times(params);
				case "quotient" -> op = new Divides(params);
				case "difference" -> op = new Minus(params);
				default -> fail("Unsupported operation: " + s);
			}
			assertEquals(val, ((RealNumber) c.eval(op)).getValue());
		} catch (IllegalConstruction e) {
			fail("Failed to evaluate operation: " + e.getMessage());
		}
	}

	/**
	 * Asserts that the operation result matches a real number value.
	 *
	 * @param val expected result value
	 */
	@Then("the operation evaluates to {double}")
	public void thenTheOperationEvaluatesTo(double val) {
		MyNumber result = (MyNumber) c.eval(op);
		assertInstanceOf(RealNumber.class, result, "Result should be a RealNumber");
		assertEquals(val, ((RealNumber) result).getValue(), 0.0001);
	}

	/**
	 * Asserts that the operation result matches an expected rational number.
	 *
	 * @param expected expected output as a string (e.g., "1/2")
	 */
	@Then("the operation evaluates to rational {string}")
	public void thenTheOperationEvaluatesToRational(String expected) {
		MyNumber result = (MyNumber) c.eval(op);
		assertInstanceOf(RationalNumber.class, result, "Result should be a RationalNumber but was " + result.getClass().getName());
		RationalNumber rationalResult = (RationalNumber) result;
		String simplifiedResult = rationalResult.simplify().toString();
		assertEquals(expected, simplifiedResult);
	}

	/**
	 * Asserts that the operation result matches an expected complex number.
	 *
	 * @param expected expected output as a string (e.g., "1+2i")
	 */
	@Then("the operation evaluates to complex {string}")
	public void thenTheOperationEvaluatesToComplex(String expected) {
		MyNumber result = (MyNumber) c.eval(op);
		assertInstanceOf(ComplexNumber.class, result, "Result should be a ComplexNumber but was " + result.getClass().getName());
		ComplexNumber complexResult = (ComplexNumber) result;
		assertEquals(expected, complexResult.simplify().toString());
	}

	/**
	 * Asserts that the operation result is Not-a-Number (NaN).
	 */
	@Then("the operation returns NaN")
	public void thenTheOperationReturnsNaN() {
		MyNumber result = (MyNumber) c.eval(op);
		assertInstanceOf(RealNumber.class, result, "Result should be a RealNumber");
		assertEquals(Double.NaN, ((RealNumber) result).getValue());
	}

	/**
	 * Verifies the INFIX notation string representation of the operation.
	 *
	 * @param s expected INFIX string output
	 */
	@Then("its INFIX notation is {string}")
	public void thenItsINFIXNotationIs(String s) {
		op.notation = Notation.INFIX;
		assertEquals(s, op.toString());
	}

	/**
	 * Verifies the PREFIX notation string representation of the operation.
	 *
	 * @param s expected PREFIX string output
	 */
	@Then("its PREFIX notation is {string}")
	public void thenItsPREFIXNotationIs(String s) {
		op.notation = Notation.PREFIX;
		assertEquals(s, op.toString());
	}

	/**
	 * Verifies the POSTFIX notation string representation of the operation.
	 *
	 * @param s expected POSTFIX string output
	 */
	@Then("its POSTFIX notation is {string}")
	public void thenItsPOSTFIXNotationIs(String s) {
		op.notation = Notation.POSTFIX;
		assertEquals(s, op.toString());
	}
}