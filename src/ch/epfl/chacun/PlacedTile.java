package ch.epfl.chacun;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * TODO Description + JavaDoc
 *
 * @author Mattia Metzler (372025)
 * @author Leoluca Bernardi (374107)
 */
public record PlacedTile(
        Tile tile, PlayerColor placer, Rotation rotation, Pos pos, Occupant occupant
    ) {
    /**
     * TODO adjust this constructor + JavaDoc
     * @param tile tile to be placed
     * @param placer color of the player placing the tile
     * @param rotation rotation in which the tile is to be placed
     * @param pos position on the board where the tile should be placed
     * @param occupant TODO specify occupant
     */
    public PlacedTile {
        Objects.requireNonNull(tile);
        Objects.requireNonNull(rotation);
        Objects.requireNonNull(pos);
    }

    /**
     * Constructor that can be called without specifying an occupant
     * @param tile tile to be placed
     * @param placer color of the player placing the tile
     * @param rotation rotation in which the tile is to be placed
     * @param pos position on the board where the tile should be placed
     */
    public PlacedTile(Tile tile, PlayerColor placer, Rotation rotation, Pos pos) {
        this(tile, placer, rotation, pos, null);
    }

    /**
     * Getter for the tile id
     * @return  id of the tile
     */
    public int id() {
        return tile.id();
    }
    public Tile.Kind kind() {
        return tile.kind();
    }

    /**
     * Method to get the tile side in a given direction. This direction is in respect to the way the tile was
     * placed down onto the playing area.
     * @param direction a direction
     * @return the TileSide of the direction we specified
     */

    public TileSide side(Direction direction) {
        // TODO Write a test for this
        return tile.sides().get(direction.rotated(rotation.negated()).ordinal());
    }

    /**
     * Method to search zones by id
     * @param id id of the zone we are looking for
     * @return zone corresponding to the id
     */
    public Zone zoneWithId(int id) {
        for (Zone zone : tile.zones()) {
            if (zone.id() == id)
                return zone;
        }
        throw new IllegalArgumentException("illegal id");
    }

    /**
     * Method to get the special power zone of a tile
     * @return the special power zone, or null if there is none
     */
    public Zone specialPowerZone() {
        for (Zone zone : tile.zones()) {
            if (zone.specialPower() != null)
                return zone;
        }
        return null;
    }
    /**
     * Method to get a set containing all unique forest zones
     * @return a set containing all forest zones
     */
    public Set<Zone.Forest> forestZones() {
        HashSet<Zone.Forest> forests = new HashSet<>();
        for (Zone zone : tile.zones()) {
            if (zone instanceof Zone.Forest forest)
                forests.add(forest);
        }
        return forests;
    }
    /**
     * Method to get a set containing all unique meadow zones
     * @return a set containing all meadow zones
     */
    public Set<Zone.Meadow> meadowZones() {
        HashSet<Zone.Meadow> meadows = new HashSet<>();
        for (Zone zone : tile.zones()) {
            if (zone instanceof Zone.Meadow meadow)
                meadows.add(meadow);
        }
        return meadows;
    }

    /**
     * Method to get a set containing all unique river zones
     * @return a set containing all river zones
     */
    public Set<Zone.River> riverZones() {
        HashSet<Zone.River> rivers = new HashSet<>();
        for (Zone zone : tile.zones()) {
            if (zone instanceof Zone.River river)
                rivers.add(river);
        }
        return rivers;
    }

    /**
     * Method to find pieces that are possible to place on this tile
     * @return a set of the pieces that are placeable
     */
    public Set<Occupant> potentialOccupants() {
        // TODO Write extensive tests for this, this is crucial for rules
        if (placer == null)
            return new HashSet<>();
        HashSet<Occupant> occupants = new HashSet<>();
        for (Zone zone : tile.zones()) {
            if (zone instanceof Zone.Forest || zone instanceof Zone.Meadow)
                occupants.add(new Occupant(Occupant.Kind.PAWN, zone.id()));
            if (zone instanceof Zone.River) {
                if (((Zone.River) zone).hasLake())
                    occupants.add(new Occupant(Occupant.Kind.PAWN, zone.id()));
                else
                    occupants.add(new Occupant(Occupant.Kind.HUT, zone.id()));
            }
            if (zone instanceof Zone.Lake)
                occupants.add(new Occupant(Occupant.Kind.HUT, zone.id()));
        }
        return occupants;
    }
    //==================================================================================================================
    // TODO this seems really bad, we need to test and redo this
    public PlacedTile withOccupant(Occupant occupant) {
        if (this.occupant != null)
            throw new IllegalArgumentException("tile is already occupied");
        return new PlacedTile(this.tile, this.placer, this.rotation, this.pos, occupant);
    }
    public PlacedTile withNoOccupant() {
        return new PlacedTile(this.tile, this.placer, this.rotation, this.pos);
    }
    //==================================================================================================================

    /**
     * Method to find the id of a zone that is occupied by a piece
     * @param occupantKind the piece we want to find the zone id for
     * @return the zone id of the zone the piece we passed occupies
     */
    public int idOfZoneOccupiedBy(Occupant.Kind occupantKind) {
        if (this.occupant == null || occupantKind != this.occupant.kind())
            return -1;
        return this.occupant.zoneId();
    }
}
