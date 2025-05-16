package calculator;

import org.junit.jupiter.api.Test;
import ui.Parser;
import visitor.Evaluator;

import static org.junit.jupiter.api.Assertions.*;

class ParserTest {

    private void assertEvaluation(String input, Expression expected, boolean preserveFractions) throws IllegalConstruction {
        Expression e = Parser.parse(input, preserveFractions);
        Evaluator eval = new Evaluator(preserveFractions);
        e.accept(eval);
        assertEquals(expected, eval.getResult());
    }

    @Test
    void testSimpleAddition() throws IllegalConstruction {
        assertEvaluation("2+3", new RealNumber(5.0), false);
    }

    @Test
    void testFractionPreserved() throws IllegalConstruction {
        assertEvaluation("3/4+1/4", new RationalNumber(new RealNumber(1.0)), true);
    }

    @Test
    void testFractionReduced() throws IllegalConstruction {
        assertEvaluation("2/4+2/4", new RationalNumber(new RealNumber(1.0)), true);
    }

    @Test
    void testFractionConvertedToDecimal() throws IllegalConstruction {
        assertEvaluation("1/2+1/2", new RealNumber(1.0), false);
    }

    @Test
    void testComplexNumber() throws IllegalConstruction {
        assertEvaluation("2+3i", new ComplexNumber(
                new RationalNumber(new RealNumber(2.0)),
                new RationalNumber(new RealNumber(3.0))
        ), true);
    }

    @Test
    void testImaginaryOnly() throws IllegalConstruction {
        assertEvaluation("-i", new ComplexNumber(
                new RationalNumber(new RealNumber(0.0)),
                new RationalNumber(new RealNumber(-1.0))
        ), true);
    }

    @Test
    void testRealOnlyComplex() throws IllegalConstruction {
        assertEvaluation("5", new RealNumber(5.0), false);
    }

    @Test
    void testSqrtInteger() throws IllegalConstruction {
        assertEvaluation("sqrt(9)", new RealNumber(3.0), false);
    }

    @Test
    void testSqrtFraction() throws IllegalConstruction {
        assertEvaluation("sqrt(1/4)", new RealNumber(0.5), false);
    }

    @Test
    void testSqrtInFractionMode() throws IllegalConstruction {
        assertEvaluation("sqrt(4/9)", new RealNumber(2.0/3.0), true);
    }

    @Test
    void testNestedSqrtAndAddition() throws IllegalConstruction {
        assertEvaluation("sqrt(4)+1", new RealNumber(3.0), false);
    }

    @Test
    void testExpressionWithPi() throws IllegalConstruction {
        assertEvaluation("π", new RealNumber(Math.PI), false);
    }

    @Test
    void testNestedParentheses() throws IllegalConstruction {
        assertEvaluation("(1+(2+3))", new RealNumber(6.0), false);
    }

    @Test
    void testPowerAsDuplication() throws IllegalConstruction {
        assertEvaluation("2*2", new RealNumber(4.0), false);
    }

    @Test
    void testAdditionAndMultiplicationPrecedence() throws IllegalConstruction {
        assertEvaluation("1+2*3", new RealNumber(7.0), false);
    }

    @Test
    void testParenthesesPrecedence() throws IllegalConstruction {
        assertEvaluation("(1+2)*3", new RealNumber(9.0), false);
    }

    @Test
    void testMixedFractionAndReal() throws IllegalConstruction {
        assertEvaluation("1/2 + 1.5", new RealNumber(2.0), false);
    }

    @Test
    void testMixedFractionAndRealPreserve() throws IllegalConstruction {
        assertEvaluation("1/2 + 1.5", new RationalNumber(new RealNumber(2.0)), true);
    }

    @Test
    void testFractionDivisionSimplified() throws IllegalConstruction {
        assertEvaluation("10/4", new RationalNumber(new RealNumber(5.0), new RealNumber(2.0)), true);
    }

    @Test
    void testExpressionEvaluatedAsFraction() throws IllegalConstruction {
        assertEvaluation("(10-5)/2", new RationalNumber(new RealNumber(5.0), new RealNumber(2.0)), true);
    }

    @Test
    void testNegativeFraction() throws IllegalConstruction {
        assertEvaluation("-1/2", new RationalNumber(new RealNumber(-1.0), new RealNumber(2.0)), true);
    }

    @Test
    void testAdditionOfComplexAndReal() throws IllegalConstruction {
        assertEvaluation("1+2i+3", new ComplexNumber(
                new RationalNumber(new RealNumber(4.0)),
                new RationalNumber(new RealNumber(2.0))
        ), true);
    }

    @Test
    void testMultiplicationOfRationals() throws IllegalConstruction {
        assertEvaluation("2/3*3/4", new RationalNumber(new RealNumber(1.0), new RealNumber(2.0)), true);
    }

    @Test
    void testSubtractionResultingInNegativeFraction() throws IllegalConstruction {
        assertEvaluation("1/4 - 1/2", new RationalNumber(new RealNumber(-1.0), new RealNumber(4.0)), true);
    }

    @Test
    void testDivisionOfRealByFraction() throws IllegalConstruction {
        assertEvaluation("2/(1/2)", new RealNumber(4.0), false);
    }

    @Test
    void testDivisionOfRealByFractionPreserved() throws IllegalConstruction {
        assertEvaluation("2/(1/2)", new RationalNumber(new RealNumber(4.0)), true);
    }

    @Test
    void testDivisionOfFractionByRealPreserved() throws IllegalConstruction {
        assertEvaluation("(1/2)/2", new RationalNumber(new RealNumber(1.0), new RealNumber(4.0)), true);
    }

    @Test
    void testComplexWithFractionImaginary() throws IllegalConstruction {
        assertEvaluation("3+1/2i", new ComplexNumber(
                new RationalNumber(new RealNumber(3.0)),
                new RationalNumber(new RealNumber(1.0), new RealNumber(2.0))
        ), true);
    }

    @Test
    void testExpressionWithMultipleOperators() throws IllegalConstruction {
        assertEvaluation("1+2*3-4/2", new RealNumber(5.0), false);
    }

    @Test
    void testNestedParenthesesFraction() throws IllegalConstruction {
        assertEvaluation("((1/2)+(1/4))/2", new RationalNumber(new RealNumber(3.0), new RealNumber(8.0)), true);
    }

    @Test
    void testMultipleFractionSimplification() throws IllegalConstruction {
        assertEvaluation("6/9 + 2/3", new RationalNumber(new RealNumber(4.0), new RealNumber(3.0)), true);
    }

    @Test
    void testExpressionStartingWithMinus() throws IllegalConstruction {
        assertEvaluation("-2+4", new RealNumber(2.0), false);
    }

    @Test
    void testExpressionWithPiAndFractions() throws IllegalConstruction {
        assertEvaluation("π/2", new RealNumber(Math.PI / 2), false);
    }

    @Test
    void testComplexMultiplication() throws IllegalConstruction {
        assertEvaluation("i*i", new ComplexNumber(
                new RationalNumber(new RealNumber(-1.0)),
                new RationalNumber(new RealNumber(0.0))
        ), true);
    }

}
