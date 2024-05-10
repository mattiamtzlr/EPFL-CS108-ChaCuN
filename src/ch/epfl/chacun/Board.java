package ch.epfl.chacun;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * This class represents the board that is played on, will be updated during the game.
 * Contains a representation of all the tiles on the board and handles directional things between
 * tiles, as well as area/zone partitioning and cancelling animals.
 *
 * @author Mattia Metzler (372025)
 * @author Leoluca Bernardi (374107)
 */
public final class Board {
    /**
     * Number of spaces from the middle tile to the edges
     */
    public static final int REACH = 12;
    private static final int NUMBER_OF_POSITIONS = 625;
    /**
     * A preset for an empty board
     */
    public static final Board EMPTY = new Board(
        new PlacedTile[NUMBER_OF_POSITIONS],
        new int[]{}, ZonePartitions.EMPTY,
        Collections.emptySet()
    );
    private static final int BOARD_SIZE = 25;
    private final PlacedTile[] placedTiles;
    private final int[] placedTileIndices;
    private final ZonePartitions zonePartitions;
    private final Set<Animal> cancelledAnimals;

    private Board(PlacedTile[] placedTiles, int[] placedTileIndices, ZonePartitions zonePartitions,
                  Set<Animal> cancelledAnimals) {
        this.placedTiles = placedTiles;
        this.placedTileIndices = placedTileIndices;
        this.zonePartitions = zonePartitions;
        this.cancelledAnimals = cancelledAnimals;
    }

    /**
     * Calculate the index corresponding to some position
     *
     * @param pos The position to convert from
     * @return The index that corresponds to position
     */
    private static int calculateTileIndex(Pos pos) {
        return (pos.x() + REACH) + (REACH + pos.y()) * BOARD_SIZE;
    }

    /**
     * Helper method to count occupants in a given area of a given player
     *
     * @param area   the area (type Z) of which the occupants need to be counted
     * @param player the player (color) of which the occupants need to be counted
     * @param <Z>    the type of the zones of the area, extends zone
     * @return the number of occupants in that area of that player
     * @see Board#occupantCount
     */
    private static <Z extends Zone> int countOccupantsInArea(Area<Z> area, PlayerColor player) {
        int occupantCount = 0;
        for (PlayerColor occupantColor : area.occupants()) {
            occupantCount += (occupantColor.equals(player)) ? 1 : 0;
        }
        return occupantCount;
    }

    /**
     * A method to get the tile at a specific position
     *
     * @param pos The position at which we search for the tile
     * @return The tile if it was found, otherwise returns null
     */
    public PlacedTile tileAt(Pos pos) {
        return (Math.abs(pos.x()) <= REACH && Math.abs(pos.y()) <= REACH)
            ? placedTiles[calculateTileIndex(pos)]
            : null;
    }

    /**
     * Get the tile that has a specific id
     *
     * @param tileId The id we want to lookup
     * @return The tile with the id that was passed
     * @throws IllegalArgumentException if there is no tile with the given id
     */
    public PlacedTile tileWithId(int tileId) {
        for (int placedTileIndex : placedTileIndices) {
            if (placedTiles[placedTileIndex].id() == tileId)
                return placedTiles[placedTileIndex];
        }
        throw new IllegalArgumentException(
            String.format("PlacedTile with id %d could not be found.", tileId));
    }

    /**
     * Returns the set of cancelled animals of the board
     *
     * @return An immutable copy of cancelledAnimals
     */
    public Set<Animal> cancelledAnimals() {
        return Collections.unmodifiableSet(cancelledAnimals);
    }

    /**
     * Method to find all occupants on the board
     *
     * @return The set of occupants (type Occupant) present on the board
     */
    public Set<Occupant> occupants() {
        return Arrays.stream(placedTileIndices)
            .mapToObj(i -> placedTiles[i])
            .filter(Objects::nonNull)
            .map(PlacedTile::occupant)
            .filter(Objects::nonNull)
            .collect(Collectors.toSet());
    }

    /**
     * Method to get the area the zone belongs to
     *
     * @param forest The zone of which we want to query the area
     * @return The area that was found
     */
    public Area<Zone.Forest> forestArea(Zone.Forest forest) {
        return zonePartitions.forests().areaContaining(forest);
    }

    /**
     * Method to get the area the zone belongs to
     *
     * @param meadow The zone of which we want to find the area
     * @return The area that was found
     */
    public Area<Zone.Meadow> meadowArea(Zone.Meadow meadow) {
        return zonePartitions.meadows().areaContaining(meadow);
    }

