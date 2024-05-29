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
    /**
     * Amount of bits that the position (of a placed tile in the fringe) encoding is shifted by
     */
    private static final int POS_SHIFT = 2;

    /**
     * Amount of bits that the occupant kind encoding is shifted by
     */
    private static final int OCC_KIND_SHIFT = 4;

    /**
     * Encoding corresponding to no occupant being chosen for any occupant action
     */
    private static final int NO_OCCUPANT = 0b11111;

    /**
     * Length (in characters) of the encoded actions relating to placing a tile
     */
    private static final int PLACE_ACTION_LENGTH = 2;

    /**
     * Length (in characters) of the encoded actions relating to occupant actions
     */
    private static final int OCC_ACTION_LENGTH = 1;

    /**
     * Number of bits used to encode the position of a placed tile in the fringe
     */
    private static final int POS_BITS = 8;

    /**
     * Number of bits used to encode the rotation of a placed tile in the fringe
     */
    private static final int ROTATION_BITS = 2;

    /**
     * Number of bits used to encode the zone id of an occupant
     */
    private static final int ZONE_ID_BITS = 4;

    /**
     * Maximum length (in characters) of any encoded action, primarily used by other classes.
     */
    public static final int MAX_ACTION_LENGTH = Math.max(PLACE_ACTION_LENGTH, OCC_ACTION_LENGTH);

    /**
     * Pairing of a GameState object with the last action that was applied to it.
     * The action is encoded in base 32
     *
     * @param state         the state to be part of the pair.
     * @param encodedAction the action to be part of the pair in base 32.
     */
    public record StateAction(GameState state, String encodedAction) { }

    private ActionEncoder() {}

    /**
     * Helper method to get the current fringe positions correctly sorted
     *
     * @param state the current game state
     * @return list of the fringe positions sorted first ascending by x then by y.
     */
    private static List<Pos> getFringeSorted(GameState state) {
        return state.board().insertionPositions().stream()
            .sorted((p1, p2) ->
                p1.x() < p2.x() ? -1 : (p1.x() > p2.x() ? 1 : Integer.compare(p1.y(), p2.y()))
            )
            .toList();
    }

    /**
     * Helper method to get the current occupants correctly sorted
     *
     * @param state the current game state
     * @return list of the occupants sorted by global zone id
     */
    private static List<Occupant> getOccupantsSorted(GameState state) {
        return state.board().occupants().stream()
            .filter(o -> o.kind().equals(Occupant.Kind.PAWN))
            .sorted(Comparator.comparingInt(Occupant::zoneId))
            .toList();
    }

    /**
     * Method that handles the action of placing a tile.
     *
     * @param state the current state.
     * @param tile  the tile to be placed.
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
     *
     * @param state    the current state.
     * @param occupant the occupant to be added to the game.
     * @return a new StateAction pair containing the GameState after withNewOccupant
     * was applied to it and the encoding of the withNewOccupant Action.
     */
    public static StateAction withNewOccupant(GameState state, Occupant occupant) {
        String encoding = occupant != null
            ? Base32.encodeBits5(
            occupant.kind().ordinal() << OCC_KIND_SHIFT
                | Zone.localId(occupant.zoneId()))

            : Base32.encodeBits5(NO_OCCUPANT);

        return new StateAction(state.withNewOccupant(occupant), encoding);
    }

    /**
     * Method that handles the action of removal of an occupant.
     *
     * @param state    the current state.
     * @param occupant the occupant to be removed from the game.
     * @return a new StateAction pair containing the GameState after withOccupantRemoved
     * was applied to it and the encoding of the withOccupantRemoved Action.
     */
    public static StateAction withOccupantRemoved(GameState state, Occupant occupant) {
        String encoding = occupant != null
            ? Base32.encodeBits5(getOccupantsSorted(state).indexOf(occupant))
            : Base32.encodeBits5(NO_OCCUPANT);

        return new StateAction(state.withOccupantRemoved(occupant), encoding);
    }

    /**
     * A method to apply an encoded action to the current state of the game.
     *
     * @param state         the current GameState.
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
                    Preconditions.checkArgument(encodedAction.length() == PLACE_ACTION_LENGTH);
                    int rotationIndex = decoded & ((1 << ROTATION_BITS) - 1);
                    Rotation rotation = Rotation.values()[rotationIndex];

                    int fringeIndex = (decoded >> POS_SHIFT) & ((1 << POS_BITS) - 1);
                    Pos pos = getFringeSorted(state).get(fringeIndex);

                    PlacedTile tileToPlace = new PlacedTile(
                        state.tileToPlace(), state.currentPlayer(), rotation, pos
                    );

                    return withPlacedTile(state, tileToPlace);
                }
                case OCCUPY_TILE -> {
                    Preconditions.checkArgument(encodedAction.length() == OCC_ACTION_LENGTH);

                    if (decoded == NO_OCCUPANT)
                        return withNewOccupant(state, null);

                    int localZoneId = decoded & ((1 << ZONE_ID_BITS) - 1);
                    int kindIndex = decoded >> OCC_KIND_SHIFT;
                    Occupant.Kind kind = Occupant.Kind.values()[kindIndex];

                    Occupant occupant = state.lastTilePotentialOccupants().stream()
                        .filter(o -> Zone.localId(o.zoneId()) == localZoneId && o.kind() == kind)
                        .findFirst()
                        .orElseThrow(IllegalArgumentException::new);

                    return withNewOccupant(state, occupant);
                }
                case RETAKE_PAWN -> {
                    Preconditions.checkArgument(encodedAction.length() == OCC_ACTION_LENGTH);

                    if (decoded == NO_OCCUPANT)
                        return withOccupantRemoved(state, null);

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
            /* FYI: this is not for debugging, it is quite useful as a player to have an
             * indication of an illegal action, displayed in the console */
            System.out.println(STR."""
                    Warning: Illegal Action: '\{encodedAction}' for current state\
                     \{state.nextAction()}\
                    """
            );
            return null;
        }
    }
}
