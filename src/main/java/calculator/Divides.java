package calculator;

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
        // If a number is decimal we treat both of them as decimal
        // and the resulting value will be as well
        if (l.isDecimal() || r.isDecimal()) {
            double lReal = l.asDouble();
            double rReal = r.asDouble();
            return op(lReal, rReal);
        } else {
            int lInt = l.integerRealPart();
            int rInt = r.integerRealPart();
            return op(lInt, rInt);
        }
    }

    private NumberValue op(int l, int r) {
        if (l == 0 && r == 0) {
            return NumberValue.ZERO;
        } else if (r == 0) {
            return NumberValue.MAX;
        } else {
            return new NumberValue(l / r, null);
        }
    }

    private NumberValue op(double l, double r) {
        if (l == 0 && r == 0) {
            return NumberValue.ZERO;
        } else if (r == 0) {
            return NumberValue.MAX;
        } else {
            double result = l / r;
            return new NumberValue((int) result, result % 1);
        }
    }
}
