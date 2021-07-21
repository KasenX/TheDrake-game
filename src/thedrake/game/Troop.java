package thedrake.game;

import java.io.PrintWriter;
import java.util.List;

import static thedrake.game.TroopFace.*;

public class Troop implements JSONSerializable {

    private final String name;
    private final Offset2D aversPivot;
    private final Offset2D reversPivot;
    private final List<TroopAction> aversActions;
    private final List<TroopAction> reverseActions;

    public Troop(String name, Offset2D aversPivot, Offset2D reversPivot, List<TroopAction> aversActions, List<TroopAction> reverseActions) {
        this.name = name;
        this.aversPivot = aversPivot;
        this.reversPivot = reversPivot;
        this.aversActions = aversActions;
        this.reverseActions = reverseActions;
    }

    public Troop(String name, Offset2D pivot, List<TroopAction> aversActions, List<TroopAction> reverseActions) {
        this.name = name;
        this.aversPivot = pivot;
        this.reversPivot = pivot;
        this.aversActions = aversActions;
        this.reverseActions = reverseActions;
    }

    public Troop(String name, List<TroopAction> aversActions, List<TroopAction> reverseActions) {
        this.name = name;
        this.aversActions = aversActions;
        this.reverseActions = reverseActions;
        this.aversPivot = new Offset2D(1, 1);
        this.reversPivot = new Offset2D(1, 1);
    }

    //Vrací seznam akcí pro zadanou stranu jednotky
    public List<TroopAction> actions(TroopFace face) {
        return face == AVERS ? aversActions : reverseActions;
    }

    public String name() {
        return this.name;
    }

    public Offset2D pivot(TroopFace face) {
        return face == AVERS ? aversPivot : reversPivot;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public void toJSON(PrintWriter writer) {
        writer.printf("\"%s\"", name);
    }
}
