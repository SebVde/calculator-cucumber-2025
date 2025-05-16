package visitor;

import calculator.*;

import java.util.ArrayList;

/**
 * Visitor implementation used to evaluate an arithmetic expression tree.
 * It supports real, rational, and complex numbers as well as function wrappers like sqrt.
 * This evaluator respects a preservation flag to retain rational representations when needed.
 */
public class Evaluator extends Visitor {

    private Expression result;
    private boolean preserveFractions = false;

    /**
     * Constructs an evaluator with a flag indicating whether to preserve rational forms.
     *
     * @param preserveFractions true to keep fractions, false to simplify to real numbers
     */
    public Evaluator(boolean preserveFractions) {
        this.preserveFractions = preserveFractions;
    }

    /**
     * Enables or disables fraction preservation mode.
     *
     * @param preserveFractions true to preserve rational forms, false otherwise
     */
    public void setPreserveFractions(boolean preserveFractions) {
        this.preserveFractions = preserveFractions;
    }

    /**
     * Retrieves the result of the evaluation after visiting an expression.
     *
     * @return the final evaluated expression
     */
    public Expression getResult() {
        return result;
    }

    /**
     * Visits a basic number (used as a fallback).
     */
    @Override
    public void visit(MyNumber n) {
        result = n;
    }

    /**
     * Visits a real number and sets it as the result.
     */
    @Override
    public void visit(RealNumber n) {
        result = n;
    }

    /**
     * Visits a rational number and simplifies it according to the mode.
     */
    @Override
    public void visit(RationalNumber n) {
        result = preserveFractions ? n.simplify(true) : n.simplify(false);
    }

    /**
     * Visits a complex number and leaves it unchanged.
     */
    @Override
    public void visit(ComplexNumber n) {
        result = n;
    }

    /**
     * Visits an operation, evaluates its arguments, and computes the result.
     * Handles rational simplification and complex number flattening when needed.
     *
     * @param o the operation to evaluate
     */
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

                    if (simplifiedReal instanceof RealNumber) {
                        simplifiedReal = new RationalNumber((RealNumber) simplifiedReal);
                    }
                    if (simplifiedImag instanceof RealNumber) {
                        simplifiedImag = new RationalNumber((RealNumber) simplifiedImag);
                    }

                    result = new ComplexNumber(simplifiedReal, simplifiedImag);
                }

                case RealNumber r -> {
                    if (preserveFractions) {
                        result = new RationalNumber(r).simplify(true);
                    } else {
                        result = r;
                    }
                }

                default -> result = computed;
            }

        } catch (Exception e) {
            throw new IllegalArgumentException("Error during evaluation: " + e.getMessage());
        }
    }

    /**
     * Visits a function (e.g. sqrt), evaluates its argument, and computes the function result.
     * Only supports real-based functions at this stage.
     *
     * @param f the function wrapper
     */
    @Override
    public void visit(FunctionWrapper f) {
        f.argument().accept(this);
        Expression arg = result;

        if (!(arg instanceof MyNumber value)) {
            throw new IllegalArgumentException("Function argument must be a number");
        }

        String name = f.functionName();
        double x;

        switch (value) {
            case RationalNumber r -> x = r.getNominator().getValue() / r.getDenominator().getValue();
            case RealNumber r -> x = r.getValue();
            default -> throw new IllegalArgumentException("Unsupported number type in function: " + value);
        }

        result = switch (name) {
            case "sqrt" -> new RealNumber(Math.sqrt(x));
            default -> throw new IllegalArgumentException("Unsupported function: " + name);
        };
    }
}
