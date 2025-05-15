package calculator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Main {

    public static void main(String[] args) {

        Expression e;
        Calculator c = new Calculator();

        try {
            // Example with a single RealNumber
            e = new RealNumber(8.0);
            c.print(e);
            c.eval(e);

            // Example with addition (Plus)
            List<Expression> params = new ArrayList<>();
            Collections.addAll(params, new RealNumber(3.0), new RealNumber(4.0), new RealNumber(5.0));
            e = new Plus(params, Notation.PREFIX);
            c.printExpressionDetails(e);
            c.eval(e);

            // Example with subtraction (Minus)
            List<Expression> params2 = new ArrayList<>();
            Collections.addAll(params2, new RealNumber(5.0), new RealNumber(3.0));
            e = new Minus(params2, Notation.INFIX);
            c.print(e);
            c.eval(e);

            // Example with multiplication (Times)
            List<Expression> params3 = new ArrayList<>();
            Collections.addAll(params3, new Plus(params), new Minus(params2));
            e = new Times(params3);
            c.printExpressionDetails(e);
            c.eval(e);

            // Example with division (Divides)
            List<Expression> params4 = new ArrayList<>();
            Collections.addAll(params4, new Plus(params), new Minus(params2), new RealNumber(5.0));
            e = new Divides(params4, Notation.POSTFIX);
            c.print(e);
            c.eval(e);

            // Example with RationalNumber
            RationalNumber rational1 = new RationalNumber(new RealNumber(3.0), new RealNumber(4.0));
            RationalNumber rational2 = new RationalNumber(new RealNumber(5.0), new RealNumber(6.0));
            List<Expression> rationalParams = new ArrayList<>();
            Collections.addAll(rationalParams, rational1, rational2);
            e = new Plus(rationalParams, Notation.INFIX);
            c.printExpressionDetails(e);
            c.eval(e);

            // Example with ComplexNumber
            ComplexNumber complex1 = new ComplexNumber(new RationalNumber(new RealNumber(2.0)), new RationalNumber(new RealNumber(3.0)));
            ComplexNumber complex2 = new ComplexNumber(new RationalNumber(new RealNumber(1.0)), new RationalNumber(new RealNumber(-1.0)));
            List<Expression> complexParams = new ArrayList<>();
            Collections.addAll(complexParams, complex1, complex2);
            e = new Times(complexParams);
            c.printExpressionDetails(e);
            c.eval(e);

            // Additional tests with RealNumber
            e = new Plus(List.of(new RealNumber(10.0), new RealNumber(-5.0)), Notation.INFIX);
            c.print(e);
            c.eval(e);

            e = new Minus(List.of(new RealNumber(15.0), new RealNumber(20.0)), Notation.PREFIX);
            c.print(e);
            c.eval(e);

            e = new Times(List.of(new RealNumber(2.0), new RealNumber(3.0), new RealNumber(4.0)), Notation.POSTFIX);
            c.print(e);
            c.eval(e);

            e = new Divides(List.of(new RealNumber(100.0), new RealNumber(5.0)), Notation.INFIX);
            c.print(e);
            c.eval(e);

            // Additional tests with RationalNumber
            RationalNumber rational3 = new RationalNumber(new RealNumber(28.0), new RealNumber(24.0));
            RationalNumber rational4 = new RationalNumber(new RealNumber(9.0), new RealNumber(12.0));
            e = new Plus(List.of(rational3, rational4), Notation.INFIX);
            c.printExpressionDetails(e);
            c.eval(e);

            e = new Minus(List.of(rational3, rational4), Notation.PREFIX);
            c.printExpressionDetails(e);
            c.eval(e);

            e = new Times(List.of(rational3, rational4), Notation.POSTFIX);
            c.printExpressionDetails(e);
            c.eval(e);

            e = new Divides(List.of(rational3, rational4), Notation.INFIX);
            c.printExpressionDetails(e);
            c.eval(e);

            // Additional tests with ComplexNumber
            ComplexNumber complex3 = new ComplexNumber(new RationalNumber(new RealNumber(3.0)), new RationalNumber(new RealNumber(4.0)));
            ComplexNumber complex4 = new ComplexNumber(new RationalNumber(new RealNumber(1.0)), new RationalNumber(new RealNumber(2.0)));
            e = new Plus(List.of(complex3, complex4), Notation.INFIX);
            c.printExpressionDetails(e);
            c.eval(e);

            e = new Minus(List.of(complex3, complex4), Notation.PREFIX);
            c.printExpressionDetails(e);
            c.eval(e);

            e = new Times(List.of(complex3, complex4), Notation.POSTFIX);
            c.printExpressionDetails(e);
            c.eval(e);

            e = new Divides(List.of(complex3, complex4), Notation.INFIX);
            c.printExpressionDetails(e);
            c.eval(e);

            // Test division of complex numbers
            ComplexNumber complex5 = new ComplexNumber(new RationalNumber(new RealNumber(3.0)), new RationalNumber(new RealNumber(4.0)));
            ComplexNumber complex6 = new ComplexNumber(new RationalNumber(new RealNumber(1.0)), new RationalNumber(new RealNumber(2.0)));
            e = new Divides(List.of(complex5, complex6), Notation.INFIX);
            c.printExpressionDetails(e);
            c.eval(e);

            ComplexNumber complex7 = new ComplexNumber(new RationalNumber(new RealNumber(3.45)), new RationalNumber(new RealNumber(4.01)));
            ComplexNumber complex8 = new ComplexNumber(new RationalNumber(new RealNumber(1.0)), new RationalNumber(new RealNumber(2.702)));
            e = new Plus(List.of(complex7, complex8), Notation.INFIX);
            c.printExpressionDetails(e);
            c.eval(e);

            RationalNumber complex9 = new RationalNumber(new RealNumber(5.0), new RealNumber(3.0));
            ComplexNumber complex10 = new ComplexNumber(new RationalNumber(new RealNumber(1.0)), new RationalNumber(new RealNumber(2.702)));
            e = new Plus(List.of(complex9, complex10), Notation.INFIX);
            c.printExpressionDetails(e);
            c.eval(e);

            RationalNumber complex11 = new RationalNumber(new RealNumber(5.0), new RealNumber(3.0));
            ComplexNumber complex12 = new ComplexNumber(new RationalNumber(new RealNumber(1.0)), new RationalNumber(new RealNumber(-1.0)));
            e = new Minus(List.of(complex12, complex11), Notation.INFIX);
            c.printExpressionDetails(e);
            c.eval(e);

            RationalNumber complex13 = new RationalNumber(new RealNumber(5.0), new RealNumber(3.0));
            ComplexNumber complex14 = new ComplexNumber(new RationalNumber(new RealNumber(1.0)), new RationalNumber(new RealNumber(-1.5)));
            e = new Times(List.of(complex13, complex14), Notation.INFIX);
            c.printExpressionDetails(e);
            c.eval(e);

            RealNumber complex15 = new RealNumber(3.0);
            ComplexNumber complex16 = new ComplexNumber(new RationalNumber(new RealNumber(2.0)), new RationalNumber(new RealNumber(3.0)));
            e = new Divides(List.of(complex16, complex15), Notation.INFIX);
            c.printExpressionDetails(e);
            c.eval(e);

            String inputPostfix = "((4,5,6)+,(7,(5,2,7)/)+,9)*";
            e = Parser.parse(inputPostfix);
            c.printExpressionDetails(e);
            c.eval(e);

            String inputPrefix = "*(+(4,5,6),+(7,/(5,2,7)),9)";
            e = Parser.parse(inputPrefix);
            c.printExpressionDetails(e);
            c.eval(e);

            String inputInfix = "(4 + 5 + 6) * (7 + (5 / 2 / 7)) * 9";
            e = Parser.parse(inputInfix);
            c.printExpressionDetails(e);
            c.eval(e);

        } catch (IllegalConstruction exception) {
            System.out.println("Cannot create operations without parameters");
        }
    }
}
