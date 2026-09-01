package leo;

import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

/**
 * Controller for {@code MainWindow.fxml}: the scrollable chat log, the
 * text field the user types commands into, and the send button. Wires
 * user input to {@link Leo#getResponse(String)} and displays both sides
 * of the conversation as {@link DialogBox} rows.
 */
public class MainWindow {
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private VBox dialogContainer;
    @FXML
    private TextField userInput;
    @FXML
    private Button sendButton;

    private Leo leo;

    private final Image userImage =
            new Image(this.getClass().getResourceAsStream("/images/DaUser.png"));
    private final Image leoImage =
            new Image(this.getClass().getResourceAsStream("/images/DaLeo.png"));

    /**
     * Called automatically by the FXML loader once the annotated
     * fields above have been injected. Keeps the chat log scrolled to
     * the newest message as it grows.
     */
    @FXML
    public void initialize() {
        scrollPane.vvalueProperty().bind(dialogContainer.heightProperty());
    }

    /**
     * Gives this window the {@link Leo} instance to send commands to,
     * and shows Leo's opening greeting as the first chat bubble. Called
     * once by {@link Main} right after the FXML is loaded.
     *
     * @param leo the Leo instance backing this window
     */
    public void setLeo(Leo leo) {
        this.leo = leo;
        dialogContainer.getChildren().add(
                DialogBox.getLeoDialog(
                        "Hello! I'm Leo.\nHow are you doing today, and how may I help?",
                        leoImage
                )
        );
    }

    /**
     * Called when the user presses Enter in the text field or clicks
     * Send. Adds a dialog box for the user's own input and one for
     * Leo's reply, then clears the text field. If the command was
     * "bye", closes the app shortly after, giving the user a moment to
     * read the goodbye message (mirroring the console version, which
     * ends the chat loop straight after showing it).
     */
    @FXML
    private void handleUserInput() {
        String input = userInput.getText();

        if (input.trim().isEmpty()) {
            return;
        }

        String response = leo.getResponse(input);

        dialogContainer.getChildren().addAll(
                DialogBox.getUserDialog(input, userImage),
                DialogBox.getLeoDialog(response, leoImage)
        );
        userInput.clear();

        if (Parser.getCommandWord(input).equals("bye")) {
            PauseTransition delay = new PauseTransition(Duration.seconds(1.5));
            delay.setOnFinished(event -> Platform.exit());
            delay.play();
        }
    }
}
