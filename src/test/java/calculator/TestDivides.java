package calculator;

//Import Junit5 libraries for unit testing:
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class TestDivides {

	private final int value1 = 8;
	private final int value2 = 6;
	private Divides op;
	private List<Expression> params;

	@BeforeEach
	void setUp() {
		  params = Arrays.asList(new RealNumber(Double.parseDouble(String.valueOf(value1))),
				  new RealNumber(Double.parseDouble(String.valueOf(value2))));
		  try {
		  	op = new Divides(params);
			op.notation = Notation.INFIX; // reset the notation to infix (which is the default) before each test
		  }
		  catch(IllegalConstruction _) { fail(); }
	}

	@Test
	void testConstructor1() {
		// It should not be possible to create an expression without null parameter list
		assertThrows(IllegalConstruction.class, () -> op = new Divides(null));
	}

	@SuppressWarnings("AssertBetweenInconvertibleTypes")
	@Test
	void testConstructor2() {
		// A Times expression should not be the same as a Divides expression
		try {
			assertNotSame(new Times(new ArrayList<>()), op);
		} catch (IllegalConstruction _) {
			fail();
		}
	}

	@Test
	void testEquals() {
		// Two similar expressions, constructed separately (and using different constructors) should be equal
		List<Expression> p = Arrays.asList(new RealNumber(Double.parseDouble(String.valueOf(value1))),
				new RealNumber(Double.parseDouble(String.valueOf(value2))));
		try {
			Divides d = new Divides(p, Notation.INFIX);
			assertEquals(op, d);
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
			Divides e = new Divides(p, Notation.INFIX);
			assertEquals(e.hashCode(), op.hashCode());
		}
		catch(IllegalConstruction _) { fail(); }
	}

	@Test
	void testNullParamList() {
		params = null;
		assertThrows(IllegalConstruction.class, () -> op = new Divides(params));
	}

	@Test
	void testRationalDivision() {
	    RationalNumber r1 = new RationalNumber(new RealNumber(28.0), new RealNumber(24.0));
	    RationalNumber r2 = new RationalNumber(new RealNumber(9.0), new RealNumber(12.0));
	    try {
	        Divides divides = new Divides(List.of(r1, r2));
	        Calculator calc = new Calculator();
	        RationalNumber result = (RationalNumber) calc.eval(divides);
	        assertEquals("14/9", result.simplify().toString());
	    } catch (IllegalConstruction _) {
	        fail();
	    }
	}

	@Test
	void testComplexDivision() {
	    ComplexNumber c1 = new ComplexNumber(new RationalNumber(new RealNumber(3.0), new RealNumber(4.0)),
	                                         new RationalNumber(new RealNumber(5.0), new RealNumber(6.0)));
	    ComplexNumber c2 = new ComplexNumber(new RationalNumber(new RealNumber(1.0), new RealNumber(2.0)),
	                                         new RationalNumber(new RealNumber(1.0), new RealNumber(3.0)));
	    try {
	        Divides divides = new Divides(List.of(c1, c2));
	        Calculator calc = new Calculator();
	        ComplexNumber result = (ComplexNumber) calc.eval(divides);
	        assertEquals("47/26 + 6/13i", result.simplify().toString());
	    } catch (IllegalConstruction _) {
	        fail();
	    }
	}

	@Test
	void testDivisionByZero() {
	    RationalNumber r1 = new RationalNumber(new RealNumber(1.0), new RealNumber(2.0));
	    RationalNumber r2 = new RationalNumber(new RealNumber(0.0), new RealNumber(1.0));
			    try {
	        Divides divides = new Divides(List.of(r1, r2));
	        Calculator calc = new Calculator();
	        assertEquals(new RealNumber(Double.NaN), calc.eval(divides));
	    } catch (IllegalConstruction _) {
	        fail();
	    }
	}

}

