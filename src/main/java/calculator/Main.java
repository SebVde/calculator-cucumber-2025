package calculator;

import ui.MainApp;
import java.util.Scanner;

public class Main {
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

    public static void main(String[] args) {
        if (args.length == 0) {
            displayHelp();
            return;
        }

        switch(args[0]) {
            case "--help" -> displayHelp();
            case "--repl" -> startRepl();
            case "--gui" -> MainApp.launch(MainApp.class, args); // Lance l'application JavaFX
            case "--eval" -> {
                if (args.length < 2) {
                    System.err.println("Error: Missing expression after --eval");
                    System.exit(1);
                }
                // Combine the rest of the args
                String expression = String.join(" ", java.util.Arrays.copyOfRange(args, 1, args.length));
//                System.out.print("[LOG] Evaluating: ");
//                System.out.println(expression);
                evaluateAndPrint(expression);
            }
            default -> {
                System.err.println("Error: Unknown option: " + args[0]);
                displayHelp();
                System.exit(1);
            }
        }
    }
    private static void displayHelp() {
        System.out.println(USAGE);
    }

    private static void evaluateAndPrint(String expression) {
        try {
            Expression result = evaluateExpression(expression);
            System.out.println(result);
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            System.exit(1);
        }
    }

    private static void startRepl() {
        System.out.println("Calculator REPL started. Type 'help' for usage information or 'exit' to quit.");

        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.print("> ");
            String input = scanner.nextLine().trim();

            if (input.isEmpty()) {
                continue;
            }

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

    private static Expression evaluateExpression(String expression) throws IllegalConstruction {
        Calculator c = new Calculator();
        return c.eval(Parser.parse(expression.replaceAll("\\s+", "")));
    }
}
