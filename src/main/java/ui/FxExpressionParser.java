package ui;

import calculator.*;

import java.util.*;
import java.util.regex.*;

public class FxExpressionParser {

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

    private enum TokenType {
        INTEGER, REAL, COMPLEX, RATIONAL, OPERATOR, LEFT_PAREN, RIGHT_PAREN, FUNCTION
    }

    private static class Token {
        TokenType type;
        String value;

        Token(TokenType type, String value) {
            this.type = type;
            this.value = value;
        }
    }

    public static Expression parse(String expression, boolean preserveFractions) throws IllegalConstruction {
        String cleaned = expression.replaceAll("\\s+", "").replace("π", String.valueOf(Math.PI));
        return parseInfix(cleaned, preserveFractions);
    }

    private static Expression parseInfix(String expression, boolean preserveFractions) throws IllegalConstruction {
        expression = preprocessFunctions(expression);
        List<Token> tokens = tokenizeInfix(expression);
        List<Token> postfix = toPostfix(tokens);
        Deque<Expression> stack = new ArrayDeque<>();

        for (Token token : postfix) {
            switch (token.type) {
                case INTEGER, REAL, RATIONAL, COMPLEX -> stack.push(createNumber(token, preserveFractions));
                case FUNCTION -> stack.push(new FunctionWrapper("sqrt", parse(token.value, preserveFractions)));
                case OPERATOR -> {
                    Expression b = stack.pop();
                    Expression a = stack.pop();
                    List<Expression> args = List.of(a, b);
                    stack.push(switch (token.value) {
                        case "+" -> new Plus(args);
                        case "-" -> new Minus(args);
                        case "*" -> new Times(args);
                        case "/" -> new Divides(args);
                        default -> throw new IllegalArgumentException("Unknown operator: " + token.value);
                    });
                }
                default -> throw new IllegalArgumentException("Unexpected token type: " + token.type);
            }
        }
        return stack.pop();
    }

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

    private static List<Token> tokenizeInfix(String input) {
        List<Token> tokens = new ArrayList<>();
        Pattern pattern = Pattern.compile("FUNC\\{[^}]+}|-?\\d+/\\d+|" + ANY_NUMBER + "|[+\\-*/()]");
        Matcher matcher = pattern.matcher(input);
        while (matcher.find()) {
            String part = matcher.group();
            if (part == null || part.isEmpty()) continue;
            Token token;
            if (part.equals("(") || part.equals(")")) {
                token = new Token(part.equals("(") ? TokenType.LEFT_PAREN : TokenType.RIGHT_PAREN, part);
            } else if (part.startsWith("FUNC{")) {
                token = new Token(TokenType.FUNCTION, part.substring(5, part.length() - 1));
            } else if (part.matches(COMPLEX_PATTERN)) {
                token = new Token(TokenType.COMPLEX, part);
            } else if (part.matches(RATIONAL_PATTERN)) {
                token = new Token(TokenType.RATIONAL, part);
            } else if (part.matches(REAL_PATTERN)) {
                token = new Token(TokenType.REAL, part);
            } else if (part.matches(INTEGER_PATTERN)) {
                token = new Token(TokenType.INTEGER, part);
            } else if ("+-*/".contains(part)) {
                token = new Token(TokenType.OPERATOR, part);
            } else {
                throw new IllegalArgumentException("Unknown token: " + part);
            }
            tokens.add(token);
        }
        return tokens;
    }

    private static List<Token> toPostfix(List<Token> infix) {
        List<Token> output = new ArrayList<>();
        Deque<Token> ops = new ArrayDeque<>();
        for (Token token : infix) {
            switch (token.type) {
                case INTEGER, REAL, RATIONAL, COMPLEX, FUNCTION -> output.add(token);
                case OPERATOR -> {
                    while (!ops.isEmpty() && precedence(ops.peek()) >= precedence(token)) {
                        output.add(ops.pop());
                    }
                    ops.push(token);
                }
                case LEFT_PAREN -> ops.push(token);
                case RIGHT_PAREN -> {
                    while (!ops.isEmpty() && ops.peek().type != TokenType.LEFT_PAREN) {
                        output.add(ops.pop());
                    }
                    if (!ops.isEmpty() && ops.peek().type == TokenType.LEFT_PAREN) {
                        ops.pop();
                    }
                }
            }
        }
        while (!ops.isEmpty()) output.add(ops.pop());
        return output;
    }

    private static int precedence(Token token) {
        return switch (token.value) {
            case "+", "-" -> 1;
            case "*", "/" -> 2;
            default -> 0;
        };
    }

    private static Expression createNumber(Token token, boolean preserveFractions) throws IllegalConstruction {
        return switch (token.type) {
            case INTEGER, REAL -> new RealNumber(Double.parseDouble(token.value));
            case RATIONAL -> {
                String[] parts = token.value.split("/");
                if (preserveFractions) {
                    yield new RationalNumber(
                            new RealNumber(Double.parseDouble(parts[0])),
                            new RealNumber(Double.parseDouble(parts[1]))
                    );
                } else {
                    yield new Divides(List.of(
                            new RealNumber(Double.parseDouble(parts[0])),
                            new RealNumber(Double.parseDouble(parts[1]))
                    ));
                }
            }
            case COMPLEX -> parseComplex(token.value);
            default -> throw new IllegalArgumentException("Not a number token: " + token.value);
        };
    }

    private static ComplexNumber parseComplex(String value) {
        if (value.equals("i")) return new ComplexNumber(new RationalNumber(new RealNumber(0.0)), new RationalNumber(new RealNumber(1.0)));
        if (value.equals("-i")) return new ComplexNumber(new RationalNumber(new RealNumber(0.0)), new RationalNumber(new RealNumber(-1.0)));

        if (value.matches("-?\\d+(\\.\\d+|/-?\\d+)?i")) {
            RationalNumber imag = value.contains("/") ? parseRational(value.replace("i", "")) : new RationalNumber(new RealNumber(Double.parseDouble(value.replace("i", ""))));
            return new ComplexNumber(new RationalNumber(new RealNumber(0.0)), imag);
        }

        String[] parts = value.split("(?=[+-])", 2);
        RationalNumber real = parseRational(parts[0]);
        String imagStr = parts[1].replace("i", "");
        if (imagStr.equals("+") || imagStr.isEmpty()) imagStr = "1";
        else if (imagStr.equals("-")) imagStr = "-1";
        RationalNumber imag = parseRational(imagStr);
        return new ComplexNumber(real, imag);
    }

    private static RationalNumber parseRational(String token) {
        if (token.contains("/")) {
            String[] parts = token.split("/");
            return new RationalNumber(new RealNumber(Double.parseDouble(parts[0])), new RealNumber(Double.parseDouble(parts[1])));
        } else {
            return new RationalNumber(new RealNumber(Double.parseDouble(token)));
        }
    }
}