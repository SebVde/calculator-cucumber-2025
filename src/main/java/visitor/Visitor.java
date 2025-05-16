package visitor;

import calculator.*;

/**
 * Abstract base class for implementing the Visitor design pattern
 * in the calculator expression hierarchy.
 * <p>
 * This class defines a set of overloaded {@code visit} methods
 * that concrete visitor classes must implement in order to handle
 * various expression types such as {@link RealNumber}, {@link RationalNumber},
 * {@link ComplexNumber}, {@link Operation}, and {@link FunctionWrapper}.
 * </p>
 *
 * @see calculator.Expression
 * @see calculator.MyNumber
 * @see calculator.Operation
 * @see calculator.FunctionWrapper
 */
public abstract class Visitor {

    /**
     * Visit method for a generic number.
     *
     * @param n the number expression
     */
    public abstract void visit(MyNumber n);

    /**
     * Visit method for a {@link RealNumber}.
     *
     * @param n the real number expression
     */
    public abstract void visit(RealNumber n);

    /**
     * Visit method for a {@link RationalNumber}.
     *
     * @param n the rational number expression
     */
    public abstract void visit(RationalNumber n);

    /**
     * Visit method for a {@link ComplexNumber}.
     *
     * @param n the complex number expression
     */
    public abstract void visit(ComplexNumber n);

    /**
     * Visit method for an {@link Operation}.
     *
     * @param o the operation expression
     */
    public abstract void visit(Operation o);

    /**
     * Visit method for a {@link FunctionWrapper}, which encapsulates
     * mathematical functions like sqrt, etc.
     *
     * @param f the function wrapper expression
     */
    public abstract void visit(FunctionWrapper f);
}
