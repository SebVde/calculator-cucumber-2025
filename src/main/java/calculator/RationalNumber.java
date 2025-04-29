package calculator;

import java.util.Objects;

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
        return Objects.equals(nominator, that.nominator) && Objects.equals(denominator, that.denominator);
    }

    /**
     * Simplifies the rational number by dividing the numerator and denominator
     * by their greatest common divisor (GCD).
     * @return A new RationalNumber that is the simplified version of this one.
     */
    public RationalNumber simplify() {
        int numerator = nominator.getValue().intValue();
        int denominator = this.denominator.getValue().intValue();
        int gcd = gcd(numerator, denominator);

        RealNumber simplifiedNumerator = new RealNumber((double) numerator / gcd);
        RealNumber simplifiedDenominator = new RealNumber((double) denominator / gcd);

        return new RationalNumber(simplifiedNumerator, simplifiedDenominator);
    }

    /**
     * Helper method to compute the greatest common divisor (GCD) of two integers
     * using the Euclidean algorithm.
     * @param a The first integer.
     * @param b The second integer.
     * @return The GCD of a and b.
     */
    private int gcd(int a, int b) {
        while (b != 0) {
            int temp = b;
            b = a % b;
            a = temp;
        }
        return Math.abs(a);
    }
}
