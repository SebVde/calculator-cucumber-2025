package calculator;

import java.util.List;

/**
 * This class represents the arithmetic multiplication operation "*".
 * It extends the abstract class {@link Operation} and implements multiplication logic
 * for combinations of numeric types: {@link RealNumber}, {@link RationalNumber}, and {@link ComplexNumber}.
 *
 * @see Operation
 * @see Plus
 * @see Minus
 * @see Divides
 */
public final class Times extends Operation {

    /**
     * Constructor for a multiplication operation with a list of expressions.
     *
     * @param elist the list of expressions to multiply
     * @throws IllegalConstruction if the list is null
     */
    public Times(List<Expression> elist) throws IllegalConstruction {
        this(elist, null);
    }

    /**
     * Constructor for a multiplication operation with a list of expressions and a specific notation.
     *
     * @param elist the list of expressions to multiply
     * @param n     the {@link Notation} used to format the operation
     * @throws IllegalConstruction if the list is null
     */
    public Times(List<Expression> elist, Notation n) throws IllegalConstruction {
        super(elist, n);
        symbol = "*";
        neutral = 1;
    }

    /**
     * Performs basic integer multiplication.
     *
     * @param l the left operand
     * @param r the right operand
     * @return the product of l and r
     */
    @Override
    public int op(int l, int r) {
        return l * r;
    }

    /**
     * Computes the result of multiplying two {@link MyNumber} instances.
     * Supports all combinations of {@link RealNumber}, {@link RationalNumber}, and {@link ComplexNumber}.
     *
     * @param left  the left operand
     * @param right the right operand
     * @return the product of both operands as a {@link MyNumber}
     * @throws IllegalConstruction if the combination of types is unsupported
     */
    @Override
    public MyNumber compute(MyNumber left, MyNumber right) throws IllegalConstruction {
        if (left instanceof RealNumber l && right instanceof RealNumber r) {
            return new RealNumber(l.getValue() * r.getValue());

        } else if (left instanceof RationalNumber l && right instanceof RationalNumber r) {
            // Multiply numerators and denominators: (a/b) * (c/d) = (a*c)/(b*d)
            Times times = new Times(List.of());
            RealNumber numerator = (RealNumber) times.compute(l.getNominator(), r.getNominator());
            RealNumber denominator = (RealNumber) times.compute(l.getDenominator(), r.getDenominator());
            return new RationalNumber(numerator, denominator);

        } else if (left instanceof RealNumber l && right instanceof RationalNumber r) {
            return compute(new RationalNumber(l), r);

        } else if (left instanceof RationalNumber l && right instanceof RealNumber r) {
            return compute(l, new RationalNumber(r));

        } else if (left instanceof ComplexNumber l && right instanceof ComplexNumber r) {
            // (a + bi)(c + di) = (ac - bd) + (ad + bc)i
            Minus minus = new Minus(List.of());
            Plus plus = new Plus(List.of());
            Times times = new Times(List.of());

            MyNumber realPart = minus.compute(
                    times.compute(l.getRealPart(), r.getRealPart()),
                    times.compute(l.getImaginaryPart(), r.getImaginaryPart())
            );

            MyNumber imaginaryPart = plus.compute(
                    times.compute(l.getRealPart(), r.getImaginaryPart()),
                    times.compute(l.getImaginaryPart(), r.getRealPart())
            );

            return new ComplexNumber(realPart, imaginaryPart);

        } else if (left instanceof ComplexNumber complex && (right instanceof RealNumber || right instanceof RationalNumber)) {
            Times times = new Times(List.of());
            MyNumber realPart = times.compute(complex.getRealPart(), right);
            MyNumber imaginaryPart = times.compute(complex.getImaginaryPart(), right);
            return new ComplexNumber(realPart, imaginaryPart);

        } else if ((left instanceof RealNumber || left instanceof RationalNumber) && right instanceof ComplexNumber complex) {
            Times times = new Times(List.of());
            MyNumber realPart = times.compute(complex.getRealPart(), left);
            MyNumber imaginaryPart = times.compute(complex.getImaginaryPart(), left);
            return new ComplexNumber(realPart, imaginaryPart);

        } else {
            throw new IllegalArgumentException("Unsupported types for multiplication");
        }
    }
}
