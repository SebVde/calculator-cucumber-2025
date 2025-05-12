package calculator;

import java.util.List;

/** This class represents the arithmetic division operation "/".
 * The class extends an abstract superclass Operation.
 * Other subclasses of Operation represent other arithmetic operations.
 * @see Operation
 * @see Minus
 * @see Times
 * @see Plus
 */
public final class Divides extends Operation
{

  /**
   * Class constructor specifying a number of Expressions to divide.
   *
   * @param elist The list of Expressions to divide
   * @throws IllegalConstruction    If an empty list of expressions if passed as parameter
   * @see #Divides(List<Expression>,Notation)
   */
  public /*constructor*/ Divides(List<Expression> elist) throws IllegalConstruction {
	this(elist, null);
  }

    /**
     * Class constructor specifying a number of Expressions to divide,
     * as well as the notation used to represent the operation.
     *
     * @param elist The list of Expressions to divide
     * @param n The Notation to be used to represent the operation
     * @throws IllegalConstruction  If an empty list of expressions if passed as parameter
     * @see #Divides(List<Expression>)
     * @see Operation#Operation(List<Expression>,Notation)
     */
  public Divides(List<Expression> elist, Notation n) throws IllegalConstruction {
	super(elist,n);
	symbol = "/";
	neutral = 1;
  }

    /**
     * The actual computation of the (binary) arithmetic division of two integers
     * @param l The first integer
     * @param r The second integer that should divide the first
     * @return The integer that is the result of the division
     */
  public int op(int l, int r)
    {
        if (r == 0 && l != 0) {
            return Integer.MAX_VALUE;
        } else if (r == 0) {
            return 0;
        } else {
            return l / r;
        }
    }

    @Override
    public MyNumber compute(MyNumber left, MyNumber right) throws IllegalConstruction {
        if (left instanceof RealNumber l && right instanceof RealNumber r) {
            if (r.getValue() == 0) return RealNumber.NaN;
            return new RealNumber(l.getValue() / r.getValue());
        } else if (left instanceof RationalNumber l && right instanceof RationalNumber r) {
            if (r.getNominator().getValue() == 0) return RealNumber.NaN;
            Times times = new Times(List.of());
            RealNumber numerator = (RealNumber) times.compute(l.getNominator(), r.getDenominator());
            RealNumber denominator = (RealNumber) times.compute(l.getDenominator(), r.getNominator());
            return new RationalNumber(numerator, denominator).simplify();
        } else if (left instanceof RealNumber l && right instanceof RationalNumber r) {
            return compute(new RationalNumber(l), r);
        } else if (left instanceof RationalNumber l && right instanceof RealNumber r) {
            return compute(l, new RationalNumber(r));
        } else if (left instanceof ComplexNumber l && right instanceof ComplexNumber r) {
            // Division of complex numbers
            RationalNumber a = l.getRealPart();
            RationalNumber b = l.getImaginaryPart();
            RationalNumber c = r.getRealPart();
            RationalNumber d = r.getImaginaryPart();

            Times times = new Times(List.of());
            Plus plus = new Plus(List.of());
            Minus minus = new Minus(List.of());

            // Compute the denominator: c^2 + d^2
            RationalNumber cSquared = (RationalNumber) times.compute(c, c);
            RationalNumber dSquared = (RationalNumber) times.compute(d, d);
            RationalNumber denominator = (RationalNumber) plus.compute(cSquared, dSquared);

            // Compute the real part: (a * c + b * d) / (c^2 + d^2)
            RationalNumber ac = (RationalNumber) times.compute(a, c);
            RationalNumber bd = (RationalNumber) times.compute(b, d);
            RationalNumber realNumerator = (RationalNumber) plus.compute(ac, bd);
            RationalNumber realPart = (RationalNumber) compute(realNumerator, denominator);

            // Compute the imaginary part: (b * c - a * d) / (c^2 + d^2)
            RationalNumber bc = (RationalNumber) times.compute(b, c);
            RationalNumber ad = (RationalNumber) times.compute(a, d);
            RationalNumber imaginaryNumerator = (RationalNumber) minus.compute(bc, ad);
            RationalNumber imaginaryPart = (RationalNumber) compute(imaginaryNumerator, denominator);

            return new ComplexNumber(realPart, imaginaryPart);
        } else if (left instanceof ComplexNumber complex && (right instanceof RealNumber || right instanceof RationalNumber)) {
            MyNumber realPart = compute(complex.getRealPart(), right);
            MyNumber imaginaryPart = compute(complex.getImaginaryPart(), right);
            if (realPart.equals(RealNumber.NaN)) return RealNumber.NaN;
            return new ComplexNumber(realPart, imaginaryPart);
        } else if ((left instanceof RealNumber || left instanceof RationalNumber) && right instanceof ComplexNumber complex ) {
            RationalNumber c = complex.getRealPart();
            RationalNumber d = complex.getImaginaryPart();

            Times times = new Times(List.of());
            Plus plus = new Plus(List.of());

            RationalNumber cSquared = (RationalNumber) times.compute(c, c);
            RationalNumber dSquared = (RationalNumber) times.compute(d, d);
            RationalNumber denominator = (RationalNumber) plus.compute(cSquared, dSquared);

            // Compute the real part: (a * c + b * d) / (c^2 + d^2)
            MyNumber realPart = times.compute(left, complex.getRealPart());
            MyNumber imaginaryPart = times.compute(left, complex.getImaginaryPart().get_opposite());
            ComplexNumber nominator = new ComplexNumber(realPart, imaginaryPart);

            return compute(nominator, denominator);
        } else {
            throw new IllegalArgumentException("Unsupported types for division");
        }
    }
}
