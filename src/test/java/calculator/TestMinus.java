package calculator;

//Import Junit5 libraries for unit testing:
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class TestMinus {

	private final int value1 = 8;
	private final int value2 = 6;
	private Minus op;
	private List<Expression> params;

	@BeforeEach
	void setUp() {
		  params = Arrays.asList(new RealNumber(Double.parseDouble(String.valueOf(value1))),
				  new RealNumber(Double.parseDouble(String.valueOf(value2))));
		  try { op = new Minus(params); }
		  catch(IllegalConstruction _) { fail(); }
	}

	@Test
	void testConstructor1() {
		// It should not be possible to create an expression without null parameter list
		assertThrows(IllegalConstruction.class, () -> op = new Minus(null));
	}

	@SuppressWarnings("AssertBetweenInconvertibleTypes")
	@Test
	void testConstructor2() {
		// A Times expression should not be the same as a Minus expression
		try {
			assertNotSame(new Times(new ArrayList<>()), op);
		} catch (IllegalConstruction _) {
			fail();
		}
	}

	@Test
	void testEquals() {
		// Two similar expressions, constructed separately (and using different constructors) should not be equal
		List<Expression> p = Arrays.asList(new RealNumber(Double.parseDouble(String.valueOf(value1))),
				new RealNumber(Double.parseDouble(String.valueOf(value2))));
		try {
			Minus e = new Minus(p, Notation.INFIX);
			assertEquals(op, e);
		}
		catch(IllegalConstruction _) { fail(); }
	}

	@Test
	void testNull() {
		assertDoesNotThrow(() -> op==null); // Direct way to to test if the null case is handled.
	}

	@Test
	void testHashCode() {
		// Two similar expressions, constructed separately (and using different constructors) should have the same hashcode
		List<Expression> p = Arrays.asList(new RealNumber(Double.parseDouble(String.valueOf(value1))),
				new RealNumber(Double.parseDouble(String.valueOf(value2))));
		try {
			Minus e = new Minus(p, Notation.INFIX);
			assertEquals(e.hashCode(), op.hashCode());
		}
		catch(IllegalConstruction _) { fail(); }
	}

	@Test
	void testNullParamList() {
		params = null;
		assertThrows(IllegalConstruction.class, () -> op = new Minus(params));
	}

	@Test
	void testRationalSubtraction() {
	    RationalNumber r1 = new RationalNumber(new RealNumber(7.0), new RealNumber(6.0));
	    RationalNumber r2 = new RationalNumber(new RealNumber(5.0), new RealNumber(6.0));
	    try {
	        Minus minus = new Minus(List.of(r1, r2));
	        Calculator calc = new Calculator();
	        RationalNumber result = (RationalNumber) calc.eval(minus);
	        assertEquals("1/3", result.simplify().toString());
	    } catch (IllegalConstruction _) {
	        fail();
	    }
	}

	@Test
	void testComplexSubtraction() {
	    ComplexNumber c1 = new ComplexNumber(new RationalNumber(new RealNumber(3.0), new RealNumber(4.0)),
	                                         new RationalNumber(new RealNumber(5.0), new RealNumber(6.0)));
	    ComplexNumber c2 = new ComplexNumber(new RationalNumber(new RealNumber(1.0), new RealNumber(2.0)),
	                                         new RationalNumber(new RealNumber(1.0), new RealNumber(3.0)));
	    try {
	        Minus minus = new Minus(List.of(c1, c2));
	        Calculator calc = new Calculator();
	        ComplexNumber result = (ComplexNumber) calc.eval(minus);
	        assertEquals("1/4 + 1/2i", result.simplify().toString());
	    } catch (IllegalConstruction _) {
	        fail();
	    }
	}

	@Test
	void testSubtractionWithZero() {
	    RationalNumber r1 = new RationalNumber(new RealNumber(3.0), new RealNumber(4.0));
	    RationalNumber r2 = new RationalNumber(new RealNumber(0.0), new RealNumber(1.0));
	    try {
	        Minus minus = new Minus(List.of(r1, r2));
	        Calculator calc = new Calculator();
	        RationalNumber result = (RationalNumber) calc.eval(minus);
	        assertEquals("3/4", result.simplify().toString());
	    } catch (IllegalConstruction _) {
	        fail();
	    }
	}

}
