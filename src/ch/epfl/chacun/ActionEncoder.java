package ch.epfl.chacun;


import java.util.Comparator;
import java.util.List;

/**
 * Class to handle players taking actions.
 *
 * @author Mattia Metzler (372025)
 * @author Leoluca Bernardi (374107)
 */
public final class ActionEncoder {
    private static final int POS_SHIFT = 2;
    private static final int OCC_KIND_SHIFT = 4;
    private static final int NO_OCCUPANT = 31; // 11111

    /**
     * Pairing of a GameState object with the last action that was applied to it.
     * The action is encoded in base 32
     * @param state the state to be part of the pair.
     * @param encodedAction the action to be part of the pair in base 32.
     */
    public record StateAction(GameState state, String encodedAction) {
    }

    private ActionEncoder() {
    }

    private static List<Pos> getFringeSorted(GameState state) {
        // Returns a list of fringe positions sorted first ascending by x then by y.
        return state.board().insertionPositions().stream()
            .sorted((p1, p2) ->
                p1.x() < p2.x() ? -1 : (p1.x() > p2.x() ? 1 : Integer.compare(p1.y(), p2.y()))
            )
            .toList();
    }

    private static List<Occupant> getOccupantsSorted(GameState state) {
        return state.board().occupants().stream()
            .filter(o -> o.kind().equals(Occupant.Kind.PAWN))
            .sorted(Comparator.comparingInt(Occupant::zoneId))
            .toList();
    }

    /**
     * Method that handles the action of placing a tile.
     * @param state the current state.
     * @param tile the tile to be placed.
     * @return a new StateAction pair containing the GameState after withPlacedTile
     * was applied to it and the encoding of the withPlacedTile Action.
     */
    public static StateAction withPlacedTile(GameState state, PlacedTile tile) {

        String encoding = Base32.encodeBits10(
            getFringeSorted(state).indexOf(tile.pos()) << POS_SHIFT
                | tile.rotation().ordinal()
        );
        return new StateAction(state.withPlacedTile(tile), encoding);
    }

    /**
     * Method that handles the action of placing an occupant.
     * @param state the current state.
     * @param occupant the occupant to be added to the game.
     * @return a new StateAction pair containing the GameState after withNewOccupant
     * was applied to it and the encoding of the withNewOccupant Action.
     */
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

    /**
     * Method that handles the action of removal of an occupant.
     * @param state the current state.
     * @param occupant the occupant to be removed from the game.
     * @return a new StateAction pair containing the GameState after withOccupantRemoved
     * was applied to it and the encoding of the withOccupantRemoved Action.
     */
    public static StateAction withOccupantRemoved(GameState state, Occupant occupant) {
        String encoding;
        if (occupant != null) {
            encoding = Base32.encodeBits5(getOccupantsSorted(state).indexOf(occupant));
        } else
            encoding = Base32.encodeBits5(NO_OCCUPANT);

        return new StateAction(state.withOccupantRemoved(occupant), encoding);
    }

    /**
     * A method to apply an encoded action to the current state of the game.
     * @param state the current GameState.
     * @param encodedAction the action to be applied to the game encoded in base 32.
     * @return a new StateAction pair that pairs the new GameState with the action that
     * was passed if it is a legal move to make in the current state.
     */
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

                    if (decoded == NO_OCCUPANT)
                        return withNewOccupant(state, null);

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

                    if (decoded == NO_OCCUPANT)
                        return withNewOccupant(state, null);
                    
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
