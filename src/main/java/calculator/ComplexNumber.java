package calculator;

import java.util.Objects;
import java.util.List;

public class ComplexNumber extends MyNumber {

    private final RationalNumber realPart;

    private final RationalNumber imaginaryPart;

    public ComplexNumber(MyNumber realPart, MyNumber imaginaryPart) {
        this.realPart = (RationalNumber) realPart;
        this.imaginaryPart = (RationalNumber) imaginaryPart;
    }

    public RationalNumber getRealPart() {
        return realPart;
    }

    public RationalNumber getImaginaryPart() {
        return imaginaryPart;
    }

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
        // TODO gérer les nombres négatifs pour partie imaginaire
        return this.realPart + " + " + this.imaginaryPart + "i";
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        ComplexNumber that = (ComplexNumber) obj;
        return this.realPart.equals(that.realPart) && this.imaginaryPart.equals(that.imaginaryPart);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.realPart, this.imaginaryPart);
    }
    
}

