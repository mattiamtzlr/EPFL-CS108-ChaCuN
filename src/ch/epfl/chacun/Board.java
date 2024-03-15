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
            new int[95], ZonePartitions.EMPTY,
            Collections.emptySet()
    );

    private Board(PlacedTile[] placedTiles, int[] placedTileIndices, ZonePartitions zonePartitions, Set<Animal> cancelledAnimals) {
        this.placedTiles = placedTiles;
        this.placedTileIndices = placedTileIndices;
        this.zonePartitions = zonePartitions;
        this.cancelledAnimals = cancelledAnimals;
    }

    public PlacedTile tileAt(Pos pos) {
        return placedTiles[(pos.x() + REACH) + (REACH + pos.y())*25];
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
        if (this.equals(EMPTY))
            return null;
        List<PlacedTile> tileList = new ArrayList<>();
        for (int placedTileIndex : placedTileIndices) {
            if (placedTiles[placedTileIndex] != null)
                tileList.add((placedTiles[placedTileIndex]));
        }
        return tileList.getLast();
    }
    public Set<Area<Zone.Forest>> forestsClosedByLastTile() {
        Set<Area<Zone.Forest>> closedForestAreas = new HashSet<>();
        for (Zone.Forest forestZone : lastPlacedTile().forestZones()) {
            if (zonePartitions.forests().areaContaining(forestZone).isClosed())
                closedForestAreas.add(zonePartitions.forests().areaContaining(forestZone));
        }
        return closedForestAreas;
    }
    public Set<Area<Zone.River>> riversClosedByLastTile() {
        Set<Area<Zone.River>> closedRiverAreas = new HashSet<>();
        for (Zone.River riverZone : lastPlacedTile().riverZones()) {
            if (zonePartitions.rivers().areaContaining(riverZone).isClosed())
                closedRiverAreas.add(zonePartitions.rivers().areaContaining(riverZone));
        }
        return closedRiverAreas;
    }
    public boolean canAddTile(PlacedTile tile) {
        if (insertionPositions().contains(tile.pos())){
            TileSide northSide = tileAt(tile.pos().neighbor(Direction.N)).side(Direction.S);
            TileSide eastSide = tileAt(tile.pos().neighbor(Direction.E)).side(Direction.W);
            TileSide southSide = tileAt(tile.pos().neighbor(Direction.S)).side(Direction.N);
            TileSide westSide = tileAt(tile.pos().neighbor(Direction.W)).side(Direction.E);

            return (tile.side(Direction.N).isSameKindAs(northSide) &&
            tile.side(Direction.E).isSameKindAs(eastSide) &&
            tile.side(Direction.S).isSameKindAs(southSide) &&
            tile.side(Direction.W).isSameKindAs(westSide));
        }
        // TODO es anders mau
        // mit .ordinal und so
        return false;

    }
}
