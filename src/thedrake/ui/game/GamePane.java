package thedrake.ui.game;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import thedrake.game.*;

import java.util.Random;

public class GamePane extends BorderPane {

    final private BoardView boardView;

    public GamePane() {
        this.boardView = new BoardView(createNewGameState());
        this.setCenter(boardView);
        setInfo();
    }

    private void setInfo() {
        // SIDE ON TURN
        HBox topBox = new HBox();
        topBox.setAlignment(Pos.CENTER);
        topBox.setPadding(new Insets(5, 0, 0, 0));
        topBox.getChildren().add(new Label("Side on turn: "));
        topBox.getChildren().add(boardView.getOnTurn());
        this.setTop(topBox);

        // STACK AND CAPTURED
        VBox blueBox = new VBox();
        VBox orangeBox = new VBox();
        blueBox.getChildren().add(new Label("Blue stack:"));
        blueBox.getChildren().add(boardView.getBlueStack());
        blueBox.getChildren().add(new Label("Captured:"));
        blueBox.getChildren().add(boardView.getCapturedByBlue());
        blueBox.setSpacing(5);
        blueBox.setPadding(new Insets(10, 10, 10, 10));
        orangeBox.getChildren().add(new Label("Orange stack:"));
        orangeBox.getChildren().add(boardView.getOrangeStack());
        orangeBox.getChildren().add(new Label("Captured:"));
        orangeBox.getChildren().add(boardView.getCapturedByOrange());
        orangeBox.setSpacing(5);
        orangeBox.setPadding(new Insets(10, 10, 10, 10));
        this.setLeft(blueBox);
        this.setRight(orangeBox);
    }

    private static GameState createNewGameState() {
        int dimension = 4;
        Board board = new Board(dimension);
        PositionFactory positionFactory = board.positionFactory();

        Random random = new Random();
        board = board.withTiles(new Board.TileAt(positionFactory.pos(random.nextInt(dimension), random.nextInt(dimension - 2) + 1), BoardTile.MOUNTAIN));

        return new StandardDrakeSetup().startState(board);
    }
}
