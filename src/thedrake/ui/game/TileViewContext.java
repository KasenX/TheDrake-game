package thedrake.ui.game;

import thedrake.game.Move;

public interface TileViewContext {

    void tileViewSelected(TileView tileView);

    void stackSelected(TileView tileView);

    void executeMove(Move move);
}
