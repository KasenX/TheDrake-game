package thedrake.ui.victory;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import thedrake.game.PlayingSide;
import thedrake.ui.game.GamePane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class VictoryController implements Initializable {
    @FXML
    private Label victoryLabel;
    @FXML
    private Button playAgain;
    @FXML
    private Button mainMenu;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void playAgainAction() {
        Stage stage = (Stage) playAgain.getScene().getWindow();
        GamePane gamePane = new GamePane();
        stage.setScene(new Scene(gamePane));
        stage.show();
    }

    public void mainMenuAction() throws IOException {
        Stage stage = (Stage) playAgain.getScene().getWindow();
        GridPane menuPane = FXMLLoader.load(getClass().getResource("/thedrake/ui/menu/menu.fxml"));
        stage.setScene(new Scene(menuPane));
        stage.show();
    }

    public void setVictoryLabel(PlayingSide playingSide) {
        if (playingSide == PlayingSide.BLUE)
            victoryLabel.setStyle("-fx-text-fill: #6495ED");
        else
            victoryLabel.setStyle("-fx-text-fill: #FF7F50");
        victoryLabel.setText(playingSide.toString());
    }
}
