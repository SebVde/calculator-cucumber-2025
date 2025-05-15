package visitor;

import calculator.*;

import java.util.stream.Stream;

public class OutputVisitor extends Visitor {

    private String output;

    public OutputVisitor() {}

    public String getOutput() {
        return output;
    }

    @Override
    public void visit(MyNumber n) {
        output = n.toString();
    }

    @Override
    public void visit(RealNumber n) {
        output = Double.toString(n.getValue());
    }

    @Override
    public void visit(RationalNumber n) {
        output = n.getNominator().toString() + "/" + n.getDenominator().toString();
    }

    @Override
    public void visit(ComplexNumber n) {
        String realPart = n.getRealPart().toString();
        String imaginaryPart = n.getImaginaryPart().toString();
        output = realPart + " + " + imaginaryPart + "i";
    }

    @Override
    public void visit(Operation o) {
        Stream<String> s = o.args.stream().map(arg -> {
            OutputVisitor subVisitor = new OutputVisitor();
            arg.accept(subVisitor);
            return subVisitor.getOutput();
        });
        output = switch (o.notation) {
            case INFIX -> "( " +
                    s.reduce((s1, s2) -> s1 + " " + o.getSymbol() + " " + s2).get() + " )";
            case PREFIX ->
                    o.getSymbol() + " " + "(" + s.reduce((s1, s2) -> s1 + ", " + s2).get() + ")";
            case POSTFIX -> "(" +
                    s.reduce((s1, s2) -> s1 + ", " + s2).get() + ")" + " " + o.getSymbol();
        };
    }

}
