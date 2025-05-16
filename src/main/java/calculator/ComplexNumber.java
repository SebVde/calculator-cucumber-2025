package calculator;

import java.util.Objects;

/**
 * This class represents a complex number composed of two {@link RationalNumber}s:
 * a real part and an imaginary part. The form is: a + bi.
 * This class extends {@link MyNumber}, which allows it to be treated as an expression.
 */
public class ComplexNumber extends MyNumber {

    /** The real part of the complex number */
    private final RationalNumber realPart;

    /** The imaginary part of the complex number */
    private final RationalNumber imaginaryPart;

    /**
     * Constructs a complex number with a real and an imaginary part.
     * Both parts must be {@link RationalNumber} instances; otherwise, a cast exception is thrown.
     *
     * @param realPart the real part of the complex number
     * @param imaginaryPart the imaginary part of the complex number
     */
    public ComplexNumber(MyNumber realPart, MyNumber imaginaryPart) {
        this.realPart = (RationalNumber) realPart;
        this.imaginaryPart = (RationalNumber) imaginaryPart;
    }

    /**
     * @return the real part of the complex number
     */
    public RationalNumber getRealPart() {
        return realPart;
    }

    /**
     * @return the imaginary part of the complex number
     */
    public RationalNumber getImaginaryPart() {
        return imaginaryPart;
    }

    /**
     * Returns a string representation of the complex number in standard format.
     * It handles special cases such as zero, unit imaginary part, and sign formatting.
     *
     * @return the string representation of the complex number
     */
    @Override
    public String toString() {
        if (this.imaginaryPart.equals(new RationalNumber(new RealNumber(0.0)))) {
            return this.realPart.toString();
        }
        if (this.realPart.equals(new RationalNumber(new RealNumber(0.0)))) {
            return this.imaginaryPart + "i";
        }
        if (this.imaginaryPart.equals(new RationalNumber(new RealNumber(1.0)))) {
            return this.realPart + " + i";
        }
        if (this.imaginaryPart.equals(new RationalNumber(new RealNumber(-1.0)))) {
            return this.realPart + " - i";
        }
        if (this.imaginaryPart.isNegative()) {
            return this.realPart + " - " + this.imaginaryPart.getOpposite() + "i";
        }

        return this.realPart + " + " + this.imaginaryPart + "i";
    }

    /**
     * Compares this complex number to another object for equality.
     * Two complex numbers are equal if both their real and imaginary parts are equal.
     *
     * @param obj the object to compare to
     * @return true if the objects represent the same complex number, false otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        ComplexNumber that = (ComplexNumber) obj;
        return this.realPart.equals(that.realPart) && this.imaginaryPart.equals(that.imaginaryPart);
    }

    /**
     * Computes a hash code for the complex number, based on its components.
     *
     * @return the hash code of the complex number
     */
    @Override
    public int hashCode() {
        return Objects.hash(this.realPart, this.imaginaryPart);
    }

    /**
     * Simplifies the complex number by simplifying its real and imaginary parts.
     *
     * @return a new {@link ComplexNumber} with simplified components
     */
    public ComplexNumber simplify() {
        return new ComplexNumber(
                realPart.simplify(),
                imaginaryPart.simplify()
        );
    }
}
