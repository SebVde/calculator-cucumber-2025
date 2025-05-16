package calculator;

import java.util.Objects;
import java.math.BigDecimal;

/**
 * This class represents a rational number (a fraction), composed of a numerator and a denominator,
 * both stored as {@link RealNumber}. It extends {@link MyNumber} so it can be used as an {@link Expression}.
 */
public class RationalNumber extends MyNumber {

    private final RealNumber nominator;
    private final RealNumber denominator;

    /**
     * Constructs a rational number with a given numerator and denominator.
     *
     * @param number the numerator
     * @param denominator the denominator
     */
    public RationalNumber(RealNumber number, RealNumber denominator) {
        this.nominator = number;
        this.denominator = denominator;
    }

    /**
     * Constructs a rational number with a given numerator and an implicit denominator of 1.
     *
     * @param number the numerator
     */
    public RationalNumber(RealNumber number) {
        this.nominator = number;
        this.denominator = new RealNumber(1.0);
    }

    /**
     * Returns the numerator of the rational number.
     *
     * @return the numerator as a {@link RealNumber}
     */
    public RealNumber getNominator() {
        return this.nominator;
    }

    /**
     * Returns the denominator of the rational number.
     *
     * @return the denominator as a {@link RealNumber}
     */
    public RealNumber getDenominator() {
        return this.denominator;
    }

    /**
     * Returns a string representation of the rational number.
     * If the denominator is 1, only the numerator is displayed.
     *
     * @return the string representation
     */
    @Override
    public String toString() {
        if (denominator.equals(new RealNumber(1.0))) {
            return nominator.toString();
        } else {
            return nominator.toString() + "/" + denominator;
        }
    }

    /**
     * Computes a hash code for this rational number.
     *
     * @return the hash code
     */
    @Override
    public int hashCode() {
        return Objects.hash(nominator, denominator);
    }

    /**
     * Compares this rational number to another object for equality.
     * Two rational numbers are considered equal if they are equal after simplification.
     *
     * @param o the object to compare
     * @return true if the two objects are equal, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RationalNumber that)) return false;
        RationalNumber a = this.simplify();
        RationalNumber b = that.simplify();
        return a.nominator.equals(b.nominator) && a.denominator.equals(b.denominator);
    }

    /**
     * Checks whether the rational number is negative.
     *
     * @return true if the value is negative, false otherwise
     */
    public boolean isNegative() {
        return (this.nominator.getValue() < 0 && this.denominator.getValue() > 0)
                || (this.nominator.getValue() > 0 && this.denominator.getValue() < 0);
    }

    /**
     * Returns the opposite of this rational number (negates the numerator).
     *
     * @return the opposite {@link RationalNumber}
     */
    public RationalNumber getOpposite() {
        return new RationalNumber(this.nominator.getOpposite(), this.denominator);
    }

    /**
     * Simplifies the rational number by dividing the numerator and denominator
     * by their greatest common divisor (GCD).
     *
     * @return a new simplified {@link RationalNumber}
     */
    public RationalNumber simplify() {
        if (denominator.equals(RealNumber.ONE)) {
            return this;
        }

        double gcd = gcd(nominator, denominator);
        RealNumber simplifiedNumerator = new RealNumber(nominator.getValue() / gcd);
        RealNumber simplifiedDenominator = new RealNumber(denominator.getValue() / gcd);

        return new RationalNumber(simplifiedNumerator, simplifiedDenominator);
    }

    /**
     * Simplifies the rational number and returns either a RationalNumber or RealNumber
     * depending on whether the result is reducible to an integer and the flag preserveFraction.
     *
     * @param preserveFraction if true, always return a RationalNumber; otherwise return RealNumber when possible
     * @return a simplified {@link MyNumber} (either RationalNumber or RealNumber)
     */
    public MyNumber simplify(boolean preserveFraction) {
        RationalNumber simplified = this.simplify();
        if (preserveFraction) {
            return simplified;
        } else if (simplified.getDenominator().equals(new RealNumber(1.0))) {
            return simplified.getNominator(); // Convert to RealNumber
        } else {
            return simplified;
        }
    }

    /**
     * Computes the greatest common divisor (GCD) of two {@link RealNumber} values using the Euclidean algorithm.
     * Handles decimals by converting to integers based on precision.
     *
     * @param numA the first real number
     * @param numB the second real number
     * @return the greatest common divisor as a double
     */
    private double gcd(RealNumber numA, RealNumber numB) {
        int maxDecimals = Math.max(
                BigDecimal.valueOf(numA.getValue()).scale(),
                BigDecimal.valueOf(numB.getValue()).scale()
        );

        double a = Math.abs(numA.getValue());
        double b = Math.abs(numB.getValue());

        if (maxDecimals > 0) {
            a *= Math.pow(10, maxDecimals);
            b *= Math.pow(10, maxDecimals);
        }

        while (b != 0) {
            double temp = b;
            b = a % b;
            a = temp;
        }

        return a / Math.pow(10, maxDecimals);
    }
}
