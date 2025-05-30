package calculator;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.Character.isDigit;

/**
 * Parser class to convert a string representation of a mathematical expression
 * into a structured {@link Expression} object, supporting infix, prefix, and postfix notation,
 * as well as real, rational, and complex number formats.
 */
public class Parser {
    private static final String COMPLEX_PATTERN = "-?\\d+(\\.\\d+|/-?\\d+)?i|-?\\d+(\\.\\d+|/-?\\d+)?[+-](\\d+(\\.\\d+|/-?\\d+)?)?i|-?i";
    private static final String REAL_PATTERN = "-?\\d*\\.\\d+";
    private static final String INTEGER_PATTERN = "-?\\d+";
    private static final String RATIONAL_PATTERN = "-?\\d+/-?\\d+";
    private static final String FUNCTION_PATTERN = "sqrt\\(([^()]*)\\)";
    private static final String ANY_NUMBER = String.format("(%s)|(%s)|(%s)|(%s)",
            COMPLEX_PATTERN,
            REAL_PATTERN,
            INTEGER_PATTERN,
            RATIONAL_PATTERN);


    /**
     * Parses a prefix notation expression string recursively.
     * For example, "+(1,2)" will be parsed as Plus(1,2).
     *
     * @param expression         the expression string
     * @param preserveFractions whether to preserve rational form
     * @return the constructed Expression tree
     * @throws IllegalConstruction if structure is invalid
     */
    public static Expression parse(String expression, boolean preserveFractions) throws IllegalConstruction {
        String cleaned = preprocessFunctions(expression).replaceAll("\\s+", "").replace("π", String.valueOf(Math.PI));
        if (cleaned.length() >= 6 && isOperator(cleaned.charAt(0)) && cleaned.charAt(1) == '('
                && cleaned.charAt(cleaned.length()-1) == ')') {
            return parsePrefixExpression(cleaned, preserveFractions);
        } else if (cleaned.length() >= 6 && isOperator(cleaned.charAt(cleaned.length()-1)) && cleaned.charAt(0) == '(') {
            return parsePostfixExpression(cleaned, preserveFractions);
        } else if (cleaned.charAt(0) == '(' || cleaned.charAt(0) == 'F' || cleaned.charAt(0) == '.' ||
                (isDigit(cleaned.charAt(0)) || cleaned.charAt(0) == 'i' ||
                        (cleaned.charAt(0) == '-' &&
                                (isDigit(cleaned.charAt(1)) || cleaned.charAt(1) == '(' || cleaned.charAt(1) == 'i' || cleaned.charAt(1) == 'F')) &&
                                (isDigit(cleaned.charAt(cleaned.length()-1)) || cleaned.charAt(cleaned.length()-1) == ')'
                                        || cleaned.charAt(cleaned.length()-1) == 'i' || cleaned.charAt(cleaned.length()-1) == '}' || cleaned.charAt(0) == '.'))) {
            return parseInfix(cleaned, preserveFractions);
        } else {
            throw new IllegalArgumentException("Unsupported notation type");
        }
    }

    private static Expression parseInfix(String expression, boolean preserveFractions) throws IllegalConstruction {
        List<Token> tokens = tokenizeInfix(expression);
        List<Token> postfixTokens = convertInfixToPostfix(tokens);

        Stack<Expression> stack = new Stack<>();

        for (Token token : postfixTokens) {
            switch (token.type) {
                case INTEGER:
                case REAL:
                case COMPLEX:
                case RATIONAL:
                    stack.push(createNumbers(token, preserveFractions));
                    break;

                case FUNCTION:
                    stack.push(new FunctionWrapper("sqrt", parse(token.value, preserveFractions)));
                    break;
                case OPERATOR:
                    if (stack.size() < 2) {
                        throw new IllegalArgumentException("Invalid postfix expression");
                    }
                    Expression right = stack.pop();
                    Expression left = stack.pop();
                    List<Expression> args = List.of(left, right);

                    switch (token.value) {
                        case "+" -> stack.push(new Plus(args));
                        case "-" -> stack.push(new Minus(args));
                        case "*" -> stack.push(new Times(args));
                        case "/" -> stack.push(new Divides(args));
                        default -> throw new IllegalArgumentException("Unknown operator: " + token.value);
                    }
                    break;

                default:
                    throw new IllegalArgumentException("Unexpected token type: " + token.type);
            }
        }

        if (stack.size() != 1) {
            throw new IllegalArgumentException("Invalid expression");
        }

        return stack.pop();
    }

