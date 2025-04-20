package calculator;

import visitor.CountVisitor;
import visitor.Visitor;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * MyNumber is a concrete class that represents arithmetic numbers,
 * which are a special kind of Expressions, just like operations are.
 *
 * @see Expression
 * @see Operation
 */
public class MyNumber implements Expression {
    /**
     * Regex for real numbers, it accepts numbers in the following form
     * 12 | 12.0 | 12. | +12 | -12 | 12E-2 | +12E2
     */
    private static final String realRegEx = "^[-+]?(?:\\d+(?:\\.\\d*)?|\\.\\d+)(?:[eE][-+]?\\d+)?$";
    /**
     * Regex for integers
     */
    private static final String intRegEx = "(\\d+)";

    /**
     * Regex for complex numbers
     */
    private static final String complexRegEx = "([+-]?(?:\\d+\\.?\\d*|\\.\\d+)(?:[Ee][+-]?\\d+)?)" +
            "([+-](?:\\d+\\.?\\d*|\\.\\d+)(?:[Ee][+-]?\\d+)?)i|" +
            "([+-]?(?:\\d+\\.?\\d*|\\.\\d+)(?:[Ee][+-]?\\d+)?)i";

    public static MyNumber parseNumber(String s) throws IllegalConstruction, NumberFormatException {
        if (s.matches(intRegEx)) {
            return new MyNumber(Integer.parseInt(s));
        } else if (s.matches(realRegEx)) {
            return new MyNumber(Double.parseDouble(s));
        }

        Pattern pattern = Pattern.compile(complexRegEx);
        Matcher matcher = pattern.matcher(s);

        if (matcher.find()) {
            double real = 0;
            double imaginary = 0;

            if (matcher.group(1) != null) {
                real = Double.parseDouble(matcher.group(1));
            } if (matcher.group(2) != null) {
                imaginary = Double.parseDouble(matcher.group(2));
            } if (matcher.group(3) != null) {
                imaginary = Double.parseDouble(matcher.group(3));
            }

            return new MyNumber(real, imaginary);
        } else {
            throw new IllegalConstruction("Couldn't parse number");
        }
    }

    private final NumberValue value;

    public MyNumber(double value) {
        this.value = new NumberValue((int) value, value % 1, null, null);
    }

    public MyNumber(int value) {
        this.value = new NumberValue(value, null, null, null);
    }

    public MyNumber(double realPart, double imaginaryPart) {
        int scaleReal = BigDecimal.valueOf(realPart).scale();
        long factorReal = (long) Math.pow(10, scaleReal);
        double realDecimalPart = (double) Math.round(factorReal * (realPart % 1)) / factorReal;

        int scaleImaginary = BigDecimal.valueOf(imaginaryPart).scale();
        long factorImaginary = (long) Math.pow(10, scaleImaginary);
        double imaginaryDecimalPart = (double) Math.round(factorImaginary * (imaginaryPart % 1)) / factorImaginary;

        this.value = new NumberValue((int) realPart, (realDecimalPart == 0 ? null : realDecimalPart) , (int) imaginaryPart, (imaginaryDecimalPart == 0 ? null : imaginaryDecimalPart));
    }

    public NumberValue getValue() {
        return this.value;
    }

    /**
     * accept method to implement the visitor design pattern to traverse arithmetic expressions.
     * Each number will pass itself to the visitor object to get processed by the visitor.
     *
     * @param v The visitor object
     */
    public void accept(Visitor v) {
        v.visit(this);
    }

    public NumberValue convertToRadians() {
        double conversion = Math.toRadians(this.value.asDouble());
        return new NumberValue((int) conversion, conversion % 1, null, null);
    }

    public NumberValue convertToDegrees() {
        double conversion = Math.toDegrees(this.value.asDouble());
        return new NumberValue((int) conversion, conversion % 1, null, null);
    }


    /**
     * The depth of a number expression is always 0
     *
     * @return The depth of a number expression
     */
    public int countDepth() {
        CountVisitor v = new CountVisitor();
        v.visit(this);
        return v.getDepthCount();
    }

    /**
     * The number of operations contained in a number expression is always 0
     *
     * @return The number of operations contained in a number expression
     */
    public int countOps() {
        CountVisitor v = new CountVisitor();
        v.visit(this);
        return v.getOpsCount();
    }

    /**
     * The number of numbers contained in a number expression is always 1
     *
     * @return The number of numbers contained in  a number expression
     */
    public int countNbs() {
        CountVisitor v = new CountVisitor();
        v.visit(this);
        return v.getNbCount();
    }

    /**
     * Two MyNumber expressions are equal if the values they contain are equal
     *
     * @param o The object to compare to
     * @return A boolean representing the result of the equality test
     */
    @Override
    public boolean equals(Object o) {
        // No object should be equal to null (not including this check can result in an exception if a MyNumber is tested against null)
        if (o == null) return false;

        // If the object is compared to itself then return true
        if (o == this) {
            return true;
        }

        // If the object is of another type then return false
        if (!(o instanceof MyNumber)) {
            return false;
        }

        return Objects.equals(this.getValue(), ((MyNumber) o).getValue());
        // Used == since the contained value is a primitive value
        // If it had been a Java object, .equals() would be needed
    }

    /**
     * The method hashCode needs to be overridden it the equals method is overridden;
     * otherwise there may be problems when you use your object in hashed collections
     * such as HashMap, HashSet, LinkedHashSet.
     *
     * @return The result of computing the hash.
     */
    @Override
    public int hashCode() {
        return getValue().hashCode();
    }

    @Override
    public String toString() {
        return String.valueOf(this.value);
    }
}
