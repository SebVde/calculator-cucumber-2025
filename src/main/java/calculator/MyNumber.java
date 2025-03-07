package calculator;

import visitor.CountVisitor;
import visitor.Visitor;

import java.util.Objects;

/**
 * MyNumber is a concrete class that represents arithmetic numbers,
 * which are a special kind of Expressions, just like operations are.
 *
 * @see Expression
 * @see Operation
 */
public abstract class MyNumber implements Expression {

    /**
     * getter method to obtain the value contained in the object
     *
     * @return The integer number contained in the object
     */
    public abstract Integer getIntegerValue();

    public abstract Double getRealValue();

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

    /**
     * Two MyNumber expressions are equal if the values they contain are equal
     *
     * @param o The object to compare to
     * @return A boolean representing the result of the equality test
     */
    @Override
    public boolean equals(Object o) {
        // No object should be equal to null (not including this check can result in an exception if a MyNumber is tested against null)
        if (o == null) return false;

        // If the object is compared to itself then return true
        if (o == this) {
            return true;
        }

        //TODO Fix this method somehow

        // If the object is of another type then return false
        if (!(o instanceof MyNumber)) {
            return false;
        }
        return Objects.equals(this.getRealValue(), ((MyNumber) o).getRealValue());
        // Used == since the contained value is a primitive value
        // If it had been a Java object, .equals() would be needed

    }

    /**
     * The method hashCode needs to be overridden it the equals method is overridden;
     * otherwise there may be problems when you use your object in hashed collections
     * such as HashMap, HashSet, LinkedHashSet.
     *
     * @return The result of computing the hash.
     */
    @Override
    public int hashCode() {
        return getRealValue().hashCode();
    }

}
