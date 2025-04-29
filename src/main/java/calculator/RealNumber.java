package calculator;

public class RealNumber extends MyNumber{
    private final Double value;

    public RealNumber(Double value){
        this.value = value;
    }

    public Double getValue() {
        return this.value;
    }

    @Override
    public MyNumber add(MyNumber other) {
        return switch (other) {
            case RealNumber realNumber -> new RealNumber(this.value + realNumber.value);
            case RationalNumber rational ->
                    this.add(new RealNumber(rational.getNominator().value / rational.getDenominator().value));
            case ComplexNumber otherComplex -> otherComplex.add(this);
            case null, default -> throw new IllegalArgumentException("Invalid type for addition");
        };
    }

    @Override
    public MyNumber subtract(MyNumber other) {
        return switch (other) {
            case RealNumber realNumber -> new RealNumber(this.value - realNumber.value);
            case RationalNumber rational ->
                    this.subtract(new RealNumber(rational.getNominator().value / rational.getDenominator().value));
            case ComplexNumber otherComplex -> new ComplexNumber(
                    this.subtract(otherComplex.getRealPart()),
                    otherComplex.getImaginaryPart().multiply(new RealNumber(-1.0))
            );
            case null, default -> throw new IllegalArgumentException("Invalid type for subtraction");
        };
    }

    @Override
    public MyNumber multiply(MyNumber other) {
        return switch (other) {
            case RealNumber realNumber -> new RealNumber(this.value * realNumber.value);
            case RationalNumber rational ->
                    this.multiply(new RealNumber(rational.getNominator().value / rational.getDenominator().value));
            case ComplexNumber otherComplex -> otherComplex.multiply(this);
            case null, default -> throw new IllegalArgumentException("Invalid type for multiplication");
        };
    }

    @Override
    public MyNumber divide(MyNumber other) {
        switch (other) {
            case RealNumber realNumber -> {
                if (realNumber.value != 0) {
                    return new RealNumber(this.value / realNumber.value);
                } else {
                    throw new ArithmeticException("Division by zero");
                }
            }
            case RationalNumber rational -> {
                if (rational.getNominator().value != 0) {
                    return this.divide(new RealNumber(rational.getNominator().value / rational.getDenominator().value));
                } else {
                    throw new ArithmeticException("Division by zero");
                }
            }
            case ComplexNumber otherComplex -> {
                return otherComplex.getConjugate().multiply(this).divide(otherComplex.getConjugate().multiply(otherComplex));
            }
            case null, default -> throw new IllegalArgumentException("Invalid type for division");
        }
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        RealNumber that = (RealNumber) obj;
        return value.equals(that.value);
    }
}
