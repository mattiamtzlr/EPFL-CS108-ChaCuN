package ch.epfl.chacun;

import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Record which handles placed tiles and their attributes
 *
 * @author Mattia Metzler (372025)
 * @author Leoluca Bernardi (374107)
 */
public record PlacedTile(
        Tile tile, PlayerColor placer, Rotation rotation, Pos pos, Occupant occupant
) {
    /**
     * PlacedTile constructor which checks if tile rotation and pos are non-null.
     *
     * @param tile     tile to be placed
     * @param placer   color of the player placing the tile (can be null for starting tile)
     * @param rotation rotation in which the tile is to be placed
     * @param pos      position on the board where the tile should be placed
     * @param occupant (Occupant) the potential occupant of the tile, null if none
     */
    public PlacedTile {
        Objects.requireNonNull(tile);
        Objects.requireNonNull(rotation);
        Objects.requireNonNull(pos);
    }

    /**
     * Constructor that can be called without specifying an occupant
     *
     * @param tile     tile to be placed
     * @param placer   color of the player placing the tile (can be null for starting tile)
     * @param rotation rotation in which the tile is to be placed
     * @param pos      position on the board where the tile should be placed
     */
    public PlacedTile(Tile tile, PlayerColor placer, Rotation rotation, Pos pos) {
        this(tile, placer, rotation, pos, null);
    }

    /**
     * Getter for the tile id
     *
     * @return (int) id of the tile
     */
    public int id() {
        return tile.id();
    }

    /**
     * Getter for the tile kind
     *
     * @return (Tile.Kind) kind of the tile
     */
    public Tile.Kind kind() {
        return tile.kind();
    }

    /**
     * Method to get the tile side in a given direction. This direction is in respect to the way the
     * tile was placed down onto the playing area.
     *
     * @param direction a direction
     * @return the TileSide of the direction we specified
     */
    public TileSide side(Direction direction) {
        return tile.sides().get(direction.rotated(rotation.negated()).ordinal());
    }

    /**
     * Method to search zones by id
     *
     * @param id id of the zone we are looking for
     * @return zone corresponding to the id
     */
    public Zone zoneWithId(int id) {
        if (Zone.tileId(id) == this.id())
            for (Zone zone : tile.zones())
                if (zone.id() == id)
                    return zone;

        throw new IllegalArgumentException(
                String.format("Can't find Zone belonging to id %d.", id));
    }

    /**
     * Method to get the special power zone of a tile
     *
     * @return the special power zone, or null if there is none
     */
    public Zone specialPowerZone() {
        return this.tile.zones().stream()
                .filter(z -> z.specialPower() != null)
                .findFirst().orElse(null);
    }

    /**
     * Method to get a set containing all unique forest zones
     *
     * @return a set containing all forest zones
     */
    public Set<Zone.Forest> forestZones() {
        return this.tile.zones().stream()
                .filter(z -> z instanceof Zone.Forest)
                .map(z -> (Zone.Forest) z)
                .collect(Collectors.toSet());
    }

    /**
     * Method to get a set containing all unique meadow zones
     *
     * @return a set containing all meadow zones
     */
    public Set<Zone.Meadow> meadowZones() {
        return this.tile.zones().stream()
                .filter(z -> z instanceof Zone.Meadow)
                .map(z -> (Zone.Meadow) z)
                .collect(Collectors.toSet());
    }

    /**
     * Method to get a set containing all unique river zones
     *
     * @return a set containing all river zones
     */
    public Set<Zone.River> riverZones() {
        return this.tile.zones().stream()
                .filter(z -> z instanceof Zone.River)
                .map(z -> (Zone.River) z)
                .collect(Collectors.toSet());
    }

    /**
     * Method to find occupants that are possible to place on this tile
     *
     * @return a set of the occupants that are placeable
     */
    public Set<Occupant> potentialOccupants() {
        if (this.placer == null)
            return Collections.emptySet();
        Set<Occupant> occupants = new HashSet<>();
        for (Zone zone : tile.zones()) {
            if (!(zone instanceof Zone.Lake)) {
                // Pawns are always possible if the zone isn't a lake.
                occupants.add(new Occupant(Occupant.Kind.PAWN, zone.id()));

                // If a river does not have a lake, a hut is possible
                if (zone instanceof Zone.River && !((Zone.River) zone).hasLake())
                    occupants.add(new Occupant(Occupant.Kind.HUT, zone.id()));
            } else {
                // lakes can always have huts
                occupants.add(new Occupant(Occupant.Kind.HUT, zone.id()));
            }
        }
        return occupants;
    }

    /**
     * Returns the same PlacedTile, with the given occupant added.
     *
     * @param occupant (Occupant) the occupant to add
     * @return (PlacedTile) The new PlacedTile with the occupant
     * @throws IllegalArgumentException if the tile is already occupied
     */
    public PlacedTile withOccupant(Occupant occupant) {
        // check whether there isn't already an occupant
        Preconditions.checkArgument(this.occupant == null);
        return new PlacedTile(this.tile, this.placer, this.rotation, this.pos, occupant);
    }

    /**
     * Returns the same PlacedTile with all occupants removed
     *
     * @return (PlacedTile) the PlacedTile with no more occupants
     */
    public PlacedTile withNoOccupant() {
        return new PlacedTile(this.tile, this.placer, this.rotation, this.pos);
    }

    /**
     * Method to find the id of a zone that is occupied by a piece
     *
     * @param occupantKind the piece we want to find the zone id for
     * @return the zone id of the zone the piece we passed occupies or -1 if the occupant doesn't
     * exist
     */
    public int idOfZoneOccupiedBy(Occupant.Kind occupantKind) {
        if (this.occupant == null || occupantKind != this.occupant.kind())
            return -1;
        return this.occupant.zoneId();
    }
}
