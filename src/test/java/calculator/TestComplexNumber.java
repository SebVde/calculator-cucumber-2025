package calculator;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;

class TestComplexNumber {

    private ComplexNumber complex;

    @BeforeEach
    void setUp() {
        complex = new ComplexNumber(
            new RationalNumber(new RealNumber(3.0), new RealNumber(4.0)),
            new RationalNumber(new RealNumber(5.0), new RealNumber(6.0))
        );
    }

    @Test
    void testEquals() {
        ComplexNumber other = new ComplexNumber(
            new RationalNumber(new RealNumber(3.0), new RealNumber(4.0)),
            new RationalNumber(new RealNumber(5.0), new RealNumber(6.0))
        );
        assertEquals(complex, other);
    }

    @Test
    void testNotEquals() {
        ComplexNumber other = new ComplexNumber(
            new RationalNumber(new RealNumber(1.0), new RealNumber(2.0)),
            new RationalNumber(new RealNumber(1.0), new RealNumber(3.0))
        );
        assertNotEquals(complex, other);
    }

    @Test
    void testToString() {
        assertEquals("3/4 + 5/6i", complex.toString());
    }

    @Test
    void testGetRealPart() {
        assertEquals(new RationalNumber(new RealNumber(3.0), new RealNumber(4.0)), complex.getRealPart());
    }

    @Test
    void testGetImaginaryPart() {
        assertEquals(new RationalNumber(new RealNumber(5.0), new RealNumber(6.0)), complex.getImaginaryPart());
    }
}
