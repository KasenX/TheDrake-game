package thedrake.ui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class TheDrakeApp extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        GridPane menuPane = FXMLLoader.load(getClass().getResource("/thedrake/ui/menu/menu.fxml"));
        stage.setTitle("The Drake");
        stage.setScene(new Scene(menuPane));
        stage.show();
    }

}