    /**
     * Method to get the area the zone belongs to
     *
     * @param river The zone of which we want to find the area
     * @return The area that was found
     */
    public Area<Zone.River> riverArea(Zone.River river) {
        return zonePartitions.rivers().areaContaining(river);
    }

    /**
     * Method to get the area the zone belongs to
     *
     * @param water The zone of which we want to find the area
     * @return The area that was found
     */
    public Area<Zone.Water> riverSystemArea(Zone.Water water) {
        return zonePartitions.riverSystems().areaContaining(water);
    }

    /**
     * Method to get all meadow areas
     *
     * @return A set of all meadow areas
     */
    public Set<Area<Zone.Meadow>> meadowAreas() {
        return zonePartitions.meadows().areas();
    }

    /**
     * Method to get all riverSystem areas
     *
     * @return A set of all riverSystem areas
     */
    public Set<Area<Zone.Water>> riverSystemAreas() {
        return zonePartitions.riverSystems().areas();
    }

    /**
     * Generate a set of meadow adjacent to a meadow zone
     *
     * @param pos        The position at which the zone is located
     * @param meadowZone The zone around which we want the adjacent meadow
     * @return The area containing all meadow zones that area both connected to the
     * zone that was passed and are not further away than one tile
     */
    public Area<Zone.Meadow> adjacentMeadow(Pos pos, Zone.Meadow meadowZone) {

        Set<Zone.Meadow> neighboringMeadowZones = new HashSet<>();
        for (PlacedTile placedTile : getTileAdjacentPositions(pos)) {
            neighboringMeadowZones.addAll(placedTile.meadowZones());
        }
        Set<Zone.Meadow> zones = new HashSet<>(meadowArea(meadowZone).zones());
        zones.retainAll(neighboringMeadowZones);
        zones.add(meadowZone);

        return new Area<>(zones, meadowArea(meadowZone).occupants(), 0);
    }

    /**
     * Helper method that returns all adjacent tiles that are not null
     *
     * @param pos the position of the tile around which the tiles need to be retrieved
     * @return the set of 8 tiles around the given position
     */
    private Set<PlacedTile> getTileAdjacentPositions(Pos pos) {
        Set<PlacedTile> returnSet = getNeighborTiles(pos);
        returnSet.add(tileAt(pos.translated(-1, -1)));
        returnSet.add(tileAt(pos.translated(1, -1)));
        returnSet.add(tileAt(pos.translated(-1, 1)));
        returnSet.add(tileAt(pos.translated(1, 1)));
        returnSet.removeIf(Objects::isNull);

        return returnSet;
    }

    /**
     * Method to count the occupants of a given kind placed by a given player
     *
     * @param player       The color of the player we enquire about
     * @param occupantKind The kind of occupant that we count
     * @return The number of occupants of a given kind placed by a given player
     */
    public int occupantCount(PlayerColor player, Occupant.Kind occupantKind) {
        switch (occupantKind) {
            case PAWN -> {
                return Stream.of(
                        zonePartitions.meadows().areas(), zonePartitions.forests().areas(),
                        zonePartitions.rivers().areas()
                    )
                    .flatMap(Collection::stream)
                    .mapToInt(a -> countOccupantsInArea(a, player))
                    .sum();
            }
            case HUT -> {
                return Stream.of(zonePartitions.riverSystems().areas())
                    .flatMap(Collection::stream)
                    .mapToInt(a -> countOccupantsInArea(a, player))
                    .sum();
            }
            default -> {
                return 0;
            }
        }
    }

    /**
     * Method to get all positions at which a new tile could be added to the board
     *
     * @return The set of possible insertion positions
     */
    public Set<Pos> insertionPositions() {
        if (placedTileIndices.length == 0)
            return Set.of(Pos.ORIGIN);


        Set<Pos> occupiedPositions = Arrays.stream(placedTileIndices)
            .filter(Objects::nonNull)
            .mapToObj(i -> placedTiles[i])
            .map(PlacedTile::pos)
            .collect(Collectors.toSet());

        return Arrays.stream(placedTileIndices)
            .filter(Objects::nonNull)
            .mapToObj(i -> placedTiles[i])
            .map((p) -> getNeighborPositions(p.pos()))
            .flatMap(Collection::stream)
            .filter(p -> !occupiedPositions.contains(p))
            .filter(p -> Math.abs(p.x()) <= REACH && Math.abs(p.y()) <= REACH)
            .collect(Collectors.toSet());
    }

