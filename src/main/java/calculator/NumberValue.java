package calculator;

/**
 * This class holds the value of the number declare in MyNumber
 * @param integerPart
 * @param decimalPart
 */
public record NumberValue(Integer integerPart, Double decimalPart) {
    public static final NumberValue ZERO = new NumberValue(0, 0.0);
    public static final NumberValue MAX = new NumberValue(Integer.MAX_VALUE, 0.0);

    public boolean isDecimal() {
        return this.decimalPart != null;
    }

    public boolean isZero() {
        return this.decimalPart == 0 && this.integerPart == 0;
    }

    @Override
    public String toString() {
        return isDecimal() ?
                String.valueOf(integerPart + decimalPart) :
                String.valueOf(integerPart);
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
     * @throws IllegalConstruction If the number is decimal and the method would lead to loss of information
     */
    public int asInt() throws IllegalConstruction {
        if (isDecimal()) {
            throw new IllegalConstruction("Number has decimal part");
        } else {
            return integerPart;
        }
    }
}
