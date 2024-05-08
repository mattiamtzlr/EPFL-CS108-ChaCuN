package ch.epfl.chacun.gui;

import ch.epfl.chacun.*;
import ch.epfl.chacun.tile.Tiles;
import javafx.application.Application;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.util.*;
import java.util.function.Function;
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

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("ChaCuN -- Best Game Ever");
        primaryStage.setMinWidth(1440);
        primaryStage.setMinHeight(1080);

        List<String> playerNames = getParameters().getUnnamed();
        Preconditions.checkArgument(!playerNames.isEmpty());
        List<PlayerColor> colors = PlayerColor.ALL.subList(0, playerNames.size());

        long userSeed = Long.parseUnsignedLong(getParameters().getNamed().get("seed"));

        List<Tile> tiles = new ArrayList<>(Tiles.TILES);
        Collections.shuffle(tiles, RandomGeneratorFactory.getDefault().create(userSeed));
        Map<Tile.Kind, List<Tile>> tilesByKind = tiles.stream()
                .collect(Collectors.groupingBy(Tile::kind));


        TileDecks tileDecks = new TileDecks(
                tilesByKind.get(Tile.Kind.START),
                tilesByKind.get(Tile.Kind.NORMAL),
                tilesByKind.get(Tile.Kind.MENHIR)
        );

        Map<PlayerColor, String> players = IntStream.range(0, colors.size()).boxed()
                .collect(Collectors.toMap(colors::get, playerNames::get));

    }
}
