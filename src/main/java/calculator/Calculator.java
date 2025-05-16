package calculator;

import visitor.Evaluator;

/**
 * This class represents the core logic of a Calculator.
 * It can be used to print and evaluate arithmetic expressions.
 * The calculator relies on the Visitor design pattern to evaluate expressions.
 *
 * @author tommens
 */
public class Calculator {

    /**
     * Evaluator instance used to compute the result of expressions.
     * The evaluator can be configured, e.g., to toggle between radians and degrees.
     */
    private Evaluator evaluator = new Evaluator(false);

    /**
     * Default constructor.
     * Currently, does not require any initialization.
     */
    public Calculator() {
        // No initialization needed for now
    }

    /**
     * Prints an arithmetic expression and its evaluated result.
     * @param e the arithmetic Expression to be printed and evaluated
     * @see #printExpressionDetails(Expression)
     */
    public void print(Expression e) {
        System.out.println("The result of evaluating expression " + e);
        System.out.println("is: " + eval(e) + ".");
        System.out.println();
    }

    /**
     * Prints detailed information about an arithmetic expression,
     * including its evaluation result, depth, number of operations, and number of operands.
     * @param e the arithmetic Expression to be analyzed and printed
     * @see #print(Expression)
     */
    public void printExpressionDetails(Expression e) {
        print(e);
        System.out.print("It contains " + e.countDepth() + " levels of nested expressions, ");
        System.out.print(e.countOps() + " operations");
        System.out.println(" and " + e.countNbs() + " numbers.");
        System.out.println();
    }

    /**
     * Evaluates an arithmetic expression using the configured Evaluator.
     * @param e the arithmetic Expression to be evaluated
     * @return The result of the evaluation
     */
    public Expression eval(Expression e) {
        e.accept(evaluator); // This triggers the visit of the expression by the evaluator
        return evaluator.getResult();
    }

    /**
     * Returns the Evaluator used by this Calculator.
     * @return the evaluator instance
     */
    public Evaluator getEvaluator() {
        return evaluator;
    }

    /**
     * Sets a new Evaluator to be used by this Calculator.
     * This can be useful for configuring different evaluation strategies.
     * @param eval the Evaluator to use
     */
    public void setEvaluator(Evaluator eval) {
        this.evaluator = eval;
    }

    /*
     * Potential additional methods for future implementation:
     * - A read method to parse a String input into an Expression, enabling a full REPL (Read-Eval-Print Loop):
     *   public Expression read(String s)
     *
     * - A validation method to verify syntactic correctness of expressions:
     *   public Boolean validate(Expression e)
     *
     * - A simplification method for algebraic expressions:
     *   public Expression simplify(Expression e)
     */
}
