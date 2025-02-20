package visitor;

import calculator.Expression;
import calculator.MyNumber;
import calculator.Operation;

import java.util.stream.Stream;

public class OutputVisitor extends Visitor {
    public OutputVisitor() {}

    private String output;

    public String getOutput() {
        return output;
    }

    public void visit(MyNumber n) {
        output = Integer.toString(n.getValue());
    }

    public void visit(Operation o) {
        Stream<String> s = o.args.stream().map(Object::toString);
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
