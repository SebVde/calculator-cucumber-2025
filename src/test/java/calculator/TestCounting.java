package calculator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import visitor.CountVisitor;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;

class TestCounting {

    private int value1, value2;
    private Expression e;

    @BeforeEach
    void setUp() {
        value1 = 8;
        value2 = 6;
        e = null;
    }

    @Test
    void testNumberCounting() {
        e = new RealNumber(8.0);
        CountVisitor countVisitor = new CountVisitor();
        e.accept(countVisitor);
        assertEquals(0, countVisitor.getDepthCount());
        assertEquals(0, countVisitor.getOpsCount());
        assertEquals(1, countVisitor.getNbCount());
    }

    @Test
    void testRationalNumberCounting() {
        e = new RationalNumber(new RealNumber(3.0), new RealNumber(4.0));
        CountVisitor countVisitor = new CountVisitor();
        e.accept(countVisitor);
        assertEquals(0, countVisitor.getDepthCount());
        assertEquals(0, countVisitor.getOpsCount());
        assertEquals(1, countVisitor.getNbCount());
    }

    @Test
    void testComplexNumberCounting() {
        e = new ComplexNumber(new RationalNumber(new RealNumber(3.0), new RealNumber(4.0)),
                              new RationalNumber(new RealNumber(5.0), new RealNumber(6.0)));
        CountVisitor countVisitor = new CountVisitor();
        e.accept(countVisitor);
        assertEquals(0, countVisitor.getDepthCount());
        assertEquals(0, countVisitor.getOpsCount());
        assertEquals(1, countVisitor.getNbCount());
    }

    @ParameterizedTest
    @ValueSource(strings = {"*", "+", "/", "-"})
    void testOperationCounting(String symbol) {
        List<Expression> params = Arrays.asList(new RealNumber(8.0), new RealNumber(6.0));
        CountVisitor countVisitor = new CountVisitor();
        try {
            Operation op = switch (symbol) {
                case "+" -> new Plus(params);
                case "-" -> new Minus(params);
                case "*" -> new Times(params);
                case "/" -> new Divides(params);
                default -> throw new IllegalArgumentException("Invalid operation");
            };
            op.accept(countVisitor);
            assertEquals(1, countVisitor.getDepthCount(), "counting depth of an Operation");
            assertEquals(1, countVisitor.getOpsCount(), "counting operations in an Operation");
            assertEquals(2, countVisitor.getNbCount(), "counting numbers in an Operation");
        } catch (IllegalConstruction e) {
            fail();
        }
    }

    @Test
    void testNestedOperationCounting() {
        try {
            Expression innerOp = new Plus(Arrays.asList(new RealNumber(3.0), new RealNumber(4.0)));
            Expression outerOp = new Times(Arrays.asList(innerOp, new RealNumber(5.0)));
            CountVisitor countVisitor = new CountVisitor();
            outerOp.accept(countVisitor);
            assertEquals(2, countVisitor.getOpsCount(), "counting operations in nested operations");
            assertEquals(3, countVisitor.getNbCount(), "counting numbers in nested operations");
            assertEquals(2, countVisitor.getDepthCount(), "counting depth of nested operations");
        } catch (IllegalConstruction e) {
            fail();
        }
    }
}
