package calculator;

public class MyInteger extends MyNumber {

    int value;
    /**
     * Constructor method
     *
     * @param v The integer value to be contained in the object
     */
    public MyInteger(int v) {
        this.value = v;
    }

    protected MyInteger(String s) {
        this.value = Integer.parseInt(s);
    }

    @Override
    public Integer getIntegerValue() {
        return this.value;
    }

    @Override
    public Double getRealValue() {
        return (double) this.value;
    }

    @Override
    public String toString() {
        return String.valueOf(this.value);
    }
}
