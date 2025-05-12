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
        input = input.replace("π", String.valueOf(Math.PI));
        List<String> tokens = tokenize(input.replaceAll("\\s+", ""));
        List<String> postfix = infixToPostfix(tokens);
        return buildExpressionTree(postfix);
    }

    private static List<String> tokenize(String input) {
        List<String> tokens = new ArrayList<>();
        // Motif regex amélioré pour reconnaître différents formats de nombres complexes
        Matcher m = Pattern.compile("([+-]?\\d+(\\.\\d+)?(?:[Ee]-?\\d+)?(\\+|\\.|/?)\\d*(\\.\\d+)?i|\\d+(\\.\\d+)?i|\\d+\\.\\d+|\\d+/\\d+|\\d+|[+\\-*/()()]|sqrt|sin|cos|tan|i)")
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
            if (isNumber(token) || token.matches("\\d+(\\.\\d+)?i") || token.matches("\\d+(\\.\\d+)?(\\+|\\.|\\/?)\\d*(\\.\\d+)?i") || token.equals("i")) {
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

    /**
     * Regex for real numbers, it accepts numbers in the following form
     * 12 | 12.0 | 12. | +12 | -12 | 12E-2 | +12E2
     */
    private static final String realRegEx = "^[-+]?(?:\\d+(?:\\.\\d*)?|\\.\\d+)(?:[eE][-+]?\\d+)?$";
    /**
     * Regex for integers
     */
    private static final String intRegEx = "(\\d+([eE][-+])?\\d+)";

    /**
     * Regex for complex numbers, it accepts numbers in the following form
     * 2+3i | 2-3i | 2.6+3.7i | +2+3i | -2-3i | 2E-2+3E2i | +2E+2-3E-4i | 3.7i
     */
    private static final String complexRegEx = "([+-]?(?:\\d+\\.?\\d*|\\.\\d+)(?:[Ee][+-]?\\d+)?)" +
            "([+-](?:\\d+\\.?\\d*|\\.\\d+)(?:[Ee][+-]?\\d+)?)i|" +
            "([+-]?(?:\\d+\\.?\\d*|\\.\\d+)(?:[Ee][+-]?\\d+)?)i";

//    private static final String complexRegEx = "\\d+\\.\\d+([eE][-+])?\\d+|\\d+([eE][-+])?\\d+/\\d+([eE][-+])?\\d+|\\d+([eE][-+])?\\d+";
//
//    public static MyNumber parseNumbers(String s) throws IllegalConstruction, NumberFormatException {
//        if (s.matches(intRegEx)) {
//            return new MyNumber(Integer.parseInt(s));
//        } else if (s.matches(realRegEx)) {
//            return new MyNumber(Double.parseDouble(s));
//        }
//
//        Pattern pattern = Pattern.compile(complexRegEx);
//        Matcher matcher = pattern.matcher(s);
//
//        if (matcher.find()) {
//            double real = 0;
//            double imaginary = 0;
//
//            if (matcher.group(1) != null) {
//                real = Double.parseDouble(matcher.group(1));
//            } if (matcher.group(2) != null) {
//                imaginary = Double.parseDouble(matcher.group(2));
//            } if (matcher.group(3) != null) {
//                imaginary = Double.parseDouble(matcher.group(3));
//            }
//
//            return new MyNumber(real, imaginary);
//        } else {
//            throw new IllegalConstruction("Couldn't parse number");
//        }
//    }

    private static Expression buildExpressionTree(List<String> postfix) throws IllegalConstruction {
        Deque<Expression> stack = new ArrayDeque<>();

        for (String token : postfix) {
            if (isNumber(token)) {
                stack.push(parseNumber(token));
            } else if (token.equals("i")) {
                stack.push(new ComplexNumber(new RationalNumber(new RealNumber(0.0)), new RationalNumber(new RealNumber(1.0))));
            } else if (token.matches("\\d+(\\.\\d+)?i")) {
                // Cas comme "4i" ou "3.5i"
                double imagValue = Double.parseDouble(token.replace("i", ""));
                stack.push(new ComplexNumber(
                        new RationalNumber(new RealNumber(0.0)),
                        new RationalNumber(new RealNumber(imagValue))
                ));
            } else if (token.matches("\\d+(\\.\\d+)?(\\+|\\.|/?)\\d*(\\.\\d+)?i")) {
                // Cas plus complexes comme "2+3i", "2.5+3i", "2/3i"
                String realPart;
                String imagPart;
                if (token.contains("+")) {
                    // Format "2+3i"
                    String[] parts = token.split("\\+");
                    realPart = parts[0];
                    imagPart = parts[1].replace("i", "");
                } else if (token.contains("/")) {
                    // Format potentiel "2/3i"
                    realPart = "0";
                    imagPart = token.replace("i", "");
                } else {
                    // Format "2.5i"
                    realPart = "0";
                    imagPart = token.replace("i", "");
                }

                RationalNumber realNum = new RationalNumber(new RealNumber(Double.parseDouble(realPart)));
                RationalNumber imagNum = new RationalNumber(new RealNumber(Double.parseDouble(imagPart)));

                stack.push(new ComplexNumber(realNum, imagNum));
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
        return token.matches("\\d+\\.\\d+([eE][-+])?\\d+|\\d+([eE][-+])?\\d+/\\d+([eE][-+])?\\d+|\\d+([eE][-+])?\\d+");
    }
}

