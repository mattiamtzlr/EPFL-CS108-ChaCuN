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
    private static final int OCC_KIND_SHIFT = 4;
    private static final int NO_OCCUPANT = 31; // 11111

    public record StateAction(GameState state, String encodedAction) {
    }

    private ActionEncoder() {
    }

    private static List<Pos> getFringeSorted(GameState state) {
        return state.board().insertionPositions().stream()
            .sorted((p1, p2) ->
                p1.x() < p2.x() ? -1 : p1.x() > p2.x() ? 1 : Integer.compare(p1.y(), p2.y())
            )
            .toList();
    }

    private static List<Occupant> getOccupantsSorted(GameState state) {
        return state.board().occupants().stream()
            .filter(o -> o.kind().equals(Occupant.Kind.PAWN))
            .sorted(Comparator.comparingInt(Occupant::zoneId))
            .toList();
    }

    public static StateAction withPlacedTile(GameState state, PlacedTile tile) {

        String encoding = Base32.encodeBits10(
            getFringeSorted(state).indexOf(tile.pos()) << POS_SHIFT
                | tile.rotation().ordinal()
        );
        return new StateAction(state.withPlacedTile(tile), encoding);
    }

    public static StateAction withNewOccupant(GameState state, Occupant occupant) {
        String encoding;
        if (occupant != null)
            encoding = Base32.encodeBits5(
                occupant.kind().ordinal() << OCC_KIND_SHIFT
                    | Zone.localId(occupant.zoneId()));

        else
            encoding = Base32.encodeBits5(NO_OCCUPANT);

        return new StateAction(state.withNewOccupant(occupant), encoding);
    }

    public static StateAction withOccupantRemoved(GameState state, Occupant occupant) {
        String encoding;
        if (occupant != null) {
            encoding = Base32.encodeBits5(getOccupantsSorted(state).indexOf(occupant));
        } else
            encoding = Base32.encodeBits5(NO_OCCUPANT);

        return new StateAction(state.withOccupantRemoved(occupant), encoding);
    }

    public static StateAction applyAction(GameState state, String encodedAction) {
        try {
            Preconditions.checkArgument(Base32.isValid(encodedAction));
            int decoded = Base32.decode(encodedAction);

            switch (state.nextAction()) {
                case PLACE_TILE -> {
                    Preconditions.checkArgument(encodedAction.length() == 2);
                    int rotationIndex = decoded & ((1 << 2) - 1);
                    Rotation rotation = Rotation.values()[rotationIndex];

                    int fringeIndex = decoded & ((1 << 8) - 1) << POS_SHIFT;
                    Pos pos = getFringeSorted(state).get(fringeIndex);

                    PlacedTile tileToPlace = new PlacedTile(
                        state.tileToPlace(), state.currentPlayer(), rotation, pos
                    );

                    return withPlacedTile(state, tileToPlace);
                }
                case OCCUPY_TILE -> {
                    Preconditions.checkArgument(encodedAction.length() == 1);
                    int localZoneId = decoded & ((1 << 4) - 1);
                    int kindIndex = decoded & (1 << OCC_KIND_SHIFT);
                    Occupant.Kind kind = Occupant.Kind.values()[kindIndex];

                    Occupant occupant = state.lastTilePotentialOccupants().stream()
                        .filter(o -> Zone.localId(o.zoneId()) == localZoneId
                            && o.kind().equals(kind))
                        .findFirst()
                        .orElseThrow(IllegalArgumentException::new);

                    return withNewOccupant(state, occupant);
                }
                case RETAKE_PAWN -> {
                    Preconditions.checkArgument(encodedAction.length() == 1);
                    Occupant occupant = getOccupantsSorted(state).get(decoded);
                    Preconditions.checkArgument(
                        state.board().tileWithId(Zone.tileId(occupant.zoneId())).placer()
                            .equals(state.currentPlayer()));
                    return withOccupantRemoved(state, occupant);
                }
                default -> {
                    return null;
                }
            }

        } catch (IllegalArgumentException | IndexOutOfBoundsException e) {
            System.out.println(STR."Warning: Illegal Action: '\{encodedAction}'");
            return null;
        }
    }
}