    /**
     * Preprocesses function calls like sqrt(...) by converting them to a special FUNC{...} format.
     * This transformation simplifies parsing by allowing function arguments to be treated as tokens.
     *
     * @param expression the input string with potential functions
     * @return the transformed expression with function calls tagged as FUNC{...}
     */
    private static String preprocessFunctions(String expression) {
        Pattern pattern = Pattern.compile(FUNCTION_PATTERN);
        Matcher matcher = pattern.matcher(expression);
        StringBuilder sb = new StringBuilder();
        while (matcher.find()) {
            String inside = matcher.group(1);
            matcher.appendReplacement(sb, "FUNC{" + inside + "}");
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

    /**
     * Tokenizes an infix expression string into a list of {@link Token} objects,
     * while accounting for negative values, parentheses, and embedded functions.
     *
     * @param expression the raw mathematical expression in infix form
     * @return a list of typed tokens extracted from the input string
     */
    private static List<Token> tokenizeInfix(String expression) {
        List<Token> tokens = new ArrayList<>();
        List<String> parts = splitKeepNumbers(expression);

        TokenType previous = null;
        for (int i = 0; i<parts.size(); i++) {
            String part = parts.get(i);

            if (part.isEmpty()) {
                continue;
            }

            if (part.length() == 1 && isOperator(part.charAt(0))) {
                if (part.equals("-") && parts.get(i+1).equals("(")) {
                    if (i==0 || Objects.equals(previous, TokenType.OPERATOR) || previous.equals(TokenType.LEFT_PAREN)
                            || previous.equals(TokenType.FUNCTION)) {
                        tokens.add(new Token(TokenType.REAL, "-1"));
                        tokens.add(new Token(TokenType.OPERATOR, "*"));
                    } else {
                        tokens.add(new Token(TokenType.OPERATOR, "-"));
                    }
                } else {
                    tokens.add(new Token(TokenType.OPERATOR, part));
                }
                previous = TokenType.OPERATOR;
                continue;
            }

            if (part.equals("(")) {
                tokens.add(new Token(TokenType.LEFT_PAREN, part));
                previous = TokenType.LEFT_PAREN;
                continue;
            }

            if (part.equals(")")) {
                tokens.add(new Token(TokenType.RIGHT_PAREN, part));
                previous = TokenType.RIGHT_PAREN;
                continue;
            }

            if (part.charAt(0) == 'F') {
                tokens.add(new Token(TokenType.FUNCTION, part.substring(5, part.length() - 1)));
                previous = TokenType.FUNCTION;
                continue;
            }

            if (part.matches(COMPLEX_PATTERN)) {
                if (part.charAt(0) != '-' || (part.charAt(0) == '-' && (i == 0 || previous.equals(TokenType.OPERATOR)
                        || previous.equals(TokenType.LEFT_PAREN) || previous.equals(TokenType.FUNCTION)))) {
                    tokens.add(new Token(TokenType.COMPLEX, part));
                } else {
                    tokens.add(new Token(TokenType.OPERATOR, "-"));
                    tokens.add(new Token(TokenType.COMPLEX, part.substring(1)));
                }
                previous = TokenType.COMPLEX;
                continue;
            }

            if (part.matches(RATIONAL_PATTERN)) {
                if (part.charAt(0) != '-' || (part.charAt(0) == '-' && (i == 0 || previous.equals(TokenType.OPERATOR)
                        || previous.equals(TokenType.LEFT_PAREN) || previous.equals(TokenType.FUNCTION)))) {
                    tokens.add(new Token(TokenType.RATIONAL, part));
                } else {
                    tokens.add(new Token(TokenType.OPERATOR, "-"));
                    tokens.add(new Token(TokenType.RATIONAL, part.substring(1)));
                }
                previous = TokenType.RATIONAL;
                continue;
            }

            if (part.matches(REAL_PATTERN)) {
                if (part.charAt(0) != '-' || (part.charAt(0) == '-' && (i == 0 || previous.equals(TokenType.OPERATOR)
                        || previous.equals(TokenType.LEFT_PAREN) || previous.equals(TokenType.FUNCTION)))) {
                    tokens.add(new Token(TokenType.REAL, part));
                } else {
                    tokens.add(new Token(TokenType.OPERATOR, "-"));
                    tokens.add(new Token(TokenType.REAL, part.substring(1)));
                }
                previous = TokenType.REAL;
                continue;
            }

            if (part.matches(INTEGER_PATTERN)) {
                if (part.charAt(0) != '-' || (part.charAt(0) == '-' && (i == 0 || previous.equals(TokenType.OPERATOR)
                        || previous.equals(TokenType.LEFT_PAREN) || previous.equals(TokenType.FUNCTION)))) {
                    tokens.add(new Token(TokenType.INTEGER, part));
                } else {
                    tokens.add(new Token(TokenType.OPERATOR, "-"));
                    tokens.add(new Token(TokenType.INTEGER, part.substring(1)));
                }
                previous = TokenType.INTEGER;
                continue;
            }

            throw new IllegalArgumentException("Unexpected token: " + part);
        }

        return tokens;
    }

    /**
     * Splits the input string into meaningful parts while preserving number formats and operators.
     *
     * @param expression the original expression string
     * @return a list of string parts including numbers, operators, and parentheses
     */
    private static List<String> splitKeepNumbers(String expression) {
        List<String> result = new ArrayList<>();
        Pattern pattern = Pattern.compile("FUNC\\{[^}]+}|-?\\d+/\\d+|" + ANY_NUMBER + "|[+\\-*/()]");
        Matcher matcher = pattern.matcher(expression);

        int lastEnd = 0;
        while (matcher.find()) {
            if (matcher.start() > lastEnd) {
                result.add(expression.substring(lastEnd, matcher.start()));
            }

            result.add(matcher.group());

            lastEnd = matcher.end();
        }

        if (lastEnd < expression.length()) {
            result.add(expression.substring(lastEnd));
        }

        return result;
    }

    /**
     * Converts a list of infix tokens to postfix notation using the Shunting Yard algorithm.
     * This step is required to transform human-readable infix expressions into a format
     * easier to evaluate with a stack-based approach.
     *
     * @param infixTokens the list of tokens in infix order
     * @return a list of tokens in postfix order
     * @throws IllegalArgumentException if parentheses are mismatched
     */

    private static List<Token> convertInfixToPostfix(List<Token> infixTokens) {
        List<Token> postfixTokens = new ArrayList<>();
        Stack<Token> operatorStack = new Stack<>();

        for (Token token : infixTokens) {
            switch (token.type) {
                case INTEGER:
                case REAL:
                case COMPLEX:
                case RATIONAL:
                case FUNCTION:
                    postfixTokens.add(token);
                    break;

                case LEFT_PAREN:
                    operatorStack.push(token);
                    break;

                case RIGHT_PAREN:
                    while (!operatorStack.isEmpty() && operatorStack.peek().type != TokenType.LEFT_PAREN) {
                        postfixTokens.add(operatorStack.pop());
                    }

                    if (!operatorStack.isEmpty() && operatorStack.peek().type == TokenType.LEFT_PAREN) {
                        operatorStack.pop();
                    } else {
                        throw new IllegalArgumentException("Mismatched parentheses");
                    }
                    break;

                case OPERATOR:
                    while (!operatorStack.isEmpty() &&
                            operatorStack.peek().type == TokenType.OPERATOR &&
                            getPrecedence(operatorStack.peek().value.charAt(0)) >= getPrecedence(token.value.charAt(0))) {
                        postfixTokens.add(operatorStack.pop());
                    }
                    operatorStack.push(token);

                    break;

                default:
                    throw new IllegalArgumentException("Unexpected token type: " + token.type);
            }
        }

        while (!operatorStack.isEmpty()) {
            if (operatorStack.peek().type == TokenType.LEFT_PAREN) {
                throw new IllegalArgumentException("Mismatched parentheses");
            }
            postfixTokens.add(operatorStack.pop());
        }

        return postfixTokens;
    }

    /**
     * Parses a prefix notation expression string recursively.
     * For example, "+(1,2)" will be parsed as Plus(1,2).
     *
     * @param expression         the expression string
     * @param preserveFractions whether to preserve rational form
     * @return the constructed Expression tree
     * @throws IllegalConstruction if structure is invalid
     */
    private static Expression parsePrefixExpression(String expression, boolean preserveFractions) throws IllegalConstruction {
        // Base case if the expression is a simple number
        if (expression.matches(COMPLEX_PATTERN)) {
            return createNumberFromString(expression, TokenType.COMPLEX, preserveFractions);
        } else if (expression.matches(RATIONAL_PATTERN)) {
            return createNumberFromString(expression, TokenType.RATIONAL, preserveFractions);
        } else if (expression.matches(REAL_PATTERN)) {
            return createNumberFromString(expression, TokenType.REAL, preserveFractions);
        } else if (expression.matches(INTEGER_PATTERN)) {
            return createNumberFromString(expression, TokenType.INTEGER, preserveFractions);
        }

        char operator = expression.charAt(0);

        // Extract the arguments inside the parentheses
        String argsString = expression.substring(2, expression.length() - 1);
        List<String> args = splitArguments(argsString);

        // Parse each argument recursively
        List<Expression> parsedArgs = new ArrayList<>();
        for (String arg : args) {
            parsedArgs.add(parsePrefixExpression(arg, preserveFractions));
        }

        // Create the appropriate expression based on the operator
        return createOperatorExpression(operator, parsedArgs);
    }

    /**
     * Parses a postfix notation expression string recursively.
     * For example, "(1,2)+" will be parsed as Plus(1,2).
     *
     * @param expression         the expression string
     * @param preserveFractions whether to preserve rational form
     * @return the constructed Expression tree
     * @throws IllegalConstruction if structure is invalid
     */
    private static Expression parsePostfixExpression(String expression, boolean preserveFractions) throws IllegalConstruction {
        // Base case if the expression is a simple number
        if (expression.matches(COMPLEX_PATTERN)) {
            return createNumberFromString(expression, TokenType.COMPLEX, preserveFractions);
        } else if (expression.matches(RATIONAL_PATTERN)) {
            return createNumberFromString(expression, TokenType.RATIONAL, preserveFractions);
        } else if (expression.matches(REAL_PATTERN)) {
            return createNumberFromString(expression, TokenType.REAL, preserveFractions);
        } else if (expression.matches(INTEGER_PATTERN)) {
            return createNumberFromString(expression, TokenType.INTEGER, preserveFractions);
        }

        char operator = expression.charAt(expression.length() - 1);

        // Extract the arguments inside the parentheses
        String argsString = expression.substring(1, expression.length() - 2);
        List<String> args = splitArguments(argsString);

        // Parse each argument recursively
        List<Expression> parsedArgs = new ArrayList<>();
        for (String arg : args) {
            parsedArgs.add(parsePostfixExpression(arg, preserveFractions));
        }

        // Create the appropriate expression based on the operator
        return createOperatorExpression(operator, parsedArgs);

    }

    /**
     * Splits a string of comma-separated expressions, respecting nested parentheses.
     *
     * @param argsString the argument string
     * @return list of argument substrings
     */
    private static List<String> splitArguments(String argsString) {
        List<String> args = new ArrayList<>();
        int depth = 0;
        StringBuilder currentArg = new StringBuilder();

        for (int i = 0; i < argsString.length(); i++) {
            char c = argsString.charAt(i);

            if (c == '(') {
                depth++;
                currentArg.append(c);
            } else if (c == ')') {
                depth--;
                currentArg.append(c);
            } else if (c == ',' && depth == 0) {
                // Only split at top-level commas
                args.add(currentArg.toString());
                currentArg = new StringBuilder();
            } else {
                currentArg.append(c);
            }
        }

        // Add the last argument
        if (!currentArg.isEmpty()) {
            args.add(currentArg.toString());
        }

        return args;
    }

    /**
     * Creates an arithmetic expression from a given operator and list of sub-expressions.
     *
     * @param operator the character representing the operator
     * @param args the list of operand expressions
     * @return the constructed Expression
     * @throws IllegalConstruction if the operation cannot be created
     */
    private static Expression createOperatorExpression(char operator, List<Expression> args) throws IllegalConstruction {
        return switch (operator) {
            case '+' -> new Plus(args);
            case '-' -> new Minus(args);
            case '*' -> new Times(args);
            case '/' -> new Divides(args);
            default -> throw new IllegalArgumentException("Unknown operator: " + operator);
        };
    }

    /**
     * Creates a numeric expression from a string value and its token type.
     *
     * @param value the string value of the number
     * @param type the token type (INTEGER, REAL, RATIONAL, COMPLEX)
     * @param preserveFractions whether to preserve fraction representation
     * @return the created {@link Expression} node
     */
    private static Expression createNumberFromString(String value, TokenType type, boolean preserveFractions) {
        Token token = new Token(type, value);
        return createNumbers(token, preserveFractions);
    }

    /**
     * Creates a numerical {@link Expression} from a given {@link Token}, based on its {@link TokenType}.
     * Handles integers, real numbers, rationals, and various formats of complex numbers.
     *
     * @param token the token representing the number
     * @param preserveFractions whether to preserve rational numbers or convert to divisions
     * @return the corresponding Expression (RealNumber, RationalNumber, or ComplexNumber)
     * @throws IllegalArgumentException if the token value is invalid or unsupported
     */
    private static Expression createNumbers(Token token, boolean preserveFractions) {
        switch (token.type) {
            case INTEGER, REAL:
                return new RealNumber(Double.parseDouble(token.value));
            case COMPLEX:
                String value = token.value;
                if (value.equals("i")) {
                    return new ComplexNumber(new RationalNumber(new RealNumber(0.0)), new RationalNumber(new RealNumber(1.0)));
                } else if (value.equals("-i")) {
                    return new ComplexNumber(new RationalNumber(new RealNumber(0.0)), new RationalNumber(new RealNumber(-1.0)));
                } else if (value.matches("-?\\d+(\\.\\d+|/-?\\d+)?i")) {
                    RationalNumber realPart = new RationalNumber(new RealNumber(0.0));
                    // Case like "5/4i" or "-5/4i"
                    if (value.contains("/")) {
                        String[] temp = value.split("/");
                        RationalNumber imagPart = new RationalNumber(
                                new RealNumber(Double.parseDouble(temp[0])),
                                new RealNumber(Double.parseDouble(temp[1].replace("i", ""))));

                        return new ComplexNumber(realPart, imagPart);
                    }
                    // Case like "4i" or "3.5i"
                    else {
                        RationalNumber imagPart = new RationalNumber(new RealNumber(Double.parseDouble(value.replace("i", ""))));
                        return new ComplexNumber(realPart, imagPart);
                    }

                } else if (value.matches("-?\\d+(\\.\\d+|/-?\\d+)?[+-](\\d+(\\.\\d+|/-?\\d+)?)?i")) {
                    // More complex cases like "-2+3i", "2.5-3.2i", "-1/5+2/3i"
                    String realString = "";
                    String imagString = "";

                    if (value.contains("+")) {
                        // Format "2+3i" or "-2.6+3/2i
                        String[] parts = value.split("\\+");
                        realString = parts[0];
                        imagString = parts[1];
                    } else if (value.contains("-")) {
                        String[] parts = value.split("-");
                        if (value.length() - value.replace("-", "").length() == 2) {
                            realString = "-" + parts[1];
                            imagString = "-" + parts[2];
                        } else {
                            realString = parts[0];
                            imagString = "-" + parts[1];
                        }
                    }

                    imagString = imagString.replace("i", "");
                    if (imagString.isEmpty()) { imagString = "1"; }

                    RationalNumber realPart;
                    RationalNumber imagPart;

                    if (realString.contains("/")) {
                        String[] temp = realString.split("/");
                        realPart = new RationalNumber(
                                new RealNumber(Double.parseDouble(temp[0])),
                                new RealNumber(Double.parseDouble(temp[1])));

                    } else {
                        realPart = new RationalNumber(new RealNumber(Double.parseDouble(realString)));
                    }

                    if (imagString.contains("/")) {
                        String[] temp = imagString.split("/");
                        imagPart = new RationalNumber(
                                new RealNumber(Double.parseDouble(temp[0])),
                                new RealNumber(Double.parseDouble(temp[1])));

                    } else {
                        imagPart = new RationalNumber(new RealNumber(Double.parseDouble(imagString)));
                    }

                    return new ComplexNumber(realPart, imagPart);
                } else
                    throw new IllegalArgumentException("Unable to create complex from token " + value);

            case RATIONAL:
                String[] parts = token.value.split("/");
                RealNumber num = new RealNumber(Double.parseDouble(parts[0]));
                RealNumber den = new RealNumber(Double.parseDouble(parts[1]));
                if (preserveFractions) {
                    return new RationalNumber(num, den).simplify();
                } else {
                    try {
                        return new Divides(List.of(num, den));
                    } catch (IllegalConstruction e) {
                        throw new IllegalArgumentException(e);
                    }
                }

            default:
                throw new IllegalArgumentException("Unexpected token type: " + token.type);
        }
    }

    /**
     * Determines whether the given character is a valid binary operator.
     *
     * @param c the character to check
     * @return true if the character is one of +, -, *, /
     */
    private static boolean isOperator(char c) {
        return c == '+' || c == '-' || c == '*' || c == '/';
    }

    /**
     * Returns the precedence of the given operator character.
     *
     * @param operator the operator character
     * @return the precedence level (higher means stronger binding)
     */
    private static int getPrecedence(char operator) {
        return switch (operator) {
            case '+', '-' -> 1;
            case '*', '/' -> 2;
            default -> 0;
        };
    }

    /**
     * Token type enumeration for recognizing parts of an expression.
     */
    private enum TokenType {
        INTEGER, REAL, COMPLEX, RATIONAL, OPERATOR, LEFT_PAREN, RIGHT_PAREN, FUNCTION
    }

    /**
     * Represents a token with type and value extracted from the input expression.
     */
    private static class Token {
        TokenType type;
        String value;

        Token(TokenType type, String value) {
            this.type = type;
            this.value = value;
        }

        @Override
        public String toString() {
            return "Token{" +
                    "type=" + type +
                    ", value='" + value + '\'' +
                    '}';
        }
    }
}