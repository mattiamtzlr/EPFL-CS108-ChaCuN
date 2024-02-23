package ch.epfl.chacun;

import java.util.HashSet;
import java.util.Set;

/**
 * TODO Description
 *
 * @author Mattia Metzler (372025)
 * @author Leoluca Bernardi (374107)
 */
public record PlacedTile(
        Tile tile, PlayerColor placer, Rotation rotation, Pos pos, Occupant occupant
    ) {
    public PlacedTile {
        Preconditions.checkArgument(tile != null);
        Preconditions.checkArgument(rotation != null);
        Preconditions.checkArgument(pos != null);
    }

    public PlacedTile(Tile tile, PlayerColor placer, Rotation rotation, Pos pos) {
        this(tile, placer, rotation, pos, null);
    }
    public int id() {
        return tile.id();
    }
    public Tile.Kind kind() {
        return tile.kind();
    }

    public TileSide side(Direction direction) {
        // TODO Write a test for this
        return tile.sides().get(direction.rotated(rotation.negated()).ordinal());
    }
    public Zone zoneWithId(int id) {
        for (Zone zone : tile.zones()) {
            if (zone.id() == id)
                return zone;
        }
        throw new IllegalArgumentException("illegal id");
    }
    public Zone specialPowerZone() {
        for (Zone zone : tile.zones()) {
            if (zone.specialPower() != null)
                return zone;
        }
        return null;
    }
    public Set<Zone.Forest> forestZones() {
        HashSet<Zone.Forest> forests = new HashSet<>();
        for (Zone zone : tile.zones()) {
            if (zone instanceof Zone.Forest forest)
                forests.add(forest);
        }
        return forests;
    }
    public Set<Zone.Meadow> meadowZones() {
        HashSet<Zone.Meadow> meadows = new HashSet<>();
        for (Zone zone : tile.zones()) {
            if (zone instanceof Zone.Meadow meadow)
                meadows.add(meadow);
        }
        return meadows;
    }
    public Set<Zone.River> riverZones() {
        HashSet<Zone.River> rivers = new HashSet<>();
        for (Zone zone : tile.zones()) {
            if (zone instanceof Zone.River river)
                rivers.add(river);
        }
        return rivers;
    }
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
     *
     * @param occupantKind
     * @return
     */
    public int idOfZoneOccupiedBy(Occupant.Kind occupantKind) {
        if (this.occupant == null || occupantKind != this.occupant.kind())
            return -1;
        return this.occupant.zoneId();
    }
}
