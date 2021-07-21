package thedrake.game;

import java.io.PrintWriter;

public class Board implements JSONSerializable {

	private final int dimension;
	private final BoardTile[][] boardTile;

	// Konstruktor. Vytvoří čtvercovou hrací desku zadaného rozměru, kde všechny dlaždice jsou prázdné, tedy BoardTile.EMPTY
	public Board(int dimension) {
		this.dimension = dimension;
		this.boardTile = new BoardTile[dimension][dimension];
		for (int i = 0; i < dimension; i++) {
			for (int j = 0; j < dimension; j++) {
				boardTile[i][j] = BoardTile.EMPTY;
			}
		}
	}

	// Rozměr hrací desky
	public int dimension() {
		return dimension;
	}

	// Vrací dlaždici na zvolené pozici.
	public BoardTile at(TilePos pos) {
		return this.boardTile[pos.i()][pos.j()];
	}

	// Vytváří novou hrací desku s novými dlaždicemi. Všechny ostatní dlaždice zůstávají stejné
	public Board withTiles(TileAt ...ats) {
		Board newBoard = new Board(dimension);
		// Copy boardTile
		for (int i = 0; i < dimension; i++) {
			for (int j = 0; j < dimension; j++) {
				newBoard.boardTile[i][j] = boardTile[i][j];
			}
		}

		for (TileAt tile : ats)
			newBoard.boardTile[tile.pos.i()][tile.pos.j()] = tile.tile;

		return newBoard;
	}

	// Vytvoří instanci PositionFactory pro výrobu pozic na tomto hracím plánu
	public PositionFactory positionFactory() {
		return new PositionFactory(dimension);
	}

	@Override
	public void toJSON(PrintWriter writer) {
		writer.printf("{\"dimension\":%d", dimension);
		writer.printf(",\"tiles\":[");
		int count = 0;
		for (int i = 0; i < dimension; i++) {
			for (int j = 0; j < dimension; j++) {
				boardTile[j][i].toJSON(writer);
				count++;
				if (count < dimension * dimension)
					writer.printf(",");
			}
		}
		writer.printf("]}");
	}

	public static class TileAt {

		public final BoardPos pos;
		public final BoardTile tile;
		
		public TileAt(BoardPos pos, BoardTile tile) {
			this.pos = pos;
			this.tile = tile;
		}
	}
}

