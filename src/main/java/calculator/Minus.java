package calculator;

import java.util.List;

/**
 * This class represents the arithmetic operation "-".
 * The class extends an abstract superclass Operation.
 * Other subclasses of Operation represent other arithmetic operations.
 *
 * @see Operation
 * @see Plus
 * @see Times
 * @see Divides
 */
public final class Minus extends Operation {

    /**
     * Class constructor specifying a number of Expressions to subtract.
     *
     * @param elist The list of Expressions to subtract
     * @throws IllegalConstruction If an empty list of expressions if passed as parameter
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
     * @param n     The Notation to be used to represent the operation
     * @throws IllegalConstruction If an empty list of expressions if passed as parameter
     * @see #Minus(List<Expression>)
     * @see Operation#Operation(List<Expression>,Notation)
     */
    public Minus(List<Expression> elist, Notation n) throws IllegalConstruction {
        super(elist, n);
        symbol = "-";
        neutral = 0;
    }

    /**
     * The actual computation of the (binary) arithmetic subtraction of two numbers
     *
     * @param l The first number
     * @param r The second number that should be subtracted from the first
     * @return The number that is the result of the subtraction
     */
    public NumberValue op(NumberValue l, NumberValue r) {
        if (l.isComplex() || r.isComplex()) {
            return opComplex(l, r);
        }
        // If a number is decimal we treat both of them as decimal
        // and the resulting value will be as well
        else if (l.isDecimal() || r.isDecimal()) {
            return op(l.asDouble(), r.asDouble());
        } else {
            return op(l.integerPart(), r.integerPart());
        }
    }

    public NumberValue op(int l, int r) {
        return new NumberValue(l - r, null, null, null);
    }

    public NumberValue op(double l, double r) {
        double result = l - r;
        return new NumberValue((int) result, result % 1, null, null);
    }

    /**
     * Performs the subtraction of two numbers, one of them at least being a complex number.
     *
     * @param l 
     * @param r 
     * @return A NumberValue object representing the result of the subtraction.
     */
    private NumberValue opComplex(NumberValue l, NumberValue r) {
        double realPart = l.integerPart() + l.getDecimalPart() - r.integerPart() - r.getDecimalPart();
        double realDecimalPart = roundDecimal(l.getDecimalPart(), r.getDecimalPart(), realPart);
        if (l.isComplex()) {
            if (r.isComplex()) {
                double imaginaryPart = l.integerImaginaryPart() + l.getDecimalImaginaryPart() - r.integerImaginaryPart() - r.getDecimalImaginaryPart();
                double imaginaryDecimalPart = roundDecimal(l.getDecimalImaginaryPart(), r.getDecimalImaginaryPart(), imaginaryPart);

                return new NumberValue((int) realPart, (realDecimalPart == 0 ? null : realDecimalPart) , (imaginaryPart == 0 && imaginaryDecimalPart == 0? null : (int) imaginaryPart), (imaginaryDecimalPart == 0 ? null : imaginaryDecimalPart));
            } else {
                return new NumberValue((int) realPart, (realDecimalPart == 0 ? null : realDecimalPart), l.integerImaginaryPart(), l.getDecimalImaginaryPart());
            }
        } else {
            return new NumberValue((int) realPart, (realDecimalPart == 0 ? null : realDecimalPart), -r.integerImaginaryPart(), -r.getDecimalImaginaryPart());

        }
    }
}
