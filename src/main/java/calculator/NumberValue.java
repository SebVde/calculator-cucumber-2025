package calculator;

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

    public double asDouble() {
        return integerPart + (decimalPart == null ? 0.0 : decimalPart);
    }

    public int asInt() throws IllegalConstruction {
        if (isDecimal()) {
            throw new IllegalConstruction("Number has decimal part");
        } else {
            return integerPart;
        }
    }
}
