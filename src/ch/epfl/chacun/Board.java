package ch.epfl.chacun;

import java.util.*;
import java.util.stream.Collectors;

/**
 * TODO Description
 *
 * @author Mattia Metzler (372025)
 * @author Leoluca Bernardi (374107)
 */
public final class Board {
    private final PlacedTile[] placedTiles;
    private final int[] placedTileIndices;
    private final ZonePartitions zonePartitions;
    private final Set<Animal> cancelledAnimals;

    public static final int REACH = 12;
    private static final int NUMBER_OF_POSITIONS = 625;
    private static final int TOTAL_TILES_AVAILABLE = 95;

    /**
     * A preset for an empty board
     */
    public static final Board EMPTY = new Board(
            new PlacedTile[NUMBER_OF_POSITIONS],
            new int[]{}, ZonePartitions.EMPTY,
            Collections.emptySet()
    );

    private Board(PlacedTile[] placedTiles, int[] placedTileIndices, ZonePartitions zonePartitions, Set<Animal> cancelledAnimals) {
        this.placedTiles = placedTiles;
        this.placedTileIndices = placedTileIndices;
        this.zonePartitions = zonePartitions;
        this.cancelledAnimals = cancelledAnimals;
    }

    /**
     * A method to get the tile at a specific position
     * @param pos The position at which we search for the tile
     * @return The tile if it was found, otherwise returns null
     */
    public PlacedTile tileAt(Pos pos) {
        return (Math.abs(pos.x()) < 13 &&
                Math.abs(pos.y()) < 13) ?
                placedTiles[calculateTileIndex(pos)]
                : null;
    }

    /**
     * Calculate the index corresponding to some position
     * @param pos The position to convert from
     * @return The index that corresponds to position
     */
    private static int calculateTileIndex(Pos pos) {
        return (pos.x() + REACH) + (REACH + pos.y()) * 25;
    }

    /**
     * Get the tile that has a specific id
     * @param tileId The id we want to lookup
     * @throws IllegalArgumentException if there is no tile with the given id
     * @return The tile with the id that was passed
     */
    public PlacedTile tileWithId(int tileId) {
        for (int placedTileIndex : placedTileIndices) {
            if (placedTiles[placedTileIndex].id() == tileId)
                return placedTiles[placedTileIndex];
        }
        throw new IllegalArgumentException("No tile with such index");
    }

    /**
     * Method to ensure immutability of cancelledAnimals
     * @return An immutable copy oc cancelledAnimals
     */
    public Set<Animal> cancelledAnimals() {
        return Set.copyOf(cancelledAnimals);
    }

    /**
     * Method to find all occupants on the board
     * @return The set of occupants present on the board (Not PlayerColor)
     */
    public Set<Occupant> occupants() {
        return Arrays.stream(placedTiles.clone())
                .filter(Objects::nonNull)
                .map(PlacedTile::occupant)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
    }

    /**
     * Method to get the area the zone belongs to
     * @param forest The zone of which we want to query the area
     * @return The area that was found
     */
    public Area<Zone.Forest> forestArea(Zone.Forest forest) {
        return zonePartitions.forests().areaContaining(forest);
    }

    /**
     * Method to get the area the zone belongs to
     * @param meadow The zone of which we want to find the area
     * @return The area that was found
     */
    public Area<Zone.Meadow> meadowArea(Zone.Meadow meadow) {
        return zonePartitions.meadows().areaContaining(meadow);
    }

    /**
     * Method to get the area the zone belongs to
     * @param river The zone of which we want to find the area
     * @return The area that was found
     */
    public Area<Zone.River> riverArea(Zone.River river) {
        return zonePartitions.rivers().areaContaining(river);
    }

    /**
     * Method to get the area the zone belongs to
     * @param water The zone of which we want to find the area
     * @return The area that was found
     */
    public Area<Zone.Water> riverSystemArea(Zone.Water water) {
        return zonePartitions.riverSystems().areaContaining(water);
    }

    /**
     * Method to get all meadow areas
     * @return A set of all meadow areas
     */
    public Set<Area<Zone.Meadow>> meadowAreas() {
        return zonePartitions.meadows().areas();
    }

    /**
     * Method to get all riverSystem areas
     * @return A set of all riverSystem areas
     */
    public Set<Area<Zone.Water>> riverSystemAreas() {
        return zonePartitions.riverSystems().areas();
    }

    /**
     * Generate a set of meadow adjacent to a meadow zone
     * @param pos The position at which the zone is located
     * @param meadowZone The zone around which we want the adjacent meadow
     * @return The area containing all meadow zones that area both connected to the
     *         zone that was passed and are not further away than one tile
     */
    public Area<Zone.Meadow> adjacentMeadow(Pos pos, Zone.Meadow meadowZone) {

        Set<Zone.Meadow> neighboringMeadowZones = new HashSet<>();
        for (PlacedTile placedTile : getTileAdjacentPositions(pos)) {
            neighboringMeadowZones.addAll(placedTile.meadowZones());
        }
        Set<Zone.Meadow> zones = new HashSet<>(Set.copyOf(meadowArea(meadowZone).zones()));
        zones.retainAll(neighboringMeadowZones);
        zones.add(meadowZone);

        return new Area<>(zones, meadowArea(meadowZone).occupants(), 0);
    }

