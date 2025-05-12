package calculator;

import java.util.List;

/** This class represents the arithmetic operation "-".
 * The class extends an abstract superclass Operation.
 * Other subclasses of Operation represent other arithmetic operations.
 * @see Operation
 * @see Plus
 * @see Times
 * @see Divides
 */
public final class Minus extends Operation
 {

  /**
   * Class constructor specifying a number of Expressions to subtract.
   *
   * @param elist The list of Expressions to subtract
   * @throws IllegalConstruction    If an empty list of expressions if passed as parameter
   * @see #Minus(List<Expression>,Notation)
   */
  public /*constructor*/ Minus(List<Expression> elist) throws IllegalConstruction {
  	this(elist, null);
  }

  /**
   * Class constructor specifying a number of Expressions to subtract,
   * as well as the Notation used to represent the operation.
   *
   * @param elist The list of Expressions to subtract
   * @param n The Notation to be used to represent the operation
   * @throws IllegalConstruction    If an empty list of expressions if passed as parameter
   * @see #Minus(List<Expression>)
   * @see Operation#Operation(List<Expression>,Notation)
   */
  public Minus(List<Expression> elist, Notation n) throws IllegalConstruction {
  	super(elist,n);
  	symbol = "-";
  	neutral = 0;
  }

    /**
     * The actual computation of the (binary) arithmetic subtraction of two integers
     * @param l The first integer
     * @param r The second integer that should be subtracted from the first
     * @return The integer that is the result of the subtraction
     */
  public int op(int l, int r) {
  	return (l-r);
  }

  @Override
  public MyNumber compute(MyNumber left, MyNumber right) throws IllegalConstruction {
      if (left instanceof RealNumber l && right instanceof RealNumber r) {
          return new RealNumber(l.getValue() - r.getValue());
      } else if (left instanceof RationalNumber l && right instanceof RationalNumber r) {
          Times times = new Times(List.of());
          Minus minus = new Minus(List.of());
          RealNumber numerator = (RealNumber) minus.compute(
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
          Minus minus = new Minus(List.of());
          return new ComplexNumber(
              minus.compute(l.getRealPart(), r.getRealPart()),
              minus.compute(l.getImaginaryPart(), r.getImaginaryPart())
          );
      } else if (left instanceof ComplexNumber complex && (right instanceof RealNumber || right instanceof RationalNumber)) {
          Minus minus = new Minus(List.of());
          return new ComplexNumber(
                  minus.compute(complex.getRealPart(), right),
                  complex.getImaginaryPart()
          );
      } else if ((left instanceof RealNumber || left instanceof RationalNumber) && right instanceof ComplexNumber complex ) {
          Minus minus = new Minus(List.of());
          return new ComplexNumber(
                  minus.compute(left, complex.getRealPart()),
                  complex.getImaginaryPart().get_opposite()
          );
      } else {
          throw new IllegalArgumentException("Unsupported types for subtraction");
      }
  }
}

