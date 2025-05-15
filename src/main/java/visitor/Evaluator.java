package visitor;

import calculator.*;

import java.util.ArrayList;
import java.util.List;

public class Evaluator extends Visitor {

    private Expression result;
    private boolean preserveFractions = false;
    private boolean useDegrees = false;

    public Evaluator(boolean preserveFractions) {
        this(false, preserveFractions);
    }

    public Evaluator(boolean useDegrees, boolean preserveFractions) {
        this.useDegrees = useDegrees;
        this.preserveFractions = preserveFractions;
        System.out.println("[Evaluator] Mode : degrees=" + useDegrees + ", preserveFractions=" + preserveFractions);
    }

    public void setPreserveFractions(boolean preserveFractions) {
        this.preserveFractions = preserveFractions;
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
        result = preserveFractions ? n.simplify(true) : n.simplify(false);
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
            System.out.println("[Evaluator] Evaluating operation: " + o.getSymbol());
            MyNumber computed = o.compute(evaluatedArgs);
            System.out.println("[Evaluator] Raw result: " + computed);
            switch (computed) {
                case RationalNumber r -> {
                    System.out.println("[Evaluator] Simplifying RationalNumber");
                    result = r.simplify(preserveFractions);
                }
                case ComplexNumber c -> {
                    System.out.println("[Evaluator] Simplifying ComplexNumber");
                    MyNumber simplifiedReal = c.getRealPart();
                    MyNumber simplifiedImag = c.getImaginaryPart();
                    if (simplifiedReal instanceof RationalNumber rr) {
                        simplifiedReal = rr.simplify(preserveFractions);
                    }
                    if (simplifiedImag instanceof RationalNumber ri) {
                        simplifiedImag = ri.simplify(preserveFractions);
                    }
                    result = new ComplexNumber(simplifiedReal, simplifiedImag);
                }
                default -> {
                    System.out.println("[Evaluator] No simplification needed");
                    result = computed;
                }
            }
        } catch (Exception e) {
            throw new IllegalArgumentException("Error during evaluation: " + e.getMessage());
        }
    }

    @Override
    public void visit(FunctionWrapper f) {
        f.getArgument().accept(this);
        Expression arg = result;

        if (!(arg instanceof MyNumber value)) {
            throw new IllegalArgumentException("Function argument must be a number");
        }

        String name = f.getFunctionName();
        double x;

        if (value instanceof RationalNumber r) {
            x = r.getNominator().getValue() / r.getDenominator().getValue();
        } else if (value instanceof RealNumber r) {
            x = r.getValue();
        } else {
            throw new IllegalArgumentException("Unsupported number type in function: " + value);
        }

        result = switch (name) {
            case "sqrt" -> new RealNumber(Math.sqrt(x));
            default -> throw new IllegalArgumentException("Unsupported function: " + name);
        };
    }

}
