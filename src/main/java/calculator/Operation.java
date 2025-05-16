package calculator;

import visitor.CountVisitor;
import visitor.OutputVisitor;
import visitor.Visitor;

import java.util.ArrayList;
import java.util.List;

/**
 * {@code Operation} is an abstract class that represents an arithmetic operation
 * such as addition, subtraction, multiplication, or division.
 * Each operation works on a list of sub-expressions (its arguments) and
 * supports different notations (prefix, infix, postfix).
 *
 * <p>This class implements the {@link Expression} interface and is the base for concrete operations like
 * {@link Plus}, {@link Minus}, {@link Times}, and {@link Divides}.
 *
 * @see Expression
 * @see MyNumber
 */
public abstract class Operation implements Expression {

	/** List of argument expressions used in this operation */
	public List<Expression> args;

	/** Symbol representing this operation (e.g., "+", "-", "*", "/") */
	protected String symbol;

	/** The neutral element for this operation (e.g., 0 for addition, 1 for multiplication) */
	protected int neutral;

	/** The notation used for string representation (default: INFIX) */
	public Notation notation = Notation.INFIX;

	/**
	 * Constructs an operation with a list of expressions as arguments.
	 * Default notation (INFIX) is used if none is specified.
	 *
	 * @param elist the list of argument expressions
	 * @throws IllegalConstruction if the list is null
	 */
	protected Operation(List<Expression> elist) throws IllegalConstruction {
		this(elist, null);
	}

	/**
	 * Constructs an operation with a list of expressions and a specified notation.
	 *
	 * @param elist the list of argument expressions
	 * @param n the notation to use (INFIX, PREFIX, POSTFIX)
	 * @throws IllegalConstruction if the list is null
	 */
	protected Operation(List<Expression> elist, Notation n) throws IllegalConstruction {
		if (elist == null) {
			throw new IllegalConstruction("Operation cannot be constructed with a null list of expressions");
		}
		this.args = new ArrayList<>(elist);
		if (n != null) this.notation = n;
	}

	/**
	 * Returns the list of argument expressions.
	 *
	 * @return list of expressions
	 */
	public List<Expression> getArgs() {
		return args;
	}

	/**
	 * Returns the operation symbol (e.g., "+", "-", "*", "/").
	 *
	 * @return the operation symbol
	 */
	public String getSymbol() {
		return symbol;
	}

	/**
	 * Adds more expressions to the argument list.
	 *
	 * @param params the list of expressions to add
	 */
	public void addMoreParams(List<Expression> params) {
		args.addAll(params);
	}

	/**
	 * Implements the Visitor pattern: delegates visiting to each argument, then visits itself.
	 *
	 * @param v the visitor object
	 */
	@Override
	public void accept(Visitor v) {
		for (Expression a : args) {
			a.accept(v);
		}
		v.visit(this);
	}

	/**
	 * Returns the depth of this expression.
	 *
	 * @return the maximum level of nested operations
	 */
	@Override
	public final int countDepth() {
		CountVisitor v = new CountVisitor();
		v.visit(this);
		return v.getDepthCount();
	}

	/**
	 * Counts the number of operations in this expression tree.
	 *
	 * @return number of operations
	 */
	@Override
	public final int countOps() {
		CountVisitor v = new CountVisitor();
		v.visit(this);
		return v.getOpsCount();
	}

	/**
	 * Counts the number of numeric operands (leaves) in this expression tree.
	 *
	 * @return number of numbers
	 */
	@Override
	public final int countNbs() {
		CountVisitor v = new CountVisitor();
		v.visit(this);
		return v.getNbCount();
	}

	/**
	 * Converts the operation to a string using the default notation.
	 *
	 * @return string representation of the operation
	 */
	@Override
	public final String toString() {
		return toString(notation);
	}

	/**
	 * Converts the operation to a string using the specified notation.
	 *
	 * @param n the notation to use (INFIX, PREFIX, POSTFIX)
	 * @return string representation of the operation
	 */
	public final String toString(Notation n) {
		OutputVisitor outputVisitor = new OutputVisitor();
		accept(outputVisitor);
		return outputVisitor.getOutput();
	}

	/**
	 * Defines the actual binary arithmetic operation.
	 * This method must be implemented by subclasses.
	 *
	 * @param l left operand
	 * @param r right operand
	 * @return result of the binary operation
	 */
	public abstract int op(int l, int r);

	/**
	 * Computes the result of the operation by evaluating each argument in order.
	 * Applies the binary operator from left to right.
	 *
	 * @param evaluatedArgs list of already-evaluated expressions
	 * @return the resulting value
	 * @throws IllegalConstruction if arguments are invalid
	 */
	public MyNumber compute(List<Expression> evaluatedArgs) throws IllegalConstruction {
		if (evaluatedArgs.size() < 2) {
			return new RealNumber(0.0);  // default fallback
		}
		MyNumber result = (MyNumber) evaluatedArgs.getFirst();
		for (int i = 1; i < evaluatedArgs.size(); i++) {
			MyNumber next = (MyNumber) evaluatedArgs.get(i);
			result = compute(result, next);
		}
		return result;
	}

	/**
	 * Computes the result of applying the operation to two numeric operands.
	 * This method must be overridden by each concrete operation.
	 *
	 * @param left the left operand
	 * @param right the right operand
	 * @return the result of the operation
	 * @throws IllegalConstruction if the operation is invalid
	 */
	public abstract MyNumber compute(MyNumber left, MyNumber right) throws IllegalConstruction;

	/**
	 * Compares this operation with another object for equality.
	 * Operations are equal if they have the same class and argument list.
	 *
	 * @param o the object to compare
	 * @return true if both represent the same operation
	 */
	@Override
	public boolean equals(Object o) {
		if (o == null) return false;
		if (this == o) return true;
		if (getClass() != o.getClass()) return false;

		Operation other = (Operation) o;
		return this.args.equals(other.getArgs());
	}

	/**
	 * Computes a hash code consistent with {@link #equals(Object)}.
	 *
	 * @return hash code for this operation
	 */
	@Override
	public int hashCode() {
		int result = 5;
		int prime = 31;
		result = prime * result + neutral;
		result = prime * result + symbol.hashCode();
		result = prime * result + args.hashCode();
		return result;
	}
}
