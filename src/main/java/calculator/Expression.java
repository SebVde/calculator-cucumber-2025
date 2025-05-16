package calculator;

import visitor.Visitor;

/**
 * This interface represents any arithmetic expression that can be evaluated or analyzed.
 * It is implemented by two main categories of expressions:
 * <ul>
 *   <li>{@link MyNumber} — for literal values (real, rational, complex)</li>
 *   <li>{@link Operation} — for composite operations (e.g., addition, multiplication)</li>
 * </ul>
 *
 * Implementations of this interface must support traversal using the Visitor design pattern.
 *
 * @see Operation
 * @see MyNumber
 */
public interface Expression {

   /**
    * Accepts a visitor that will perform an operation on the expression.
    * This is a key part of the Visitor pattern implementation.
    *
    * @param v the visitor object that visits this expression
    */
   void accept(Visitor v);

   /**
    * Computes the depth of nested expressions within this expression.
    * For example, a flat sum has depth 1, while nested operations increase the depth.
    *
    * @return the depth level of the expression tree
    */
   int countDepth();

   /**
    * Counts the number of arithmetic operations contained in this expression,
    * including nested ones.
    *
    * @return the total number of operations
    */
   int countOps();

   /**
    * Counts the number of numerical values used in this expression,
    * including those inside nested subexpressions.
    *
    * @return the total number of numerical values (operands)
    */
   int countNbs();
}
