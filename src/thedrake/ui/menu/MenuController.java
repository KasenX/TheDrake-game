package thedrake.ui.menu;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import thedrake.ui.game.GamePane;


public class MenuController {

    @FXML
    private Button versusGameButton;
    @FXML
    private Button singlePlayerButton;
    @FXML
    private Button multiPlayerButton;
    @FXML
    private Button closeButton;

    public void versusGameAction() {
        Stage stage = (Stage) versusGameButton.getScene().getWindow();
        GamePane gamePane = new GamePane();
        stage.setScene(new Scene(gamePane));
        stage.show();
    }

    public void singlePlayerAction() {
    }

    public void multiPlayerAction() {

    }

    public void closeAction() {
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }
}
