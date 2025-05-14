package calculator;

import org.junit.jupiter.api.Test;
import ui.FxExpressionParser;
import visitor.Evaluator;

import static org.junit.jupiter.api.Assertions.*;

class FxExpressionParserTest {

    @Test
    void testRealNumberParsing() throws IllegalConstruction {
        Expression e = FxExpressionParser.parse("3.5");
        assertEquals(new RationalNumber(new RealNumber(3.5)), e);
    }

    @Test
    void testRationalNumberParsing() throws IllegalConstruction {
        Expression e = FxExpressionParser.parse("3/4");
        assertEquals(new RationalNumber(new RealNumber(3.0), new RealNumber(4.0)), e);
    }

    @Test
    void testComplexNumberParsing() throws IllegalConstruction {
        Expression e = FxExpressionParser.parse("2+3i");
        Evaluator eval = new Evaluator(false);
        e.accept(eval);
        Expression result = eval.getResult();
        assertEquals("2 + 3i", result.toString());
    }

    @Test
    void testFunctionParsing() throws IllegalConstruction {
        Expression e = FxExpressionParser.parse("sqrt(9)");
        FunctionWrapper expected = new FunctionWrapper("sqrt", new RationalNumber(new RealNumber(9.0)));
        assertEquals(expected, e);
    }

    @Test
    void testAddition() throws IllegalConstruction {
        Expression e = FxExpressionParser.parse("2+3");
        Expression expected = new Plus(
                java.util.List.of(
                        new RationalNumber(new RealNumber(2.0)),
                        new RationalNumber(new RealNumber(3.0))
                )
        );
        assertEquals(expected, e);
    }

    @Test
    void testNestedFunctions() throws IllegalConstruction {
        Expression e = FxExpressionParser.parse("sin(cos(0))");
        FunctionWrapper inner = new FunctionWrapper("cos", new RationalNumber(new RealNumber(0.0)));
        FunctionWrapper expected = new FunctionWrapper("sin", inner);
        assertEquals(expected, e);
    }

    @Test
    void testPiSymbol() throws IllegalConstruction {
        Expression e = FxExpressionParser.parse("π");
        assertEquals(new RationalNumber(new RealNumber(Math.PI)), e);
    }

    @Test
    void testNegativeImaginaryUnit() throws IllegalConstruction {
        Expression e = FxExpressionParser.parse("-i");
        ComplexNumber expected = new ComplexNumber(
                new RationalNumber(new RealNumber(0.0)),
                new RationalNumber(new RealNumber(-1.0))
        );
        assertEquals(expected, e);
    }

    @Test
    void testRadModeEvaluation() throws IllegalConstruction {
        Expression e = FxExpressionParser.parse("sin(1)");
        Expression e2 = FxExpressionParser.parse("cos(1)");
        Expression e3 = FxExpressionParser.parse("tan(1)");
        Evaluator eval = new Evaluator(false); // Radians
        e.accept(eval);
        assertEquals(Math.sin(1.0), ((RealNumber) eval.getResult()).getValue(), 1e-9);
        e2.accept(eval);
        assertEquals(Math.cos(1.0), ((RealNumber) eval.getResult()).getValue(), 1e-9);
        e3.accept(eval);
        assertEquals(Math.tan(1.0), ((RealNumber) eval.getResult()).getValue(), 1e-9);
    }

    @Test
    void testDegModeEvaluation() throws IllegalConstruction {
        Expression e = FxExpressionParser.parse("sin(90)");
        Expression e2 = FxExpressionParser.parse("cos(0)");
        Expression e3 = FxExpressionParser.parse("tan(45)");
        Evaluator eval = new Evaluator(true); // Degrees
        e.accept(eval);
        assertEquals(1.0, ((RealNumber) eval.getResult()).getValue(), 1e-9);
        e2.accept(eval);
        assertEquals(1.0, ((RealNumber) eval.getResult()).getValue(), 1e-9);
        e3.accept(eval);
        assertEquals(1.0, ((RealNumber) eval.getResult()).getValue(), 1e-9);
    }

    @Test
    void testInvalidExpressionThrows() {
        assertThrows(IllegalArgumentException.class, () -> FxExpressionParser.parse("+3"));
        assertThrows(IllegalArgumentException.class, () -> FxExpressionParser.parse("3++4"));
        assertThrows(IllegalArgumentException.class, () -> FxExpressionParser.parse("(3+4"));
    }
}