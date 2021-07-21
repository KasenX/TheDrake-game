package thedrake.ui.game;

import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import thedrake.game.*;
import thedrake.ui.victory.VictoryController;

import java.io.IOException;
import java.util.List;

public class BoardView extends GridPane implements TileViewContext {

    private GameState gameState;

    private TileView selectedTile = null;

    final private TileView blueStack;

    final private TileView orangeStack;

    private ValidMoves validMoves;

    final private Label onTurn;

    final private Label capturedByBlue;

    final private Label capturedByOrange;

    public BoardView(GameState gameState) {
        this.gameState = gameState;
        this.validMoves = new ValidMoves(gameState);

        PositionFactory positionFactory = gameState.board().positionFactory();
        int dimension = gameState.board().dimension();
        for (int y = 0; y < dimension; y++) {
            for (int x = 0; x < dimension; x++) {
                BoardPos boardPos = positionFactory.pos(x, (dimension - 1) - y);
                add(new TileView(boardPos, gameState.tileAt(boardPos), this), x, y);
            }
        }

        blueStack = new TileView(null, new TroopTile(gameState.army(PlayingSide.BLUE).stack().get(0), PlayingSide.BLUE, TroopFace.AVERS), this);
        orangeStack = new TileView(null, new TroopTile(gameState.army(PlayingSide.ORANGE).stack().get(0), PlayingSide.ORANGE, TroopFace.AVERS), this);
        onTurn = new Label("BLUE");
        onTurn.setAlignment(Pos.CENTER);
        capturedByBlue = new Label("");
        capturedByOrange = new Label("");

        setHgap(5);
        setVgap(5);
        setPadding(new Insets(15));
        setAlignment(Pos.CENTER);
    }

    @Override
    public void tileViewSelected(TileView tileView) {
        if (selectedTile != null && selectedTile != tileView)
            selectedTile.unselect();

        selectedTile = tileView;

        clearMoves();
        showMoves(validMoves.boardMoves(tileView.position()));
    }

    @Override
    public void stackSelected(TileView tileView) {
        if (selectedTile != null && selectedTile != tileView)
            selectedTile.unselect();

        selectedTile = tileView;

        clearMoves();
        if (tileView.side() == gameState.sideOnTurn())
            showMoves(validMoves.movesFromStack());
    }

    public Label getOnTurn() {
        return onTurn;
    }

    public TileView getBlueStack() {
        return blueStack;
    }

    public TileView getOrangeStack() {
        return orangeStack;
    }

    public Label getCapturedByBlue() {
        return capturedByBlue;
    }

    public Label getCapturedByOrange() {
        return capturedByOrange;
    }

    private void clearMoves() {
        for (Node node : getChildren()) {
            TileView tileView = (TileView) node;
            tileView.clearMove();
        }
    }

    private void showMoves(List<Move> moves) {
        for (Move move : moves) {
            tileView(move.target()).setMove(move);
        }
    }

    private TileView tileView(BoardPos pos) {
        int dimension = gameState.board().dimension();
        int index = ((dimension - 1) - pos.j()) * dimension + pos.i();
        return (TileView) getChildren().get(index);
    }

    @Override
    public void executeMove(Move move) {
        selectedTile.unselect();
        selectedTile = null;
        clearMoves();
        gameState = move.execute(gameState);
        validMoves = new ValidMoves(gameState);
        updateTurn();
        updateTiles();
        updateStack();
        updateCaptured();
        try {
            checkVictory();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void updateTurn() {
        onTurn.setText(gameState.sideOnTurn().toString());
    }

    private void updateTiles() {
        // GAME BOARD UPDATE
        for (Node node : getChildren()) {
            TileView tileView = (TileView) node;
            tileView.setTile(gameState.tileAt(tileView.position()));
        }
            }

    private void updateStack() {
        // BLUE STACK UPDATE
        if (gameState.sideOnTurn() == PlayingSide.ORANGE) {
            if (!gameState.army(PlayingSide.BLUE).stack().isEmpty())
                blueStack.setTile(new TroopTile(gameState.army(PlayingSide.BLUE).stack().get(0), PlayingSide.BLUE, TroopFace.AVERS));
            else
                blueStack.setTile(null);
        }
        // ORANGE STACK UPDATE
        else {
            if (!gameState.army(PlayingSide.ORANGE).stack().isEmpty())
                orangeStack.setTile(new TroopTile(gameState.army(PlayingSide.ORANGE).stack().get(0), PlayingSide.ORANGE, TroopFace.AVERS));
            else
                orangeStack.setTile(null);
        }
    }

    private void updateCaptured() {
        StringBuilder text = new StringBuilder();
        List<Troop> troops = gameState.armyNotOnTurn().captured();
        for (int i = 0; i < troops.size(); ++i) {
            if (i != 0)
                text.append('\n');
            text.append(troops.get(i).toString());
        }
        if (gameState.sideOnTurn() == PlayingSide.BLUE)
            capturedByBlue.setText(text.toString());
        else
            capturedByOrange.setText(text.toString());
    }

    private void checkVictory() throws IOException {
        if (gameState.result() != GameResult.IN_PLAY) {
            Stage stage = (Stage) this.getScene().getWindow();
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/thedrake/ui/victory/victory.fxml"));
            GridPane victoryPane = fxmlLoader.load();
            ((VictoryController) fxmlLoader.getController()).setVictoryLabel(gameState.sideNotOnTurn());
            stage.setScene(new Scene(victoryPane));

        }
    }
}