    /**
     * Method to get the positions of the immediate neighboring tiles.
     *
     * @param pos Position of the middle tile.
     * @return Set of the requested positions.
     */
    private Set<Pos> getNeighborPositions(Pos pos) {
        return Set.of(
            pos.neighbor(Direction.N), pos.neighbor(Direction.E),
            pos.neighbor(Direction.S), pos.neighbor(Direction.W)
        );
    }

    /**
     * Method to get the immediate neighbor tiles (Cross)
     *
     * @param pos The position of the middle tile.
     * @return Set that contains all (placed) tiles that share a border with the middle tile
     */
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
     *
     * @return The tile that was last placed, null if no tile was placed yet
     */
    public PlacedTile lastPlacedTile() {
        if (placedTileIndices.length == 0)
            return null;
        return placedTiles[placedTileIndices[placedTileIndices.length - 1]];
    }

    /**
     * Method to find the forest areas that were closed by the placement of the last tile
     *
     * @return The forest areas that were closed, empty set if none
     */
    public Set<Area<Zone.Forest>> forestsClosedByLastTile() {
        if (lastPlacedTile() == null)
            return Collections.emptySet();

        return lastPlacedTile().forestZones().stream()
            .map(this::forestArea)
            .filter(Area::isClosed)
            .collect(Collectors.toSet());
    }

    /**
     * Method to find the river areas that were closed by the placement of the last tile
     *
     * @return The river areas that were closed, empty set if none
     */
    public Set<Area<Zone.River>> riversClosedByLastTile() {
        if (lastPlacedTile() == null)
            return Collections.emptySet();

        return lastPlacedTile().riverZones().stream()
            .map(this::riverArea)
            .filter(Area::isClosed)
            .collect(Collectors.toSet());
    }

    /**
     * Determine whether a tile can be placed at a specific position
     *
     * @param tile The tile (PlacedTile) we want to test
     * @return True if the tile can be placed, false otherwise
     */
    public boolean canAddTile(PlacedTile tile) {

        if (!insertionPositions().contains(tile.pos()))
            return false;

        for (Direction direction : Direction.ALL) {
            PlacedTile neighbor = tileAt(tile.pos().neighbor(direction));
            if (neighbor != null &&
                !tile.side(direction).isSameKindAs(neighbor.side(direction.opposite())))
                return false;
        }
        return true;
    }

    /**
     * Determine whether there exists a position on the board where the given tile could be placed
     *
     * @param tile The tile to be tested
     * @return True if there exists a position where the tile can be added, false otherwise
     */
    public boolean couldPlaceTile(Tile tile) {
        for (Pos insertionPosition : insertionPositions()) {
            for (Rotation rotation : Rotation.ALL) {
                if (canAddTile(new PlacedTile(tile, null, rotation, insertionPosition)))
                    return true;
            }
        }
        return false;
    }

    /**
     * Method to add a tile to the board
     *
     * @param tile The tile (PlacedTile) to be placed
     * @return A new board with the given tile added
     * @throws IllegalFormatCodePointException if tile cannot be added
     */
    public Board withNewTile(PlacedTile tile) {

        if (!canAddTile(tile))
            throw new IllegalArgumentException(
                String.format("Cannot add tile with id %d.", tile.id()));

        PlacedTile[] newPlacedTiles = placedTiles.clone();
        newPlacedTiles[calculateTileIndex(tile.pos())] = tile;

        int[] newPlacedTileIndices = Arrays.copyOf(
            placedTileIndices,
            placedTileIndices.length + 1
        );

        newPlacedTileIndices[placedTileIndices.length] = calculateTileIndex(tile.pos());

        ZonePartitions.Builder builder = new ZonePartitions.Builder(zonePartitions);
        builder.addTile(tile.tile());
        for (Direction direction : Direction.ALL) {
            PlacedTile neighbor = tileAt(tile.pos().neighbor(direction));

            if (neighbor != null)
                builder.connectSides(
                    tile.side(direction), neighbor.side(direction.opposite())
                );
        }

        return new Board(
            newPlacedTiles, newPlacedTileIndices, builder.build(), cancelledAnimals()
        );
    }

