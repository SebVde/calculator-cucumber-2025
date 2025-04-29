package calculator;

import visitor.CountVisitor;
import visitor.Visitor;

/**
 * MyNumber is a concrete class that represents arithmetic numbers,
 * which are a special kind of Expressions, just like operations are.
 *
 * @see Expression
 * @see Operation
 */
public abstract class MyNumber implements Expression {
    /**
     * accept method to implement the visitor design pattern to traverse arithmetic expressions.
     * Each number will pass itself to the visitor object to get processed by the visitor.
     *
     * @param v The visitor object
     */
    public void accept(Visitor v) {
        v.visit(this);
    }


    /**
     * The depth of a number expression is always 0
     *
     * @return The depth of a number expression
     */
    public int countDepth() {
        CountVisitor v = new CountVisitor();
        v.visit(this);
        return v.getDepthCount();
    }

    /**
     * The number of operations contained in a number expression is always 0
     *
     * @return The number of operations contained in a number expression
     */
    public int countOps() {
        CountVisitor v = new CountVisitor();
        v.visit(this);
        return v.getOpsCount();
    }

    /**
     * The number of numbers contained in a number expression is always 1
     *
     * @return The number of numbers contained in  a number expression
     */
    public int countNbs() {
        CountVisitor v = new CountVisitor();
        v.visit(this);
        return v.getNbCount();
    }

    public abstract MyNumber add(MyNumber other);

    public abstract MyNumber subtract(MyNumber other);

    public abstract MyNumber multiply(MyNumber other);

    public abstract MyNumber divide(MyNumber other);

}
