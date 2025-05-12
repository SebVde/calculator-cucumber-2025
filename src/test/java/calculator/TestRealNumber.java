package calculator;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;

class TestRealNumber {

    private RealNumber real;
    private RealNumber real2;

    @BeforeEach
    void setUp() {
        real = new RealNumber(42.0);
        real2 = new RealNumber(42.5);
    }

    @Test
    void testEquals() {
        RealNumber other = new RealNumber(42.0);
        RealNumber other2 = new RealNumber(42.5);
        assertEquals(real, other);
        assertNotEquals(real, other2);
    }

    @Test
    void testNotEquals() {
        RealNumber other = new RealNumber(43.0);
        RealNumber other2 = new RealNumber(42.5);
        assertNotEquals(real, other);
        assertNotEquals(real, other2);
    }

    @Test
    void testToString() {
        assertEquals("42", real.toString());
        assertEquals("42.5", real2.toString());
    }

    @Test
    void testGetValue() {
        assertEquals(42.0, real.getValue());
        assertEquals(42.5, real2.getValue());
    }
}
