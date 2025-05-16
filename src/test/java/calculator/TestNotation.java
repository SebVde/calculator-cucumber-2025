package calculator;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Arrays;
import java.util.List;


class TestNotation {

    /* This is an auxilary method to avoid code duplication.
     */
	void testNotation(String s,Operation o,Notation n) {
		o.notation = n;
		assertEquals(s, o.toString());
	}

	/* This is an auxilary method to avoid code duplication.
     */
	void testNotations(String symbol, MyNumber value1, MyNumber value2, Operation op) {		//prefix notation:
		testNotation(symbol +" (" + value1 + ", " + value2 + ")", op, Notation.PREFIX);
		//infix notation:
		testNotation("( " + value1 + " " + symbol + " " + value2 + " )", op, Notation.INFIX);
		//postfix notation:
		testNotation("(" + value1 + ", " + value2 + ") " + symbol, op, Notation.POSTFIX);
	}

	@ParameterizedTest
	@ValueSource(strings = {"*", "+", "/", "-"})
	void testOutput(String symbol) {
		MyNumber value1 = new RealNumber(8.0);
		MyNumber value2 = new RealNumber(6.0);
		Operation op = null;
		List<Expression> params = Arrays.asList(new RealNumber(Double.parseDouble(String.valueOf(value1))),
				new RealNumber(Double.parseDouble(String.valueOf(value2))));
		try {
			//construct another type of operation depending on the input value
			//of the parameterised test

			switch (symbol) {
				case "+"	->	op = new Plus(params);
				case "-"	->	op = new Minus(params);
				case "*"	->	op = new Times(params);
				case "/"	->	op = new Divides(params);
				default		->	fail();
			}

		} catch (IllegalConstruction _) {
			fail();
		}
		testNotations(symbol, value1, value2, op);
	}

	@ParameterizedTest
	@ValueSource(strings = {"*", "+", "/", "-"})
	void testRationalNotation(String symbol) {
		RationalNumber r1 = new RationalNumber(new RealNumber(3.0), new RealNumber(4.0));
		RationalNumber r2 = new RationalNumber(new RealNumber(5.0), new RealNumber(6.0));
		List<Expression> params = List.of(r1, r2);
		Operation op = null;
		try {
			switch (symbol) {
				case "+" -> op = new Plus(params);
				case "-" -> op = new Minus(params);
				case "*" -> op = new Times(params);
				case "/" -> op = new Divides(params);
				default -> fail();
			}
		} catch (IllegalConstruction _) {
			fail();
		}
		testNotations(symbol, r1, r2, op);
	}

	@ParameterizedTest
	@ValueSource(strings = {"*", "+", "/", "-"})
	void testComplexNotation(String symbol) {
		ComplexNumber c1 = new ComplexNumber(new RationalNumber(new RealNumber(3.0), new RealNumber(4.0)),
											 new RationalNumber(new RealNumber(5.0), new RealNumber(6.0)));
		ComplexNumber c2 = new ComplexNumber(new RationalNumber(new RealNumber(1.0), new RealNumber(2.0)),
											 new RationalNumber(new RealNumber(1.0), new RealNumber(3.0)));
		List<Expression> params = List.of(c1, c2);
		Operation op = null;
		try {
			switch (symbol) {
				case "+" -> op = new Plus(params);
				case "-" -> op = new Minus(params);
				case "*" -> op = new Times(params);
				case "/" -> op = new Divides(params);
				default -> fail();
			}
		} catch (IllegalConstruction _) {
			fail();
		}
		testNotations(symbol, c1, c2, op);
	}

}
