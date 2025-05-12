package calculator;

import static org.junit.jupiter.api.Assertions.*;

import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.util.ArrayList;
import java.util.List;

public class CalculatorSteps {

	private ArrayList<Expression> params;
	private Operation op;
	private Calculator c;

	@Before
	public void resetMemoryBeforeEachScenario() {
		params = new ArrayList<>();
		op = null;
		c = new Calculator();
	}

	@Given("I initialise a calculator")
	public void givenIInitialiseACalculator() {
		c = new Calculator();
	}

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

	@Given("a rational operation {string}")
	public void givenARationalOperation(String s) {
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

	@Given("a real operation {string}")
	public void givenARealOperation(String s) {
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

	@Given("a complex operation {string}")
	public void givenAComplexOperation(String s) {
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

	@Given("the following list of integer numbers")
	public void givenTheFollowingListOfNumbers(List<List<String>> numbers) {
		if (params == null) {
			params = new ArrayList<>();
		}
		// Add all numbers from the first line of the table
		numbers.getFirst().forEach(n -> params.add(new RealNumber(Double.parseDouble(n))));

		// Associate operation with parameters if we have an operation defined
		if (op != null) {
			try {
				op.addMoreParams(params);
			} catch (Exception e) {
				fail("Failed to add parameters to operation: " + e.getMessage());
			}
		}
	}

	@Given("the following list of rational numbers")
	public void givenTheFollowingListOfRationalNumbers(List<List<String>> numbers) {
		if (params == null) {
			params = new ArrayList<>();
		}

		numbers.getFirst().forEach(n -> {
			String[] fraction = n.split("/");
			if (fraction.length == 2) {
				RealNumber numerator = new RealNumber(Double.parseDouble(fraction[0]));
				RealNumber denominator = new RealNumber(Double.parseDouble(fraction[1]));
				params.add(new RationalNumber(numerator, denominator));
			} else {
				fail("Invalid rational number format: " + n);
			}
		});

		// Associate operation with parameters if we have an operation defined
		if (op != null) {
			try {
				op.addMoreParams(params);
			} catch (Exception e) {
				fail("Failed to add parameters to operation: " + e.getMessage());
			}
		}
	}

	@Given("the following list of complex numbers")
	public void givenTheFollowingListOfComplexNumbers(List<List<String>> numbers) {
		if (params == null) {
			params = new ArrayList<>();
		}

		numbers.getFirst().forEach(n -> {
			try {
				// Format expected: "a/b+c/di" or "a/b-c/di"
				String[] parts;
				boolean negativeImaginary = n.contains("-");

				if (negativeImaginary) {
					parts = n.split("-");
					// Handle case where real part might be negative
					if (parts[0].isEmpty()) {
						parts[0] = "-" + parts[1];
						parts[1] = parts[2];
					}
				} else {
					parts = n.split("\\+");
				}

				String realPart = parts[0].trim();
				String imaginaryPart = parts[1].replace("i", "").trim();

				if (negativeImaginary) {
					imaginaryPart = "-" + imaginaryPart;
				}

				RationalNumber real = parseRational(realPart);
				RationalNumber imaginary = parseRational(imaginaryPart);

				params.add(new ComplexNumber(real, imaginary));
			} catch (Exception e) {
				fail("Failed to parse complex number: " + n + " - " + e.getMessage());
			}
		});

		// Associate operation with parameters if we have an operation defined
		if (op != null) {
			try {
				op.addMoreParams(params);
			} catch (Exception e) {
				fail("Failed to add parameters to operation: " + e.getMessage());
			}
		}
	}

	@Given("the following list of real numbers")
	public void givenTheFollowingListOfRealNumbers(List<List<String>> numbers) {
		if (params == null) {
			params = new ArrayList<>();
		}
		// Add all numbers from the first line of the table
		numbers.getFirst().forEach(n -> params.add(new RealNumber(Double.parseDouble(n))));

		// Associate operation with parameters if we have an operation defined
		if (op != null) {
			try {
				op.addMoreParams(params);
			} catch (Exception e) {
				fail("Failed to add parameters to operation: " + e.getMessage());
			}
		}
	}

	private RationalNumber parseRational(String fraction) {
		try {
			String[] parts = fraction.split("/");
			if (parts.length == 2) {
				RealNumber numerator = new RealNumber(Double.parseDouble(parts[0]));
				RealNumber denominator = new RealNumber(Double.parseDouble(parts[1]));
				return new RationalNumber(numerator, denominator);
			} else if (parts.length == 1) {
				// Handle real numbers (not fractions)
				return new RationalNumber(new RealNumber(Double.parseDouble(parts[0])), new RealNumber(1.0));
			} else {
				throw new IllegalArgumentException("Invalid rational format: " + fraction);
			}
		} catch (NumberFormatException _) {
			throw new IllegalArgumentException("Invalid number in rational: " + fraction);
		}
	}

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

	@When("^I provide a second number (\\d+\\.\\d+)$")
	public void whenIProvideASecondRealNumber(double val) {
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

	@Then("the operation evaluates to {double}")
	public void thenTheOperationEvaluatesTo(double val) {
		MyNumber result = (MyNumber) c.eval(op);
		assertInstanceOf(RealNumber.class, result, "Result should be a RealNumber");
		assertEquals(val, ((RealNumber) result).getValue(), 0.0001);
	}

	@Then("the operation evaluates to rational {string}")
	public void thenTheOperationEvaluatesToRational(String expected) {
		MyNumber result = (MyNumber) c.eval(op);
		assertInstanceOf(RationalNumber.class, result, "Result should be a RationalNumber but was " + result.getClass().getName());
		RationalNumber rationalResult = (RationalNumber) result;
		String simplifiedResult = rationalResult.simplify().toString();
		assertEquals(expected, simplifiedResult);
	}

	@Then("the operation evaluates to complex {string}")
	public void thenTheOperationEvaluatesToComplex(String expected) {
		MyNumber result = (MyNumber) c.eval(op);
		assertInstanceOf(ComplexNumber.class, result, "Result should be a ComplexNumber but was " + result.getClass().getName());
		ComplexNumber complexResult = (ComplexNumber) result;
		assertEquals(expected, complexResult.toString());
	}

	@Then("the operation returns NaN")
	public void thenTheOperationReturnsNaN() {
		MyNumber result = (MyNumber) c.eval(op);
		assertInstanceOf(RealNumber.class, result, "Result should be a RealNumber");
		assertEquals(Double.NaN, ((RealNumber) result).getValue());
	}

	@Then("its INFIX notation is {string}")
	public void thenItsINFIXNotationIs(String s) {
		op.notation = Notation.INFIX;
		assertEquals(s, op.toString());
	}

	@Then("its PREFIX notation is {string}")
	public void thenItsPREFIXNotationIs(String s) {
		op.notation = Notation.PREFIX;
		assertEquals(s, op.toString());
	}

	@Then("its POSTFIX notation is {string}")
	public void thenItsPOSTFIXNotationIs(String s) {
		op.notation = Notation.POSTFIX;
		assertEquals(s, op.toString());
	}
}