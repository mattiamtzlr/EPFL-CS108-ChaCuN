package ch.epfl.chacun;

import java.util.*;

/**
 * TODO Description
 *
 * @author Mattia Metzler (372025)
 * @author Leoluca Bernardi (374107)
 */
public class Board {
    private final PlacedTile[] placedTiles;
    private final int[] placedTileIndices;
    private final ZonePartitions zonePartitions;
    private final Set<Animal> cancelledAnimals;

    public static final int REACH = 12;
    public static final Board EMPTY = new Board(
            new PlacedTile[625],
            new int[]{}, ZonePartitions.EMPTY,
            Collections.emptySet()
    );

    private Board(PlacedTile[] placedTiles, int[] placedTileIndices, ZonePartitions zonePartitions, Set<Animal> cancelledAnimals) {
        this.placedTiles = placedTiles;
        this.placedTileIndices = placedTileIndices;
        this.zonePartitions = zonePartitions;
        this.cancelledAnimals = cancelledAnimals;
    }

    public PlacedTile tileAt(Pos pos) {
        return placedTiles[calculateTileIndex(pos)];
    }

    private static int calculateTileIndex(Pos pos) {
        return (pos.x() + REACH) + (REACH + pos.y())*25;
    }

    public PlacedTile tileWithId(int tileId) {
        for (int placedTileIndex : placedTileIndices) {
            if (placedTiles[placedTileIndex].id() == tileId)
                return placedTiles[placedTileIndex];
        }
        throw new IllegalArgumentException("No tile with such index");
    }
    public Set<Animal> cancelledAnimals(){
        return Set.copyOf(cancelledAnimals);
    }

    public Set<Occupant> occupants(){
        Set<Occupant> occupants = new HashSet<>();
        for (int placedTileIndex : placedTileIndices) {
            occupants.add(placedTiles[placedTileIndex].occupant());

        }
        return occupants;
    }
    public Area<Zone.Forest> forestArea(Zone.Forest forest) {
        return zonePartitions.forests().areaContaining(forest);
    }
    public Area<Zone.Meadow> meadowArea(Zone.Meadow meadow) {
        return zonePartitions.meadows().areaContaining(meadow);
    }
    public Area<Zone.River> riverArea(Zone.River river) {
        return zonePartitions.rivers().areaContaining(river);
    }
    public Area<Zone.Water> riverSystemArea(Zone.Water water) {
        return zonePartitions.riverSystems().areaContaining(water);
    }
    public Set<Area<Zone.Meadow>> meadowAreas() {
        return zonePartitions.meadows().areas();
    }
    public Set<Area<Zone.Water>> riverSystemAreas() {
        return zonePartitions.riverSystems().areas();
    }
    public Area<Zone.Meadow> adjacentMeadow(Pos pos, Zone.Meadow meadowZone) {
        Set<Zone.Meadow> neighboringMeadowZones = new HashSet<>();
        for (PlacedTile placedTile : getTileNeighbourhood(pos)) {
            neighboringMeadowZones.addAll(placedTile.meadowZones());
        }
        Set<Zone.Meadow> zones = new HashSet<>(Set.copyOf(meadowArea(meadowZone).zones()));
        zones.retainAll(neighboringMeadowZones);

        return new Area<>(zones, meadowArea(meadowZone).occupants(), 0);
    }

