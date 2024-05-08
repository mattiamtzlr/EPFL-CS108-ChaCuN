package ch.epfl.chacun.gui;

import ch.epfl.chacun.*;
import ch.epfl.chacun.tile.Tiles;
import javafx.application.Application;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.*;
import java.util.function.Consumer;
import java.util.random.RandomGeneratorFactory;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * TODO Description
 *
 * @author Mattia Metzler (372025)
 * @author Leoluca Bernardi (374107)
 */
public class Main extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    private static int BOARD_SIZE = 3;

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("ChaCuN -- Best Game Ever");
        primaryStage.setMinWidth(1440);
        primaryStage.setMinHeight(1080);


        // Creating an observable GameState
        List<String> playerNames = getParameters().getUnnamed();
        System.out.println(STR."playing with \{playerNames}");
        Preconditions.checkArgument(!playerNames.isEmpty());
        List<PlayerColor> colors = PlayerColor.ALL.subList(0, playerNames.size());

        long userSeed = Long.parseUnsignedLong(getParameters().getNamed().get("seed"));
        System.out.println(STR."seed by user is: \{userSeed}");

        List<Tile> tiles = new ArrayList<>(Tiles.TILES);
        Collections.shuffle(tiles, RandomGeneratorFactory.getDefault().create(userSeed));
        Map<Tile.Kind, List<Tile>> tilesByKind = tiles.stream()
                .collect(Collectors.groupingBy(Tile::kind));


        TileDecks tileDecks = new TileDecks(
                tilesByKind.get(Tile.Kind.START),
                tilesByKind.get(Tile.Kind.NORMAL),
                tilesByKind.get(Tile.Kind.MENHIR)
        );



        Map<PlayerColor, String> players = IntStream.range(0, colors.size())
                .boxed()
                .collect(Collectors.toMap(colors::get, playerNames::get));

        TextMaker textMaker = new TextMakerFr(players);

        SimpleObjectProperty<GameState> oGameState = new SimpleObjectProperty<>(
                GameState.initial(colors, tileDecks, textMaker));

        // Setting the scene
        // TODO this is only temporary, want to see what it looks like :)

        List<String> actions = new ArrayList<>(List.of());

        SimpleObjectProperty<List<String>> observableActions = new SimpleObjectProperty<>(actions);
        Node actionsUI = ActionsUI.create(
                observableActions, System.out::println);


        SimpleObjectProperty<String> altText = new SimpleObjectProperty<>("");

        Consumer<Occupant> consumer = (_ -> {
            System.out.println("Pawn action cancelled.");
            altText.set("");
        });

        altText.set("Click to cancel pawn action.");


        Node decksUI = DecksUI.create(
                oGameState.map(o -> o.tileDecks().topTile(Tile.Kind.NORMAL)),
                oGameState.map(o -> o.tileDecks().deckSize(Tile.Kind.NORMAL)),
                oGameState.map(o -> o.tileDecks().deckSize(Tile.Kind.MENHIR)),
                altText,
                consumer
        );


        VBox bottomBox = new VBox(actionsUI, decksUI);
        bottomBox.setPrefWidth(ImageLoader.LARGE_TILE_FIT_SIZE);

        SimpleObjectProperty<Set<Integer>> tileIds = new SimpleObjectProperty<>();
        Node messageBoardUI = MessageBoardUI.create(
                oGameState.map(o -> o.messageBoard().messages()),
                tileIds
        );

        Node playersUI = PlayersUI.create(oGameState, textMaker);

        BorderPane rightPane = new BorderPane(
                messageBoardUI, playersUI, null, bottomBox, null
        );

        SimpleObjectProperty<Rotation> tileToPlaceRotationP =
                new SimpleObjectProperty<>(Rotation.NONE);
        SimpleObjectProperty<Set<Occupant>> visibleOccupantsP =
                new SimpleObjectProperty<>(new HashSet<>());
        SimpleObjectProperty<Set<Integer>> highlightedTilesP = new SimpleObjectProperty<>(Set.of());

        Consumer<Rotation> rotationHandler =
                r -> tileToPlaceRotationP.set(tileToPlaceRotationP.get().add(r));
        Consumer<Pos> placeTileHandler = t -> {
            PlacedTile tileToPlace = new PlacedTile(
                    oGameState.get().tileToPlace(),
                    oGameState.get().currentPlayer(),
                    tileToPlaceRotationP.getValue(),
                    t);
            oGameState.set(oGameState.get().withPlacedTile(tileToPlace));
            Set<Occupant> currentlyDisplayedOccupants = new HashSet<>(visibleOccupantsP.getValue());

            currentlyDisplayedOccupants.addAll(oGameState.get().lastTilePotentialOccupants());

            visibleOccupantsP.set(currentlyDisplayedOccupants);

        };
        Consumer<Occupant> occupantPlacementHandler = o -> {
            Set<Occupant> currentlyDisplayedOccupants = new HashSet<>(visibleOccupantsP.getValue());
            currentlyDisplayedOccupants.removeAll(oGameState.get().lastTilePotentialOccupants());
            oGameState.set(oGameState.get().withNewOccupant(o));
            currentlyDisplayedOccupants.add(o);
            visibleOccupantsP.set(currentlyDisplayedOccupants);
        };

        Node boardUI = BoardUI.create(
                BOARD_SIZE,
                oGameState,
                tileToPlaceRotationP,
                visibleOccupantsP,
                highlightedTilesP,
                rotationHandler,
                placeTileHandler,
                occupantPlacementHandler
                );

        BorderPane root = new BorderPane(boardUI, null, rightPane, null, null);


        primaryStage.setScene(new Scene(root));


        primaryStage.show();
        oGameState.set(oGameState.get().withStartingTilePlaced());


    }
}
