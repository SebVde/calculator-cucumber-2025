package ui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Objects;

/**
 * Point d'entrée principal de l'application JavaFX pour le Cucumber Calculator.
 */
public class MainApp extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Chargement du fichier FXML
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/calculator_view.fxml"));
        Parent root = loader.load();

        // Configuration de la fenêtre principale
        primaryStage.setTitle("Cucumber Calculator");
        Scene scene = new Scene(root, 700, 600);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/ui/style.css")).toExternalForm());

        // Connecter les touches physiques même si le champ n’est pas focus
        CalculatorController controller = loader.getController();
        scene.setOnKeyPressed(controller::handleKeyPress);  // ← important

        primaryStage.setScene(scene);

        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args); // Lance l'application JavaFX
    }
}
