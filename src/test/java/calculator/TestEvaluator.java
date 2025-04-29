package calculator;

//Import Junit5 libraries for unit testing:
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Arrays;
import java.util.List;

class TestEvaluator {

    private Calculator calc;
    private RealNumber value1, value2;

    @BeforeEach
    void setUp() {
        calc = new Calculator();
        value1 = new RealNumber(8.0);
        value2 = new RealNumber(6.0);
    }

    @Test
    void testEvaluatorMyNumber() {
        assertEquals(value1, calc.eval(new RealNumber(Double.parseDouble(String.valueOf(value1)))));
        assertEquals(value2, calc.eval(new RealNumber(Double.parseDouble(String.valueOf(value2)))));
    }

    @Test
    void testEvaluatorRationalNumber() {
        RationalNumber rational = new RationalNumber(new RealNumber(28.0), new RealNumber(24.0));
        assertEquals("7/6", calc.eval(rational.simplify()).toString());
    }
}