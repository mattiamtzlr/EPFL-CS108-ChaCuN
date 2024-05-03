package ch.epfl.chacun;


import java.util.Comparator;
import java.util.List;

/**
 * TODO DESCRIPTION
 *
 * @author Mattia Metzler (372025)
 * @author Leoluca Bernardi (374107)
 */
public final class ActionEncoder {
    private static final int POS_SHIFT = 2;
    private static final int OCC_TYPE_SHIFT = 4;
    private static final int NO_OCCUPANT = 31; // 11111

    public record StateAction(GameState state, String encodedAction) {}

    private ActionEncoder() {}

    public static StateAction withPlacedTile(GameState state, PlacedTile tile) {
        List<Pos> fringeSorted = state.board().insertionPositions().stream()
                .sorted((p1, p2) ->
                        p1.x() < p2.x() ? -1 : p1.x() > p2.x() ? 1 : Integer.compare(p1.y(), p2.y())
                )
                .toList();

        String encoding = Base32.encodeBits10(
                fringeSorted.indexOf(tile.pos()) << POS_SHIFT | tile.rotation().ordinal()
        );
        return new StateAction(state.withPlacedTile(tile), encoding);
    }

    public static StateAction withNewOccupant(GameState state, Occupant occupant) {
        String encoding;
        if (occupant != null)
            encoding = Base32.encodeBits5(
                    occupant.kind().ordinal() << OCC_TYPE_SHIFT | occupant.zoneId()
            );

        else
            encoding = Base32.encodeBits5(NO_OCCUPANT);

        return new StateAction(state.withNewOccupant(occupant), encoding);
    }

    public static StateAction withOccupantRemoved(GameState state, Occupant occupant) {
        String encoding;
        if (occupant != null) {
            List<Occupant> occupantsSorted = state.board().occupants().stream()
                    .filter(o -> o.kind().equals(Occupant.Kind.PAWN))
                    .sorted(Comparator.comparingInt(Occupant::zoneId))
                    .toList();

            encoding = Base32.encodeBits5(occupantsSorted.indexOf(occupant));
        }
        else
            encoding = Base32.encodeBits5(NO_OCCUPANT);

        return new StateAction(state.withOccupantRemoved(occupant), encoding);
    }

    public static StateAction applyAction(GameState state, String encodedAction) {
        try {
            Preconditions.checkArgument(Base32.isValid(encodedAction));
            Preconditions.checkArgument(encodedAction.length() <= 2);

            return null; // TODO

        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
