package visitor;

import calculator.*;

import java.util.ArrayList;

public class Evaluator extends Visitor {

    private Expression result;

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
            throw new RuntimeException("Error during evaluation: " + e.getMessage());
        }
    }
}
