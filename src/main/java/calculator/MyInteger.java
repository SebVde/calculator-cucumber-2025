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

    @Override
    public Integer getIntegerValue() {
        return this.value;
    }

    @Override
    public Double getRealValue() {
        return (double) this.value;
    }
}
