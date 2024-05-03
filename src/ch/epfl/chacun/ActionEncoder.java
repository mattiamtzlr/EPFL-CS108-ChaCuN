package ch.epfl.chacun;

/**
 * TODO DESCRIPTION
 *
 * @author Mattia Metzler (372025)
 * @author Leoluca Bernardi (374107)
 */
public final class ActionEncoder {
    public record StateAction(GameState state, String encodedAction) {}
    private ActionEncoder() {}

    public StateAction withPlacedTile(GameState state, PlacedTile tile) {
        String encoding = ""; // TODO

        return new StateAction(state.withPlacedTile(tile), encoding);
    }

    public StateAction withNewOccupant(GameState state, Occupant occupant) {
        String encoding = ""; // TODO

        return new StateAction(state.withNewOccupant(occupant), encoding);
    }

    public StateAction withOccupantRemoved(GameState state, Occupant occupant) {
        String encoding = ""; // TODO

        return new StateAction(state.withOccupantRemoved(occupant), encoding);
    }

    public StateAction applyAction(GameState state, String encodedAction) {
        // TODO
        return null;
    }
}
