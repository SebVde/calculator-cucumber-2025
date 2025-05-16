package calculator;

import java.util.List;

/** This class represents the arithmetic sum operation "+".
 * The class extends an abstract superclass Operation.
 * Other subclasses of Operation represent other arithmetic operations.
 * @see Operation
 * @see Minus
 * @see Times
 * @see Divides
 */
public final class Plus extends Operation
 {

  /**
   * Class constructor specifying a number of Expressions to add.
   *
   * @param elist The list of Expressions to add
   * @throws IllegalConstruction    If an empty list of expressions if passed as parameter
   * @see #Plus(List<Expression>,Notation)
   */
  public /*constructor*/ Plus(List<Expression> elist) throws IllegalConstruction {
	this(elist, null);
  }

  /**
   * Class constructor specifying a number of Expressions to add,
   * as well as the Notation used to represent the operation.
   *
   * @param elist The list of Expressions to add
   * @param n The Notation to be used to represent the operation
   * @throws IllegalConstruction    If an empty list of expressions if passed as parameter
   * @see #Plus(List<Expression>)
   * @see Operation#Operation(List<Expression>,Notation)
   */
  public Plus(List<Expression> elist, Notation n) throws IllegalConstruction {
  	super(elist,n);
  	symbol = "+";
  	neutral = 0;
  }

  /**
   * The actual computation of the (binary) arithmetic addition of two integers
   * @param l The first integer
   * @param r The second integer that should be added to the first
   * @return The integer that is the result of the addition
   */
  public int op(int l, int r) {
  	return (l+r);
  }

  @Override
  public MyNumber compute(MyNumber left, MyNumber right) throws IllegalConstruction {
      if (left instanceof RealNumber l && right instanceof RealNumber r) {
          return new RealNumber(l.getValue() + r.getValue());
      } else if (left instanceof RationalNumber l && right instanceof RationalNumber r) {
          Times times = new Times(List.of());
          Plus plus = new Plus(List.of());
          RealNumber numerator = (RealNumber) plus.compute(
              times.compute(l.getNominator(), r.getDenominator()),
              times.compute(r.getNominator(), l.getDenominator())
          );
          RealNumber denominator = (RealNumber) times.compute(l.getDenominator(), r.getDenominator());
          return new RationalNumber(numerator, denominator);
      } else if (left instanceof RealNumber l && right instanceof RationalNumber r) {
          return compute(new RationalNumber(l), r);
      } else if (left instanceof RationalNumber l && right instanceof RealNumber r) {
          return compute(l, new RationalNumber(r));
      } else if (left instanceof ComplexNumber l && right instanceof ComplexNumber r) {
          Plus plus = new Plus(List.of());
          return new ComplexNumber(
              plus.compute(l.getRealPart(), r.getRealPart()),
              plus.compute(l.getImaginaryPart(), r.getImaginaryPart())
          );
      } else if (left instanceof ComplexNumber complex && (right instanceof RealNumber || right instanceof RationalNumber)) {
          Plus plus = new Plus(List.of());
          return new ComplexNumber(
                  plus.compute(complex.getRealPart(), right),
                  complex.getImaginaryPart());
      } else if ((left instanceof RealNumber || left instanceof RationalNumber) && right instanceof ComplexNumber complex ) {
          Plus plus = new Plus(List.of());
          return new ComplexNumber(
                  plus.compute(complex.getRealPart(), left),
                  complex.getImaginaryPart());
      } else {
          throw new IllegalArgumentException("Unsupported types for addition");
      }
  }
}
