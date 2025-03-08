package calculator;

public class MyReal extends MyNumber {

    private final double value;

    public MyReal(double value) {
        this.value = value;
    }

    public MyReal(String s) throws IllegalConstruction {
        if (s.contains("E")) {
            String[] split = s.split("E");

            if(split.length != 2)
                throw new IllegalConstruction("Invalid scientific notation format");


            try {
                double first = Double.parseDouble(split[0]);
                double second = Double.parseDouble(split[1]);

                this.value = first * Math.pow(10, second);

            } catch (NumberFormatException e) {
                throw new IllegalConstruction("Invalid numeric values in scientific notation");
            }
        } else {

            try {
                this.value = Double.parseDouble(s);
            } catch (NumberFormatException e) {
                throw new IllegalConstruction("Invalid decimal number");
            }
        }

    }

    @Override
    public Integer getIntegerValue() {
        return (int) this.value;
    }

    @Override
    public Double getRealValue() {
        return this.value;
    }

    public double degreesToRadians() {
        return Math.toRadians(this.value);
    }

    public double radiansToDegrees() {
        return Math.toDegrees(this.value);
    }

    @Override
    public String toString() {
        return String.valueOf(this.value);
    }
}
