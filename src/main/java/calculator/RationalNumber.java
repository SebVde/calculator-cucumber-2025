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
    public MyNumber add(MyNumber other) {
        switch (other) {
            case RationalNumber otherRational -> {
                RealNumber newNominator = (RealNumber) this.nominator.multiply(otherRational.denominator)
                        .add(otherRational.nominator.multiply(this.denominator));
                RealNumber newDenominator = (RealNumber) this.denominator.multiply(otherRational.denominator);
                return new RationalNumber(newNominator, newDenominator);
            }
            case RealNumber realNumber -> {
                return this.add(new RationalNumber(realNumber));
            }
            case ComplexNumber otherComplex -> {
                return otherComplex.add(this);
            }
            case null, default -> throw new IllegalArgumentException("Invalid type for addition");
        }
    }

    @Override
    public MyNumber subtract(MyNumber other) {
        switch (other) {
            case RationalNumber otherRational -> {
                RealNumber newNominator = (RealNumber) this.nominator.multiply(otherRational.denominator)
                        .subtract(otherRational.nominator.multiply(this.denominator));

                RealNumber newDenominator = (RealNumber) this.denominator.multiply(otherRational.denominator);
                return new RationalNumber(newNominator, newDenominator);
            }
            case RealNumber realNumber -> {
                return this.subtract(new RationalNumber(realNumber));
            }
            case ComplexNumber otherComplex -> {
                return new ComplexNumber(this.subtract(otherComplex.getRealPart()),
                        otherComplex.getImaginaryPart().multiply(new RealNumber(-1.0)));
            }
            case null, default ->
                throw new IllegalArgumentException("Invalid type for subtraction");
        }
    }

    @Override
    public MyNumber multiply(MyNumber other) {
        switch (other) {
            case RationalNumber otherRational -> {
                RealNumber newNominator = (RealNumber) this.nominator.multiply(otherRational.nominator);
                RealNumber newDenominator = (RealNumber) this.denominator.multiply(otherRational.denominator);
                return new RationalNumber(newNominator, newDenominator);
            }
            case RealNumber realNumber -> {
                return this.multiply(new RationalNumber(realNumber));
            }
            case ComplexNumber otherComplex -> {
                return otherComplex.multiply(this);
            }
            case null, default -> throw new IllegalArgumentException("Invalid type for multiplication");
        }
    }

    @Override
    public MyNumber divide(MyNumber other) {
        switch (other) {
            case RationalNumber otherRational -> {
                if (!Objects.equals(otherRational.nominator, new RealNumber(0.0))) {
                    RealNumber newNominator = (RealNumber) this.nominator.multiply(otherRational.denominator);
                    RealNumber newDenominator = (RealNumber) this.denominator.multiply(otherRational.nominator);
                    return new RationalNumber(newNominator, newDenominator);
                } else {
                    throw new ArithmeticException("Division by zero");
                }
            }
            case RealNumber realNumber -> {
                return this.divide(new RationalNumber(realNumber));
            }
            case ComplexNumber otherComplex -> {
                return otherComplex.getConjugate().multiply(this).divide(otherComplex.getConjugate().multiply(otherComplex));
            }
            case null, default -> throw new IllegalArgumentException("Invalid type for division");
        }
    }

    @Override
    public String toString() {
        return nominator.toString() + "/" + denominator.toString();
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
}
