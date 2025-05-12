package calculator;

import visitor.Visitor;

/**
 * Représente une fonction mathématique à un seul argument (ex : sqrt(x), sin(x)).
 */
public class FunctionWrapper implements Expression {

    private final String functionName;
    private final Expression argument;

    public FunctionWrapper(String functionName, Expression argument) {
        this.functionName = functionName;
        this.argument = argument;
    }

    public String getFunctionName() {
        return functionName;
    }

    public Expression getArgument() {
        return argument;
    }

    @Override
    public void accept(Visitor v) {
        argument.accept(v);
        try {
            v.visit(this);
        } catch (IllegalConstruction e) {
            throw new RuntimeException(e);
        }
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

    @Override
    public int hashCode() {
        return functionName.hashCode() + 31 * argument.hashCode();
    }
}
