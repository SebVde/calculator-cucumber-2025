package calculator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * This class represents the arithmetic division operation "/".
 * The class extends an abstract superclass Operation.
 * Other subclasses of Operation represent other arithmetic operations.
 *
 * @see Operation
 * @see Minus
 * @see Times
 * @see Plus
 */
public final class Divides extends Operation {

    /**
     * Class constructor specifying a number of Expressions to divide.
     *
     * @param elist The list of Expressions to divide
     * @throws IllegalConstruction If an empty list of expressions if passed as parameter
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
     * @param n     The Notation to be used to represent the operation
     * @throws IllegalConstruction If an empty list of expressions if passed as parameter
     * @see #Divides(List<Expression>)
     * @see Operation#Operation(List<Expression>,Notation)
     */
    public Divides(List<Expression> elist, Notation n) throws IllegalConstruction {
        super(elist, n);
        symbol = "/";
        neutral = 1;
    }

    /**
     * The actual computation of the (binary) arithmetic division of two numbers
     *
     * @param l The first number
     * @param r The second number that should divide the first
     * @return The number that is the result of the division
     */
    public NumberValue op(NumberValue l, NumberValue r) {
        if (l.isZero() && r.isZero()) {
            return NumberValue.ZERO;
        } else if (r.isZero()) {
            return NumberValue.MAX;
        } else if (l.isComplex() || r.isComplex()) {
            return opComplex(l, r);
        }
        // If a number is decimal we treat both of them as decimal
        // and the resulting value will be as well
        else if (l.isDecimal() || r.isDecimal()) {
            double lReal = l.asDouble();
            double rReal = r.asDouble();
            return op(lReal, rReal);
        } else {
            int lInt = l.integerPart();
            int rInt = r.integerPart();
            return op(lInt, rInt);
        }
    }

    private NumberValue op(int l, int r) {
        if (l == 0 && r == 0) {
            return NumberValue.ZERO;
        } else if (r == 0) {
            return NumberValue.MAX;
        } else {
            return new NumberValue(l / r, null, null, null);
        }
    }

    private NumberValue op(double l, double r) {
        if (l == 0 && r == 0) {
            return NumberValue.ZERO;
        } else if (r == 0) {
            return NumberValue.MAX;
        } else {
            double result = l / r;
            return new NumberValue((int) result, result % 1, null, null);
        }
    }

    private NumberValue getConjugate(NumberValue complexNumber) {
        return new NumberValue(complexNumber.integerPart(), complexNumber.decimalPart(), -complexNumber.integerImaginaryPart(), complexNumber.decimalImaginaryPart());
    }

    private NumberValue multiply(NumberValue l, NumberValue r) {
        if (l.isComplex()) {
            double realPart = (l.integerPart() + l.getDecimalPart()) * (r.integerPart() + r.getDecimalPart())
                    - (l.integerImaginaryPart() + l.getDecimalImaginaryPart()) * (r.integerImaginaryPart() + r.getDecimalImaginaryPart());
            double realDecimalPart = roundDecimal(l.getDecimalPart(), r.getDecimalPart(), realPart);

            double imaginaryPart = (l.integerPart() + l.getDecimalPart()) * (r.integerImaginaryPart() + r.getDecimalImaginaryPart())
                    + (l.integerImaginaryPart() + l.getDecimalImaginaryPart()) * (r.integerPart() + r.getDecimalPart());
            double imaginaryDecimalPart = roundDecimal(l.getDecimalImaginaryPart(), r.getDecimalImaginaryPart(), imaginaryPart);

            return new NumberValue((int) realPart, (realDecimalPart == 0 ? null : realDecimalPart) , ((int) imaginaryPart == 0 ? null : (int) imaginaryPart), (imaginaryDecimalPart == 0 ? null : imaginaryDecimalPart));
        } else {
            double realPart = (l.integerPart() + l.getDecimalPart()) * (r.integerPart() + r.getDecimalPart());
            double realDecimalPart = roundDecimal((l.isDecimal() ? 0.000000001 : 0), (l.isDecimal() ? 0.000000001 : r.getDecimalPart()), realPart);

            double imaginaryPart = (l.integerPart() + l.getDecimalPart()) * (r.integerImaginaryPart() + r.getDecimalImaginaryPart());
            double imaginaryDecimalPart = roundDecimal((l.isDecimal() ? 0.000000001 : 0), (l.isDecimal() ? 0.000000001 : r.getDecimalImaginaryPart()), imaginaryPart);
            return new NumberValue((int) realPart, (realDecimalPart == 0 ? null : realDecimalPart) , (int) imaginaryPart, (imaginaryDecimalPart == 0 ? null : imaginaryDecimalPart));
        }
    }

    public NumberValue opComplex(NumberValue l, NumberValue r) {
        if (l.isComplex()) {
            if (r.isComplex()) {
                NumberValue rConjugate = getConjugate(r);
                NumberValue numerator = multiply(l, rConjugate);
                NumberValue denominator = multiply(r, rConjugate);
                return op(numerator, denominator);
            } else {
                double realPart = (l.integerPart() + l.getDecimalPart()) / (r.integerPart() + r.getDecimalPart());
                double realDecimalPart = roundDecimal(0.000000001, 0.000000001, realPart);
                double imaginaryPart = (l.integerImaginaryPart() + l.getDecimalImaginaryPart()) / (r.integerPart() + r.getDecimalPart());
                double imaginaryDecimalPart = roundDecimal(0.000000001, 0.000000001, imaginaryPart);
                return new NumberValue((int) realPart, (realDecimalPart == 0 ? null : realDecimalPart) , (int) imaginaryPart, (imaginaryDecimalPart == 0 ? null : imaginaryDecimalPart));
            }
        } else {
            NumberValue rConjugate = getConjugate(r);
            NumberValue numerator = multiply(l, rConjugate);
            NumberValue denominator = multiply(r, rConjugate);
            return op(numerator, denominator);
        }
    }
}
