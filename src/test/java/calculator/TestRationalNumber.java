package calculator;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;

class TestRationalNumber {

    private RationalNumber rational;

    @BeforeEach
    void setUp() {
        rational = new RationalNumber(new RealNumber(28.0), new RealNumber(24.0));
    }

    @Test
    void testSimplify() {
        assertEquals("7/6", rational.simplify().toString());
    }

    @Test
    void testEquals() {
        RationalNumber other = new RationalNumber(new RealNumber(28.0), new RealNumber(24.0));
        assertEquals(rational, other);
    }

    @Test
    void testNotEquals() {
        RationalNumber other = new RationalNumber(new RealNumber(29.0), new RealNumber(24.0));
        assertNotEquals(rational, other);
    }

    @Test
    void testToString() {
        assertEquals("28/24", rational.toString());
    }

    @Test
    void testGetNominator() {
        assertEquals(new RealNumber(28.0), rational.getNominator());
    }

    @Test
    void testGetDenominator() {
        assertEquals(new RealNumber(24.0), rational.getDenominator());
    }
}
