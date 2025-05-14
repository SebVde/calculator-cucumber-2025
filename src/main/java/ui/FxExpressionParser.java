package ui;

import calculator.*;

import java.util.*;
import java.util.regex.*;

public class FxExpressionParser {

    private static final Set<String> OPERATORS = Set.of("+", "-", "*", "/");
    private static final Set<String> FUNCTIONS = Set.of("sqrt", "sin", "cos", "tan");
    private static final Map<String, Integer> PRECEDENCE = Map.of(
            "+", 1, "-", 1,
            "*", 2, "/", 2
    );

    public static Expression parse(String input) throws IllegalConstruction {
        input = input.replace("π", String.valueOf(Math.PI));

        if (!hasAtLeastOneNumber(input))
            throw new IllegalArgumentException("No numbers in expression: " + input);
        if (!areParenthesesEqual(input))
            throw new IllegalArgumentException("Mismatched parentheses in expression");
        if (!checkOperatorsOk(input))
            throw new IllegalArgumentException("Invalid operator sequence");

        List<String> tokens = tokenize(input.replaceAll("\\s+", ""));
        List<String> postfix = infixToPostfix(tokens);
        return buildExpressionTree(postfix);
    }

    private static List<String> tokenize(String input) {
        List<String> tokens = new ArrayList<>();
        Matcher m = Pattern.compile(
                "sqrt|sin|cos|tan|" +
                        "-?π|" +
                        "\\d+\\.\\d+|" +
                        "\\d+/\\d+|" +
                        "\\d+|" +
                        "\\d+(\\.\\d+)?i|" +
                        "i|" +
                        "\\d+(\\.\\d+)?[+-]\\d+(\\.\\d+)?i|" +
                        "[+\\-*/()]"
        ).matcher(input);
        while (m.find()) {
            tokens.add(m.group());
        }

        // Handle negative numbers and expressions
        return processUnaryMinus(tokens);
    }

    private static List<String> processUnaryMinus(List<String> tokens) {
        List<String> processed = new ArrayList<>();
        for (int i = 0; i < tokens.size(); i++) {
            String token = tokens.get(i);

            // Handle unary minus
            if (token.equals("-")) {
                // Check if this is a unary minus (at start or after an operator or open parenthesis)
                boolean isUnary = i == 0 ||
                        OPERATORS.contains(processed.getLast()) ||
                        processed.getLast().equals("(");

                if (isUnary && i + 1 < tokens.size()) {
                    String nextToken = tokens.get(i + 1);
                    // If next token is a number, combine with minus
                    if (nextToken.matches("\\d+(\\.\\d+)?|\\d+/\\d+|i|\\d+i|\\d+\\.\\d+i")) {
                        processed.add("-" + nextToken);
                        i++; // Skip the next token
                    } else if (nextToken.equals("(")) {
                        // Handle expressions like -(3+4)
                        processed.add("-1");
                        processed.add("*");
                        processed.add(nextToken);
                    } else {
                        processed.add(token);
                    }
                } else {
                    processed.add(token);
                }
            } else if (token.matches("\\d+(\\.\\d+)?[+-]\\d+(\\.\\d+)?i")) {
                // Handle complex numbers with both real and imaginary parts
                processed.add(token);
            } else {
                processed.add(token);
            }
        }
        return processed;
    }

    private static List<String> infixToPostfix(List<String> tokens) {
        List<String> output = new ArrayList<>();
        Deque<String> ops = new ArrayDeque<>();

        for (String token : tokens) {
            if (isNumber(token) || isComplex(token)) {
                output.add(token);
            } else if (FUNCTIONS.contains(token)) {
                ops.push(token);
            } else if (OPERATORS.contains(token)) {
                while (!ops.isEmpty() && (OPERATORS.contains(ops.peek()) || FUNCTIONS.contains(ops.peek())) &&
                        PRECEDENCE.getOrDefault(token, 0) <= PRECEDENCE.getOrDefault(ops.peek(), 0)) {
                    output.add(ops.pop());
                }
                ops.push(token);
            } else if (token.equals("(")) {
                ops.push(token);
            } else if (token.equals(")")) {
                while (!ops.isEmpty() && !ops.peek().equals("(")) {
                    output.add(ops.pop());
                }
                if (!ops.isEmpty() && ops.peek().equals("(")) ops.pop();
                if (!ops.isEmpty() && FUNCTIONS.contains(ops.peek())) output.add(ops.pop());
            }
        }

        while (!ops.isEmpty()) {
            output.add(ops.pop());
        }

        return output;
    }