    private Set<PlacedTile> getTileAdjacentPositions(Pos pos) {
        Set<PlacedTile> returnSet = getNeighborTiles(pos);
        returnSet.add(tileAt(pos.translated(-1, -1)));
        returnSet.add(tileAt(pos.translated(1, -1)));
        returnSet.add(tileAt(pos.translated(-1, 1)));
        returnSet.add(tileAt(pos.translated(1, 1)));
        returnSet.removeIf(Objects::isNull);

        return returnSet;
    }

    private static <Z extends Zone> int countOccupantsInArea(Area<Z> area, PlayerColor player) {
        int occupantCount = 0;
        for (PlayerColor occupantColor : area.occupants()) {
            occupantCount += (occupantColor.equals(player)) ? 1 : 0;
        }
        return occupantCount;
    }

    /**
     * Method to count the occupants of a given kind placed by a given player
     * @param player The color of the player we inquire about
     * @param occupantKind The kind of occupant that we count
     * @return The number of occupants of a given kind placed by a given player
     */
    public int occupantCount(PlayerColor player, Occupant.Kind occupantKind) {
        int occupantCount = 0;
        switch (occupantKind) {
            case PAWN -> {
                for (Area<Zone.Meadow> area : meadowAreas()) {
                    occupantCount += countOccupantsInArea(area, player);
                }
                for (Area<Zone.Forest> area : zonePartitions.forests().areas()) {
                    occupantCount += countOccupantsInArea(area, player);
                }
                for (Area<Zone.River> area : zonePartitions.rivers().areas()) {
                    occupantCount += countOccupantsInArea(area, player);
                }
            }
            case HUT -> {
                for (Area<Zone.Water> area : riverSystemAreas()) {
                    occupantCount += countOccupantsInArea(area, player);
                }
            }
        }
        return occupantCount;
    }

    /**
     * A method to get all positions at which a new tile could be added to the board
     * @return The set of possible insertion positions
     */
    public Set<Pos> insertionPositions() {
        if (this.equals(Board.EMPTY))
            return Set.of(new Pos(0,0));

        Set<Pos> occupiedPositions =  Arrays.stream(placedTiles.clone())
                .filter(Objects::nonNull)
                .map(PlacedTile::pos)
                .collect(Collectors.toSet());

        Set<Pos> potentialInsertPosition = new HashSet<>();
        Arrays.stream(placedTiles.clone())
                .filter(Objects::nonNull)
                .map((a) -> getNeighborPositions(a.pos()))
                .forEach(potentialInsertPosition::addAll);


        potentialInsertPosition.removeAll(occupiedPositions);
        return potentialInsertPosition;
    }
    private Set<Pos> getNeighborPositions(Pos pos) {
        return Set.of(
                pos.neighbor(Direction.N), pos.neighbor(Direction.E),
                pos.neighbor(Direction.S), pos.neighbor(Direction.W)
        );
    }
    private Set<PlacedTile> getNeighborTiles(Pos pos) {

        Set<PlacedTile> returnSet = new HashSet<>();
        for (Direction direction : Direction.ALL) {
            returnSet.add(tileAt(pos.neighbor(direction)));
        }
        returnSet.removeIf(Objects::isNull);

        return returnSet;
    }

