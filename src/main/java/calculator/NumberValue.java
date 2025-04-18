package calculator;

/**
 * This class holds the value of the number declare in MyNumber
 * @param integerPart
 * @param decimalPart
 * @param integerImaginaryPart
 * @param decimalImaginaryPart
 */
public record NumberValue(Integer integerPart, Double decimalPart, Integer integerImaginaryPart, Double decimalImaginaryPart) {
    public static final NumberValue ZERO = new NumberValue(0, 0.0, 0, 0.0);
    public static final NumberValue MAX = new NumberValue(Integer.MAX_VALUE, 0.0, Integer.MAX_VALUE, 0.0);

    public boolean isDecimal() {
        return this.decimalPart != null && this.integerImaginaryPart == null && this.decimalImaginaryPart == null;
    }

    public boolean isComplex() { return this.integerImaginaryPart != null; }

    public boolean isZero() {
        return this.decimalPart == 0 && this.integerPart == 0 && this.integerImaginaryPart == 0 && this.decimalImaginaryPart == 0;
    }

    @Override
    public String toString() {
        if (isDecimal()) return String.valueOf(integerPart + decimalPart);
        else if (isComplex()) {
            return String.valueOf(integerPart + (decimalPart == null ? 0 : decimalPart))
                    + (integerImaginaryPart >= 0 ? "+" : "")
                    + String.valueOf(integerImaginaryPart + (decimalImaginaryPart == null ? 0 : decimalImaginaryPart))
                    + "i";
        }

        else
            return String.valueOf(integerPart);
    }

    /**
     * Transforms  the number into a double
     * @return The number value as a double
     */
    public double asDouble() {
        return integerPart + (decimalPart == null ? 0.0 : decimalPart);
    }

    /**
     * Transforms the number into an integer
     * @return Number value as an int
     * @throws IllegalConstruction If the number is decimal or complex and the method would lead to loss of information
     */
    public int asInt() throws IllegalConstruction {
        if (isComplex() && (integerImaginaryPart != 0 || (decimalImaginaryPart != null && decimalImaginaryPart != 0))) {
            throw new IllegalConstruction("Number is complex");
        }
        else if (decimalPart != 0) {
            throw new IllegalConstruction("Number has decimal part");
        }
        else {
            return integerPart;
        }
    }
}
