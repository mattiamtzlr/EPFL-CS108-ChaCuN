package ch.epfl.chacun.gui;

import ch.epfl.chacun.*;
import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.beans.property.*;
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
 * The main class that creates the entire UI as well as the event handlers.
 * These include some of the game logic concerning occupants.
 *
 * @author Mattia Metzler (372025)
 * @author Leoluca Bernardi (374107)
 */
public class Main extends Application {
    private static final int BOARD_SIZE = 12;


    /**
     * The main method. Pass the players as well as the seed (optional)
     *
     * @param args Consecutively list player names and give seed x with --seed=x. Passing a seed is
     *             optional and said seed will be chosen at random if none is provided.
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Helper method to add a new action to the observable actions list
     *
     * @param observableActions the list to add the action to
     * @param action            the action to be added
     */
    private void addAction(ObjectProperty<List<String>> observableActions, String action) {
        List<String> newActions = new ArrayList<>(observableActions.get());
        newActions.add(action);
        observableActions.set(newActions);
    }

    /**
     * The method that sets up a new game.
     *
     * @param primaryStage the primary stage for this application, onto which
     *                     the application scene can be set.
     *                     Applications may create other stages, if needed, but they will not be
     *                     primary stages.
     * @throws Exception if the game were to crash
     */
    @Override
    public void start(Stage primaryStage) throws Exception {

        //------------------------ Setting up the initial values of the game -----------------------

        primaryStage.setTitle("ChaCuN");
        primaryStage.setWidth(1440);
        primaryStage.setHeight(1080);

        List<String> playerNames = getParameters().getUnnamed();
        Preconditions.checkArgument(2 <= playerNames.size() && playerNames.size() <= 5);
        List<PlayerColor> colors = PlayerColor.ALL.subList(0, playerNames.size());

        String userSeed = getParameters().getNamed().get("seed");

        List<Tile> tiles = new ArrayList<>(Tiles.TILES);

        if (userSeed == null) {
            Collections.shuffle(tiles, RandomGeneratorFactory.getDefault().create());
        }
        else {
            long seed = Long.parseUnsignedLong(userSeed);
            Collections.shuffle(tiles, RandomGeneratorFactory.getDefault()
                    .create(seed));
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

        String cancelOccupyText = textMaker.clickToOccupy();
        String cancelRetakeText = textMaker.clickToUnoccupy();


        //--------------------- Instantiating Observable Values and Properties ---------------------

        ObjectProperty<GameState> oGameState = new SimpleObjectProperty<>(
                GameState.initial(colors, tileDecks, textMaker));

        ObjectProperty<List<String>> observableActions =
                new SimpleObjectProperty<>(Collections.emptyList());

        ObjectProperty<Set<Integer>> tileIds =
                new SimpleObjectProperty<>(Collections.emptySet());

        ObjectProperty<Rotation> tileToPlaceRotationP =
                new SimpleObjectProperty<>(Rotation.NONE);

        ObjectProperty<Set<Occupant>> visibleOccupantsP =
                new SimpleObjectProperty<>(Collections.emptySet());


        ObjectProperty<Set<Integer>> highlightedTilesP =
                new SimpleObjectProperty<>(Collections.emptySet());

        ObjectProperty<String> altText = new SimpleObjectProperty<>("");
        BooleanProperty isCancelableAction = new SimpleBooleanProperty(false);
        BooleanProperty isRetakeAction = new SimpleBooleanProperty(false);
        StringProperty cancelText = new SimpleStringProperty();

        //--------------------------------- Connecting Properties ----------------------------------

        highlightedTilesP.bind(tileIds);

        visibleOccupantsP.bind(oGameState.map(g -> {
            switch (g.nextAction()) {
                case OCCUPY_TILE -> {
                    Set<Occupant> occupyOccupants = new HashSet<>(g.board().occupants());
                    occupyOccupants.addAll(g.lastTilePotentialOccupants());
                    return occupyOccupants;
                }

                case START_GAME -> {
                    return Collections.emptySet();
                }

                default -> {
                    return g.board().occupants();
                }
            }
        }));

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


        //------------------------------------- Event Handlers -------------------------------------

        // ··········································· Event handler for canceling occupant actions
        Consumer<Occupant> cancelOccupyHandler = (_ -> {
            switch (oGameState.get().nextAction()) {
                case OCCUPY_TILE -> {
                    ActionEncoder.StateAction next = ActionEncoder.withNewOccupant(
                            oGameState.get(), null
                    );
                    oGameState.set(next.state());

                    addAction(observableActions, next.encodedAction());
                }

                case RETAKE_PAWN -> {
                    ActionEncoder.StateAction next = ActionEncoder.withOccupantRemoved(
                            oGameState.get(), null
                    );
                    oGameState.set(next.state());

                    addAction(observableActions, next.encodedAction());
                }
            }
        });

        // ······················································· Event handler for rotating tiles
        Consumer<Rotation> rotationHandler =
                r -> tileToPlaceRotationP.set(tileToPlaceRotationP.get().add(r));


        // ························································ Event handler for placing tiles
        Consumer<Pos> placeTileHandler = t -> {
            PlacedTile tileToPlace = new PlacedTile(
                    oGameState.get().tileToPlace(),
                    oGameState.get().currentPlayer(),
                    tileToPlaceRotationP.getValue(),
                    t);

            if (oGameState.get().board().canAddTile(tileToPlace)) {

                ActionEncoder.StateAction next = ActionEncoder.withPlacedTile(
                        oGameState.get(), tileToPlace
                );

                oGameState.set(next.state());

                addAction(observableActions, next.encodedAction());

                // reset rotation of tile to place
                tileToPlaceRotationP.set(Rotation.NONE);
            }
        };


        // ········································· Event handler for placing / retaking occupants
        Consumer<Occupant> occupantHandler = o -> {
            switch (oGameState.get().nextAction()) {
                case OCCUPY_TILE -> {
                    if (oGameState.get().lastTilePotentialOccupants().contains(o)) {

                        ActionEncoder.StateAction next = ActionEncoder.withNewOccupant(
                                oGameState.get(), o
                        );
                        oGameState.set(next.state());

                        addAction(observableActions, next.encodedAction());
                    }
                }

                case RETAKE_PAWN -> {
                    boolean colorCheck = oGameState.get().currentPlayer() ==
                            oGameState.get().board().tileWithId(Zone.tileId(o.zoneId())).placer();

                    if (o.kind() == Occupant.Kind.PAWN && colorCheck) {
                        ActionEncoder.StateAction next = ActionEncoder.withOccupantRemoved(
                                oGameState.get(), o
                        );
                        oGameState.set(next.state());

                        addAction(observableActions, next.encodedAction());
                    }

                }
            }
        };

        // ··········································· Event handler for adding actions to the list
        Consumer<String> actionHandler = action -> {
            ActionEncoder.StateAction next = ActionEncoder.applyAction(oGameState.get(), action);
            if (next != null) {
                oGameState.set(next.state());

                addAction(observableActions, action);
            }
        };

        //----------------------------------- Creating the Scene -----------------------------------

        Node actionsUI = ActionUI.create(
                observableActions, actionHandler
        );

        Node messageBoardUI = MessageBoardUI.create(
                oGameState.map(o -> o.messageBoard().messages()),
                tileIds
        );

        Node playersUI = PlayersUI.create(oGameState, textMaker);

        Node decksUI = DecksUI.create(
                oGameState.map(GameState::tileToPlace),
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
                occupantHandler
        );

        BorderPane root = new BorderPane(
                boardUI, null, rightPane, null, null
        );

        primaryStage.setScene(new Scene(root));

        primaryStage.show();

        oGameState.set(oGameState.get().withStartingTilePlaced());
    }
}
