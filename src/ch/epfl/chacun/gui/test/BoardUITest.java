package ch.epfl.chacun.gui.test;

import ch.epfl.chacun.*;
import ch.epfl.chacun.gui.BoardUI;
import ch.epfl.chacun.Tiles;
import javafx.application.Application;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static ch.epfl.chacun.Direction.*;
import static ch.epfl.chacun.Occupant.Kind.HUT;
import static ch.epfl.chacun.Occupant.Kind.PAWN;
import static ch.epfl.chacun.PlayerColor.*;
import static ch.epfl.chacun.Pos.ORIGIN;
import static ch.epfl.chacun.Tiles.TILES;

public final class BoardUITest extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    PlacedTile t1WestOf56 = new PlacedTile(
            Tiles.TILES.get(1), PURPLE, Rotation.NONE, ORIGIN.neighbor(W)
    );
    Occupant pawnR11 = new Occupant(Occupant.Kind.PAWN, 11);
    PlacedTile t61NorthOf56 = new PlacedTile(
            Tiles.TILES.get(61), YELLOW, Rotation.NONE, ORIGIN.neighbor(N)
    );

    // extensive testing
    PlacedTile t11RotNoneWestOf56 = new PlacedTile(
            TILES.get(11), RED, Rotation.NONE, ORIGIN.neighbor(W)
    );

    Occupant hutL118 = new Occupant(HUT, 118);

    PlacedTile t23RotNoneEastOf56 = new PlacedTile(
            TILES.get(23), BLUE, Rotation.NONE, ORIGIN.neighbor(E)
    );
    Occupant hutR231 = new Occupant(HUT, 231);

    PlacedTile t32RotNoneNorthOf56 = new PlacedTile(
            TILES.get(32), GREEN, Rotation.NONE, ORIGIN.neighbor(Direction.N)
    );
    Occupant pawnM321 = new Occupant(PAWN, 321);

    PlacedTile t68RotNoneSouthOf56 = new PlacedTile(
            TILES.get(68), YELLOW, Rotation.NONE, ORIGIN.neighbor(Direction.S)
    );
    Occupant hutR682 = new Occupant(HUT, 682);

    PlacedTile t16RotNoneNorthEastOf56 = new PlacedTile(
            TILES.get(16), PURPLE, Rotation.NONE, ORIGIN.translated(1, -1)
    );
    Occupant pawnM162 = new Occupant(PAWN, 162);

    PlacedTile t66RotNoneSouthEastOf56 = new PlacedTile(
            TILES.get(66), RED, Rotation.NONE, ORIGIN.translated(1, 1)
    );

    PlacedTile t74RotNoneNorthWestOf56 = new PlacedTile(
            TILES.get(74), BLUE, Rotation.NONE, ORIGIN.translated(-1, -1)
    );
    Occupant pawnM740 = new Occupant(PAWN, 740);

    PlacedTile t33RotNoneSouthWestOf56 = new PlacedTile(
            TILES.get(33), GREEN, Rotation.NONE, ORIGIN.translated(-1, 1)
    );
    Occupant pawnF330 = new Occupant(PAWN, 330);

    @Override
    public void start(Stage primaryStage) throws Exception {
        Map<PlayerColor, String> playerNames = Map.of(
                PURPLE, "Leo",
                GREEN, "Mattia",
                RED, "Anthony",
                BLUE, "Ali",
                YELLOW, "Schinz"
                );

        List<PlayerColor> playerColors = playerNames.keySet().stream()
                .sorted()
                .toList();

        var tilesByKind = Tiles.TILES.stream()
                .collect(Collectors.groupingBy(Tile::kind));

        TileDecks tileDecks = new TileDecks(
                tilesByKind.get(Tile.Kind.START),
                tilesByKind.get(Tile.Kind.NORMAL),
                tilesByKind.get(Tile.Kind.MENHIR)
        );

        TextMaker textMaker = new TextMakerFr(playerNames);

        GameState gameState = GameState.initial(playerColors, tileDecks, textMaker);

        var tileToPlaceRotationP =
                new SimpleObjectProperty<>(Rotation.NONE);
        var visibleOccupantsP =
                new SimpleObjectProperty<>(Set.of(
                        hutL118,
                        hutR231,
                        hutR682,
                        pawnF330,
                        pawnM162,
                        pawnM321,
                        pawnM740

                ));
        var highlightedTilesP =
                new SimpleObjectProperty<>(Set.<Integer>of());

        var gameStateO = new SimpleObjectProperty<>(gameState);
        var boardNode = BoardUI
                .create(2,
                        gameStateO,
                        tileToPlaceRotationP,
                        visibleOccupantsP,
                        highlightedTilesP,
                        r -> System.out.println("Rotate: " + r),
                        t -> System.out.println("Place: " + t),
                        o -> System.out.println("Select: " + o));


        gameStateO.set(gameStateO.get().withStartingTilePlaced());

        var rootNode = new BorderPane(boardNode);
        primaryStage.setScene(new Scene(rootNode));

        primaryStage.setTitle("ChaCuN test");
        primaryStage.show();


/*
        gameStateO.set(gameStateO.get().withPlacedTile(t1WestOf56));
        gameStateO.set(gameStateO.get().withNewOccupant(pawnR11));
        gameStateO.set(gameStateO.get().withPlacedTile(t61NorthOf56));
        gameStateO.set(gameStateO.get().withNewOccupant(null));
*/

        gameStateO.set(gameStateO.get().withPlacedTile(t11RotNoneWestOf56));
        gameStateO.set(gameStateO.get().withNewOccupant(hutL118));
        gameStateO.set(gameStateO.get().withPlacedTile(t23RotNoneEastOf56));
        gameStateO.set(gameStateO.get().withNewOccupant(hutR231));
        gameStateO.set(gameStateO.get().withPlacedTile(t32RotNoneNorthOf56));
        gameStateO.set(gameStateO.get().withNewOccupant(pawnM321));
        gameStateO.set(gameStateO.get().withPlacedTile(t68RotNoneSouthOf56));
        gameStateO.set(gameStateO.get().withNewOccupant(hutR682));

        gameStateO.set(gameStateO.get().withPlacedTile(t16RotNoneNorthEastOf56));
        gameStateO.set(gameStateO.get().withNewOccupant(pawnM162));
        gameStateO.set(gameStateO.get().withPlacedTile(t66RotNoneSouthEastOf56));
        gameStateO.set(gameStateO.get().withNewOccupant(null));
        gameStateO.set(gameStateO.get().withPlacedTile(t74RotNoneNorthWestOf56));
        gameStateO.set(gameStateO.get().withNewOccupant(pawnM740));
        gameStateO.set(gameStateO.get().withPlacedTile(t33RotNoneSouthWestOf56));
        gameStateO.set(gameStateO.get().withNewOccupant(pawnF330));
    }
}