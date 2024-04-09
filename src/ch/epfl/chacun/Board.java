package ch.epfl.chacun;

import java.util.*;
import java.util.stream.Collectors;

/**
 * This class represents the board that is played on, will be updated during the game.
 * Contains a representation of all the tiles on the board and handles directional things between
 * tiles, as well as area/zone partitioning and cancelling animals.
 *
 * @author Mattia Metzler (372025)
 * @author Leoluca Bernardi (374107)
 */
public final class Board {
    private final PlacedTile[] placedTiles;
    private final int[] placedTileIndices;
    private final ZonePartitions zonePartitions;
    private final Set<Animal> cancelledAnimals;

    /**
     * Number of spaces from the middle tile to the edges
     */
    public static final int REACH = 12;
    private static final int NUMBER_OF_POSITIONS = 625;
    private static final int TOTAL_TILES_AVAILABLE = 95;
    private static final int BOARD_SIZE = 25;

    /**
     * A preset for an empty board
     */
    public static final Board EMPTY = new Board(
            new PlacedTile[NUMBER_OF_POSITIONS],
            new int[]{}, ZonePartitions.EMPTY,
            Collections.emptySet()
    );

    private Board(PlacedTile[] placedTiles, int[] placedTileIndices, ZonePartitions zonePartitions,
                  Set<Animal> cancelledAnimals) {
        this.placedTiles = placedTiles;
        this.placedTileIndices = placedTileIndices;
        this.zonePartitions = zonePartitions;
        this.cancelledAnimals = cancelledAnimals;
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
     * Calculate the index corresponding to some position
     *
     * @param pos The position to convert from
     * @return The index that corresponds to position
     */
    private static int calculateTileIndex(Pos pos) {
        return (pos.x() + REACH) + (REACH + pos.y()) * BOARD_SIZE;
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
        return Set.copyOf(cancelledAnimals);
    }

    /**
     * Method to find all occupants on the board
     *
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
        Set<Zone.Meadow> zones = new HashSet<>(Set.copyOf(meadowArea(meadowZone).zones()));
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

    private static <Z extends Zone> int countOccupantsInArea(Area<Z> area, PlayerColor player) {
        int occupantCount = 0;
        for (PlayerColor occupantColor : area.occupants()) {
            occupantCount += (occupantColor.equals(player)) ? 1 : 0;
        }
        return occupantCount;
    }

    /**
     * Method to count the occupants of a given kind placed by a given player
     *
     * @param player       The color of the player we inquire about
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
     * Method to get all positions at which a new tile could be added to the board
     *
     * @return The set of possible insertion positions
     */
    public Set<Pos> insertionPositions() {
        if (this.equals(Board.EMPTY))
            return Set.of(Pos.ORIGIN);

        Set<Pos> occupiedPositions = Arrays.stream(placedTiles.clone())
                .filter(Objects::nonNull)
                .map(PlacedTile::pos)
                .collect(Collectors.toSet());

        return Arrays.stream(placedTiles.clone())
                .filter(Objects::nonNull)
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
        if (this.equals(EMPTY))
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
                .filter(fz -> forestArea(fz).isClosed())
                .map(this::forestArea)
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
                .filter(rz -> riverArea(rz).isClosed())
                .map(this::riverArea)
                .collect(Collectors.toSet());
    }

    /**
     * Determine whether a tile can be placed at a specific position
     *
     * @param tile The tile (PlacedTile) we want to test
     * @return True if the tile can be placed, false otherwise
     */
    public boolean canAddTile(PlacedTile tile) {

        if (!insertionPositions().isEmpty() && !insertionPositions().contains(tile.pos()))
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

        if (!canAddTile(tile) || placedTileIndices.length == TOTAL_TILES_AVAILABLE)
            throw new IllegalArgumentException(
                    String.format("Cannot add tile with id %d.", tile.id()));

        PlacedTile[] placedTilesWithNewTile = placedTiles.clone();
        placedTilesWithNewTile[calculateTileIndex(tile.pos())] = tile;

        int[] placedTileIndicesWithNewTile = Arrays.copyOf(
                placedTileIndices,
                placedTileIndices.length + 1
        );

        placedTileIndicesWithNewTile[placedTileIndices.length]
                = calculateTileIndex(tile.pos());

        ZonePartitions.Builder builder = new ZonePartitions.Builder(zonePartitions);
        builder.addTile(tile.tile());
        for (Direction direction : Direction.ALL) {
            PlacedTile neighbor = tileAt(tile.pos().neighbor(direction));

            if (neighbor != null)
                builder.connectSides(
                        tile.side(direction),
                        neighbor.side(direction.opposite())
                );
        }

        return new Board(
                placedTilesWithNewTile, placedTileIndicesWithNewTile,
                builder.build(), cancelledAnimals()
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
        if (Objects.nonNull(targetTile.occupant()))
            throw new IllegalArgumentException(
                    String.format("Tile with id %d is already occupied", targetTile.id()));

        builder.addInitialOccupant(occupantColor, occupant.kind(), targetZone);

        PlacedTile[] placedTilesWithNewOccupant = placedTiles.clone();
        int targetIndex = calculateTileIndex(targetTile.pos());
        placedTilesWithNewOccupant[targetIndex] =
                placedTilesWithNewOccupant[targetIndex].withOccupant(occupant);

        return new Board(placedTilesWithNewOccupant, placedTileIndices.clone(),
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

        PlacedTile[] placedTilesWithoutOccupant = placedTiles.clone();
        int targetIndex = calculateTileIndex(targetTile.pos());
        placedTilesWithoutOccupant[targetIndex] =
                placedTilesWithoutOccupant[targetIndex].withNoOccupant();

        return new Board(placedTilesWithoutOccupant, placedTileIndices.clone(),
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
        return new Board(newPlacedTiles, placedTileIndices.clone(),
                builder.build(), cancelledAnimals());
    }

    /**
     * Method to add cancelled animals to the list and update the board accordingly.
     *
     * @param newlyCancelledAnimals A set of animals to be cancelled.
     * @return A new board with more cancelled animals.
     */
    public Board withMoreCancelledAnimals(Set<Animal> newlyCancelledAnimals) {

        ZonePartitions.Builder builder = new ZonePartitions.Builder(zonePartitions);
        Set<Animal> newCancelledAnimals = new HashSet<>(cancelledAnimals());
        newCancelledAnimals.addAll(newlyCancelledAnimals);

        return new Board(placedTiles.clone(), placedTileIndices.clone(),
                builder.build(), newCancelledAnimals);
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
