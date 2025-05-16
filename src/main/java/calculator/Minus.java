package calculator;

import java.util.List;

/**
 * This class represents the arithmetic subtraction operation "-".
 * It extends the {@link Operation} class and supports subtraction for real,
 * rational, and complex numbers.
 *
 * @see Operation
 * @see Plus
 * @see Times
 * @see Divides
 */
public final class Minus extends Operation {

    /**
     * Constructs a Minus operation with a list of expressions to subtract.
     * The notation is set to default (INFIX) unless specified otherwise.
     *
     * @param elist the list of expressions to subtract
     * @throws IllegalConstruction if the list is null
     */
    public Minus(List<Expression> elist) throws IllegalConstruction {
        this(elist, null);
    }

    /**
     * Constructs a Minus operation with a list of expressions and a specific notation.
     *
     * @param elist the list of expressions to subtract
     * @param n     the notation to use (INFIX, PREFIX, or POSTFIX)
     * @throws IllegalConstruction if the list is null
     */
    public Minus(List<Expression> elist, Notation n) throws IllegalConstruction {
        super(elist, n);
        symbol = "-";
        neutral = 0;
    }

    /**
     * Performs basic subtraction between two integers.
     *
     * @param l the left operand (minuend)
     * @param r the right operand (subtrahend)
     * @return the result of l - r
     */
    @Override
    public int op(int l, int r) {
        return l - r;
    }

    /**
     * Performs subtraction between two {@link MyNumber} instances.
     * Supports mixed types (Real, Rational, Complex).
     *
     * @param left  the left operand
     * @param right the right operand
     * @return a new {@link MyNumber} as result of the subtraction
     * @throws IllegalConstruction if the operation is invalid or unsupported
     */
    @Override
    public MyNumber compute(MyNumber left, MyNumber right) throws IllegalConstruction {
        if (left instanceof RealNumber l && right instanceof RealNumber r) {
            return new RealNumber(l.getValue() - r.getValue());
        }

        if (left instanceof RationalNumber l && right instanceof RationalNumber r) {
            Times times = new Times(List.of());
            Minus minus = new Minus(List.of());

            RealNumber numerator = (RealNumber) minus.compute(
                    times.compute(l.getNominator(), r.getDenominator()),
                    times.compute(r.getNominator(), l.getDenominator())
            );
            RealNumber denominator = (RealNumber) times.compute(l.getDenominator(), r.getDenominator());
            return new RationalNumber(numerator, denominator);
        }

        if (left instanceof RealNumber l && right instanceof RationalNumber r) {
            return compute(new RationalNumber(l), r);
        }

        if (left instanceof RationalNumber l && right instanceof RealNumber r) {
            return compute(l, new RationalNumber(r));
        }

        if (left instanceof ComplexNumber l && right instanceof ComplexNumber r) {
            Minus minus = new Minus(List.of());
            return new ComplexNumber(
                    minus.compute(l.getRealPart(), r.getRealPart()),
                    minus.compute(l.getImaginaryPart(), r.getImaginaryPart())
            );
        }

        if (left instanceof ComplexNumber complex && (right instanceof RealNumber || right instanceof RationalNumber)) {
            Minus minus = new Minus(List.of());
            return new ComplexNumber(
                    minus.compute(complex.getRealPart(), right),
                    complex.getImaginaryPart()
            );
        }

        if ((left instanceof RealNumber || left instanceof RationalNumber) && right instanceof ComplexNumber complex) {
            Minus minus = new Minus(List.of());
            return new ComplexNumber(
                    minus.compute(left, complex.getRealPart()),
                    complex.getImaginaryPart().getOpposite()
            );
        }

        throw new IllegalArgumentException("Unsupported types for subtraction");
    }

    /**
     * Compares this Minus operation with another object.
     *
     * @param o the object to compare
     * @return true if the objects are both Minus and equal in structure
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Minus)) return false;
        return super.equals(o); // Delegate structural comparison to superclass
    }

    /**
     * Computes the hash code for this Minus operation.
     *
     * @return hash code consistent with equals()
     */
    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
