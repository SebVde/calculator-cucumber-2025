package visitor;

import calculator.*;

import java.util.HashSet;
import java.util.Set;

public class CountVisitor extends Visitor {
    private int opsCount = 0;
    private int nbCount = 0;
    private int currentDepth = 0;
    private int maxDepth = 0;
    private final Set<MyNumber> countedNumbers = new HashSet<>(); // To avoid double-counting numbers
    private final Set<Operation> countedOperations = new HashSet<>(); // To avoid double-counting operations

    @Override
    public void visit(MyNumber n) {
        if (!countedNumbers.contains(n)) {
            nbCount++;
            countedNumbers.add(n);
        }
    }

    @Override
    public void visit(RealNumber n) {
        visit((MyNumber) n);
    }

    @Override
    public void visit(RationalNumber n) {
        visit((MyNumber) n);
    }

    @Override
    public void visit(ComplexNumber n) {
        visit((MyNumber) n);
    }

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
