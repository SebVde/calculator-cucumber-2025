package calculator;

import visitor.CountVisitor;
import visitor.Visitor;

import java.util.Objects;

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
    private static final String complexRegEx = "^[-+]?(?:\\d+(?:\\.\\d*)?|\\.\\d+)(?:[eE][-+]?\\d+)?[-+](?:\\d+(?:\\.\\d*)?|\\.\\d+)(?:[eE][-+]?\\d+)?i$";

    public static MyNumber parseNumber(String s) throws IllegalConstruction, NumberFormatException {
        if (s.matches(intRegEx)) {
            return new MyNumber(Integer.parseInt(s));
        } else if (s.matches(realRegEx)) {
            if (s.contains("E")) {
                String[] split = s.split("E");
                if (split.length != 2)
                    throw new IllegalConstruction("Invalid scientific notation format");

                double first = Double.parseDouble(split[0]);
                double second = Double.parseDouble(split[1]);
                return new MyNumber(first * Math.pow(10, second));

            } else {
                return new MyNumber(Double.parseDouble(s));
            }
        } else if (s.matches(complexRegEx)) {
            if (s.contains("E")) {
                String[] split = s.split("E");
                if (split.length == 2) {
                    return parseComplexWithSingleExponent(split);
                } else if (split.length == 3){
                    return parseComplexWithDoubleExponent(split);
                } else {
                    throw new IllegalConstruction("Invalid scientific notation format");
                }
            } else {
                
            }
        } else {
            throw new IllegalConstruction("Couldn't parse number");
        }
        return null;
    }

    private static MyNumber parseComplexWithSingleExponent(String[] splitted) {
        // TODO: implement this
        return null;
    }

    private static MyNumber parseComplexWithDoubleExponent(String[] splitted) {
        double first = Double.parseDouble(splitted[0]);
        double fourth = Double.parseDouble(splitted[2].replace("i", ""));
        String[] splitImaginary = splitted[1].split("\\+");

        if (splitImaginary.length == 2) { // _E_+_E_i
            double second = Double.parseDouble(splitImaginary[0]);
            double third = Double.parseDouble(splitImaginary[1]);
            return new MyNumber(first * Math.pow(10, second), third * Math.pow(10, fourth));
        }

        else if (splitImaginary[0].length() - splitImaginary[0].replace("-", "").length() == 2) { // _E-_-_E_i
            splitImaginary = splitted[1].split("-");
            double second = Double.parseDouble(splitImaginary[0]);
            double third = Double.parseDouble(splitImaginary[1]);
            return new MyNumber(first * Math.pow(10, -second), -third * Math.pow(10, fourth));
        }

        else { // _E_-_E_i
            splitImaginary = splitted[1].split("-");
            double second = Double.parseDouble(splitImaginary[0]);
            double third = Double.parseDouble(splitImaginary[1]);
            return new MyNumber(first * Math.pow(10, second), -third * Math.pow(10, fourth));
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
        this.value = new NumberValue((int) realPart, realPart % 1, (int) imaginaryPart, imaginaryPart % 1);
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
