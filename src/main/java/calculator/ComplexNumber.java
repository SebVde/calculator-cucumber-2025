package calculator;

import java.util.Objects;

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

    public ComplexNumber getConjugate() {
        return new ComplexNumber(this.realPart, this.imaginaryPart.multiply(new RealNumber(-1.0)));
    }

    @Override
    public MyNumber add(MyNumber other) {
        switch (other) {
            case ComplexNumber otherComplex -> {
                RationalNumber newRealPart = (RationalNumber) this.realPart.add(otherComplex.getRealPart());
                RationalNumber newImaginaryPart = (RationalNumber) this.imaginaryPart.add(otherComplex.getImaginaryPart());
                return new ComplexNumber(newRealPart, newImaginaryPart);
            }
            case RealNumber otherReal -> {
                RationalNumber newRealPart = (RationalNumber) this.realPart.add(otherReal);
                return new ComplexNumber(newRealPart, this.imaginaryPart);
            }
            case RationalNumber otherRational -> {
                RationalNumber newRealPart = (RationalNumber) this.realPart.add(otherRational);
                return new ComplexNumber(newRealPart, this.imaginaryPart);
            }
            default ->
                    throw new IllegalArgumentException("Invalid type of addition");
        }
    }

    @Override
    public MyNumber subtract(MyNumber other) {
        switch (other) {
            case ComplexNumber otherComplex -> {
                RationalNumber newRealPart = (RationalNumber) this.realPart.subtract(otherComplex.getRealPart());
                RationalNumber newImaginaryPart = (RationalNumber) this.imaginaryPart.subtract(otherComplex.getImaginaryPart());
                return new ComplexNumber(newRealPart, newImaginaryPart);
            }
            case RealNumber otherReal -> {
                RationalNumber newRealPart = (RationalNumber) this.realPart.subtract(otherReal);
                return new ComplexNumber(newRealPart, this.imaginaryPart);
            }
            case RationalNumber otherRational -> {
                RationalNumber newRealPart = (RationalNumber) this.realPart.subtract(otherRational);
                return new ComplexNumber(newRealPart, this.imaginaryPart);
            }
            default ->
                    throw new IllegalArgumentException("Invalid type of subtraction");
        }
    }

    @Override
    public MyNumber multiply(MyNumber other) {
        switch (other) {
            case ComplexNumber otherComplex -> {
                RationalNumber newRealPart = (RationalNumber) this.realPart.multiply(otherComplex.getRealPart())
                        .subtract(this.imaginaryPart.multiply(otherComplex.getImaginaryPart()));
                RationalNumber newImaginaryPart = (RationalNumber) this.realPart.multiply(otherComplex.getImaginaryPart())
                        .add(this.imaginaryPart.multiply(otherComplex.getRealPart()));
                return new ComplexNumber(newRealPart, newImaginaryPart);
            }
            case RealNumber otherReal -> {
                RationalNumber newRealPart = (RationalNumber) this.realPart.multiply(otherReal);
                RationalNumber newImaginaryPart = (RationalNumber) this.imaginaryPart.multiply(otherReal);
                return new ComplexNumber(newRealPart, newImaginaryPart);
            }
            case RationalNumber otherRational -> {
                RationalNumber newRealPart = (RationalNumber) this.realPart.multiply(otherRational);
                RationalNumber newImaginaryPart = (RationalNumber) this.imaginaryPart.multiply(otherRational);
                return new ComplexNumber(newRealPart, newImaginaryPart);
            }
            default ->
                    throw new IllegalArgumentException("Invalid type of multiplication");
        }
    }

    @Override
    public MyNumber divide(MyNumber other) {
        switch (other) {
            case ComplexNumber otherComplex -> {
                ComplexNumber otherConjugate = otherComplex.getConjugate();
                ComplexNumber numerator = (ComplexNumber) this.multiply(otherConjugate);
                RationalNumber denominator = (RationalNumber) otherComplex.getRealPart().multiply(otherComplex.getRealPart())
                        .add(otherComplex.getImaginaryPart().multiply(otherComplex.getImaginaryPart()));
                return numerator.divide(denominator);
            }
            case RealNumber otherReal-> {
                if (otherReal.getValue() != 0) {
                    RationalNumber newRealPart = (RationalNumber) this.realPart.divide(otherReal);
                    RationalNumber newImaginaryPart = (RationalNumber) this.imaginaryPart.divide(otherReal);
                    return new ComplexNumber(newRealPart, newImaginaryPart);
                } else { throw new ArithmeticException("Division by zero"); }
            }
            case RationalNumber otherRational -> {
                if (otherRational.getNominator().getValue() != 0) {
                    RationalNumber newRealPart = (RationalNumber) this.realPart.divide(otherRational);
                    RationalNumber newImaginaryPart = (RationalNumber) this.imaginaryPart.divide(otherRational);
                    return new ComplexNumber(newRealPart, newImaginaryPart);
                } else { throw new ArithmeticException("Division by zero"); }
            }
            default ->
                    throw new IllegalArgumentException("Invalid type of division");
        }
    }

    @Override
    public String toString() {
        return String.valueOf(this.realPart) + this.imaginaryPart + "i";
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
