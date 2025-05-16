package calculator;

/**
 * Enumeration representing the different ways to display arithmetic expressions as strings.
 * These notations affect how operations and operands are ordered and grouped.
 *
 * <ul>
 *   <li>{@link #PREFIX} — The operator appears before its operands, e.g., {@code +(1,2)} or {@code + 1 2}</li>
 *   <li>{@link #INFIX} — The operator appears between its operands, e.g., {@code 1 + 2}</li>
 *   <li>{@link #POSTFIX} — The operator appears after its operands, e.g., {@code (1,2)+} or {@code 1 2 +}</li>
 * </ul>
 *
 * These notations are used when converting an expression to a string via {@code toString(Notation)}.
 */
public enum Notation {

  /**
   * Prefix notation, e.g., "+(1,2)" or "+ 1 2"
   */
  PREFIX,

  /**
   * Infix notation, e.g., "1+2"
   */
  INFIX,

  /**
   * Postfix notation, e.g., "(1,2)+" or "1 2 +"
   */
  POSTFIX
}