    // TODO This needs some improvements
    private Set<PlacedTile> getTileNeighbourhood(Pos pos) {
        return Set.of(
                tileAt(pos),
                tileAt(pos.translated(-1, -1)),
                tileAt(pos.translated(0, -1)),
                tileAt(pos.translated(1, -1)),
                tileAt(pos.translated(1, 0)),
                tileAt(pos.translated(1, 1)),
                tileAt(pos.translated(0, 1)),
                tileAt(pos.translated(-1, 1)),
                tileAt(pos.translated(-1, 0))
        );
    }
    public int occupantCount(PlayerColor player, Occupant.Kind occupantKind) {
        int occupantCount = 0;
        switch (occupantKind) {
            case PAWN -> {
                for (Area<Zone.Meadow> area : zonePartitions.meadows().areas()) {
                    for (PlayerColor occupantColor: area.occupants()) {
                        occupantCount += (occupantColor.equals(player)) ? 1 : 0;
                    }
                }
                for (Area<Zone.Forest> area : zonePartitions.forests().areas()) {
                    for (PlayerColor occupantColor: area.occupants()) {
                        occupantCount += (occupantColor.equals(player)) ? 1 : 0;
                    }
                }
                for (Area<Zone.River> area : zonePartitions.rivers().areas()) {
                    for (PlayerColor occupantColor: area.occupants()) {
                        occupantCount += (occupantColor.equals(player)) ? 1 : 0;
                    }
                }
            }
            case HUT -> {
                for (Area<Zone.Water> area : zonePartitions.riverSystems().areas()) {
                    for (PlayerColor occupantColor: area.occupants()) {
                        occupantCount += (occupantColor.equals(player)) ? 1 : 0;
                    }
                }
            }
        }
        return occupantCount;
    }

    public Set<Pos> insertionPositions() {
        // TODO mol fett go teste
        Set<PlacedTile> insertionTiles = new HashSet<>();
        for (int placedTileIndex : placedTileIndices) {
            insertionTiles.addAll(getTileCross(placedTiles[placedTileIndex].pos()));
        }
        Arrays.asList(placedTiles).forEach(insertionTiles::remove);
        Set<Pos> insertionPositions = new HashSet<>();
        for (PlacedTile insertionTile : insertionTiles) {
            insertionPositions.add(insertionTile.pos());
        }
        return insertionPositions;

    }
    private Set<PlacedTile> getTileCross(Pos pos) {
        return Set.of(
                tileAt(pos.neighbor(Direction.N)),
                tileAt(pos.neighbor(Direction.E)),
                tileAt(pos.neighbor(Direction.S)),
                tileAt(pos.neighbor(Direction.W))
        );
    }

    public PlacedTile lastPlacedTile() {
        // TODO testing much important yes
        // This will never work like this like what
        if (this.equals(EMPTY))
            return null;

        return placedTiles[placedTileIndices[placedTileIndices.length-1]];
        /*
        ** NOW LEGACY DUE TO CHANGES TO PLACEDTILESINDICES **
        List<PlacedTile> tileList = new ArrayList<>();
        for (int placedTileIndex : placedTileIndices) {
            if (placedTiles[placedTileIndex] != null)
                tileList.add((placedTiles[placedTileIndex]));
        }
        return tileList.getLast();
        */
    }
    public Set<Area<Zone.Forest>> forestsClosedByLastTile() {
        Set<Area<Zone.Forest>> closedForestAreas = new HashSet<>();
        for (Zone.Forest forestZone : lastPlacedTile().forestZones()) {
            if (forestArea(forestZone).isClosed())
                closedForestAreas.add(forestArea(forestZone));
        }
        return closedForestAreas;
    }
    public Set<Area<Zone.River>> riversClosedByLastTile() {
        Set<Area<Zone.River>> closedRiverAreas = new HashSet<>();
        for (Zone.River riverZone : lastPlacedTile().riverZones()) {
            if (riverArea(riverZone).isClosed())
                closedRiverAreas.add(riverArea(riverZone));
        }
        return closedRiverAreas;
    }
    public boolean canAddTile(PlacedTile tile) {
        // TODO this might have to account for rotation and stuff, have to test
        if (!insertionPositions().contains(tile.pos()))
            return false;

        for (Direction direction : Direction.ALL) {
            if (!tile.side(direction).isSameKindAs(
                    tileAt(tile.pos().neighbor(direction))
                            .side(direction.opposite())))
                return false;
        }
        return true;
    }

    public boolean couldPlaceTile(Tile tile) {
        for (Pos insertionPosition : insertionPositions()) {
            for (Rotation rotation : Rotation.ALL) {
                // TODO Check if the placer parameter really can be null without causing problems
                if (canAddTile(new PlacedTile(tile, null, rotation , insertionPosition)))
                    return true;
            }
        }
        return false;
    }

