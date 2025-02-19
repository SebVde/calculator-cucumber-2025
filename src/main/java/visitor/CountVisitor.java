package visitor;

import calculator.Expression;
import calculator.MyNumber;
import calculator.Operation;

public class CountVisitor extends Visitor {
    private int opsCount = 0;
    private int nbCount = 0;
    private int currentDepth = 0;
    private int maxDepth = 0;

    @Override
    public void visit(MyNumber n) {
        nbCount++;
    }

    @Override
    public void visit(Operation o) {
        opsCount++;

        currentDepth++;
        maxDepth = Math.max(maxDepth, currentDepth);
        currentDepth--;
    }

    public int getOpsCount() {
        return opsCount;
    }

    public int getDepthCount() {
        return maxDepth;
    }

    public int getNbCount() {
        return nbCount;
    }
}
