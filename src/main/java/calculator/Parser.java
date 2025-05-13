package calculator;

import java.util.*;
import java.util.regex.*;

public class Parser {

    private static final Set<String> OPERATORS = Set.of("+", "-", "*", "/");
    private static final Map<String, Integer> PRECEDENCE = Map.of(
            "+", 1, "-", 1,
            "*", 2, "/", 2
    );

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
        // Motif regex amélioré pour reconnaître différents formats de nombres complexes
        Matcher m = Pattern.compile("-?\\d+(\\.\\d+|/\\d+)?i" +
                        "|-?\\d+(\\.\\d+|/\\d+)?[+-](\\d+(\\.\\d+|/\\d+)?)?i|-?i" +
                        "|\\d+\\.\\d+" +
                        "|\\d+/\\d+" +
                        "|\\d+" +
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
            } else if (OPERATORS.contains(token)) {
                while (!ops.isEmpty() && OPERATORS.contains(ops.peek()) &&
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
                // Cas comme "5/4i" ou "-5/4i"
                if (token.contains("/")) {
                    String[] temp = token.split("/");
                    RationalNumber imagPart = new RationalNumber(
                            new RealNumber(Double.parseDouble(temp[0])),
                            new RealNumber(Double.parseDouble(temp[1].replace("i", ""))));

                    stack.push(new ComplexNumber(
                            realPart,
                            imagPart)
                    );
                }
                // Cas comme "4i" ou "3.5i"
                else {
                    RationalNumber imagPart = new RationalNumber(new RealNumber(Double.parseDouble(token.replace("i", ""))));
                    stack.push(new ComplexNumber(
                            realPart,
                            imagPart)
                    );
                }

            } else if (token.matches("-?\\d+(\\.\\d+|/\\d+)?[+-](\\d+(\\.\\d+|/\\d+)?)?i")) {
                // Cas plus complexes comme "-2+3i", "2.5-3.2i", "-1/5+2/3i"
                String realString = "";
                String imagString = "";

                if (token.contains("+")) {
                    // Format "2+3i" ou "-2.6+3/2i
                    String[] parts = token.split("\\+");
                    realString = parts[0];
                    imagString = parts[1];
                } else if (token.contains("-")) {
                    String[] parts = token.split("-");
                    if (token.length() - token.replace("-", "").length() == 2) {
                        realString = "-" + parts[0];
                    } else {
                        realString = parts[0];
                    }
                    imagString = "-" + parts[1];
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

                stack.push(new ComplexNumber(realPart, imagPart));
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
            } else {
                throw new IllegalArgumentException("Unable to parse this token: " + token);
            }
        }

        return stack.pop();
    }

    private static MyNumber parseNumber(String token) {
        if (token.contains("/")) {
            String[] parts = token.split("/");
            RealNumber num = new RealNumber(Double.parseDouble(parts[0]));
            RealNumber den = new RealNumber(Double.parseDouble(parts[1]));
            return new RationalNumber(num, den);
        } else {
            return new RealNumber(Double.parseDouble(token));
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
         return input.length() - input.replace("(", "").length() == input.length() - input.replace(")", "").length();
    }

    private static boolean checkOperatorsOk(String input) throws IllegalArgumentException {
        String expr = input.replaceAll("\\s+", "");
        if (expr.charAt(0) == '+' || expr.charAt(0) == '/' || expr.charAt(0) == '*')
            throw new IllegalArgumentException("Illegal operator begins the expression: " + expr.charAt(0));

        for (int i = 0; i < expr.length() - 1; i++) {
            char current = expr.charAt(i);
            char next = expr.charAt(i + 1);

            if (OPERATORS.contains(String.valueOf(current)) &&
                    OPERATORS.contains(String.valueOf(next))) {
                // On autorise seulement "/-" et "*-"
                if (!(next == '-' && (current == '*' || current == '/'))) {
                    return false;
                }
            }
        }
        return true;
    }

    private static boolean hasAtLeastOneNumber(String input) {
        return input.replace("\\s+", "").matches(".*\\d+.*|.*i.*");
    }
}

