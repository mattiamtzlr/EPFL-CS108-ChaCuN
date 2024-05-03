package ch.epfl.chacun.stage10;

import ch.epfl.chacun.*;
import ch.epfl.chacun.tile.Tiles;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static ch.epfl.chacun.Direction.N;
import static ch.epfl.chacun.PlayerColor.*;
import static ch.epfl.chacun.Pos.ORIGIN;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

class MyActionEncoderTest {
    static Map<PlayerColor, String> playerNames = Map.of(
            PURPLE, "Leo",
            GREEN, "Mattia",
            RED, "Anthony",
            BLUE, "Ali",
            YELLOW, "Schinz"
    );

    static List<PlayerColor> playerColors = playerNames.keySet().stream()
            .sorted()
            .toList();

    static Map<Tile.Kind, List<Tile>> tilesByKind = Tiles.TILES.stream()
            .collect(Collectors.groupingBy(Tile::kind));

    static TileDecks tileDecks = new TileDecks(
            tilesByKind.get(Tile.Kind.START),
            tilesByKind.get(Tile.Kind.NORMAL),
            tilesByKind.get(Tile.Kind.MENHIR)
    );

    static TextMaker textMaker = new TextMakerFr(playerNames);

    PlacedTile t61NorthOf56 = new PlacedTile(
            Tiles.TILES.get(61), YELLOW, Rotation.HALF_TURN, ORIGIN.neighbor(N)
    );

    @Test
    void withPlacedTile() {
        GameState gameState = GameState.initial(playerColors, tileDecks, textMaker);
        gameState = gameState.withStartingTilePlaced();

        // first tile is placed to the north of the starting tile (0, -1), rotated by a half-turn
        String expected = "AG";
        assertEquals(
                expected,
                ActionEncoder.withPlacedTile(gameState, t61NorthOf56).encodedAction()
        );
    }

    @Test
    void withNewOccupant() {
    }

    @Test
    void withOccupantRemoved() {
    }
}