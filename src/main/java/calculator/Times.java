package calculator;

import java.util.List;

/** This class represents the arithmetic multiplication operation "*".
 * The class extends an abstract superclass Operation.
 * Other subclasses of Operation represent other arithmetic operations.
 * @see Operation
 * @see Minus
 * @see Plus
 * @see Divides
 */
public final class Times extends Operation
 {
  /**
   * Class constructor specifying a number of Expressions to multiply.
   *
   * @param elist The list of Expressions to multiply
   * @throws IllegalConstruction    If an empty list of expressions if passed as parameter
   * @see #Times(List<Expression>,Notation)
   */
  public /*constructor*/ Times(List<Expression> elist) throws IllegalConstruction {
  	this(elist, null);
  }

  /**
   * Class constructor specifying a number of Expressions to multiply,
   * as well as the Notation used to represent the operation.
   *
   * @param elist The list of Expressions to multiply
   * @param n The Notation to be used to represent the operation
   * @throws IllegalConstruction    If an empty list of expressions if passed as parameter
   * @see #Times(List<Expression>)
   * @see Operation#Operation(List<Expression>,Notation)
   */
  public Times(List<Expression> elist, Notation n) throws IllegalConstruction {
  	super(elist,n);
  	symbol = "*";
  	neutral = 1;
  }

  /**
   * The actual computation of the (binary) arithmetic multiplication of two integers
   * @param l The first integer
   * @param r The second integer that should be multiplied with the first
   * @return The integer that is the result of the multiplication
   */
  public int op(int l, int r)
    { return (l*r); }

  @Override
  public MyNumber compute(MyNumber left, MyNumber right) throws IllegalConstruction {
      if (left instanceof RealNumber l && right instanceof RealNumber r) {
          return new RealNumber(l.getValue() * r.getValue());
      } else if (left instanceof RationalNumber l && right instanceof RationalNumber r) {
          Times times = new Times(List.of());
          RealNumber numerator = (RealNumber) times.compute(l.getNominator(), r.getNominator());
          RealNumber denominator = (RealNumber) times.compute(l.getDenominator(), r.getDenominator());
          return new RationalNumber(numerator, denominator).simplify();
      } else if (left instanceof RealNumber l && right instanceof RationalNumber r) {
          return compute(new RationalNumber(l), r);
      } else if (left instanceof RationalNumber l && right instanceof RealNumber r) {
          return compute(l, new RationalNumber(r));
      } else if (left instanceof ComplexNumber l && right instanceof ComplexNumber r) {
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
      } else if ((left instanceof RealNumber || left instanceof RationalNumber) && right instanceof ComplexNumber complex ) {
          Times times = new Times(List.of());
          MyNumber realPart = times.compute(complex.getRealPart(), left);
          MyNumber imaginaryPart = times.compute(complex.getImaginaryPart(), left);

          return new ComplexNumber(realPart, imaginaryPart);
      } else {
          throw new IllegalArgumentException("Unsupported types for multiplication");
      }
  }
}
