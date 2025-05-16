package calculator;

import visitor.CountVisitor;
import visitor.Visitor;

/**
 * {@code MyNumber} is an abstract class representing a numeric value
 * (e.g., real, rational, or complex) within an arithmetic expression.
 * It is a concrete implementation of the {@link Expression} interface,
 * and serves as a base class for numeric types like {@link RealNumber}, {@link RationalNumber}, and {@link ComplexNumber}.
 *
 * <p>Unlike composite expressions (like operations), a number is a leaf node in the expression tree.
 *
 * @see Expression
 * @see Operation
 * @see RealNumber
 * @see RationalNumber
 * @see ComplexNumber
 */
public abstract class MyNumber implements Expression {

    /**
     * Accepts a {@link Visitor} according to the Visitor design pattern.
     * The visitor will process this numeric expression directly.
     *
     * @param v the visitor that processes this number
     */
    @Override
    public void accept(Visitor v) {
        v.visit(this);
    }

    /**
     * The depth of a single number expression is always 0
     * because it does not contain any sub-expressions.
     *
     * @return {@code 0}, representing the depth
     */
    @Override
    public int countDepth() {
        CountVisitor v = new CountVisitor();
        v.visit(this);
        return v.getDepthCount();
    }

    /**
     * A numeric value does not contain any operations.
     *
     * @return {@code 0}, as no operations are present
     */
    @Override
    public int countOps() {
        CountVisitor v = new CountVisitor();
        v.visit(this);
        return v.getOpsCount();
    }

    /**
     * A numeric value counts as exactly one operand in an expression.
     *
     * @return {@code 1}, representing a single number
     */
    @Override
    public int countNbs() {
        CountVisitor v = new CountVisitor();
        v.visit(this);
        return v.getNbCount();
    }
}