    /**
     * Method to get the last placed tile
     * @return The tile that was last placed, null if no tile was placed yet
     */
    public PlacedTile lastPlacedTile() {
        if (this.equals(EMPTY))
            return null;
        return placedTiles[placedTileIndices[placedTileIndices.length - 1]];
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

        if (!insertionPositions().isEmpty() && !insertionPositions().contains(tile.pos()))
            return false;

        for (Direction direction : Direction.ALL) {
            if (tileAt(tile.pos().neighbor(direction))!=null &&
                    !tile.side(direction).isSameKindAs(
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
                if (canAddTile(new PlacedTile(tile, null, rotation, insertionPosition)))
                    return true;
            }
        }
        return false;
    }


    public Board withNewTile(PlacedTile tile) {

        if (!canAddTile(tile) || placedTileIndices.length == TOTAL_TILES_AVAILABLE)
            throw new IllegalArgumentException("Cannot add tile");

        // This part seems pretty right to me, the cloning should be safe
        PlacedTile[] placedTilesWithNewTile = placedTiles.clone();
        placedTilesWithNewTile[calculateTileIndex(tile.pos())] = tile;

        int[] placedTileIndicesWithNewTile = Arrays.copyOf(placedTileIndices,
                placedTileIndices.length + 1);
        placedTileIndicesWithNewTile[placedTileIndices.length] =
                calculateTileIndex(tile.pos());

        ZonePartitions.Builder builder = new ZonePartitions.Builder(zonePartitions);
        builder.addTile(tile.tile());
        for (Direction direction : Direction.ALL) {
            if (tileAt(tile.pos().neighbor(direction)) != null) {
                builder.connectSides(
                        tile.side(direction),
                        tileAt(tile.pos().neighbor(direction))
                            .side(direction.opposite()));
            }
        }

        return new Board(
                placedTilesWithNewTile, placedTileIndicesWithNewTile,
                builder.build(), cancelledAnimals()
        );
    }

    public Board withOccupant(Occupant occupant) {
        //                           ??? ●﹏● ???
        PlacedTile targetTile = tileWithId(Zone.tileId(occupant.zoneId()));
        Zone targetZone = targetTile.zoneWithId(occupant.zoneId());
        ZonePartitions.Builder builder = new ZonePartitions.Builder(zonePartitions);
        PlayerColor occupantColor = targetTile.placer();

        Objects.requireNonNull(occupantColor);
        if (Objects.nonNull(targetTile.occupant())) {
            throw new IllegalArgumentException("Tile already occupied");
        }

        builder.addInitialOccupant(occupantColor, occupant.kind(), targetZone);

        PlacedTile[] placedTilesWithNewOccupant = placedTiles.clone();
        placedTilesWithNewOccupant[calculateTileIndex(targetTile.pos())] =
                placedTilesWithNewOccupant[calculateTileIndex(targetTile.pos())].withOccupant(occupant);

        return new Board(placedTilesWithNewOccupant, placedTileIndices.clone(),
                builder.build(), cancelledAnimals());
    }

    public Board withoutOccupant(Occupant occupant) {

        if (Objects.nonNull(occupant) && !occupants().contains(occupant)) {
            throw new IllegalArgumentException("Illegal Occupant");
        }

        PlacedTile targetTile = tileWithId(Zone.tileId(occupant.zoneId()));
        Zone targetZone = targetTile.zoneWithId(occupant.zoneId());
        ZonePartitions.Builder builder = new ZonePartitions.Builder(zonePartitions);
        PlayerColor occupantColor = targetTile.placer();

        builder.removePawn(occupantColor, targetZone);

        PlacedTile[] placedTilesWithoutOccupant = placedTiles.clone();
        placedTilesWithoutOccupant[calculateTileIndex(targetTile.pos())] =
                placedTilesWithoutOccupant[calculateTileIndex(targetTile.pos())].withNoOccupant();

        return new Board(placedTilesWithoutOccupant, placedTileIndices.clone(),
                builder.build(), cancelledAnimals());
    }

    public Board withoutGatherersOrFishersIn(
            Set<Area<Zone.Forest>> forests, Set<Area<Zone.River>> rivers) {

        ZonePartitions.Builder builder = new ZonePartitions.Builder(zonePartitions);

        PlacedTile[] placedTilesWithoutGatherersOrFishers = placedTiles.clone();
        for (Area<Zone.Forest> forest : forests) {
            builder.clearGatherers(forest);

            for (Integer tileId : forest.tileIds()) {
                placedTilesWithoutGatherersOrFishers[calculateTileIndex(tileWithId(tileId).pos())] =
                        placedTilesWithoutGatherersOrFishers[
                        calculateTileIndex(tileWithId(tileId).pos())].withNoOccupant();
            }

        }
        for (Area<Zone.River> river : rivers) {
            builder.clearFishers(river);

            for (Integer tileId : river.tileIds()) {
                if (Objects.nonNull(placedTilesWithoutGatherersOrFishers[
                        calculateTileIndex(tileWithId(tileId).pos())].occupant()) &&
                        placedTilesWithoutGatherersOrFishers[
                        calculateTileIndex(tileWithId(tileId).pos())]
                        .occupant().kind() == Occupant.Kind.PAWN) {

                    placedTilesWithoutGatherersOrFishers[
                            calculateTileIndex(tileWithId(tileId).pos())] =
                            placedTilesWithoutGatherersOrFishers[
                            calculateTileIndex(tileWithId(tileId).pos())]
                            .withNoOccupant();
                }
            }
        }
        return new Board(placedTilesWithoutGatherersOrFishers, placedTileIndices.clone(),
                builder.build(), cancelledAnimals());
    }

    public Board withMoreCancelledAnimals(Set<Animal> newlyCancelledAnimals) {

        ZonePartitions.Builder builder = new ZonePartitions.Builder(zonePartitions);
        Set<Animal> freshCancelledAnimals = new HashSet<>();
        freshCancelledAnimals.addAll(cancelledAnimals());

        freshCancelledAnimals.addAll(newlyCancelledAnimals);

        return new Board(placedTiles.clone(), placedTileIndices.clone(),
                builder.build(), freshCancelledAnimals);
    }

    @Override
    public boolean equals(Object obj) {

        if (obj instanceof Board) {
            return (this.cancelledAnimals.equals(((Board) obj).cancelledAnimals) &&
                    Arrays.equals(this.placedTiles, ((Board) obj).placedTiles) &&
                    Arrays.equals(this.placedTileIndices, ((Board) obj).placedTileIndices) &&
                    this.zonePartitions.equals(((Board) obj).zonePartitions));
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(Arrays.hashCode(placedTiles), Arrays.hashCode(placedTileIndices),
                zonePartitions, cancelledAnimals);
    }
}
