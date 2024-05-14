package ch.epfl.chacun.gui;

import ch.epfl.chacun.*;
import ch.epfl.chacun.Tiles;
import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
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
        primaryStage.setWidth(1440);
        primaryStage.setHeight(1080);

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

        String cancelOccupyText = textMaker.clickToOccupy();
        String cancelRetakeText = textMaker.clickToUnoccupy();


        //--------------------------------------------Instantiating Observable Values and Properties
        SimpleObjectProperty<GameState> oGameState = new SimpleObjectProperty<>(
                GameState.initial(colors, tileDecks, textMaker));

        SimpleObjectProperty<List<String>> observableActions =
                new SimpleObjectProperty<>(new ArrayList<>());

        SimpleObjectProperty<Set<Integer>> tileIds =
                new SimpleObjectProperty<>(Collections.emptySet());

        SimpleObjectProperty<Rotation> tileToPlaceRotationP =
                new SimpleObjectProperty<>(Rotation.NONE);

        SimpleObjectProperty<Set<Occupant>> visibleOccupantsP =
                new SimpleObjectProperty<>(Collections.emptySet());


        SimpleObjectProperty<Set<Integer>> highlightedTilesP =
                new SimpleObjectProperty<>(Collections.emptySet());

        SimpleObjectProperty<String> altText = new SimpleObjectProperty<>("");
        SimpleBooleanProperty isCancelableAction = new SimpleBooleanProperty(false);
        SimpleBooleanProperty isRetakeAction = new SimpleBooleanProperty(false);
        SimpleStringProperty cancelText = new SimpleStringProperty();

        //---------------------------------------------------------------------Connecting Properties
        highlightedTilesP.bind(tileIds);



        visibleOccupantsP.bind(oGameState.map(g -> {
            switch(g.nextAction()) {
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
                }}
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


        //------------------------------------------------------------------------Building Consumers
        Consumer<Occupant> cancelOccupyHandler = (_ -> {
            switch (oGameState.get().nextAction()) {
                case OCCUPY_TILE -> {
                    ActionEncoder.StateAction next =
                        ActionEncoder.withNewOccupant(oGameState.get(), null);
                    oGameState.set(next.state());
                    List<String> nextActionsList = new ArrayList<>(observableActions.get());
                    nextActionsList.add(next.encodedAction());
                    observableActions.set(nextActionsList);
                }

                case RETAKE_PAWN -> {
                    ActionEncoder.StateAction next =
                        ActionEncoder.withOccupantRemoved(oGameState.get(), null);
                    oGameState.set(next.state());
                    List<String> nextActionsList = new ArrayList<>(observableActions.get());
                    nextActionsList.add(next.encodedAction());
                    observableActions.set(nextActionsList);
                }
            }
        });

        Consumer<Rotation> rotationHandler =
                r -> tileToPlaceRotationP.set(tileToPlaceRotationP.get().add(r));

        Consumer<Pos> placeTileHandler = t -> {

            PlacedTile tileToPlace = new PlacedTile(
                    oGameState.get().tileToPlace(),
                    oGameState.get().currentPlayer(),
                    tileToPlaceRotationP.getValue(),
                    t);
            ActionEncoder.StateAction next =
                    ActionEncoder.withPlacedTile(oGameState.get(), tileToPlace);
            oGameState.set(next.state());
            List<String> nextActionsList = new ArrayList<>(observableActions.get());
            nextActionsList.add(next.encodedAction());
            observableActions.set(nextActionsList);
            tileToPlaceRotationP.set(Rotation.NONE);
        };
        Consumer<Occupant> occupantPlacementHandler = o -> {
            switch (oGameState.get().nextAction()) {
                case OCCUPY_TILE -> {
                    ActionEncoder.StateAction next =
                        ActionEncoder.withNewOccupant(oGameState.get(), o);
                    oGameState.set(next.state());
                    List<String> nextActionsList = new ArrayList<>(observableActions.get());
                    nextActionsList.add(next.encodedAction());
                    observableActions.set(nextActionsList);

                }
                case RETAKE_PAWN -> {
                    boolean colorCheck = oGameState.get().currentPlayer() ==
                            oGameState.get().board().tileWithId(Zone.tileId(o.zoneId())).placer();

                    if (o.kind() == Occupant.Kind.PAWN && colorCheck) {

                        ActionEncoder.StateAction next =
                                ActionEncoder.withOccupantRemoved(oGameState.get(), o);
                        oGameState.set(next.state());
                        List<String> nextActionsList = new ArrayList<>(observableActions.get());
                        nextActionsList.add(next.encodedAction());
                        observableActions.set(nextActionsList);
                    }

                }
            }
        };

        Consumer<String> actionHandler = action -> {
            ActionEncoder.StateAction next = ActionEncoder.applyAction(oGameState.get(), action);
            if (next != null) {
                oGameState.set(next.state());
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
                occupantPlacementHandler
        );

        BorderPane root = new BorderPane(boardUI, null, rightPane, null, null);


        primaryStage.setScene(new Scene(root));


        primaryStage.show();
        oGameState.set(oGameState.get().withStartingTilePlaced());


    }
}
