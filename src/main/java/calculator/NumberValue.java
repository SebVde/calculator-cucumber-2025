package calculator;

/**
 * This class holds the value of the number declare in MyNumber
 * @param integerRealPart
 * @param decimalRealPart
 * @param integerImaginaryPart
 * @param decimalImaginaryPart
 */
public record NumberValue(Integer integerRealPart, Double decimalRealPart, Integer integerImaginaryPart, Double decimalImaginaryPart) {
    public static final NumberValue ZERO = new NumberValue(0, 0.0, 0, 0.0);
    public static final NumberValue MAX = new NumberValue(Integer.MAX_VALUE, 0.0, Integer.MAX_VALUE, 0.0);

    public boolean isDecimal() {
        return this.decimalRealPart != null && this.integerImaginaryPart != null && this.decimalImaginaryPart != null;
    }

    public boolean isComplex() { return this.integerImaginaryPart != null; }

    public boolean isZero() {
        return this.decimalRealPart == 0 && this.integerRealPart == 0 && this.integerImaginaryPart == 0 && this.decimalImaginaryPart == 0;
    }

    @Override
    public String toString() {
        if (isDecimal()) return String.valueOf(integerRealPart + decimalRealPart);
        else if (isComplex()) {
            return String.valueOf(integerRealPart + (decimalRealPart == null ? 0 : decimalRealPart))
                    + (integerImaginaryPart >= 0 ? "+" : "")
                    + String.valueOf(integerImaginaryPart + (decimalImaginaryPart == null ? 0 : decimalImaginaryPart))
                    + "i";
        }

        else
            return String.valueOf(integerRealPart);
    }

    /**
     * Transforms  the number into a double
     * @return The number value as a double
     * @throws IllegalConstruction If the number is complex and the method would lead to loss of information
     */
    public double asDouble() throws IllegalConstruction{
        if (isComplex()) {
            throw new IllegalConstruction("Number is complex");
        }
        else {
            return integerRealPart + (decimalRealPart == null ? 0.0 : decimalRealPart);
        }
    }

    /**
     * Transforms the number into an integer
     * @return Number value as an int
     * @throws IllegalConstruction If the number is decimal or complex and the method would lead to loss of information
     */
    public int asInt() throws IllegalConstruction {
        if (isDecimal()) {
            throw new IllegalConstruction("Number has decimal part");
        }
        else if (isComplex()) {
            throw new IllegalConstruction("Number is complex");
        }
        else {
            return integerRealPart;
        }
    }
}
