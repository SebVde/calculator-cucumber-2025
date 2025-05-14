package visitor;

import calculator.*;

import java.util.ArrayList;
import java.util.List;

public class Evaluator extends Visitor {

    private Expression result;

    private boolean useDegrees = false;

    public Evaluator(boolean useDegrees) {
        this.useDegrees = useDegrees;
    }

    public void setUseDegrees(boolean useDegrees) {
        this.useDegrees = useDegrees;
    }

    private double convert(double angle) {
        return useDegrees ? Math.toRadians(angle) : angle;
    }

    public Expression getResult() {
        return result;
    }

    @Override
    public void visit(MyNumber n) {
        result = n;
    }

    @Override
    public void visit(RealNumber n) {
        result = n;
    }

    @Override
    public void visit(RationalNumber n) {
        result = n;
    }

    @Override
    public void visit(ComplexNumber n) {
        result = n;
    }

    @Override
    public void visit(Operation o) {
        ArrayList<Expression> evaluatedArgs = new ArrayList<>();
        for (Expression arg : o.args) {
            arg.accept(this);
            evaluatedArgs.add(result);
        }
        try {
            result = o.compute(evaluatedArgs);
        } catch (Exception e) {
            throw new IllegalArgumentException("Error during evaluation: " + e.getMessage());
        }
    }

    @Override
    public void visit(FunctionWrapper f) {
        f.getArgument().accept(this);
        Expression evaluated = result;

        if (!(evaluated instanceof MyNumber value)) {
            throw new IllegalArgumentException("Function argument must be a number");
        }

        switch (value) {
            case RealNumber r -> {
                double x = convert(r.getValue());
                result = applyRealFunction(f.getFunctionName(), x);
            }
            case RationalNumber r -> {
                double x = r.getNominator().getValue() / r.getDenominator().getValue();
                x = convert(x);
                result = applyRealFunction(f.getFunctionName(), x);
            }
            case ComplexNumber c -> {
                double a = c.getRealPart().getNominator().getValue() / c.getRealPart().getDenominator().getValue();
                double b = c.getImaginaryPart().getNominator().getValue() / c.getImaginaryPart().getDenominator().getValue();

                final double real1 = Math.sin(a) * Math.cosh(b);
                final double imag1 = Math.cos(a) * Math.sinh(b);
                final double real2 = Math.cos(a) * Math.cosh(b);
                final double imag2 = -Math.sin(a) * Math.sinh(b);
                result = switch (f.getFunctionName()) {
                    case "sin" -> complexToExpr(
                            real1,
                            imag1
                    );
                    case "cos" -> complexToExpr(
                            real2,
                            imag2
                    );
                    case "tan" -> {
                        Expression sinZ = complexToExpr(real1, imag1);
                        Expression cosZ = complexToExpr(real2, imag2);

                        try {
                            Divides divide = new Divides(List.of(sinZ, cosZ));
                            Evaluator subEval = new Evaluator(this.useDegrees);
                            divide.accept(subEval);
                            yield subEval.getResult();
                        } catch (IllegalConstruction e) {
                            throw new IllegalArgumentException("Error during tan(z) evaluation: " + e.getMessage());
                        }
                    }
                    case "sqrt" -> {
                        double modulus = Math.hypot(a, b);
                        double real = Math.sqrt((modulus + a) / 2);
                        double imag = Math.signum(b) * Math.sqrt((modulus - a) / 2);
                        yield complexToExpr(real, imag);
                    }
                    default ->
                            throw new IllegalArgumentException("Function not supported for complex numbers: " + f.getFunctionName());
                };
            }
            default -> {
                throw new IllegalArgumentException("Unsupported number type");
            }
        }
    }

    private Expression applyRealFunction(String name, double x) {
        System.out.println("applyRealFunction(" + name + ", " + x + ") — useDegrees: " + useDegrees);
        return switch (name) {
            case "sqrt" -> new RealNumber(Math.sqrt(x));
            case "sin" -> new RealNumber(Math.sin(x));
            case "cos" -> new RealNumber(Math.cos(x));
            case "tan" -> new RealNumber(Math.tan(x));
            default -> throw new IllegalArgumentException("Unknown function: " + name);
        };
    }

    private ComplexNumber complexToExpr(double real, double imag) {
        return new ComplexNumber(
                new RationalNumber(new RealNumber(real)),
                new RationalNumber(new RealNumber(imag))
        );
    }

}
