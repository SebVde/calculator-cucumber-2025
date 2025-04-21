package calculator;

/**
 * This class holds the value of the number declare in MyNumber
 * @param integerPart
 * @param decimalPart
 * @param integerImaginaryPart
 * @param decimalImaginaryPart
 */
public record NumberValue(Integer integerPart, Double decimalPart, Integer integerImaginaryPart, Double decimalImaginaryPart) {
    public static final NumberValue ZERO = new NumberValue(0, null, null, null);
    public static final NumberValue MAX = new NumberValue(Integer.MAX_VALUE, null, null, null);

    public boolean isDecimal() {
        return this.decimalPart != null && this.integerImaginaryPart == null && this.decimalImaginaryPart == null;
    }

    public boolean isComplex() { return this.integerImaginaryPart != null; }

    public boolean isZero() {
        return this.integerPart == 0 && (this.decimalPart == null || this.decimalPart == 0) && (this.integerImaginaryPart == null || this.integerImaginaryPart == 0) && (this.decimalImaginaryPart == null || this.decimalImaginaryPart == 0);
    }

    @Override
    public String toString() {
        if (isDecimal()) return String.valueOf(integerPart + decimalPart);
        else if (isComplex()) {
            if (integerPart == 0 && this.getDecimalPart() == 0) {
                return imaginaryToString();
            } else {
                return realPartToString()
                        + (integerImaginaryPart >= 0 && this.getDecimalImaginaryPart() >= 0 ? "+" : "")
                        + imaginaryToString();
            }
        } else
            return String.valueOf(integerPart);
    }

    private String realPartToString() {
        if (this.getDecimalPart() != 0) {
            return String.valueOf(integerPart + this.getDecimalPart());
        } else {
            return String.valueOf(integerPart);
        }
    }

    private String imaginaryToString() {
        if (this.getDecimalImaginaryPart() != 0) {
            return (integerImaginaryPart + this.getDecimalImaginaryPart())
                    + "i";
        } else {
            return integerImaginaryPart + "i";
        }
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
        else if (decimalPart != null && decimalPart != 0) {
            throw new IllegalConstruction("Number has decimal part");
        }
        else {
            return integerPart;
        }
    }

    public double getDecimalPart() {
        return (this.decimalPart == null ? 0 : this.decimalPart);
    }

    public double getDecimalImaginaryPart() {
        return (this.decimalImaginaryPart == null ? 0 : this.decimalImaginaryPart);
    }
}
