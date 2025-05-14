package ui;

import calculator.*;

import java.util.*;
import java.util.regex.*;

public class FxExpressionParser {

    private static final Set<String> OPERATORS = Set.of("+", "-", "*", "/");
    private static final Map<String, Integer> PRECEDENCE = Map.of(
            "+", 1, "-", 1,
            "*", 2, "/", 2
    );
    private static final Set<String> FUNCTIONS = Set.of("sqrt", "sin", "cos", "tan");

    public static Expression parse(String input) throws IllegalConstruction {
        if (!hasAtLeastOneNumber(input))
            throw new IllegalArgumentException("No numbers in expression " + input);
        if (!areParenthesesEqual(input))
            throw new IllegalArgumentException("The opened and closed parentheses are not equal");
        if (!checkOperatorsOk(input))
            throw new IllegalArgumentException("Invalid operators sequence in expression " + input);

        input = input.replace("π", String.valueOf(Math.PI));
        List<String> tokens = tokenize(input.replaceAll("\\s+", ""));
        List<String> postfix = infixToPostfix(tokens);
        return buildExpressionTree(postfix);
    }

    private static List<String> tokenize(String input) {
        List<String> tokens = new ArrayList<>();
        Matcher m = Pattern.compile("-?\\d+(\\.\\d+|/\\d+)?i" +
                        "|-?\\d+(\\.\\d+|/\\d+)?[+-](\\d+(\\.\\d+|/\\d+)?)?i|-?i" +
                        "|\\d+\\.\\d+" +
                        "|\\d+/\\d+" +
                        "|\\d+" +
                        "|sqrt|sin|cos|tan" +
                        "|[+\\-*/()]")
                .matcher(input);
        while (m.find()) {
            tokens.add(m.group());
        }
        return tokens;
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
                if (!ops.isEmpty() && ops.peek().equals("(")) {
                    ops.pop();
                }
                if (!ops.isEmpty() && FUNCTIONS.contains(ops.peek())) {
                    output.add(ops.pop());
                }
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
                RationalNumber realPart = new RationalNumber(new RealNumber(0.0));
                String imag = token.replace("i", "");
                RationalNumber imagPart = imag.contains("/") ? parseRational(imag) : new RationalNumber(new RealNumber(Double.parseDouble(imag)));
                stack.push(new ComplexNumber(realPart, imagPart));
            } else if (token.matches("-?\\d+(\\.\\d+|/\\d+)?[+-](\\d+(\\.\\d+|/\\d+)?)?i")) {
                String[] parts = token.split("(?=[+-])", 2);
                RationalNumber real = parseRational(parts[0]);
                String imagStr = parts[1].replace("i", "");
                if (imagStr.isEmpty() || imagStr.equals("+")) imagStr = "1";
                if (imagStr.equals("-")) imagStr = "-1";
                RationalNumber imag = parseRational(imagStr);
                stack.push(new ComplexNumber(real, imag));
            } else if (isNumber(token)) {
                stack.push(parseNumber(token));
            } else if (OPERATORS.contains(token)) {
                Expression right = stack.pop();
                Expression left = stack.pop();
                List<Expression> args = List.of(left, right);
                switch (token) {
                    case "+" -> stack.push(new Plus(args));
                    case "-" -> stack.push(new Minus(args));
                    case "*" -> stack.push(new Times(args));
                    case "/" -> stack.push(new Divides(args));
                    default -> throw new IllegalArgumentException("Unknown operator: " + token);
                }
            } else if (FUNCTIONS.contains(token)) {
                Expression arg = stack.pop();
                stack.push(new FunctionWrapper(token, arg));
            }
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
        return token.matches("\\d+\\.\\d+|\\d+/\\d+|\\d+");
    }

    private static boolean isComplex(String token) {
        return token.matches("-?\\d+(\\.\\d+|/\\d+)?i" +
                "|-?\\d+(\\.\\d+|/\\d+)?[+-](\\d+(\\.\\d+|/\\d+)?)?i" +
                "|-?i");
    }

    private static boolean areParenthesesEqual(String input) {
        return input.chars().filter(ch -> ch == '(').count() == input.chars().filter(ch -> ch == ')').count();
    }

    private static boolean checkOperatorsOk(String input) {
        String expr = input.replaceAll("\\s+", "");
        if (expr.isEmpty()) return true;
        if (OPERATORS.contains(String.valueOf(expr.charAt(0))) && expr.charAt(0) != '-')
            return false;

        for (int i = 0; i < expr.length() - 1; i++) {
            char current = expr.charAt(i);
            char next = expr.charAt(i + 1);
            if (OPERATORS.contains(String.valueOf(current)) &&
                    OPERATORS.contains(String.valueOf(next)) && !(next == '-' && (current == '*' || current == '/'))) {
                    return false;
                }

        }
        return true;
    }

    private static boolean hasAtLeastOneNumber(String input) {
        return input.replaceAll("\\s+", "").matches(".*\\d+.*|.*i.*");
    }
}