    private static Expression buildExpressionTree(List<String> postfix) throws IllegalConstruction {
        Deque<Expression> stack = new ArrayDeque<>();
        for (String token : postfix) {
            if (token.equals("i")) {
                stack.push(new ComplexNumber(new RationalNumber(new RealNumber(0.0)), new RationalNumber(new RealNumber(1.0))));
            } else if (token.equals("-i")) {
                stack.push(new ComplexNumber(new RationalNumber(new RealNumber(0.0)), new RationalNumber(new RealNumber(-1.0))));
            } else if (token.matches("-?\\d+(\\.\\d+|/\\d+)?i")) {
                // Handle imaginary numbers like 2i, -3i
                RationalNumber realPart = new RationalNumber(new RealNumber(0.0));
                String imag = token.replace("i", "");
                RationalNumber imagPart = imag.contains("/") ? parseRational(imag) : new RationalNumber(new RealNumber(Double.parseDouble(imag)));
                stack.push(new ComplexNumber(realPart, imagPart));
            } else if (token.matches("-?\\d+(\\.\\d+|/\\d+)?[+-](\\d+(\\.\\d+|/\\d+)?)?i")) {
                // Handle complex numbers like 3+2i, -3+2i
                String[] parts;
                if (token.startsWith("-")) {
                    // Handle negative complex numbers correctly
                    String withoutMinus = token.substring(1);
                    if (withoutMinus.contains("+")) {
                        parts = withoutMinus.split("\\+", 2);
                        parts[0] = "-" + parts[0];
                        parts[1] = "+" + parts[1];
                    } else if (withoutMinus.contains("-")) {
                        parts = withoutMinus.split("-", 2);
                        parts[0] = "-" + parts[0];
                        parts[1] = "-" + parts[1];
                    } else {
                        parts = new String[]{ token };
                    }
                } else {
                    parts = token.split("(?=[+-])", 2);
                }

                RationalNumber real = parseRational(parts[0]);
                String imagStr = parts[1].replace("i", "");
                if (imagStr.equals("+") || imagStr.isEmpty()) {
                    imagStr = "1";
                } else if (imagStr.equals("-")) {
                    imagStr = "-1";
                }
                RationalNumber imag = parseRational(imagStr);
                stack.push(new ComplexNumber(real, imag));
            } else if (isNumber(token)) {
                stack.push(parseNumber(token));
            } else if (OPERATORS.contains(token)) {
                if (stack.size() < 2) {
                    throw new IllegalArgumentException("Invalid expression: not enough operands for operator " + token);
                }
                Expression b = stack.pop();
                Expression a = stack.pop();
                List<Expression> args = List.of(a, b);
                switch (token) {
                    case "+" -> stack.push(new Plus(args));
                    case "-" -> stack.push(new Minus(args));
                    case "*" -> stack.push(new Times(args));
                    case "/" -> stack.push(new Divides(args));
                    default -> throw new IllegalArgumentException("Unknown operator: " + token);
                }
            } else if (FUNCTIONS.contains(token)) {
                if (stack.isEmpty()) {
                    throw new IllegalArgumentException("Invalid expression: not enough arguments for function " + token);
                }
                Expression arg = stack.pop();
                stack.push(new FunctionWrapper(token, arg));
            }
        }

        if (stack.isEmpty()) {
            throw new IllegalArgumentException("Invalid expression: empty result");
        }

        return stack.pop();
    }

    private static MyNumber parseNumber(String token) {
        return parseRational(token);
    }

    private static RationalNumber parseRational(String token) {
        if (token.contains("/")) {
            String[] parts = token.split("/");
            return new RationalNumber(new RealNumber(Double.parseDouble(parts[0])), new RealNumber(Double.parseDouble(parts[1])));
        } else {
            return new RationalNumber(new RealNumber(Double.parseDouble(token)));
        }
    }

    private static boolean isNumber(String token) {
        return token.matches("-?\\d+\\.\\d+|-?\\d+/\\d+|-?\\d+");
    }

    private static boolean isComplex(String token) {
        return token.matches("-?\\d+(\\.\\d+|/\\d+)?i" +
                "|-?\\d+(\\.\\d+|/\\d+)?[+-](\\d+(\\.\\d+|/\\d+)?)?i" +
                "|-?i");
    }

    private static boolean hasAtLeastOneNumber(String input) {
        return input.replaceAll("\\s+", "").matches(".*\\d+.*|.*i.*");
    }

    private static boolean areParenthesesEqual(String input) {
        return input.chars().filter(c -> c == '(').count() == input.chars().filter(c -> c == ')').count();
    }

    private static boolean checkOperatorsOk(String input) {
        String expr = input.replaceAll("\\s+", "");
        if (expr.isEmpty()) return true;

        // Allow starting with minus (for negative numbers)
        if (OPERATORS.contains(String.valueOf(expr.charAt(0))) && expr.charAt(0) != '-')
            return false;

        // Check for consecutive operators except when handling negative numbers
        for (int i = 0; i < expr.length() - 1; i++) {
            char c = expr.charAt(i);
            char n = expr.charAt(i + 1);

            if (OPERATORS.contains("" + c) && OPERATORS.contains("" + n)) {
                // Allow combinations like "+-", "--", "*-", "/-" (for negative numbers)
                if (n == '-') continue;
                return false;
            }
        }

        // Check if expression ends with an operator
        return !OPERATORS.contains(String.valueOf(expr.charAt(expr.length() - 1)));
    }
}