package visitor;

import calculator.*;

import java.util.stream.Stream;

/**
 * A visitor that traverses an arithmetic expression tree
 * and produces a formatted String representation of the expression.
 * Supports INFIX, PREFIX, and POSTFIX notations.
 */
public class OutputVisitor extends Visitor {

    private String output;

    /**
     * Default constructor for the visitor.
     */
    public OutputVisitor() {
        // No initialization needed
    }

    /**
     * Returns the formatted string output after visiting an expression.
     *
     * @return the output string representation
     */
    public String getOutput() {
        return output;
    }

    /**
     * Visits a generic number and stores its string representation.
     *
     * @param n the number to visit
     */
    @Override
    public void visit(MyNumber n) {
        output = n.toString();
    }

    /**
     * Visits a real number and stores its double value as a string.
     *
     * @param n the real number
     */
    @Override
    public void visit(RealNumber n) {
        output = Double.toString(n.getValue());
    }

    /**
     * Visits a rational number and formats it as "numerator/denominator".
     *
     * @param n the rational number
     */
    @Override
    public void visit(RationalNumber n) {
        output = n.getNominator().toString() + "/" + n.getDenominator().toString();
    }

    /**
     * Visits a complex number and formats it as "a + bi".
     *
     * @param n the complex number
     */
    @Override
    public void visit(ComplexNumber n) {
        String realPart = n.getRealPart().toString();
        String imaginaryPart = n.getImaginaryPart().toString();
        output = realPart + " + " + imaginaryPart + "i";
    }

    /**
     * Visits an operation and builds its formatted string depending on the notation mode.
     *
     * @param o the operation to visit
     */
    @Override
    public void visit(Operation o) {
        Stream<String> s = o.args.stream().map(arg -> {
            OutputVisitor subVisitor = new OutputVisitor();
            arg.accept(subVisitor);
            return subVisitor.getOutput();
        });

        output = switch (o.notation) {
            case INFIX -> "( " + s.reduce((s1, s2) -> s1 + " " + o.getSymbol() + " " + s2).get() + " )";
            case PREFIX -> o.getSymbol() + " (" + s.reduce((s1, s2) -> s1 + ", " + s2).get() + ")";
            case POSTFIX -> "(" + s.reduce((s1, s2) -> s1 + ", " + s2).get() + ") " + o.getSymbol();
        };
    }

    /**
     * Visits a function wrapper (e.g., sqrt(x)) and formats it as "name(argument)".
     *
     * @param f the function wrapper
     */
    @Override
    public void visit(FunctionWrapper f) {
        OutputVisitor argVisitor = new OutputVisitor();
        f.argument().accept(argVisitor);
        output = f.functionName() + "(" + argVisitor.getOutput() + ")";
    }
}
