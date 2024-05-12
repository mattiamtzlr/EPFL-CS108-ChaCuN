package ch.epfl.chacun.gui;

import ch.epfl.chacun.*;
import ch.epfl.chacun.tile.Tiles;
import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableBooleanValue;
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
 * Behold: The main game!
 *
 * @author Mattia Metzler (372025)
 * @author Leoluca Bernardi (374107)
 */
public class Main extends Application {
    private static final int BOARD_SIZE = 12;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        //-----------------------------------------------------------------------Setting up the game
        primaryStage.setTitle("ChaCuN");
        primaryStage.setMinWidth(1440);
        primaryStage.setMinHeight(1080);

        String cancelOccupyText = "Cliquez sur le pion ou la hutte que vous" +
                " désirez placer, ou ici pour ne pas en placer.";
        String cancelRetakeText = "Cliquez sur le pion que vous" +
                " désirez reprendre, ou ici pour ne pas en reprendre.";

        List<String> playerNames = getParameters().getUnnamed();
        System.out.println(STR."playing with \{playerNames}");
        Preconditions.checkArgument(2 <= playerNames.size() && playerNames.size() <= 5);
        List<PlayerColor> colors = PlayerColor.ALL.subList(0, playerNames.size());

        String userSeed = getParameters().getNamed().get("seed");

        List<Tile> tiles = new ArrayList<>(Tiles.TILES);
        // TODO remove before final
        // Trigger shaman
        tiles = tiles.stream().filter(t -> !(t.kind() == Tile.Kind.MENHIR && t.id() != 88))
                .collect(Collectors.toList());

        if (userSeed == null)
            Collections.shuffle(tiles, RandomGeneratorFactory.getDefault().create());
        else {
            long seed = Long.parseUnsignedLong(userSeed);
            Collections.shuffle(tiles, RandomGeneratorFactory.getDefault()
                    .create(seed));
            System.out.println(STR."seed by user is: \{seed}");
        }

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


        //--------------------------------------------Instantiating Observable Values and Properties
        SimpleObjectProperty<GameState> oGameState = new SimpleObjectProperty<>(
                GameState.initial(colors, tileDecks, textMaker));

        SimpleObjectProperty<List<String>> observableActions =
                new SimpleObjectProperty<>(new ArrayList<>(List.of()));

        SimpleObjectProperty<Set<Integer>> tileIds = new SimpleObjectProperty<>(Set.of());

        SimpleObjectProperty<Rotation> tileToPlaceRotationP =
                new SimpleObjectProperty<>(Rotation.NONE);

        SimpleObjectProperty<Set<Occupant>> visibleOccupantsP =
                new SimpleObjectProperty<>(new HashSet<>());

        SimpleObjectProperty<Set<Occupant>> placedOccupants =
                new SimpleObjectProperty<>(new HashSet<>());

        SimpleObjectProperty<Set<Integer>> highlightedTilesP = new SimpleObjectProperty<>(Set.of());

        SimpleObjectProperty<String> altText = new SimpleObjectProperty<>("");
        SimpleBooleanProperty isCancelableAction = new SimpleBooleanProperty(false);
        SimpleBooleanProperty isRetakeAction = new SimpleBooleanProperty(false);
        SimpleStringProperty cancelText = new SimpleStringProperty();

        //---------------------------------------------------------------------Connecting Properties
        highlightedTilesP.bind(tileIds);
        placedOccupants.bind(oGameState.map(g -> g.board().occupants()));

        isRetakeAction.bind(oGameState.map(g -> g.nextAction() == GameState.Action.RETAKE_PAWN));
        isCancelableAction.bind(oGameState.map(g ->
                g.nextAction() == GameState.Action.OCCUPY_TILE ||
                g.nextAction() == GameState.Action.RETAKE_PAWN));

        cancelText.bind(Bindings.when(isRetakeAction)
                .then(cancelRetakeText)
                .otherwise(cancelOccupyText));

        altText.bind(Bindings.when(isCancelableAction)
                .then(cancelText)
                .otherwise(""));


        //------------------------------------------------------------------------Building Consumers
        Consumer<Occupant> cancelOccupyHandler = (_ -> {
            switch (oGameState.get().nextAction()) {
                case OCCUPY_TILE -> oGameState.set(oGameState.get().withNewOccupant(null));
                case RETAKE_PAWN -> oGameState.set(oGameState.get().withOccupantRemoved(null));
            }
            visibleOccupantsP.set(placedOccupants.getValue());
        });

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
            if (oGameState.get().nextAction() == GameState.Action.OCCUPY_TILE) {
                currentlyDisplayedOccupants.addAll(oGameState.get().lastTilePotentialOccupants());
            }

            visibleOccupantsP.set(currentlyDisplayedOccupants);

            tileToPlaceRotationP.set(Rotation.NONE);
        };

        Consumer<Occupant> occupantPlacementHandler = o -> {
            switch (oGameState.get().nextAction()) {
                case OCCUPY_TILE -> {
                    oGameState.set(oGameState.get().withNewOccupant(o));
                }
                case RETAKE_PAWN -> {
                    oGameState.set(oGameState.get().withOccupantRemoved(o));
                }
                //TODO remove this
                default -> System.out.println("what do you want do do?");
            }
            visibleOccupantsP.set(placedOccupants.getValue());
        };

        Consumer<String> actionHandler = action -> {
            ActionEncoder.StateAction next = ActionEncoder.applyAction(oGameState.get(), action);
            if (next != null) {
                oGameState.set(
                        next.state()
                );

                List<String> newActions = new ArrayList<>(observableActions.get());
                newActions.add(action);
                observableActions.set(newActions);
            }
        };

        //================================================================Putting the scene together

        Node actionsUI = ActionsUI.create(
                observableActions, actionHandler
        );

        Node messageBoardUI = MessageBoardUI.create(
                oGameState.map(o -> o.messageBoard().messages()),
                tileIds
        );

        Node playersUI = PlayersUI.create(oGameState, textMaker);

        Node decksUI = DecksUI.create(
                oGameState.map(o -> o.tileDecks().topTile(Tile.Kind.NORMAL)),
                oGameState.map(o -> o.tileDecks().deckSize(Tile.Kind.NORMAL)),
                oGameState.map(o -> o.tileDecks().deckSize(Tile.Kind.MENHIR)),
                altText,
                cancelOccupyHandler
        );
        VBox bottomBox = new VBox(actionsUI, decksUI);
        bottomBox.setPrefWidth(ImageLoader.LARGE_TILE_FIT_SIZE);
        BorderPane rightPane = new BorderPane(
                messageBoardUI, playersUI, null, bottomBox, null
        );

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
