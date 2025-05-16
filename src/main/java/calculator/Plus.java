package calculator;

import java.util.List;

/**
 * This class represents the arithmetic addition operation "+".
 * It extends the abstract {@link Operation} class and implements the actual logic
 * for computing sums between various types of {@link MyNumber}, including:
 * <ul>
 *     <li>{@link RealNumber}</li>
 *     <li>{@link RationalNumber}</li>
 *     <li>{@link ComplexNumber}</li>
 * </ul>
 *
 * @see Operation
 * @see Minus
 * @see Times
 * @see Divides
 */
public final class Plus extends Operation {

    /**
     * Constructor to create a Plus operation with a list of expressions.
     *
     * @param elist The list of expressions to add
     * @throws IllegalConstruction If the list is null
     * @see #Plus(List, Notation)
     */
    public Plus(List<Expression> elist) throws IllegalConstruction {
        this(elist, null);
    }

    /**
     * Constructor to create a Plus operation with a list of expressions and a given notation.
     *
     * @param elist The list of expressions to add
     * @param n The {@link Notation} used for formatting the expression
     * @throws IllegalConstruction If the list is null
     */
    public Plus(List<Expression> elist, Notation n) throws IllegalConstruction {
        super(elist, n);
        symbol = "+";
        neutral = 0;
    }

    /**
     * Computes the sum of two integers.
     *
     * @param l The left integer
     * @param r The right integer
     * @return The result of l + r
     */
    @Override
    public int op(int l, int r) {
        return l + r;
    }

    /**
     * Computes the result of adding two {@link MyNumber} operands.
     * Handles combinations of real, rational, and complex numbers, including mixed types.
     *
     * @param left The left operand
     * @param right The right operand
     * @return The resulting {@link MyNumber} after addition
     * @throws IllegalConstruction If the operation cannot be completed due to unsupported types
     */
    @Override
    public MyNumber compute(MyNumber left, MyNumber right) throws IllegalConstruction {
        if (left instanceof RealNumber l && right instanceof RealNumber r) {
            return new RealNumber(l.getValue() + r.getValue());

        } else if (left instanceof RationalNumber l && right instanceof RationalNumber r) {
            // a/b + c/d = (a*d + c*b) / (b*d)
            Times times = new Times(List.of());
            Plus plus = new Plus(List.of());

            RealNumber numerator = (RealNumber) plus.compute(
                    times.compute(l.getNominator(), r.getDenominator()),
                    times.compute(r.getNominator(), l.getDenominator())
            );
            RealNumber denominator = (RealNumber) times.compute(l.getDenominator(), r.getDenominator());

            return new RationalNumber(numerator, denominator).simplify();

        } else if (left instanceof RealNumber l && right instanceof RationalNumber r) {
            return compute(new RationalNumber(l), r);

        } else if (left instanceof RationalNumber l && right instanceof RealNumber r) {
            return compute(l, new RationalNumber(r));

        } else if (left instanceof ComplexNumber l && right instanceof ComplexNumber r) {
            // Complex addition: (a + bi) + (c + di) = (a+c) + (b+d)i
            Plus plus = new Plus(List.of());
            return new ComplexNumber(
                    plus.compute(l.getRealPart(), r.getRealPart()),
                    plus.compute(l.getImaginaryPart(), r.getImaginaryPart())
            );

        } else if (left instanceof ComplexNumber complex && (right instanceof RealNumber || right instanceof RationalNumber)) {
            Plus plus = new Plus(List.of());
            return new ComplexNumber(
                    plus.compute(complex.getRealPart(), right),
                    complex.getImaginaryPart()
            );

        } else if ((left instanceof RealNumber || left instanceof RationalNumber) && right instanceof ComplexNumber complex) {
            Plus plus = new Plus(List.of());
            return new ComplexNumber(
                    plus.compute(complex.getRealPart(), left),
                    complex.getImaginaryPart()
            );

        } else {
            throw new IllegalArgumentException("Unsupported types for addition");
        }
    }
}
