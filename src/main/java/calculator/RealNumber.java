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
    public String toString() {
        // Check if the value has a decimal part
        if (value % 1 == 0) {
            // If it does not, return the value as an integer
            return String.valueOf(value.intValue());
        }
        // Otherwise, return the value as a double
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
