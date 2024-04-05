package ch.epfl.chacun.week6;

import ch.epfl.chacun.*;
import ch.epfl.chacun.tile.Tiles;
import org.junit.jupiter.api.Test;

import ch.epfl.chacun.week6.TextMakerImplementedForTests;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class GameStateTest {
    static List<Tile> tiles = Tiles.TILES;
    @Test
    void testInitial() {
        List<PlayerColor> players = List.of(PlayerColor.RED, PlayerColor.BLUE);

        List<Tile> startTiles = tiles.stream().filter(i -> i.kind() == Tile.Kind.START).toList();

        List<Tile> normalTiles = tiles.stream().filter(i -> i.kind() == Tile.Kind.NORMAL).toList();

        List<Tile> menhirTiles = tiles.stream().filter(i -> i.kind() == Tile.Kind.MENHIR).toList();



        TileDecks tileDecks = new TileDecks(startTiles, normalTiles, menhirTiles);

        GameState gameState;

        gameState = GameState.initial(players, tileDecks, new TextMakerImplementedForTests());

        assertNotNull(gameState);
        assertEquals(players, gameState.players());
        assertInstanceOf(TextMaker.class, gameState.messageBoard().textMaker());
        assertEquals(tileDecks, gameState.tileDecks());
        assertEquals(GameState.Action.START_GAME, gameState.nextAction());

    }
    @Test
    void testCurrentPlayer() {
        List<PlayerColor> players = List.of(PlayerColor.RED, PlayerColor.BLUE);

        List<Tile> startTiles = tiles.stream().filter(i -> i.kind() == Tile.Kind.START).toList();

        List<Tile> normalTiles = tiles.stream().filter(i -> i.kind() == Tile.Kind.NORMAL).toList();

        List<Tile> menhirTiles = tiles.stream().filter(i -> i.kind() == Tile.Kind.MENHIR).toList();



        TileDecks tileDecks = new TileDecks(startTiles, normalTiles, menhirTiles);

        GameState gameState = GameState.initial(players, tileDecks, new TextMakerImplementedForTests());
        assertNull(gameState.currentPlayer()); //game not started so null expected
        gameState = gameState.withStartingTilePlaced();
        assertEquals(PlayerColor.RED, gameState.currentPlayer());
    }
    @Test
    void testWithStartingTilePlaced() {
        List<PlayerColor> players = List.of(PlayerColor.RED, PlayerColor.BLUE);

        List<Tile> startTiles = tiles.stream().filter(i -> i.kind() == Tile.Kind.START).toList();

        List<Tile> normalTiles = tiles.stream().filter(i -> i.kind() == Tile.Kind.NORMAL).toList();

        List<Tile> menhirTiles = tiles.stream().filter(i -> i.kind() == Tile.Kind.MENHIR).toList();



        TileDecks tileDecks = new TileDecks(startTiles, normalTiles, menhirTiles);

        GameState gameState;

        gameState = GameState.initial(players, tileDecks, new TextMakerImplementedForTests());

        gameState = gameState.withStartingTilePlaced();

        assertEquals(GameState.Action.PLACE_TILE, gameState.nextAction());
        assertEquals(tileDecks.withTopTileDrawn(Tile.Kind.START).withTopTileDrawn(Tile.Kind.NORMAL), gameState.tileDecks());
        assertEquals(PlayerColor.RED, gameState.currentPlayer());
        assertThrows(IllegalArgumentException.class, gameState::withStartingTilePlaced);
    }
    @Test
    void testWithPlacedTileNormal(){
        List<PlayerColor> players = List.of(PlayerColor.RED, PlayerColor.BLUE);

        List<Tile> startTiles = tiles.stream().filter(i -> i.kind() == Tile.Kind.START).toList();

        List<Tile> normalTiles = tiles.stream().filter(i -> i.kind() == Tile.Kind.NORMAL).toList();

        List<Tile> menhirTiles = tiles.stream().filter(i -> i.kind() == Tile.Kind.MENHIR).toList();



        TileDecks tileDecks = new TileDecks(startTiles, normalTiles, menhirTiles);

        GameState gameState;

        gameState = GameState.initial(players, tileDecks, new TextMakerImplementedForTests());

        gameState = gameState.withStartingTilePlaced();

        PlacedTile placedTile = new PlacedTile(tiles.get(0), PlayerColor.RED, Rotation.RIGHT, new Pos(-1,0));

        gameState = gameState.withPlacedTile(placedTile);

        assertEquals(placedTile, gameState.board().lastPlacedTile());
        assertEquals(GameState.Action.OCCUPY_TILE, gameState.nextAction());
        assertEquals(PlayerColor.RED, gameState.currentPlayer());
    }
    @Test
    void testWithPlacedTileMenhir(){
        List<PlayerColor> players = List.of(PlayerColor.RED, PlayerColor.BLUE);

        List<Tile> startTiles = tiles.stream().filter(i -> i.kind() == Tile.Kind.START).toList();

        List<Tile> normalTiles = tiles.stream().filter(i -> i.kind() == Tile.Kind.NORMAL).toList();

        List<Tile> menhirTiles = tiles.stream().filter(i -> i.kind() == Tile.Kind.MENHIR).toList();

        TileDecks tileDecks = new TileDecks(startTiles, normalTiles, menhirTiles);

        GameState gameState;

        gameState = GameState.initial(players, tileDecks, new ch.epfl.chacun.week6.TextMakerImplementedForTests());

        gameState = gameState.withStartingTilePlaced();

        PlacedTile placedTile = new PlacedTile(tiles.get(93), PlayerColor.RED, Rotation.NONE, new Pos(0,-1));

        gameState = gameState.withPlacedTile(placedTile);


        assertEquals(placedTile, gameState.board().lastPlacedTile());
        assertEquals(GameState.Action.OCCUPY_TILE, gameState.nextAction());
        assertEquals(PlayerColor.RED, gameState.currentPlayer());
    }
    @Test
    void testWithPlacedTileHuntingTrap() {
        List<PlayerColor> players = List.of(PlayerColor.RED, PlayerColor.BLUE);

        List<Tile> startTiles = tiles.stream().filter(i -> i.kind() == Tile.Kind.START).toList();

        List<Tile> normalTiles = tiles.stream().filter(i -> i.kind() == Tile.Kind.NORMAL).toList();

        List<Tile> menhirTiles = tiles.stream().filter(i -> i.kind() == Tile.Kind.MENHIR).toList();

        TileDecks tileDecks = new TileDecks(startTiles, normalTiles, menhirTiles);

        GameState gameState;

        gameState = GameState.initial(players, tileDecks, new ch.epfl.chacun.week6.TextMakerImplementedForTests());

        gameState = gameState.withStartingTilePlaced();
        List<MessageBoard.Message> messages = new ArrayList<>();
        MessageBoard messageBoard = new MessageBoard(new ch.epfl.chacun.week6.TextMakerImplementedForTests(), messages);

        PlacedTile placedTile = new PlacedTile(tiles.get(94), PlayerColor.RED, Rotation.NONE, new Pos(0, -1));

        gameState = gameState.withPlacedTile(placedTile);
        Area<Zone.Meadow> adjacentMeadowArea = gameState.board().adjacentMeadow(placedTile.pos(),(Zone.Meadow) placedTile.specialPowerZone());
        messageBoard = messageBoard.withScoredHuntingTrap(PlayerColor.RED, adjacentMeadowArea);


        assertFalse(gameState.board().cancelledAnimals().isEmpty());
        assertEquals(messageBoard.messages(), gameState.messageBoard().messages());
        assertEquals(Set.of(new Animal(5600, Animal.Kind.AUROCHS)), gameState.board().cancelledAnimals());
        assertEquals(GameState.Action.OCCUPY_TILE, gameState.nextAction());
    }
    @Test
    void testWithPlacedTileWithLogBoat() {
        List<PlayerColor> players = List.of(PlayerColor.RED, PlayerColor.BLUE);

        List<Tile> startTiles = tiles.stream().filter(i -> i.kind() == Tile.Kind.START).toList();

        List<Tile> normalTiles = tiles.stream().filter(i -> i.kind() == Tile.Kind.NORMAL).toList();

        List<Tile> menhirTiles = tiles.stream().filter(i -> i.kind() == Tile.Kind.MENHIR).toList();

        TileDecks tileDecks = new TileDecks(startTiles, normalTiles, menhirTiles);

        GameState gameState;

        gameState = GameState.initial(players, tileDecks, new ch.epfl.chacun.week6.TextMakerImplementedForTests());

        gameState = gameState.withStartingTilePlaced();
        List<MessageBoard.Message> messages = new ArrayList<>();
        MessageBoard messageBoard = new MessageBoard(new ch.epfl.chacun.week6.TextMakerImplementedForTests(), messages);

        PlacedTile placedTile = new PlacedTile(tiles.get(93), PlayerColor.RED, Rotation.NONE, new Pos(-1, 0));

        gameState = gameState.withPlacedTile(placedTile);
        Area<Zone.Water> riverSystemArea = gameState.board().riverSystemArea((Zone.Water) placedTile.zoneWithId(93_8));
        messageBoard = messageBoard.withScoredLogboat(PlayerColor.RED, riverSystemArea);

        assertEquals(messageBoard.messages(), gameState.messageBoard().messages());
        assertEquals(GameState.Action.OCCUPY_TILE, gameState.nextAction());
    }
    @Test
    void testWithPlacedTileShamanWithoutPreviousPawns() {
        List<PlayerColor> players = List.of(PlayerColor.RED, PlayerColor.BLUE);

        List<Tile> startTiles = tiles.stream().filter(i -> i.kind() == Tile.Kind.START).toList();

        List<Tile> normalTiles = tiles.stream().filter(i -> i.kind() == Tile.Kind.NORMAL).toList();

        List<Tile> menhirTiles = tiles.stream().filter(i -> i.kind() == Tile.Kind.MENHIR).toList();

        TileDecks tileDecks = new TileDecks(startTiles, normalTiles, menhirTiles);

        GameState gameState;

        gameState = GameState.initial(players, tileDecks, new ch.epfl.chacun.week6.TextMakerImplementedForTests());

        gameState = gameState.withStartingTilePlaced();

        PlacedTile placedTile = new PlacedTile(tiles.get(88), PlayerColor.RED, Rotation.LEFT, new Pos(0, -1));
        gameState = gameState.withPlacedTile(placedTile);

        assertEquals(GameState.Action.OCCUPY_TILE, gameState.nextAction());
    }
    @Test
    void testWithNewOccupant() {
        //Continue from where the previous test left off
        List<PlayerColor> players = List.of(PlayerColor.RED, PlayerColor.BLUE);

        List<Tile> startTiles = tiles.stream().filter(i -> i.kind() == Tile.Kind.START).toList();

        List<Tile> normalTiles = tiles.stream().filter(i -> i.kind() == Tile.Kind.NORMAL).toList();

        List<Tile> menhirTiles = tiles.stream().filter(i -> i.kind() == Tile.Kind.MENHIR).toList();

        TileDecks tileDecks = new TileDecks(startTiles, normalTiles, menhirTiles);

        GameState gameState;

        gameState = GameState.initial(players, tileDecks, new ch.epfl.chacun.week6.TextMakerImplementedForTests());

        gameState = gameState.withStartingTilePlaced();

        PlacedTile placedTile = new PlacedTile(tiles.get(62), PlayerColor.RED, Rotation.LEFT, new Pos(0, -1));


        Occupant occupant = new Occupant(Occupant.Kind.PAWN, placedTile.meadowZones().iterator().next().id()); //only need first meadow zone

        GameState finalGameState = gameState;
        assertThrows(IllegalArgumentException.class, () -> finalGameState.withNewOccupant(occupant));

        gameState = gameState.withPlacedTile(placedTile);

        assertEquals(GameState.Action.OCCUPY_TILE, gameState.nextAction());

        gameState = gameState.withNewOccupant(occupant);
        assertEquals(GameState.Action.PLACE_TILE, gameState.nextAction());
        //red has a pawn now
        //do blue now
        assertEquals(PlayerColor.BLUE, gameState.currentPlayer());
        placedTile = new PlacedTile(tiles.get(87), PlayerColor.BLUE, Rotation.RIGHT, new Pos(1, 0));
        gameState = gameState.withPlacedTile(placedTile);

        gameState = gameState.withNewOccupant(null); //no occupant for blue

        assertEquals(Set.of(occupant), gameState.board().occupants()); //check for the existence of occupant
        assertEquals(occupant, gameState.board().tileAt(new Pos(0, -1)).occupant()); //check for position of occupant
        assertEquals(4, gameState.freeOccupantsCount(PlayerColor.RED, Occupant.Kind.PAWN));
        assertEquals(3, gameState.freeOccupantsCount(PlayerColor.RED, Occupant.Kind.HUT));
        assertEquals(5, gameState.freeOccupantsCount(PlayerColor.BLUE, Occupant.Kind.PAWN));
        assertEquals(3, gameState.freeOccupantsCount(PlayerColor.BLUE, Occupant.Kind.HUT));
    }
    @Test
    void testWithPlacedTileShamanWithPawns() {
        List<PlayerColor> players = List.of(PlayerColor.RED, PlayerColor.BLUE);

        List<Tile> startTiles = tiles.stream().filter(i -> i.kind() == Tile.Kind.START).toList();

        List<Tile> normalTiles = tiles.stream().filter(i -> i.kind() == Tile.Kind.NORMAL).toList();

        List<Tile> menhirTiles = tiles.stream().filter(i -> i.kind() == Tile.Kind.MENHIR).toList();

        TileDecks tileDecks = new TileDecks(startTiles, normalTiles, menhirTiles);

        GameState gameState;

        gameState = GameState.initial(players, tileDecks, new ch.epfl.chacun.week6.TextMakerImplementedForTests());

        gameState = gameState.withStartingTilePlaced();

        PlacedTile placedTile = new PlacedTile(tiles.get(62), PlayerColor.RED, Rotation.LEFT, new Pos(0, -1));


        Occupant occupant = new Occupant(Occupant.Kind.PAWN, placedTile.meadowZones().iterator().next().id()); //only need first meadow zone

        GameState finalGameState = gameState;
        assertThrows(IllegalArgumentException.class, () -> finalGameState.withNewOccupant(occupant));

        gameState = gameState.withPlacedTile(placedTile);

        assertEquals(GameState.Action.OCCUPY_TILE, gameState.nextAction());


        gameState = gameState.withNewOccupant(occupant);
        assertEquals(GameState.Action.PLACE_TILE, gameState.nextAction());
        //red has a pawn now
        //do blue now
        assertEquals(PlayerColor.BLUE, gameState.currentPlayer());
        placedTile = new PlacedTile(tiles.get(87), PlayerColor.BLUE, Rotation.RIGHT, new Pos(1, 0));
        gameState = gameState.withPlacedTile(placedTile);

        gameState = gameState.withNewOccupant(null); //no occupant for blue

        placedTile = new PlacedTile(tiles.get(88), PlayerColor.RED, Rotation.HALF_TURN, new Pos(0, 1));

        gameState = gameState.withPlacedTile(placedTile);

        assertEquals(GameState.Action.RETAKE_PAWN, gameState.nextAction());
    }

    @Test
    void testWithOccupantRemovedWithOccupantNull() {
        //again start off from previous test
        List<PlayerColor> players = List.of(PlayerColor.RED, PlayerColor.BLUE);

        List<Tile> startTiles = tiles.stream().filter(i -> i.kind() == Tile.Kind.START).toList();

        List<Tile> normalTiles = tiles.stream().filter(i -> i.kind() == Tile.Kind.NORMAL).toList();

        List<Tile> menhirTiles = tiles.stream().filter(i -> i.kind() == Tile.Kind.MENHIR).toList();

        TileDecks tileDecks = new TileDecks(startTiles, normalTiles, menhirTiles);

        GameState gameState;

        gameState = GameState.initial(players, tileDecks, new ch.epfl.chacun.week6.TextMakerImplementedForTests());

        gameState = gameState.withStartingTilePlaced();

        PlacedTile placedTile = new PlacedTile(tiles.get(62), PlayerColor.RED, Rotation.LEFT, new Pos(0, -1));

        Occupant occupant = new Occupant(Occupant.Kind.PAWN, placedTile.meadowZones().iterator().next().id()); //only need first meadow zone

        GameState finalGameState = gameState;
        assertThrows(IllegalArgumentException.class, () -> finalGameState.withNewOccupant(occupant));

        gameState = gameState.withPlacedTile(placedTile);

        assertEquals(GameState.Action.OCCUPY_TILE, gameState.nextAction());


        gameState = gameState.withNewOccupant(occupant);
        assertEquals(GameState.Action.PLACE_TILE, gameState.nextAction());
        //red has a pawn now
        //do blue now
        assertEquals(PlayerColor.BLUE, gameState.currentPlayer());
        placedTile = new PlacedTile(tiles.get(87), PlayerColor.BLUE, Rotation.RIGHT, new Pos(1, 0));
        gameState = gameState.withPlacedTile(placedTile);

        gameState = gameState.withNewOccupant(null); //no occupant for blue

        placedTile = new PlacedTile(tiles.get(88), PlayerColor.RED, Rotation.HALF_TURN, new Pos(0, 1));

        gameState = gameState.withPlacedTile(placedTile);

        assertEquals(GameState.Action.RETAKE_PAWN, gameState.nextAction());

        //Testing not wanting to retake pawn

        gameState = gameState.withOccupantRemoved(null);

        assertEquals(4, gameState.freeOccupantsCount(gameState.currentPlayer(), Occupant.Kind.PAWN));
        assertEquals(occupant, gameState.board().tileAt(new Pos(0, -1)).occupant());
    }@Test
    void testWithOccupantRemovedWithOccupant() {
        //again start off from previous test
        List<PlayerColor> players = List.of(PlayerColor.RED, PlayerColor.BLUE);

        List<Tile> startTiles = tiles.stream().filter(i -> i.kind() == Tile.Kind.START).toList();

        List<Tile> normalTiles = tiles.stream().filter(i -> i.kind() == Tile.Kind.NORMAL).toList();

        List<Tile> menhirTiles = tiles.stream().filter(i -> i.kind() == Tile.Kind.MENHIR).toList();

        TileDecks tileDecks = new TileDecks(startTiles, normalTiles, menhirTiles);

        GameState gameState;

        gameState = GameState.initial(players, tileDecks, new ch.epfl.chacun.week6.TextMakerImplementedForTests());

        gameState = gameState.withStartingTilePlaced();

        PlacedTile placedTile = new PlacedTile(tiles.get(62), PlayerColor.RED, Rotation.LEFT, new Pos(0, -1));


        Occupant occupant = new Occupant(Occupant.Kind.PAWN, placedTile.meadowZones().iterator().next().id()); //only need first meadow zone

        GameState finalGameState = gameState;
        assertThrows(IllegalArgumentException.class, () -> finalGameState.withNewOccupant(occupant));

        gameState = gameState.withPlacedTile(placedTile);

        assertEquals(GameState.Action.OCCUPY_TILE, gameState.nextAction());


        gameState = gameState.withNewOccupant(occupant);
        assertEquals(GameState.Action.PLACE_TILE, gameState.nextAction());
        //red has a pawn now
        //do blue now
        assertEquals(PlayerColor.BLUE, gameState.currentPlayer());
        placedTile = new PlacedTile(tiles.get(87), PlayerColor.BLUE, Rotation.RIGHT, new Pos(1, 0));
        gameState = gameState.withPlacedTile(placedTile);

        gameState = gameState.withNewOccupant(null); //no occupant for blue

        placedTile = new PlacedTile(tiles.get(88), PlayerColor.RED, Rotation.HALF_TURN, new Pos(0, 1));

        GameState finalGameState1 = gameState;
        assertThrows(IllegalArgumentException.class, () -> finalGameState1.withOccupantRemoved(null));

        gameState = gameState.withPlacedTile(placedTile);

        assertEquals(GameState.Action.RETAKE_PAWN, gameState.nextAction());

        //Testing not wanting to retake pawn
        GameState finalGameState2 = gameState;
        assertThrows(IllegalArgumentException.class, () -> finalGameState2.withOccupantRemoved(
                new Occupant(Occupant.Kind.HUT, 000) //random aah hut
        ));
        gameState = gameState.withOccupantRemoved(occupant);

        assertEquals(5, gameState.freeOccupantsCount(gameState.currentPlayer(), Occupant.Kind.PAWN));
        assertNull(gameState.board().tileAt(new Pos(0, -1)).occupant());
        assertEquals(GameState.Action.OCCUPY_TILE, gameState.nextAction());
    }
    @Test
    void testEndGame() {
        List<PlayerColor> players = List.of(PlayerColor.RED, PlayerColor.BLUE);

        List<Tile> startTiles = tiles.stream().filter(i -> i.kind() == Tile.Kind.START).toList();

        List<Tile> normalTiles = new ArrayList<>();//tiles.stream().filter(i -> i.kind() == Tile.Kind.NORMAL).toList();
        normalTiles.add(tiles.get(83));
        normalTiles.add(tiles.get(93));

        List<Tile> menhirTiles = new ArrayList<>();//tiles.stream().filter(i -> i.kind() == Tile.Kind.MENHIR).toList();

        TileDecks tileDecks = new TileDecks(startTiles, normalTiles, menhirTiles);

        GameState gameState;

        gameState = GameState.initial(players, tileDecks, new ch.epfl.chacun.week6.TextMakerImplementedForTests());

        gameState = gameState.withStartingTilePlaced();
        //tileDecks = tileDecks.withTopTileDrawn(Tile.Kind.START);
        PlacedTile placedTile = new PlacedTile(
                gameState.tileToPlace(),
                PlayerColor.RED,
                Rotation.RIGHT,
                new Pos(-1, 0)
        );
        gameState = gameState.withPlacedTile(placedTile);
        gameState = gameState.withNewOccupant(null);
        placedTile = new PlacedTile(
                tiles.get(93),
                PlayerColor.BLUE,
                Rotation.NONE,
                new Pos(0,-1)
        );
        gameState = gameState.withPlacedTile(placedTile);
        gameState = gameState.withNewOccupant(null);

        assertEquals(GameState.Action.END_GAME, gameState.nextAction());

    }
    @Test
    void testClosedForests() {
        List<PlayerColor> players = List.of(PlayerColor.RED, PlayerColor.BLUE);

        List<Tile> normalTiles = Tiles.TILES.stream().filter(t -> t.kind() == Tile.Kind.NORMAL).limit(4).toList();
        List<Tile> menhirTiles = Tiles.TILES.stream().filter(t -> t.kind() == Tile.Kind.MENHIR).toList();
        List<Tile> startTiles = Tiles.TILES.stream().filter(t -> t.kind() == Tile.Kind.START).toList();

        TileDecks tileDeck = new TileDecks(startTiles, normalTiles, menhirTiles);
        GameState state = GameState.initial(players, tileDeck, new ch.epfl.chacun.week6.TextMakerImplementedForTests());

        state = state.withStartingTilePlaced();

        PlacedTile p0 = new PlacedTile(
                state.tileToPlace(),
                state.currentPlayer(),
                Rotation.NONE,
                new Pos(1, 0)
        );

//        System.out.println(p0);

        assertEquals(0, state.tileToPlace().id());
        assertEquals( PlayerColor.RED, state.currentPlayer());

        state = state.withPlacedTile(p0);
        state = state.withNewOccupant(new Occupant(Occupant.Kind.PAWN, 4));

        PlacedTile p1 = new PlacedTile(
                state.tileToPlace(),
                state.currentPlayer(),
                Rotation.HALF_TURN,
                new Pos(0, 1)
        );

//        System.out.println(p1);
        assertEquals(1, state.tileToPlace().id());
        assertEquals(PlayerColor.BLUE, state.currentPlayer());

        state = state.withPlacedTile(p1);
        state = state.withNewOccupant(new Occupant(Occupant.Kind.PAWN, 15));

        PlacedTile p79 = new PlacedTile(
                state.tileToPlace(),
                state.currentPlayer(),
                Rotation.RIGHT,
                new Pos(1, 1)
        );



//        System.out.println(p2);
        assertEquals(79, state.tileToPlace().id() );
        assertEquals(PlayerColor.BLUE, state.currentPlayer());

        state = state.withPlacedTile(p79);
        GameState tempState = state;
        assertThrows(IllegalArgumentException.class, () -> {
            tempState.withNewOccupant(new Occupant(Occupant.Kind.PAWN, 793));
        });
        state = state.withNewOccupant(new Occupant(Occupant.Kind.HUT, 793));

        PlacedTile p2 = new PlacedTile(
                state.tileToPlace(),
                state.currentPlayer(),
                Rotation.RIGHT,
                new Pos(1, -1)
        );

        assertEquals(2, state.tileToPlace().id() );
        assertEquals(PlayerColor.RED, state.currentPlayer());

        state = state.withPlacedTile(p2);
        state = state.withNewOccupant(new Occupant(Occupant.Kind.PAWN, 21));

        PlacedTile p3 = new PlacedTile(
                state.tileToPlace(),
                state.currentPlayer(),
                Rotation.NONE,
                new Pos(-1, 1)
        );
        assertEquals(3, state.tileToPlace().id() );
        assertEquals(PlayerColor.BLUE, state.currentPlayer());

        state = state.withPlacedTile(p3);
        state = state.withNewOccupant(new Occupant(Occupant.Kind.PAWN, 33));

//        System.out.println(state.messageBoard().messages());
//        System.out.println(state.messageBoard().points());

        assertEquals(11, state.messageBoard().points().get(PlayerColor.RED));
        assertEquals(15, state.messageBoard().points().get(PlayerColor.BLUE));
    }

    @Test
    void testShaman() {
        List<PlayerColor> players = List.of(PlayerColor.RED, PlayerColor.BLUE);

        List<Tile> normalTiles = Tiles.TILES.stream().filter(t -> t.kind() == Tile.Kind.NORMAL).limit(4).toList();
        List<Tile> menhirTiles = Tiles.TILES.stream().filter(t -> t.kind() == Tile.Kind.MENHIR).toList();
        List<Tile> startTiles = Tiles.TILES.stream().filter(t -> t.kind() == Tile.Kind.START).toList();

        TileDecks tileDeck = new TileDecks(startTiles, normalTiles, menhirTiles);
        GameState state = GameState.initial(players, tileDeck, new ch.epfl.chacun.week6.TextMakerImplementedForTests());

        state = state.withStartingTilePlaced();

        PlacedTile p0 = new PlacedTile(
                state.tileToPlace(),
                state.currentPlayer(),
                Rotation.NONE,
                new Pos(1, 0)
        );

        assertEquals(0, state.tileToPlace().id());
        assertEquals( PlayerColor.RED, state.currentPlayer());

        state = state.withPlacedTile(p0);
        state = state.withNewOccupant(new Occupant(Occupant.Kind.PAWN, 4));

        PlacedTile p1 = new PlacedTile(
                state.tileToPlace(),
                state.currentPlayer(),
                Rotation.HALF_TURN,
                new Pos(0, 1)
        );

        assertEquals(1, state.tileToPlace().id());
        assertEquals(PlayerColor.BLUE, state.currentPlayer());

        state = state.withPlacedTile(p1);
        state = state.withNewOccupant(new Occupant(Occupant.Kind.PAWN, 15));

        PlacedTile shamanTile = new PlacedTile(
                Tiles.TILES.get(88),
                state.currentPlayer(),
                Rotation.RIGHT,
                new Pos(-1, 0)
        );

        assertEquals(PlayerColor.BLUE, state.currentPlayer());

        state = state.withPlacedTile(shamanTile);
        GameState throwState = state;

        assertThrows(IllegalArgumentException.class, () -> {
            throwState.withNewOccupant(new Occupant(Occupant.Kind.PAWN, 88));
        });

        state = state.withOccupantRemoved(new Occupant(Occupant.Kind.PAWN, 15));
//        System.out.println(state.board().occupants());
    }

    @Test
    void testPitTrap() {
        List<PlayerColor> players = List.of(PlayerColor.RED, PlayerColor.BLUE);

        List<Tile> normalTiles = Tiles.TILES.stream().filter(t -> t.kind() == Tile.Kind.NORMAL).limit(5).toList();
        List<Tile> menhirTiles = Tiles.TILES.stream().filter(t -> t.kind() == Tile.Kind.MENHIR).toList();
        List<Tile> startTiles = Tiles.TILES.stream().filter(t -> t.kind() == Tile.Kind.START).toList();

        TileDecks tileDeck = new TileDecks(startTiles, normalTiles, menhirTiles);
        GameState state = GameState.initial(players, tileDeck, new ch.epfl.chacun.week6.TextMakerImplementedForTests());

        state = state.withStartingTilePlaced();

        PlacedTile p0 = new PlacedTile(
                state.tileToPlace(),
                state.currentPlayer(),
                Rotation.NONE,
                new Pos(1, 0)
        );

        assertEquals( PlayerColor.RED, state.currentPlayer());

        state = state.withPlacedTile(p0);
        state = state.withNewOccupant(null);

        PlacedTile p18 = new PlacedTile(
                Tiles.TILES.get(18),
                PlayerColor.BLUE,
                Rotation.NONE,
                new Pos(-1, 0)
        );

        assertEquals( PlayerColor.BLUE, state.currentPlayer());

        state = state.withPlacedTile(p18);
        state = state.withNewOccupant(new Occupant(Occupant.Kind.PAWN, 180));

        PlacedTile p1 = new PlacedTile(
                Tiles.TILES.get(1),
                PlayerColor.RED,
                Rotation.HALF_TURN,
                new Pos(0, 1)
        );

        assertEquals( PlayerColor.RED, state.currentPlayer());

        state = state.withPlacedTile(p1);
        state = state.withNewOccupant(null);

        PlacedTile pitTrapTile = new PlacedTile(
                Tiles.TILES.get(92),
                PlayerColor.RED,
                Rotation.NONE,
                new Pos(0, -1)
        );

        assertEquals( PlayerColor.RED, state.currentPlayer());

        state = state.withPlacedTile(pitTrapTile);
        state = state.withNewOccupant(null);

        PlacedTile p19 = new PlacedTile(
                Tiles.TILES.get(19),
                PlayerColor.BLUE,
                Rotation.LEFT,
                new Pos(-1, 1)
        );

        assertEquals( PlayerColor.BLUE, state.currentPlayer());

        state = state.withPlacedTile(p19);
        state = state.withNewOccupant(null);

        PlacedTile p37 = new PlacedTile(
                Tiles.TILES.get(37),
                PlayerColor.RED,
                Rotation.NONE,
                new Pos(-1, -1)
        );

        assertEquals( PlayerColor.RED, state.currentPlayer());

        state = state.withPlacedTile(p37);
        state = state.withNewOccupant(null);



//        System.out.println(state.board().cancelledAnimals());
//        System.out.println(state.messageBoard().messages());
//        System.out.println(state.messageBoard().points());

        assertEquals(6, state.messageBoard().points().get(PlayerColor.BLUE));
    }


    @Test
    void testFire() {
        List<PlayerColor> players = List.of(PlayerColor.RED, PlayerColor.BLUE);

        List<Tile> normalTiles = Tiles.TILES.stream().filter(t -> t.kind() == Tile.Kind.NORMAL).limit(5).toList();
        List<Tile> menhirTiles = Tiles.TILES.stream().filter(t -> t.kind() == Tile.Kind.MENHIR).toList();
        List<Tile> startTiles = Tiles.TILES.stream().filter(t -> t.kind() == Tile.Kind.START).toList();

        TileDecks tileDeck = new TileDecks(startTiles, normalTiles, menhirTiles);
        GameState state = GameState.initial(players, tileDeck, new ch.epfl.chacun.week6.TextMakerImplementedForTests());

        state = state.withStartingTilePlaced();

        PlacedTile p0 = new PlacedTile(
                state.tileToPlace(),
                state.currentPlayer(),
                Rotation.NONE,
                new Pos(1, 0)
        );

        assertEquals( PlayerColor.RED, state.currentPlayer());

        state = state.withPlacedTile(p0);
        state = state.withNewOccupant(null);

        PlacedTile p18 = new PlacedTile(
                Tiles.TILES.get(18),
                PlayerColor.BLUE,
                Rotation.NONE,
                new Pos(-1, 0)
        );

        assertEquals( PlayerColor.BLUE, state.currentPlayer());

        state = state.withPlacedTile(p18);
        state = state.withNewOccupant(new Occupant(Occupant.Kind.PAWN, 180));

        PlacedTile p1 = new PlacedTile(
                Tiles.TILES.get(1),
                PlayerColor.RED,
                Rotation.HALF_TURN,
                new Pos(0, 1)
        );

        assertEquals( PlayerColor.RED, state.currentPlayer());

        state = state.withPlacedTile(p1);
        state = state.withNewOccupant(null);

        PlacedTile fireTile = new PlacedTile(
                Tiles.TILES.get(85),
                PlayerColor.RED,
                Rotation.NONE,
                new Pos(0, -1)
        );

        assertEquals( PlayerColor.RED, state.currentPlayer());


        state = state.withPlacedTile(fireTile);

        GameState throwState = state;
        assertThrows(IllegalArgumentException.class, () -> {
            throwState.withNewOccupant(null);
        });

        PlacedTile p19 = new PlacedTile(
                Tiles.TILES.get(19),
                PlayerColor.BLUE,
                Rotation.LEFT,
                new Pos(-1, 1)
        );

        assertEquals( PlayerColor.BLUE, state.currentPlayer());

        state = state.withPlacedTile(p19);
        state = state.withNewOccupant(null);

        PlacedTile p37 = new PlacedTile(
                Tiles.TILES.get(37),
                PlayerColor.RED,
                Rotation.NONE,
                new Pos(-1, -1)
        );

        assertEquals( PlayerColor.RED, state.currentPlayer());

        state = state.withPlacedTile(p37);
        state = state.withNewOccupant(null);



//        System.out.println(state.board().cancelledAnimals());
//        System.out.println(state.messageBoard().messages());
//        System.out.println(state.messageBoard().points());

        assertEquals(4, state.messageBoard().points().get(PlayerColor.BLUE));
    }


    @Test
    void testHuntingTrap() {
        List<PlayerColor> players = List.of(PlayerColor.RED, PlayerColor.BLUE);

        List<Tile> normalTiles = Tiles.TILES.stream().filter(t -> t.kind() == Tile.Kind.NORMAL).limit(5).toList();
        List<Tile> menhirTiles = Tiles.TILES.stream().filter(t -> t.kind() == Tile.Kind.MENHIR).toList();
        List<Tile> startTiles = Tiles.TILES.stream().filter(t -> t.kind() == Tile.Kind.START).toList();

        TileDecks tileDeck = new TileDecks(startTiles, normalTiles, menhirTiles);
        GameState state = GameState.initial(players, tileDeck, new ch.epfl.chacun.week6.TextMakerImplementedForTests());

        state = state.withStartingTilePlaced();

        PlacedTile p0 = new PlacedTile(
                state.tileToPlace(),
                state.currentPlayer(),
                Rotation.NONE,
                new Pos(1, 0)
        );

        assertEquals( PlayerColor.RED, state.currentPlayer());

        state = state.withPlacedTile(p0);
        state = state.withNewOccupant(null);

        PlacedTile p18 = new PlacedTile(
                Tiles.TILES.get(18),
                PlayerColor.BLUE,
                Rotation.NONE,
                new Pos(-1, 0)
        );

        assertEquals( PlayerColor.BLUE, state.currentPlayer());

        state = state.withPlacedTile(p18);
        state = state.withNewOccupant(new Occupant(Occupant.Kind.PAWN, 180));

        PlacedTile p1 = new PlacedTile(
                Tiles.TILES.get(1),
                PlayerColor.RED,
                Rotation.HALF_TURN,
                new Pos(0, 1)
        );

        assertEquals( PlayerColor.RED, state.currentPlayer());

        state = state.withPlacedTile(p1);
        state = state.withNewOccupant(null);

        PlacedTile huntingTrapTile = new PlacedTile(
                Tiles.TILES.get(94),
                PlayerColor.RED,
                Rotation.NONE,
                new Pos(0, -1)
        );

        assertEquals( PlayerColor.RED, state.currentPlayer());


        state = state.withPlacedTile(huntingTrapTile);

        state = state.withNewOccupant(null);

        PlacedTile p19 = new PlacedTile(
                Tiles.TILES.get(19),
                PlayerColor.BLUE,
                Rotation.LEFT,
                new Pos(-1, 1)
        );

        assertEquals( PlayerColor.BLUE, state.currentPlayer());

        state = state.withPlacedTile(p19);
        state = state.withNewOccupant(null);

        PlacedTile p37 = new PlacedTile(
                Tiles.TILES.get(37),
                PlayerColor.RED,
                Rotation.NONE,
                new Pos(-1, -1)
        );

        assertEquals( PlayerColor.RED, state.currentPlayer());

        state = state.withPlacedTile(p37);
        state = state.withNewOccupant(null);



//        System.out.println(state.board().cancelledAnimals());
//        System.out.println(state.messageBoard().messages());
//        System.out.println(state.messageBoard().points());

        System.out.println("This will fail until we are able to add the cancelled animals into the right method");
        assertEquals(2, state.messageBoard().points().get(PlayerColor.RED));
        assertEquals(1, state.messageBoard().points().get(PlayerColor.BLUE));
    }

    @Test
    void testRaftAndLogBoat() {
        List<PlayerColor> players = List.of(PlayerColor.RED, PlayerColor.BLUE);

        List<Tile> normalTiles = Tiles.TILES.stream().filter(t -> t.kind() == Tile.Kind.NORMAL).limit(4).toList();
        List<Tile> menhirTiles = Tiles.TILES.stream().filter(t -> t.kind() == Tile.Kind.MENHIR).toList();
        List<Tile> startTiles = Tiles.TILES.stream().filter(t -> t.kind() == Tile.Kind.START).toList();

        TileDecks tileDeck = new TileDecks(startTiles, normalTiles, menhirTiles);
        GameState state = GameState.initial(players, tileDeck, new TextMakerImplementedForTests());

        state = state.withStartingTilePlaced();

        PlacedTile p0 = new PlacedTile(
                state.tileToPlace(),
                state.currentPlayer(),
                Rotation.NONE,
                new Pos(1, 0)
        );

        assertEquals( PlayerColor.RED, state.currentPlayer());

        state = state.withPlacedTile(p0);
        state = state.withNewOccupant(null);

        PlacedTile p1 = new PlacedTile(
                Tiles.TILES.get(1),
                PlayerColor.BLUE,
                Rotation.HALF_TURN,
                new Pos(0, 1)
        );

        assertEquals( PlayerColor.BLUE, state.currentPlayer());

        state = state.withPlacedTile(p1);
        state = state.withNewOccupant(null);

        PlacedTile raftTile = new PlacedTile(
                Tiles.TILES.get(91),
                PlayerColor.BLUE,
                Rotation.HALF_TURN,
                new Pos(-1, 0)
        );

        assertEquals( PlayerColor.BLUE, state.currentPlayer());

        state = state.withPlacedTile(raftTile);
        state = state.withNewOccupant(null);

        PlacedTile p2 = new PlacedTile(
                Tiles.TILES.get(2),
                PlayerColor.RED,
                Rotation.HALF_TURN,
                new Pos(1, 1)
        );

        assertEquals( PlayerColor.RED, state.currentPlayer());

        state = state.withPlacedTile(p2);
        state = state.withNewOccupant(null);

        PlacedTile p4 = new PlacedTile(
                Tiles.TILES.get(4),
                PlayerColor.BLUE,
                Rotation.HALF_TURN,
                new Pos(2, 1)
        );

        assertEquals( PlayerColor.BLUE, state.currentPlayer());

        state = state.withPlacedTile(p4);
        state = state.withNewOccupant(null);

        PlacedTile logBoatTile = new PlacedTile(
                Tiles.TILES.get(93),
                PlayerColor.BLUE,
                Rotation.NONE,
                new Pos(-1, 1)
        );

        assertEquals( PlayerColor.BLUE, state.currentPlayer());

        state = state.withPlacedTile(logBoatTile);
        state = state.withNewOccupant(new Occupant(Occupant.Kind.HUT, 938));

//        System.out.println(state.board().cancelledAnimals());
//        System.out.println(state.messageBoard().messages());
//        System.out.println(state.messageBoard().points());

        assertEquals(22, state.messageBoard().points().get(PlayerColor.BLUE));
    }


}
