package calculator;

import javafx.application.Application;
import ui.MainApp;
import java.util.Scanner;

/**
 * Entry point of the Calculator application.
 * Supports command-line options:
 * <ul>
 *   <li><code>--help</code>: display usage information</li>
 *   <li><code>--repl</code>: start REPL (interactive shell)</li>
 *   <li><code>--gui</code>: launch the JavaFX user interface</li>
 *   <li><code>--eval &lt;expression&gt;</code>: evaluate the given expression and print the result</li>
 * </ul>
 */
public class Main {

    /** Help message displayed for --help and on incorrect usage */
    private static final String USAGE = """
            Calculator - A simple calculator application

            Usage:
              java -jar calculator.jar [OPTION]

            Options:
              --help        Display this help message
              --repl        Start interactive REPL mode
              --gui         Launch graphical user interface
              --eval <expr> Evaluate the given expression

            If no option is provided, this help message is displayed.

            In REPL mode, you can type expressions for evaluation or special commands.

            Special commands:
              exit, quit - Exit the calculator
              help       - Show this help message

            Examples:
              --eval "2 + 3"
              --repl
              --gui
            """;

    /**
     * Main method to launch the calculator.
     * Interprets command-line arguments and dispatches accordingly.
     *
     * @param args command-line arguments
     */
    public static void main(String[] args) {
        if (args.length == 0) {
            displayHelp();
            return;
        }

        switch (args[0]) {
            case "--help" -> displayHelp();
            case "--repl" -> startRepl();
            case "--gui" -> Application.launch(MainApp.class, args); // Launches the JavaFX app
            case "--eval" -> {
                if (args.length < 2) {
                    System.err.println("Error: Missing expression after --eval");
                    System.exit(1);
                }
                // Combine all args after --eval to support multi-word expressions
                String expression = String.join(" ", java.util.Arrays.copyOfRange(args, 1, args.length));
                evaluateAndPrint(expression);
            }
            default -> {
                System.err.println("Error: Unknown option: " + args[0]);
                displayHelp();
                System.exit(1);
            }
        }
    }

    /**
     * Displays the help message.
     */
    private static void displayHelp() {
        System.out.println(USAGE);
    }

    /**
     * Evaluates the given expression string and prints the result.
     *
     * @param expression the string representing the arithmetic expression
     */
    private static void evaluateAndPrint(String expression) {
        try {
            Expression result = evaluateExpression(expression);
            System.out.println(result);
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            System.exit(1);
        }
    }

    /**
     * Starts an interactive REPL (Read-Eval-Print Loop) for evaluating expressions.
     * Accepts user input and evaluates expressions until 'exit' or 'quit' is typed.
     */
    private static void startRepl() {
        System.out.println("Calculator REPL started. Type 'help' for usage information or 'exit' to quit.");

        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.print("> ");
            String input = scanner.nextLine().trim();

            if (input.isEmpty()) continue;

            switch (input.toLowerCase()) {
                case "exit", "quit" -> {
                    System.out.println("Goodbye!");
                    return;
                }
                case "help" -> System.out.println(USAGE);
                default -> {
                    try {
                        Expression result = evaluateExpression(input);
                        System.out.println(result);
                    } catch (Exception e) {
                        System.out.println("Error: " + e.getMessage());
                    }
                }
            }
        }
    }

    /**
     * Parses and evaluates the given arithmetic expression string.
     *
     * @param expression the expression string to evaluate
     * @return the resulting Expression object after evaluation
     * @throws IllegalConstruction if the parsed expression is invalid
     */
    private static Expression evaluateExpression(String expression) throws IllegalConstruction {
        Calculator c = new Calculator();
        return c.eval(Parser.parse(expression.replaceAll("\\s+", ""), true));
    }
}
