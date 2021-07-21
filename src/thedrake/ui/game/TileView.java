package thedrake.ui.game;

import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import thedrake.game.*;

public class TileView extends Pane {

    final private BoardPos pos;

    private Tile tile;

    private final TileViewContext tileViewContext;

    private Move move = null;

    final private TileBackgrounds backgrounds = new TileBackgrounds();

    final private Border selectionBorder = new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(3)));

    private final ImageView moveImage;

    public TileView(BoardPos pos, Tile tile, TileViewContext tileViewContext) {
        this.pos = pos;
        this.tile = tile;
        this.tileViewContext = tileViewContext;
        setPrefSize(100, 100);
        update();

        setOnMouseClicked(e -> onClick());

        moveImage = new ImageView(getClass().getResource("/assets/move.png").toString());
        moveImage.setVisible(false);
        getChildren().add(moveImage);
    }

    public BoardPos position() {
        return pos;
    }

    private void onClick() {
        if (move != null)
            tileViewContext.executeMove(move);
        else if (tile != null && tile.hasTroop())
            select();
    }

    private void select() {
        setBorder(selectionBorder);
        if (pos == null)
            tileViewContext.stackSelected(this);
        else
            tileViewContext.tileViewSelected(this);
    }

    public void unselect() {
        setBorder(null);
    }

    public void setMove(Move move) {
        this.move = move;
        moveImage.setVisible(true);
    }

    public void clearMove() {
        this.move = null;
        moveImage.setVisible(false);
    }

    public void setTile(Tile tile) {
        this.tile = tile;
        update();
    }

    private void update() {
        setBackground(backgrounds.get(tile));
    }

    PlayingSide side() {
        TroopTile troopTile = (TroopTile) tile;
        return troopTile.side();
    }
}
