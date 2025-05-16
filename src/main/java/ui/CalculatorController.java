package ui;

import calculator.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import visitor.Evaluator;

import java.util.Objects;

/**
 * JavaFX Controller for the calculator user interface.
 * Handles user interaction, button events, keyboard input, and delegates
 * parsing and evaluation logic to the core calculator engine.
 */
public class CalculatorController {

    @FXML private TextField inputField;
    @FXML private Button angleModeButton;
    @FXML private ToggleButton fractionModeToggle;
    @FXML private Label resultModeLabel;

    private final StringBuilder currentInput = new StringBuilder();
    private final Calculator calculator = new Calculator();

    private boolean startNew = true;
    private boolean preserveFractions = false;

    /**
     * Initializes the controller, binding UI behavior (such as toggling fraction mode)
     * and keyboard shortcuts.
     */
    @FXML
    public void initialize() {
        inputField.setOnKeyPressed(this::handleKeyPress);
        if (fractionModeToggle != null) {
            preserveFractions = fractionModeToggle.isSelected();
            resultModeLabel.setText(preserveFractions ? "Résultat en fraction" : "Résultat décimal");
            fractionModeToggle.setOnAction(e -> {
                preserveFractions = fractionModeToggle.isSelected();
                resultModeLabel.setText(preserveFractions ? "Résultat en fraction" : "Résultat décimal");
                inputField.requestFocus();
            });
        }
    }

    /**
     * Handles clicks on digit and operator buttons.
     * Updates the current expression accordingly.
     *
     * @param event the action event triggered by button press
     */
    @FXML
    private void handleButtonPress(ActionEvent event) {
        String value = ((Button) event.getSource()).getText();
        ((Button) event.getSource()).getParent().requestFocus();
        processInput(value);
    }

    /**
     * Handles clicks on scientific operator buttons (sqrt, square, π).
     *
     * @param event the action event triggered by scientific button press
     */
    @FXML
    private void handleScientific(ActionEvent event) {
        String value = ((Button) event.getSource()).getText();
        switch (value) {
            case "√" -> currentInput.append("sqrt(");
            case "x²" -> {
                if (!currentInput.isEmpty()) {
                    String expr = currentInput.toString();
                    currentInput.setLength(0);
                    currentInput.append("(").append(expr).append(")*(").append(expr).append(")");
                }
            }
            case "π" -> currentInput.append("π");
            default -> throw new IllegalArgumentException("Unknown scientific value: " + value);
        }
        inputField.setText(currentInput.toString());
        ((Button) event.getSource()).getParent().requestFocus();
    }

    /**
     * Evaluates the current expression in the input field and displays the result.
     * Handles syntax errors and resets the input in case of exception.
     */
    @FXML
    private void handleEvaluate() {
        if (currentInput.isEmpty()) return;
        try {
            Evaluator eval = new Evaluator(preserveFractions);
            calculator.setEvaluator(eval);
            Expression expr = Parser.parse(currentInput.toString(), preserveFractions);
            expr.accept(eval);
            Expression result = eval.getResult();
            inputField.setText(result.toString());
            currentInput.setLength(0);
            currentInput.append(result);
            startNew = true;
        } catch (Exception _) {
            inputField.setText("Error");
            currentInput.setLength(0);
            startNew = true;
        }
        inputField.requestFocus();
    }

    /**
     * Clears the current input field and resets the expression.
     */
    @FXML
    private void handleClear() {
        currentInput.setLength(0);
        inputField.setText("");
        startNew = true;
    }

    /**
     * Processes button or key input and appends it to the current expression buffer.
     * Handles special symbols (×, ÷, −) and replaces them with standard operators.
     *
     * @param value the character or symbol to process
     */
    private void processInput(String value) {
        if (startNew) {
            String current = currentInput.toString();
            boolean isFunctionStart = current.endsWith("(") || current.matches(".*(sqrt)\\($");
            if (!value.matches("[+\\-*/]") && !isFunctionStart && !current.matches(".*[πi]$")) {
                currentInput.setLength(0);
            }
        }
        startNew = false;

        switch (value) {
            case "×" -> currentInput.append("*");
            case "÷" -> currentInput.append("/");
            case "−" -> currentInput.append("-");
            default -> currentInput.append(value);
        }

        inputField.setText(currentInput.toString());
    }

    /**
     * Handles keyboard key presses while the input field is focused.
     * Supports ENTER (evaluate), ESCAPE (clear), BACKSPACE, and basic input.
     *
     * @param event the key event triggered by user input
     */
    void handleKeyPress(KeyEvent event) {
        KeyCode code = event.getCode();
        String text = event.getText();

        if (code == KeyCode.ENTER) {
            event.consume();
            handleEvaluate();
        } else if (code == KeyCode.BACK_SPACE && !currentInput.isEmpty()) {
            handleBackspace();
        } else if (code == KeyCode.ESCAPE) {
            handleClear();
        } else if (Objects.equals(text, "²")) {
            // Handle square character typed from keyboard
            if (!currentInput.isEmpty()) {
                String expr = currentInput.toString();
                currentInput.setLength(0);
                currentInput.append("(").append(expr).append(")*(").append(expr).append(")");
                inputField.setText(currentInput.toString());
            }
        } else if (Objects.equals(text.toLowerCase(), "i")) {
            currentInput.append("i");
            inputField.setText(currentInput.toString());
        } else if (text.matches("[0-9+\\-*/().]") || text.equals("π")) {
            processInput(text);
        }
    }

    /**
     * Removes the last character from the current expression.
     * Called on BACKSPACE key or button press.
     */
    @FXML
    private void handleBackspace() {
        if (!currentInput.isEmpty()) {
            currentInput.setLength(currentInput.length() - 1);
            inputField.setText(currentInput.toString());
            inputField.requestFocus();
        }
    }
}
