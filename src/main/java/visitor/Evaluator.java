package visitor;

import calculator.*;

import java.util.ArrayList;

public class Evaluator extends Visitor {

    private Expression result;
    private boolean preserveFractions = false;

    public Evaluator(boolean preserveFractions) {
        this.preserveFractions = preserveFractions;
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
            MyNumber computed = o.compute(evaluatedArgs);
            switch (computed) {
                case RationalNumber r -> result = r.simplify(preserveFractions);
                case ComplexNumber c -> {
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
                default -> result = computed;
            }
        } catch (Exception e) {
            throw new IllegalArgumentException("Error during evaluation: " + e.getMessage());
        }
    }

}