    public Board withNewTile(PlacedTile tile) {
        /* TODO The wording makes it seem like we should call couldPlaceTile,
            but they pass a placedTile???
            Why can they not just give clear instructions. Like do I have to add
            to the zone partition too ??? Should this tile be connected  to any sides ????
            What
         */
        //=======================================================================
        //==                            VERSION 2                              ==
        //==                      THE SWELTERING DISARRAY                      ==
        //=======================================================================

        // CouldPlaceTile makes no sense here as per the rules, a tile which cannot be added to
        /* The board is supposed to be imediately removed from the game
        if (!couldPlaceTile(tile.tile()))
            throw new IllegalArgumentException("This tile cannot be placed");
         */
        if (!canAddTile(tile))
            throw new IllegalArgumentException("Cannot add tile at this position");

        // This part seems pretty right to me, the cloning should be safe
        PlacedTile[] placedTilesWithNewTile = placedTiles.clone();
        placedTilesWithNewTile[calculateTileIndex(tile.pos())] = tile;

        // This part might need refactoring since I changed the way placedTileIndices works
        // in order to get this to work :)
        // TODO Rename placedTileIndicesWithNewTile, this is too long
        int[] placedTileIndicesWithNewTile = new int[placedTileIndices.length+1];
        System.arraycopy(
                placedTileIndices, 0,
                placedTileIndicesWithNewTile, 0,
                placedTileIndices.length);
        placedTileIndicesWithNewTile[placedTileIndicesWithNewTile.length-1] =
                calculateTileIndex(tile.pos());

        // I have no clue if this part is actually necessary, they do not mention anything
        // but it would be very stupid to do this someplace else
        ZonePartitions.Builder builder = new ZonePartitions.Builder(zonePartitions);
        builder.addTile(tile.tile());
        for (Direction direction : Direction.ALL) {
            if (tile.pos().neighbor(direction) != null) {
                builder.connectSides(
                        tile.side(direction),
                        tileAt(tile.pos().neighbor(direction)).side(direction.opposite()));
            }
        }

        return new Board(
                placedTilesWithNewTile, placedTileIndicesWithNewTile,
                builder.build(), cancelledAnimals()
        );

    }

    public Board withOccupant(Occupant occupant) {
        //                           ??? ●﹏● ???

        /* Step 1:
            Figure out which tile and area we should add the occupant to
            First only for tĥe case of pawns
        */
        PlacedTile targetTile = tileWithId(Zone.tileId(occupant.zoneId()));

        Zone targetZone = targetTile.zoneWithId(occupant.zoneId());

        // This might work ???????? no clue
        Area targetArea;
        if(occupant.kind() == Occupant.Kind.PAWN) {
            switch(targetZone) {
                case Zone.Forest(int id, Zone.Forest.Kind kind) -> {
                    targetArea = forestArea((Zone.Forest) targetZone);
                }
                case Zone.Meadow(int id, List<Animal> animals, Zone.SpecialPower specialPower) -> {
                    targetArea = meadowArea((Zone.Meadow) targetZone);
                }
                case Zone.River(int id, int fishCount, Zone.Lake lake) -> {
                    targetArea = riverArea((Zone.River) targetZone);
                }
                default -> throw new IllegalStateException("Unexpected value: " + targetZone.id());
            }
        } else {
            targetArea = riverSystemArea((Zone.Water) targetZone);
        }

        /* Step 2:
            Add the occupant to all the places it belongs to
                - How should an occupant be added to the partition if we do not get the player ?
            TODO this version is purposefully neither concise nor compact, but much more readable.
             We need to refactor once all the logic is in place
        */
        PlayerColor occupantColor = PlayerColor.RED; // TODO here we have to find the color, this is wip

        targetArea = targetArea.withInitialOccupant(occupantColor);

        placedTiles[calculateTileIndex(targetTile.pos())].withOccupant(occupant);

        // TODO return a new board while maintaining immutability
        Board boardWithOccupant = new Board()



    }



}
