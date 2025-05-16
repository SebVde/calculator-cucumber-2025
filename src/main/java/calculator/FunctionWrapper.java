package calculator;

import visitor.Visitor;

/**
 * Represents a single-argument mathematical function, such as sqrt(x), sin(x), etc.
 * This class wraps both the function name and its argument, and supports the Visitor pattern.
 *
 * @param functionName the name of the function (e.g., "sqrt", "sin", "cos", "tan")
 * @param argument the expression the function is applied to
 */
public record FunctionWrapper(String functionName, Expression argument) implements Expression {

    /**
     * Accepts a visitor to process this function expression.
     * First delegates the visitor to the inner argument, then applies itself.
     *
     * @param v the visitor to accept
     */
    @Override
    public void accept(Visitor v) {
        argument.accept(v);
        v.visit(this);
    }

    /**
     * Calculates the depth of the expression tree.
     * A function wrapper adds one level of depth to its argument.
     *
     * @return the depth of this expression
     */
    @Override
    public int countDepth() {
        return 1 + argument.countDepth();
    }

    /**
     * Counts the number of operations in the expression tree.
     * A function wrapper is considered as one operation.
     *
     * @return the number of operations
     */
    @Override
    public int countOps() {
        return 1 + argument.countOps();
    }

    /**
     * Counts the number of numerical values (leaves) in the expression.
     *
     * @return the number of operands (numeric values)
     */
    @Override
    public int countNbs() {
        return argument.countNbs();
    }

    /**
     * Returns a string representation of the function in standard notation.
     *
     * @return a string like "sin(x)" or "sqrt(3 + 5)"
     */
    @Override
    public String toString() {
        return functionName + "(" + argument + ")";
    }

    /**
     * Checks equality between this FunctionWrapper and another object.
     * Two function wrappers are equal if they have the same name and argument.
     *
     * @param obj the object to compare
     * @return true if both function name and argument match
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof FunctionWrapper(String name, Expression argument1))) return false;
        return functionName.equals(name) && argument.equals(argument1);
    }
}
