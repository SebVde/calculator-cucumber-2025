package calculator;

import static org.junit.jupiter.api.Assertions.*;

import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CalculatorSteps {

//	static final Logger log = getLogger(lookup().lookupClass());

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

    @Given("an operation {string}")
    public void givenAnOperation(String s) {
        // Write code here that turns the phrase above into concrete actions
        params = new ArrayList<>(); // create an empty set of parameters to be filled in
        try {
            switch (s) {
                case "+" -> op = new Plus(params);
                case "-" -> op = new Minus(params);
                case "*" -> op = new Times(params);
                case "/" -> op = new Divides(params);
                default -> fail();
            }
        } catch (IllegalConstruction e) {
            fail(e.getMessage());
        }
    }

    // The following example shows how to use a DataTable provided as input.
    // The example looks slightly complex, since DataTables can take as input
    //  tables in two dimensions, i.e. rows and lines. This is why the input
    //  is a list of lists.
    @Given("the following list of numbers")
    public void givenTheFollowingListOfNumbers(List<List<String>> numbers) {
        // Since we only use one line of input, we use getFirst to take the first line of the list,
        // which is a list of strings, that we will manually convert to integers:

        try {
            ArrayList<Expression> expressions = new ArrayList<>();
            for (String s : numbers.getFirst()) {
                MyNumber myNumber = MyNumber.parseNumber(s);
                expressions.add(myNumber);
            }
            params = expressions;
        } catch (IllegalConstruction e) {
            fail(e.getMessage());
        }

        params.forEach(n -> System.out.println("value = " + n));
        op = null;
    }

    // The string in the Given annotation shows how to use regular expressions...
    // In this example, the notation d+ is used to represent numbers, i.e. nonempty sequences of digits
//	@Given("^the sum of two numbers (\\d+) and (\\d+)$")
    // The alternative, and in this case simpler, notation would be:
    // @Given("the sum of two numbers {int} and {int}")
    @Given("the sum of two numbers {word} and {word}")
    public void givenTheSum(String n1, String n2) {
        try {
            MyNumber parsedN1 = MyNumber.parseNumber(n1);
            MyNumber parsedN2 = MyNumber.parseNumber(n2);
            params = new ArrayList<>();
            params.add(parsedN1);
            params.add(parsedN2);
            op = new Plus(params);
        } catch (IllegalConstruction e) {
            fail(e.getMessage());
        }
    }

    @Then("its (.*) notation is (.*)")
    public void thenItsNotationIs(String notation, String s) {
        if (notation.equals("PREFIX") || notation.equals("POSTFIX") || notation.equals("INFIX")) {
            op.notation = Notation.valueOf(notation);
            assertEquals(s, op.toString());
        } else fail(notation + " is not a correct notation! ");
    }

    @When("I provide a {word} number {word}")
    public void whenIProvideANumber(String s, String n) {
        //add extra parameter to the operation
        params = new ArrayList<>();
        try {
            params.add(MyNumber.parseNumber(n));
        } catch (IllegalConstruction e) {
            fail(e.getMessage());
        }
        op.addMoreParams(params);
    }

    // TODO only compares the integer part of the operation, add decimal
    @Then("the {word} is {word}")
    public void thenTheOperationIs(String s, String val) {
        try {
            switch (s) {
                case "sum" -> op = new Plus(params);
                case "product" -> op = new Times(params);
                case "quotient" -> op = new Divides(params);
                case "difference" -> op = new Minus(params);
                default -> fail();
            }
            assertEquals(MyNumber.parseNumber(val).getValue(), c.eval(op));
        } catch (IllegalConstruction e) {
            fail();
        }
    }

    @Then("the operation evaluates to {word}")
    public void thenTheOperationEvaluatesTo(String val) {
        try {
            assertEquals(MyNumber.parseNumber(val).getValue(), c.eval(op));
        } catch (IllegalConstruction e) {
            fail(e.getMessage());
        }
    }

    @Then("the operation returns infinity")
    public void thenTheOperationReturnsInfinity() {
        assertEquals(NumberValue.MAX, c.eval(op));
    }

    @Then("the undefined operation is equal to 0")
    public void thenTheOperationIsUndefined() {
        assertEquals(NumberValue.ZERO, c.eval(op));
    }

    @Then("its INFIX notation is {}")
    public void thenItsINFIXNotationIs(String s) {
        op.notation = Notation.INFIX;
        assertEquals(s, op.toString());
    }

    @Then("its PREFIX notation is {}")
    public void thenItsPREFIXNotationIs(String s) {
        op.notation = Notation.PREFIX;
        assertEquals(s, op.toString());
    }

    @Then("its POSTFIX notation is {}")
    public void thenItsPOSTFIXNotationIs(String s) {
        op.notation = Notation.POSTFIX;
        assertEquals(s, op.toString());
    }
}
