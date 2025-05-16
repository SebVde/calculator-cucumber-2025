package visitor;

import calculator.*;

import java.util.HashSet;
import java.util.Set;

/**
 * A visitor implementation that traverses an arithmetic expression tree
 * and counts:
 * <ul>
 *     <li>The number of distinct operations</li>
 *     <li>The number of distinct numbers</li>
 *     <li>The maximum nesting depth of operations</li>
 * </ul>
 */
public class CountVisitor extends Visitor {

    private int opsCount = 0;
    private int nbCount = 0;
    private int currentDepth = 0;
    private int maxDepth = 0;

    // Sets used to avoid double-counting in case of shared sub-expressions
    private final Set<MyNumber> countedNumbers = new HashSet<>();
    private final Set<Operation> countedOperations = new HashSet<>();

    /**
     * Visits a generic number and increments the count if not already seen.
     *
     * @param n the number to visit
     */
    @Override
    public void visit(MyNumber n) {
        if (!countedNumbers.contains(n)) {
            nbCount++;
            countedNumbers.add(n);
        }
    }

    /**
     * Visits a real number and delegates to the generic number visitor.
     *
     * @param n the real number
     */
    @Override
    public void visit(RealNumber n) {
        visit((MyNumber) n);
    }

    /**
     * Visits a rational number and delegates to the generic number visitor.
     *
     * @param n the rational number
     */
    @Override
    public void visit(RationalNumber n) {
        visit((MyNumber) n);
    }

    /**
     * Visits a complex number and delegates to the generic number visitor.
     *
     * @param n the complex number
     */
    @Override
    public void visit(ComplexNumber n) {
        visit((MyNumber) n);
    }

    /**
     * Visits an operation, increments the operation count if not already counted,
     * and recursively visits its arguments while tracking depth.
     *
     * @param o the operation to visit
     */
    @Override
    public void visit(Operation o) {
        if (!countedOperations.contains(o)) {
            opsCount++;
            countedOperations.add(o);
        }

        currentDepth++;
        maxDepth = Math.max(maxDepth, currentDepth);

        for (var arg : o.args) {
            arg.accept(this);
        }

        currentDepth--;
    }

    /**
     * Visits a function wrapper (e.g., sqrt) and treats it as an operation.
     *
     * @param f the function wrapper
     */
    @Override
    public void visit(FunctionWrapper f) {
        opsCount++; // Count the function as an operation
        currentDepth++;
        maxDepth = Math.max(maxDepth, currentDepth);
        f.argument().accept(this);
        currentDepth--;
    }

    /**
     * @return the total number of operations encountered
     */
    public int getOpsCount() {
        return opsCount;
    }

    /**
     * @return the maximum depth of nested operations
     */
    public int getDepthCount() {
        return maxDepth;
    }

    /**
     * @return the total number of numbers encountered
     */
    public int getNbCount() {
        return nbCount;
    }
}
