package ui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Objects;

/**
 * Main entry point for the JavaFX application "Cucumber Calculator".
 * It loads the FXML UI layout, sets the style sheet, and binds the controller
 * for managing user interaction and keyboard events.
 */
public class MainApp extends Application {

    /**
     * Called when the JavaFX application starts.
     * It initializes the primary window, loads the FXML UI, and sets up styling and key bindings.
     *
     * @param primaryStage the main stage (window) provided by the JavaFX runtime
     * @throws Exception if the FXML or stylesheet fails to load
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        // Load the FXML layout file
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/calculator_view.fxml"));
        Parent root = loader.load();

        // Configure the main application window
        primaryStage.setTitle("Cucumber Calculator");
        Scene scene = new Scene(root, 700, 600);

        // Load and apply the stylesheet
        scene.getStylesheets().add(Objects.requireNonNull(
                getClass().getResource("/ui/style.css")
        ).toExternalForm());

        // Bind key events globally to the controller (even if input field is not focused)
        CalculatorController controller = loader.getController();
        scene.setOnKeyPressed(controller::handleKeyPress);

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * Launches the JavaFX application.
     *
     * @param args command-line arguments passed to the application (unused)
     */
    public static void main(String[] args) {
        launch(args); // Start the JavaFX application lifecycle
    }
}
