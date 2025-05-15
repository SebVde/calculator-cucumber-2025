package calculator;

import java.util.Objects;
import java.math.BigDecimal;

public class RationalNumber extends MyNumber{
    private final RealNumber nominator;
    private final RealNumber denominator;

    public RationalNumber(RealNumber number, RealNumber denominator){
        this.nominator = number;
        this.denominator = denominator;
    }

    public RationalNumber(RealNumber number){
        this.nominator = number;
        this.denominator = new RealNumber(1.0);
    }

    public RealNumber getNominator() {
        return this.nominator;
    }

    public RealNumber getDenominator() {
        return this.denominator;
    }

    @Override
    public String toString() {
        if (denominator.equals(new RealNumber(1.0))) {
            return nominator.toString();
        } else {
            return nominator.toString() + "/" + denominator;
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(nominator, denominator);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RationalNumber that)) return false;
        RationalNumber a = this.simplify();
        RationalNumber b = that.simplify();
        return a.nominator.equals(b.nominator) && a.denominator.equals(b.denominator);
    }

    public boolean is_negative() {
        return (this.nominator.getValue() < 0 && this.denominator.getValue() > 0) || (this.nominator.getValue() > 0 && this.denominator.getValue() < 0);
    }

    public RationalNumber get_opposite() {
        return new RationalNumber(this.nominator.get_opposite(), this.denominator);
    }

    /**
     * Simplifies the rational number by dividing the numerator and denominator
     * by their greatest common divisor (GCD).
     * @return A new RationalNumber that is the simplified version of this one.
     */
    public RationalNumber simplify() {
        System.out.println("RationalNumber.simplify() called");
        if (denominator.equals(RealNumber.ONE)) { return this; }

        double gcd = gcd(nominator, denominator);

        RealNumber simplifiedNumerator = new RealNumber(nominator.getValue() / gcd);
        RealNumber simplifiedDenominator = new RealNumber(denominator.getValue() / gcd);

        return new RationalNumber(simplifiedNumerator, simplifiedDenominator);
    }

    /**
     * Helper method to compute the greatest common divisor (GCD) of two integers
     * using the Euclidean algorithm.
     * @param num_a The first integer.
     * @param num_b The second integer.
     * @return The GCD of a and b.
     */
    private double gcd(RealNumber num_a, RealNumber num_b) {
        int max_decimals = Math.max(BigDecimal.valueOf(num_a.getValue()).scale(), BigDecimal.valueOf(num_b.getValue()).scale());
        double a = Math.abs(num_a.getValue());
        double b = Math.abs(num_b.getValue());

        if (max_decimals > 0) {
            a *= Math.pow(10, max_decimals);
            b *= Math.pow(10, max_decimals);
        }

        while (b != 0) {
            double temp = b;
            b = a % b;
            a = temp;
        }
        return a/Math.pow(10, max_decimals);
    }

    public MyNumber simplify(boolean preserveFraction) {
        RationalNumber simplified = this.simplify();
        if (preserveFraction) {
            System.out.println("Preserving fraction: " + simplified);
            return simplified;
        } else if (simplified.getDenominator().equals(new RealNumber(1.0))) {
            System.out.println("Simplified to integer: " + simplified.getNominator());
            return simplified.getNominator();  // => RealNumber
        } else {
            System.out.println("Simplified to fraction: " + simplified);
            return simplified;
        }
    }

}