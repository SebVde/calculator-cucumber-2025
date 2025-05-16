package calculator;

import java.util.List;

/**
 * This class represents the arithmetic division operation ("/").
 * It supports division for real numbers, rational numbers, and complex numbers.
 * It extends the abstract class {@link Operation}, which defines the behavior of arithmetic expressions.
 *
 * @see Operation
 * @see Minus
 * @see Times
 * @see Plus
 */
public final class Divides extends Operation {

    /**
     * Constructor for division with a list of expressions.
     *
     * @param elist the list of expressions to divide
     * @throws IllegalConstruction if the list is null
     * @see #Divides(List, Notation)
     */
    public Divides(List<Expression> elist) throws IllegalConstruction {
        this(elist, null);
    }

    /**
     * Constructor for division with a list of expressions and a specific notation.
     *
     * @param elist the list of expressions to divide
     * @param n     the notation (INFIX, PREFIX, or POSTFIX)
     * @throws IllegalConstruction if the list is null
     */
    public Divides(List<Expression> elist, Notation n) throws IllegalConstruction {
        super(elist, n);
        symbol = "/";
        neutral = 1;
    }

    /**
     * Performs integer division between two integers.
     * Handles division by zero by returning a safe fallback value.
     *
     * @param l dividend
     * @param r divisor
     * @return result of the integer division
     */
    @Override
    public int op(int l, int r) {
        if (r == 0 && l != 0) {
            return Integer.MAX_VALUE; // undefined, fallback
        } else if (r == 0) {
            return 0; // 0 / 0 fallback
        } else {
            return l / r;
        }
    }

    /**
     * Performs division between two {@link MyNumber} instances.
     * Supports RealNumber, RationalNumber, and ComplexNumber combinations.
     *
     * @param left  the left operand
     * @param right the right operand
     * @return the result of the division
     * @throws IllegalConstruction if the operation is not properly constructed
     */
    @Override
    public MyNumber compute(MyNumber left, MyNumber right) throws IllegalConstruction {
        if (left instanceof RealNumber l && right instanceof RealNumber r) {
            if (r.getValue() == 0) return RealNumber.NaN;
            return new RealNumber(l.getValue() / r.getValue());
        }

        if (left instanceof RationalNumber l && right instanceof RationalNumber r) {
            if (r.getNominator().getValue() == 0) return RealNumber.NaN;

            Times times = new Times(List.of());
            RealNumber numerator = (RealNumber) times.compute(l.getNominator(), r.getDenominator());
            RealNumber denominator = (RealNumber) times.compute(l.getDenominator(), r.getNominator());
            return new RationalNumber(numerator, denominator);
        }

        if (left instanceof RealNumber l && right instanceof RationalNumber r) {
            return compute(new RationalNumber(l), r);
        }

        if (left instanceof RationalNumber l && right instanceof RealNumber r) {
            return compute(l, new RationalNumber(r));
        }

        if (left instanceof ComplexNumber l && right instanceof ComplexNumber r) {
            // (a + bi) / (c + di)
            RationalNumber a = l.getRealPart();
            RationalNumber b = l.getImaginaryPart();
            RationalNumber c = r.getRealPart();
            RationalNumber d = r.getImaginaryPart();

            Times times = new Times(List.of());
            Plus plus = new Plus(List.of());
            Minus minus = new Minus(List.of());

            RationalNumber cSquared = (RationalNumber) times.compute(c, c);
            RationalNumber dSquared = (RationalNumber) times.compute(d, d);
            RationalNumber denominator = (RationalNumber) plus.compute(cSquared, dSquared);

            RationalNumber ac = (RationalNumber) times.compute(a, c);
            RationalNumber bd = (RationalNumber) times.compute(b, d);
            RationalNumber realNumerator = (RationalNumber) plus.compute(ac, bd);
            RationalNumber realPart = (RationalNumber) compute(realNumerator, denominator);

            RationalNumber bc = (RationalNumber) times.compute(b, c);
            RationalNumber ad = (RationalNumber) times.compute(a, d);
            RationalNumber imaginaryNumerator = (RationalNumber) minus.compute(bc, ad);
            RationalNumber imaginaryPart = (RationalNumber) compute(imaginaryNumerator, denominator);

            return new ComplexNumber(realPart, imaginaryPart);
        }

        if (left instanceof ComplexNumber complex && (right instanceof RealNumber || right instanceof RationalNumber)) {
            MyNumber realPart = compute(complex.getRealPart(), right);
            MyNumber imaginaryPart = compute(complex.getImaginaryPart(), right);
            if (realPart.equals(RealNumber.NaN)) return RealNumber.NaN;
            return new ComplexNumber(realPart, imaginaryPart);
        }

        if ((left instanceof RealNumber || left instanceof RationalNumber) && right instanceof ComplexNumber complex) {
            // a / (c + di)
            RationalNumber c = complex.getRealPart();
            RationalNumber d = complex.getImaginaryPart();

            Times times = new Times(List.of());
            Plus plus = new Plus(List.of());

            RationalNumber cSquared = (RationalNumber) times.compute(c, c);
            RationalNumber dSquared = (RationalNumber) times.compute(d, d);
            RationalNumber denominator = (RationalNumber) plus.compute(cSquared, dSquared);

            // Multiply numerator by the conjugate of the denominator
            MyNumber realPart = times.compute(left, c);
            MyNumber imaginaryPart = times.compute(left, d.getOpposite());
            ComplexNumber nominator = new ComplexNumber(realPart, imaginaryPart);

            return compute(nominator, denominator);
        }

        throw new IllegalArgumentException("Unsupported types for division");
    }
}
