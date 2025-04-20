package calculator;

import java.util.List;

/**
 * This class represents the arithmetic multiplication operation "*".
 * The class extends an abstract superclass Operation.
 * Other subclasses of Operation represent other arithmetic operations.
 *
 * @see Operation
 * @see Minus
 * @see Plus
 * @see Divides
 */
public final class Times extends Operation {
    /**
     * Class constructor specifying a number of Expressions to multiply.
     *
     * @param elist The list of Expressions to multiply
     * @throws IllegalConstruction If an empty list of expressions if passed as parameter
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
     * @param n     The Notation to be used to represent the operation
     * @throws IllegalConstruction If an empty list of expressions if passed as parameter
     * @see #Times(List<Expression>)
     * @see Operation#Operation(List<Expression>,Notation)
     */
    public Times(List<Expression> elist, Notation n) throws IllegalConstruction {
        super(elist, n);
        symbol = "*";
        neutral = 1;
    }

    /**
     * The actual computation of the (binary) arithmetic multiplication of two numbers
     *
     * @param l The first number
     * @param r The second number that should be multiplied with the first
     * @return The number that is the result of the multiplication
     */
    public NumberValue op(NumberValue l, NumberValue r) {
        if (l.isZero() || r.isZero()) {
            return NumberValue.ZERO;
        } else if (l.isComplex() || r.isComplex()) {
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
        return new NumberValue(l * r, null, null, null);
    }

    public NumberValue op(double l, double r) {
        double result = l * r;
        return new NumberValue((int) result, result % 1, null, null);
    }

    public NumberValue opComplex(NumberValue l, NumberValue r) {
        if (l.isComplex()) {
            if (r.isComplex()) {
                double realPart = (l.integerPart() + l.getDecimalPart()) * (r.integerPart() + r.getDecimalPart())
                                - (l.integerImaginaryPart() + l.getDecimalImaginaryPart()) * (r.integerImaginaryPart() + r.getDecimalImaginaryPart());
                double realDecimalPart = roundDecimal(l.getDecimalPart(), r.getDecimalPart(), realPart);

                double imaginaryPart = (l.integerPart() + l.getDecimalPart()) * (r.integerImaginaryPart() + r.getDecimalImaginaryPart())
                                     + (l.integerImaginaryPart() + l.getDecimalImaginaryPart()) * (r.integerPart() + r.getDecimalPart());
                double imaginaryDecimalPart = roundDecimal(l.getDecimalImaginaryPart(), r.getDecimalImaginaryPart(), imaginaryPart);
                return new NumberValue((int) realPart, (realDecimalPart == 0 ? null : realDecimalPart) , (imaginaryPart == 0 && imaginaryDecimalPart == 0? null : (int) imaginaryPart), (imaginaryDecimalPart == 0 ? null : imaginaryDecimalPart));
            } else {
                double realPart = (l.integerPart() + l.getDecimalPart()) * (r.integerPart() + r.getDecimalPart());
                double realDecimalPart = roundDecimal((r.isDecimal() ? 0.000000001 : l.getDecimalPart()), (r.isDecimal() ? 0.000000001 : 0), realPart);

                double imaginaryPart = (l.integerPart() + l.getDecimalPart()) * (r.integerImaginaryPart() + r.getDecimalImaginaryPart());
                double imaginaryDecimalPart = roundDecimal((r.isDecimal() ? 0.000000001 : l.getDecimalImaginaryPart()), (r.isDecimal() ? 0.000000001 : 0), imaginaryPart);
                return new NumberValue((int) realPart, (realDecimalPart == 0 ? null : realDecimalPart) , (imaginaryPart == 0 && imaginaryDecimalPart == 0? null : (int) imaginaryPart), (imaginaryDecimalPart == 0 ? null : imaginaryDecimalPart));
            }
        } else {
            double realPart = (l.integerPart() + l.getDecimalPart()) * (r.integerPart() + r.getDecimalPart());
            double realDecimalPart = roundDecimal((l.isDecimal() ? 0.000000001 : 0), (l.isDecimal() ? 0.000000001 : r.getDecimalPart()), realPart);

            double imaginaryPart = (l.integerPart() + l.getDecimalPart()) * (r.integerImaginaryPart() + r.getDecimalImaginaryPart());
            double imaginaryDecimalPart = roundDecimal((l.isDecimal() ? 0.000000001 : 0), (l.isDecimal() ? 0.000000001 : r.getDecimalImaginaryPart()), imaginaryPart);
            return new NumberValue((int) realPart, (realDecimalPart == 0 ? null : realDecimalPart) , (imaginaryPart == 0 && imaginaryDecimalPart == 0? null : (int) imaginaryPart), (imaginaryDecimalPart == 0 ? null : imaginaryDecimalPart));
        }
    }
}
