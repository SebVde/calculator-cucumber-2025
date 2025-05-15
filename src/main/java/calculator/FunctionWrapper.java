package calculator;

import visitor.Visitor;

/**
 * Représente une fonction mathématique à un seul argument (ex : sqrt(x), sin(x)).
 */
public record FunctionWrapper(String functionName, Expression argument) implements Expression {

    @Override
    public void accept(Visitor v) {
        argument.accept(v);
        v.visit(this);
    }

    @Override
    public int countDepth() {
        return 1 + argument.countDepth();
    }

    @Override
    public int countOps() {
        return 1 + argument.countOps();
    }

    @Override
    public int countNbs() {
        return argument.countNbs();
    }

    @Override
    public String toString() {
        return functionName + "(" + argument + ")";
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof FunctionWrapper other)) return false;
        return functionName.equals(other.functionName) && argument.equals(other.argument);
    }

}
