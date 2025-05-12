package visitor;

import calculator.*;

/**
 * Visitor design pattern
 */
public abstract class Visitor {

    public abstract void visit(MyNumber n);

    public abstract void visit(RealNumber n);

    public abstract void visit(RationalNumber n);

    public abstract void visit(ComplexNumber n);

    public abstract void visit(Operation o);

    public abstract void visit(FunctionWrapper f) throws IllegalConstruction;

}
