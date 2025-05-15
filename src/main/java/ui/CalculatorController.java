package ui;

import calculator.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import visitor.Evaluator;

import java.util.Objects;

public class CalculatorController {

    @FXML
    private TextField inputField;

    @FXML
    private Button angleModeButton;

    @FXML
    private ToggleButton fractionModeToggle;

    @FXML
    private Label resultModeLabel;

    private final StringBuilder currentInput = new StringBuilder();
    private final Calculator calculator = new Calculator();

    private boolean startNew = true;
    private final boolean useDegrees = false;
    private boolean preserveFractions = false;

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

    @FXML
    private void handleButtonPress(ActionEvent event) {
        String value = ((Button) event.getSource()).getText();
        ((Button) event.getSource()).getParent().requestFocus();
        processInput(value);
    }

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
        }
        inputField.setText(currentInput.toString());
        ((Button) event.getSource()).getParent().requestFocus();
    }

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
        } catch (Exception e) {
            inputField.setText("Erreur");
            currentInput.setLength(0);
            startNew = true;
        }
        inputField.requestFocus();
    }

    @FXML
    private void handleClear() {
        currentInput.setLength(0);
        inputField.setText("");
        startNew = true;
    }

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

    @FXML
    private void handleBackspace() {
        if (!currentInput.isEmpty()) {
            currentInput.setLength(currentInput.length() - 1);
            inputField.setText(currentInput.toString());
            inputField.requestFocus();
        }
    }

}
