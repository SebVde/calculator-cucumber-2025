package calculator;

/**
 * This class represents a real number as a leaf node in an arithmetic expression.
 * It extends {@link MyNumber} and encapsulates a {@code Double} value.
 */
public class RealNumber extends MyNumber {

    /** Constant representing the real number 1.0 */
    public static final RealNumber ONE = new RealNumber(1.0);

    /** Constant representing Not-a-Number (NaN) */
    public static final RealNumber NaN = new RealNumber(Double.NaN);

    /** The internal value of the real number */
    private final Double value;

    /**
     * Constructs a real number from a {@code Double} value.
     *
     * @param value the double value representing this real number
     */
    public RealNumber(Double value) {
        this.value = value;
    }

    /**
     * Returns the internal double value of this real number.
     *
     * @return the value as a {@link Double}
     */
    public Double getValue() {
        return this.value;
    }

    /**
     * Returns the additive inverse of this real number.
     *
     * @return a new {@link RealNumber} that is the negation of this one
     */
    public RealNumber getOpposite() {
        return new RealNumber(-this.value);
    }

    /**
     * Returns a string representation of the number, with formatting simplifications:
     * <ul>
     *     <li>If the value is close to 0, returns "0"</li>
     *     <li>If the value is close to 1, returns "1"</li>
     *     <li>If the value is close to -1, returns "-1"</li>
     *     <li>If the value has no decimal part, it's printed as an integer</li>
     *     <li>Otherwise, the decimal value is returned as-is</li>
     * </ul>
     *
     * @return a string representation of the number
     */
    @Override
    public String toString() {
        if (Math.abs(value) < 1e-10) return "0";
        if (Math.abs(value - 1.0) < 1e-10) return "1";
        if (Math.abs(value + 1.0) < 1e-10) return "-1";

        // If the value is a whole number, return it as an integer
        if (value % 1 == 0) {
            return String.valueOf(value.intValue());
        }

        // Otherwise, return as double
        return String.valueOf(value);
    }

    /**
     * Computes the hash code based on the internal value.
     *
     * @return the hash code
     */
    @Override
    public int hashCode() {
        return value.hashCode();
    }

    /**
     * Compares this real number with another object for equality.
     * Two {@link RealNumber} instances are equal if their values are exactly equal.
     *
     * @param obj the object to compare with
     * @return true if the objects represent the same value, false otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        RealNumber that = (RealNumber) obj;
        return value.equals(that.value);
    }
}