    /**
     * Method to create a board with an additional occupant.
     *
     * @param occupant The occupant we wish to add.
     * @return A board that was updated with a new occupant.
     * @throws IllegalArgumentException if the tile where we would like to place the occupant is
     *                                  already occupied
     */
    public Board withOccupant(Occupant occupant) {
        PlacedTile targetTile = tileWithId(Zone.tileId(occupant.zoneId()));
        Zone targetZone = targetTile.zoneWithId(occupant.zoneId());
        ZonePartitions.Builder builder = new ZonePartitions.Builder(zonePartitions);
        PlayerColor occupantColor = targetTile.placer();

        Objects.requireNonNull(occupantColor);
        builder.addInitialOccupant(occupantColor, occupant.kind(), targetZone);

        PlacedTile[] newPlacedTiles = placedTiles.clone();
        int targetIndex = calculateTileIndex(targetTile.pos());
        newPlacedTiles[targetIndex] = newPlacedTiles[targetIndex].withOccupant(occupant);

        return new Board(newPlacedTiles, placedTileIndices,
            builder.build(), cancelledAnimals());
    }

    /**
     * Method to create a board without one of its occupants.
     *
     * @param occupant The occupant we wish to add.
     * @return A board that was updated with a new occupant.
     * @throws IllegalArgumentException if the occupant that should be removed is not on the board.
     */
    public Board withoutOccupant(Occupant occupant) {
        PlacedTile targetTile = tileWithId(Zone.tileId(occupant.zoneId()));
        Zone targetZone = targetTile.zoneWithId(occupant.zoneId());
        ZonePartitions.Builder builder = new ZonePartitions.Builder(zonePartitions);
        PlayerColor occupantColor = targetTile.placer();

        builder.removePawn(occupantColor, targetZone);

        PlacedTile[] newPlacedTiles = placedTiles.clone();
        int targetIndex = calculateTileIndex(targetTile.pos());
        newPlacedTiles[targetIndex] = newPlacedTiles[targetIndex].withNoOccupant();

        return new Board(newPlacedTiles, placedTileIndices,
            builder.build(), cancelledAnimals());
    }

    /**
     * Method to remove all pawn type occupants from the given river and forest areas
     *
     * @param forests The forest areas that should be cleared.
     * @param rivers  The river areas that should be cleared.
     * @return A new board without pawns in the given areas.
     */
    public Board withoutGatherersOrFishersIn(
        Set<Area<Zone.Forest>> forests, Set<Area<Zone.River>> rivers
    ) {

        ZonePartitions.Builder builder = new ZonePartitions.Builder(zonePartitions);

        PlacedTile[] newPlacedTiles = placedTiles.clone();
        for (Area<Zone.Forest> forest : forests) {
            builder.clearGatherers(forest);

            for (Integer tileId : forest.tileIds()) {
                int targetIndex = calculateTileIndex(tileWithId(tileId).pos());
                newPlacedTiles[targetIndex] = newPlacedTiles[targetIndex].withNoOccupant();
            }

        }
        for (Area<Zone.River> river : rivers) {
            builder.clearFishers(river);

            for (Integer tileId : river.tileIds()) {
                int targetIndex = calculateTileIndex(tileWithId(tileId).pos());
                if (
                    Objects.nonNull(newPlacedTiles[targetIndex].occupant())
                        && newPlacedTiles[targetIndex].occupant().kind().equals(Occupant.Kind.PAWN)
                ) {
                    newPlacedTiles[targetIndex] = newPlacedTiles[targetIndex].withNoOccupant();
                }
            }
        }
        return new Board(newPlacedTiles, placedTileIndices,
            builder.build(), cancelledAnimals());
    }

    /**
     * Method to add cancelled animals to the list and update the board accordingly.
     *
     * @param newlyCancelledAnimals A set of animals to be cancelled.
     * @return A new board with more cancelled animals.
     */
    public Board withMoreCancelledAnimals(Set<Animal> newlyCancelledAnimals) {

        Set<Animal> newCancelledAnimals = new HashSet<>(cancelledAnimals());
        newCancelledAnimals.addAll(newlyCancelledAnimals);

        return new Board(placedTiles, placedTileIndices,
            this.zonePartitions, newCancelledAnimals);
    }

    @Override
    public boolean equals(Object obj) {

        if (obj instanceof Board that) {
            return (this.cancelledAnimals.equals(that.cancelledAnimals) &&
                Arrays.equals(this.placedTiles, that.placedTiles) &&
                Arrays.equals(this.placedTileIndices, that.placedTileIndices) &&
                this.zonePartitions.equals(that.zonePartitions));
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(Arrays.hashCode(placedTiles), Arrays.hashCode(placedTileIndices),
            zonePartitions, cancelledAnimals);
    }
}
