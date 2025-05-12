package ui;

import calculator.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.util.Objects;

public class CalculatorController {

    @FXML
    private TextField inputField;

    private final StringBuilder currentInput = new StringBuilder();
    private final Calculator calculator = new Calculator();

    private boolean startNew = true;

    @FXML
    private Button angleModeButton;

    private boolean useDegrees = false;



    @FXML
    public void initialize() {
        inputField.setOnKeyPressed(this::handleKeyPress);
    }

    @FXML
    private void handleButtonPress(ActionEvent event) {
        String value = ((Button) event.getSource()).getText();
        ((Button) event.getSource()).getParent().requestFocus();
        processInput(value);
    }

    @FXML
    private void handleEvaluate() {
        if (currentInput.isEmpty()) return; // évite d’évaluer si vide
        try {
            calculator.getEvaluator().setUseDegrees(useDegrees);
            Expression expr = FxExpressionParser.parse(currentInput.toString());
            Expression result = calculator.eval(expr);
            inputField.setText(result.toString());
            currentInput.setLength(0);
            currentInput.append(result.toString());
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
            case "sin", "cos", "tan" -> currentInput.append(value).append("(");
            default -> {}
        }
        inputField.setText(currentInput.toString());
    }

    private void processInput(String value) {
        if (startNew) {
            String current = currentInput.toString();
            boolean isFunctionStart = current.endsWith("(") || current.matches(".*(sin|cos|tan|sqrt)\\($");

            // Clear if NOT a continuation operator
            if (!value.matches("[+\\-*/]") && !isFunctionStart) {
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
            handleScientific(new ActionEvent(new Button("x²"), null));
        } else if (Objects.equals(text.toLowerCase(), "i")) {
            currentInput.append("i");
            inputField.setText(currentInput.toString());
        } else if (text.matches("[0-9+\\-*/().]") || text.equals("π")) {
            processInput(text);
        }
    }

    @FXML
    private void toggleAngleMode() {
        useDegrees = !useDegrees;
        angleModeButton.setText(useDegrees ? "Deg" : "Rad");
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