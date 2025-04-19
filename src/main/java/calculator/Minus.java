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

    public NumberValue opComplex(NumberValue l, NumberValue r) {
        if (l.isComplex()) {
            if (r.isComplex()) {
                double realPart = l.integerPart() + l.decimalPart() - r.integerPart() - r.decimalPart();
                double realDecimalPart = roundDecimal(l.decimalPart(), r.decimalPart(), realPart);

                double imaginaryPart = l.integerImaginaryPart() + l.decimalImaginaryPart() - r.integerImaginaryPart() - r.decimalImaginaryPart();
                double imaginaryDecimalPart = roundDecimal(l.decimalImaginaryPart(), r.decimalImaginaryPart(), imaginaryPart);

                return new NumberValue((int) realPart, realDecimalPart, (int) imaginaryPart, imaginaryDecimalPart);
            }

            else if (r.isDecimal()) {
                double realPart = l.integerPart() + l.decimalPart() - r.integerPart() - r.decimalPart();
                double realDecimalPart = roundDecimal(l.decimalPart(), r.decimalPart(), realPart);
                return new NumberValue((int) realPart, realDecimalPart, l.integerImaginaryPart(), l.decimalImaginaryPart());
            }

            else {
                double realPart = l.integerPart() + l.decimalPart() - r.integerPart();
                double realDecimalPart = roundDecimal(l.decimalPart(), 0, realPart);
                return new NumberValue((int) realPart, realDecimalPart, l.integerImaginaryPart(), l.decimalImaginaryPart());
            }
        }

        else {
            if (l.isDecimal()) {
                double realPart = l.integerPart() + l.decimalPart() - r.integerPart() - r.decimalPart();
                double realDecimalPart = roundDecimal(l.decimalPart(), r.decimalPart(), realPart);
                return new NumberValue((int) realPart, realDecimalPart, -r.integerImaginaryPart(), -r.decimalImaginaryPart());
            }

            else {
                double realPart = l.integerPart() - r.integerPart() - r.decimalPart();
                double realDecimalPart = roundDecimal(0, r.decimalPart(), realPart);
                return new NumberValue((int) realPart, realDecimalPart, -r.integerImaginaryPart(), -r.decimalImaginaryPart());
            }
        }
    }
}
