package thedrake.game;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import static thedrake.game.TroopFace.*;

public class TroopTile implements Tile, JSONSerializable {

    private final Troop troop;

    private final PlayingSide side;

    private final TroopFace face;

    // Konstruktor
    public TroopTile(Troop troop, PlayingSide side, TroopFace face) {
        this.troop = troop;
        this.side = side;
        this.face = face;
    }

    // Vrací barvu, za kterou hraje jednotka na této dlaždici
    public PlayingSide side() {
        return this.side;
    }

    // Vrací stranu, na kterou je jednotka otočena
    public TroopFace face() {
        return this.face;
    }

    // Jednotka, která stojí na této dlaždici
    public Troop troop() {
        return this.troop;
    }

    // Vrací False, protože na dlaždici s jednotkou se nedá vstoupit
    public boolean canStepOn() {
        return false;
    }

    // Vrací True
    public boolean hasTroop() {
        return true;
    }

    @Override
    public List<Move> movesFrom(BoardPos pos, GameState state) {
        List<Move> result = new ArrayList<>();
        List<TroopAction> actions = troop.actions(face);

        for (TroopAction it: actions)
            result.addAll(it.movesFrom(pos, side, state));

        return result;
    }

    // Vytvoří novou dlaždici, s jednotkou otočenou na opačnou stranu
    // (z rubu na líc nebo z líce na rub)
    public TroopTile flipped() {
        return new TroopTile(this.troop, this.side, this.face == AVERS ? REVERS : AVERS);
    }

    @Override
    public void toJSON(PrintWriter writer) {
        writer.printf("{\"troop\":");
        troop.toJSON(writer);
        writer.printf(",\"side\":");
        side.toJSON(writer);
        writer.printf(",\"face\":");
        face.toJSON(writer);
        writer.printf("}");
    }
}
