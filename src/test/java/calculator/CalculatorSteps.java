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
		params = null;
		op = null;
	}

	@Given("I initialise a calculator")
	public void givenIInitialiseACalculator() {
		c = new Calculator();
	}

	@Given("an integer operation {string}")
	public void givenAnIntegerOperation(String s) {
		// Write code here that turns the phrase above into concrete actions
		params = new ArrayList<>(); // create an empty set of parameters to be filled in
		try {
			switch (s) {
				case "+"	->	op = new Plus(params);
				case "-"	->	op = new Minus(params);
				case "*"	->	op = new Times(params);
				case "/"	->	op = new Divides(params);
				default		->	fail();
			}
		} catch (IllegalConstruction e) {
			fail();
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
	            default -> fail();
	        }
	    } catch (IllegalConstruction e) {
	        fail();
	    }
	}

	// The following example shows how to use a DataTable provided as input.
	// The example looks slightly complex, since DataTables can take as input
	//  tables in two dimensions, i.e. rows and lines. This is why the input
	//  is a list of lists.
	@Given("the following list of integer numbers")
	public void givenTheFollowingListOfNumbers(List<List<String>> numbers) {
		params = new ArrayList<>();
		// Since we only use one line of input, we use get(0) to take the first line of the list,
		// which is a list of strings, that we will manually convert to integers:
		numbers.getFirst().forEach(n -> params.add(new RealNumber(Double.parseDouble(n))));
	    params.forEach(n -> System.out.println("value ="+ n));
		op = null;
	}

	@Given("the following list of rational numbers")
	public void givenTheFollowingListOfRationalNumbers(List<List<String>> numbers) {
	    params = new ArrayList<>();
	    numbers.getFirst().forEach(n -> {
	        String[] fraction = n.split("/");
	        RealNumber numerator = new RealNumber(Double.parseDouble(fraction[0]));
	        RealNumber denominator = new RealNumber(Double.parseDouble(fraction[1]));
	        params.add(new RationalNumber(numerator, denominator));
	    });
	    params.forEach(n -> System.out.println("value = " + n));
	    op = null;
	}

	@Given("the following list of complex numbers")
	public void givenTheFollowingListOfComplexNumbers(List<List<String>> numbers) {
	    params = new ArrayList<>();
	    numbers.getFirst().forEach(n -> {
	        String[] parts = n.split("\\+");
	        String realPart = parts[0].trim();
	        String imaginaryPart = parts[1].replace("i", "").trim();
	        RationalNumber real = parseRational(realPart);
	        RationalNumber imaginary = parseRational(imaginaryPart);
	        params.add(new ComplexNumber(real, imaginary));
	    });
	    params.forEach(n -> System.out.println("value = " + n));
	    op = null;
	}

	private RationalNumber parseRational(String fraction) {
	    String[] parts = fraction.split("/");
	    RealNumber numerator = new RealNumber(Double.parseDouble(parts[0]));
	    RealNumber denominator = new RealNumber(Double.parseDouble(parts[1]));
	    return new RationalNumber(numerator, denominator);
	}

	// The string in the Given annotation shows how to use regular expressions...
	// In this example, the notation d+ is used to represent numbers, i.e. nonempty sequences of digits
	@Given("^the sum of two numbers (\\d+) and (\\d+)$")
	// The alternative, and in this case simpler, notation would be:
	// @Given("the sum of two numbers {int} and {int}")
	public void givenTheSum(int n1, int n2) {
		try {
			params = new ArrayList<>();
		    params.add(new RealNumber(Double.parseDouble(String.valueOf(n1))));
		    params.add(new RealNumber(Double.parseDouble(String.valueOf(n2))));
		    op = new Plus(params);}
		catch(IllegalConstruction e) { fail(); }
	}

	@Given("^the sum of two rational numbers (\\d+/\\d+) and (\\d+/\\d+)$")
	public void givenTheSumOfTwoRationalNumbers(String r1, String r2) {
	    try {
	        params = new ArrayList<>();
	        String[] fraction1 = r1.split("/");
	        String[] fraction2 = r2.split("/");
	        params.add(new RationalNumber(new RealNumber(Double.parseDouble(fraction1[0])), new RealNumber(Double.parseDouble(fraction1[1]))));
	        params.add(new RationalNumber(new RealNumber(Double.parseDouble(fraction2[0])), new RealNumber(Double.parseDouble(fraction2[1]))));
	        op = new Plus(params);
	    } catch (IllegalConstruction e) {
	        fail();
	    }
	}

	@Then("^its (.*) notation is (.*)$")
	public void thenItsNotationIs(String notation, String s) {
		if (notation.equals("PREFIX")||notation.equals("POSTFIX")||notation.equals("INFIX")) {
			op.notation = Notation.valueOf(notation);
			assertEquals(s, op.toString());
		}
		else fail(notation + " is not a correct notation! ");
	}

	@When("^I provide a (.*) number (\\d+)$")
	public void whenIProvideANumber(String s, int val) {
		//add extra parameter to the operation
		params = new ArrayList<>();
		params.add(new RealNumber(Double.parseDouble(String.valueOf(val))));
		op.addMoreParams(params);
	}

	@Then("^the (.*) is (\\d+)$")
	public void thenTheOperationIs(String s, int val) {
		try {
			switch (s) {
				case "sum"			->	op = new Plus(params);
				case "product"		->	op = new Times(params);
				case "quotient"		->	op = new Divides(params);
				case "difference"	->	op = new Minus(params);
				default -> fail();
			}
			assertEquals(val, c.eval(op));
		} catch (IllegalConstruction e) {
			fail();
		}
	}

	@Then("the operation evaluates to {int}")
	public void thenTheOperationEvaluatesTo(int val) {
	    assertEquals(new RealNumber((double) val), c.eval(op));
	}

	@Then("the operation evaluates to rational {string}")
	public void thenTheOperationEvaluatesToRational(String expected) {
	    RationalNumber result = (RationalNumber) c.eval(op);
	    assertEquals(expected, result.simplify().toString());
	}

	@Then("the operation evaluates to complex {string}")
	public void thenTheOperationEvaluatesToComplex(String expected) {
	    ComplexNumber result = (ComplexNumber) c.eval(op);
	    assertEquals(expected, result.toString());
	}

	@Then("the operation returns NaN")
	public void thenTheOperationReturnsInfinity() {
	    assertEquals(new RealNumber(Double.NaN), c.eval(op));
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

