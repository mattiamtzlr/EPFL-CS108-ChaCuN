package ch.epfl.chacun.stage10;

import ch.epfl.chacun.*;
import ch.epfl.chacun.tile.Tiles;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static ch.epfl.chacun.Direction.*;
import static ch.epfl.chacun.PlayerColor.*;
import static ch.epfl.chacun.Pos.ORIGIN;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class MyActionEncoderTest {
    Map<PlayerColor, String> playerNames = Map.of(
        PURPLE, "Leo",
        GREEN, "Mattia"
    );

    List<PlayerColor> playerColors = playerNames.keySet().stream()
        .sorted()
        .toList();

    Map<Tile.Kind, List<Tile>> tilesByKind = Tiles.TILES.stream()
        .collect(Collectors.groupingBy(Tile::kind));

    TileDecks tileDecks = new TileDecks(
        tilesByKind.get(Tile.Kind.START),
        tilesByKind.get(Tile.Kind.NORMAL),
        tilesByKind.get(Tile.Kind.MENHIR)
    );

    TextMaker textMaker = new TextMakerFr(playerNames);

    PlacedTile t60EastOf56 = new PlacedTile(
        Tiles.TILES.get(60), GREEN, Rotation.NONE, ORIGIN.neighbor(E)
    );
    Occupant pawnF601 = new Occupant(Occupant.Kind.PAWN, 601);

    PlacedTile t61NorthOf56 = new PlacedTile(
        Tiles.TILES.get(61), GREEN, Rotation.HALF_TURN, ORIGIN.neighbor(N)
    );
    Occupant pawnM610 = new Occupant(Occupant.Kind.PAWN, 610);

    PlacedTile t19WestOf56 = new PlacedTile(
        Tiles.TILES.get(19), PURPLE, Rotation.LEFT, ORIGIN.neighbor(W)
    );
    Occupant hutR191 = new Occupant(Occupant.Kind.HUT, 191);
    Occupant pawnM192 = new Occupant(Occupant.Kind.PAWN, 192);

    PlacedTile t88LEFTNorthEastOf56SHAMAN = new PlacedTile(
        Tiles.TILES.get(88), GREEN, Rotation.LEFT, ORIGIN.translated(1, -1)
    );
    PlacedTile t88NONENorthEastOf56SHAMAN = new PlacedTile(
            Tiles.TILES.get(88), GREEN, Rotation.NONE, ORIGIN.translated(1, -1)
    );

    GameState initial = GameState.initial(playerColors, tileDecks, textMaker);

    @Test
    void withPlacedTile() {
        GameState gameState = initial.withStartingTilePlaced();

        // first tile is placed to the north of the starting tile (0, -1), rotated by a half-turn
        // 00000 00110
        assertEquals(
            "AG",
            ActionEncoder.withPlacedTile(gameState, t61NorthOf56).encodedAction()
        );

        // first tile is placed to the west of the starting tile (-1, 0), rotated to the left
        // 00000 00011
        assertEquals(
            "AD",
            ActionEncoder.withPlacedTile(gameState, t19WestOf56).encodedAction()
        );
    }

    @Test
    void withNewOccupant() {
        GameState gameState = initial.withStartingTilePlaced()
            .withPlacedTile(t19WestOf56);

        // first tile is occupied with a hut in zone 1
        // 10001
        assertEquals(
            "R",
            ActionEncoder.withNewOccupant(gameState, hutR191).encodedAction()
        );

        // no occupant is placed
        // 11111
        assertEquals(
            "7",
            ActionEncoder.withNewOccupant(gameState, null).encodedAction()
        );
    }

    @Test
    void withOccupantRemoved() {
        GameState gameState = initial.withStartingTilePlaced()
            .withPlacedTile(t60EastOf56)
            .withNewOccupant(pawnF601)
            .withPlacedTile(t19WestOf56)
            .withNewOccupant(pawnM192)
            .withPlacedTile(t88LEFTNorthEastOf56SHAMAN);

        // remove the occupant on tile 19
        // 00000 (as they are sorted by their zone id)
        assertEquals(
            "A",
            ActionEncoder.withOccupantRemoved(gameState, pawnM192).encodedAction()
        );

        // no occupant is removed
        // 11111
        assertEquals(
            "7",
            ActionEncoder.withOccupantRemoved(gameState, null).encodedAction()
        );
    }

    @Test
    void applyActionWorksForValidActions() {
        GameState gameState = initial.withStartingTilePlaced();

        // State: PLACE_TILE -----------------------------------------------------------------------
        // TODO


        gameState = gameState.withPlacedTile(t61NorthOf56);

        // State: OCCUPY_TILE ----------------------------------------------------------------------
        assertEquals(
                // not placing an occupant: 11111 = "7"
                gameState.withNewOccupant(null),
                ActionEncoder.applyAction(gameState, "7").state()
        );

        // TODO

        gameState = gameState.withNewOccupant(pawnM610)
                .withPlacedTile(t19WestOf56).withNewOccupant(null)
                .withPlacedTile(t88NONENorthEastOf56SHAMAN);

        // State: RETAKE_PAWN ----------------------------------------------------------------------
        assertEquals(
                // not retaking a pawn: 11111 = "7"
                gameState.withOccupantRemoved(null),
                ActionEncoder.applyAction(gameState, "7").state()
        );

        // TODO
    }

    @Test
    void applyActionReturnsNullForInvalidActions() {
        GameState gameState = initial.withStartingTilePlaced();

        // State: PLACE_TILE -----------------------------------------------------------------------
        assertNull(
            // wrong length
            ActionEncoder.applyAction(gameState, "B")
        );

        assertNull(
            // invalid fringe index: 67
            ActionEncoder.applyAction(gameState, "IM")
        );


        gameState = gameState.withPlacedTile(t61NorthOf56);

        // State: OCCUPY_TILE ----------------------------------------------------------------------
        assertNull(
                // wrong type: hut in meadow zone 0 => 10000 = "Q"
                ActionEncoder.applyAction(gameState, "Q")
        );

        assertNull(
                // invalid zoneId: 12 => 01100 = "M"
                ActionEncoder.applyAction(gameState, "M")
        );


        gameState = gameState.withNewOccupant(pawnM610)
                .withPlacedTile(t19WestOf56).withNewOccupant(null)
                .withPlacedTile(t88NONENorthEastOf56SHAMAN);

        // State: RETAKE_PAWN ----------------------------------------------------------------------
        assertNull(
                // invalid pawn index: 29 => 11101 = "5"
                ActionEncoder.applyAction(gameState, "5")
        );
    }
}