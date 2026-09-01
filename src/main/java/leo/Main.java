package leo;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * The JavaFX GUI entry point. Builds the main window from
 * {@code MainWindow.fxml}, hands the window's controller a {@link Leo}
 * instance to talk to, and shows it. Started via {@link Launcher} rather
 * than directly (see its Javadoc for why).
 */
public class Main extends Application {
    private final Leo leo = new Leo();

    @Override
    public void start(Stage stage) {
        try {
            FXMLLoader fxmlLoader =
                    new FXMLLoader(Main.class.getResource("/view/MainWindow.fxml"));
            AnchorPane root = fxmlLoader.load();
            Scene scene = new Scene(root);

            stage.setTitle("Leo");
            stage.setMinHeight(220);
            stage.setMinWidth(417);
            stage.setScene(scene);

            MainWindow controller = fxmlLoader.getController();
            controller.setLeo(leo);

            stage.show();
        } catch (IOException e) {
            // Thrown if MainWindow.fxml is missing or malformed; there is
            // no sensible way to recover, so surface it on the console
            // and let the GUI fail to open rather than showing a broken
            // window.
            e.printStackTrace();
        }
    }
}
