package calculator;

public class RealNumber extends MyNumber{
    private final Double value;

    public RealNumber(Double value){
        this.value = value;
    }

    @Override
    public MyNumber add(MyNumber other) {
        if (other instanceof RealNumber) {
            return new RealNumber(this.value + ((RealNumber) other).value);
        } else if (other instanceof RationalNumber rational) {
            return this.add(new RealNumber(rational.getNominator().value / rational.getDenominator().value));
        } else {
            throw new IllegalArgumentException("Invalid type for addition");
        }
    }

    @Override
    public MyNumber subtract(MyNumber other) {
        if (other instanceof RealNumber) {
            return new RealNumber(this.value - ((RealNumber) other).value);
        } else if (other instanceof RationalNumber rational) {
            return this.subtract(new RealNumber(rational.getNominator().value / rational.getDenominator().value));
        } else {
            throw new IllegalArgumentException("Invalid type for subtraction");
        }
    }

    @Override
    public MyNumber multiply(MyNumber other) {
        if (other instanceof RealNumber) {
            return new RealNumber(this.value * ((RealNumber) other).value);
        } else if (other instanceof RationalNumber rational) {
            return this.multiply(new RealNumber(rational.getNominator().value / rational.getDenominator().value));
        } else {
            throw new IllegalArgumentException("Invalid type for multiplication");
        }
    }

    @Override
    public MyNumber divide(MyNumber other) {
        if (other instanceof RealNumber) {
            if (((RealNumber) other).value != 0) {
                return new RealNumber(this.value / ((RealNumber) other).value);
            } else {
                throw new ArithmeticException("Division by zero");
            }
        } else if (other instanceof RationalNumber rational) {
            if (rational.getNominator().value != 0) {
                return this.divide(new RealNumber(rational.getNominator().value / rational.getDenominator().value));
            } else {
                throw new ArithmeticException("Division by zero");
            }
        } else {
            throw new IllegalArgumentException("Invalid type for division");
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
