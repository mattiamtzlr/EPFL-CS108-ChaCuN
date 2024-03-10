package ch.epfl.chacun;

/**
 * TODO Description
 *
 * @author Mattia Metzler (372025)
 * @author Leoluca Bernardi (374107)
 */
public record ZonePartitions(ZonePartition<Zone.Forest> forests,
                             ZonePartition<Zone.Meadow> meadows,
                             ZonePartition<Zone.River> rivers,
                             ZonePartition<Zone.Water> riverSystems ) {
    // TODO there is no way this is right, this is just way too stupid
    public final static ZonePartitions EMPTY = new ZonePartitions(
            new ZonePartition<>(), new ZonePartition<>(),
            new ZonePartition<>(), new ZonePartition<>()
    );

    public static final class Builder {
        private ZonePartition.Builder<Zone.Forest> forestBuilder;
        private ZonePartition.Builder<Zone.Meadow> meadowBuilder;
        private ZonePartition.Builder<Zone.River> riverBuilder;
        private ZonePartition.Builder<Zone.Water> riverSystemsBuilder;

        public Builder(ZonePartitions initial) {
            // Not sure if this is sufficiently careful with immutable things involved,
            forestBuilder = new ZonePartition.Builder<>(initial.forests);
            meadowBuilder = new ZonePartition.Builder<>(initial.meadows);
            riverBuilder = new ZonePartition.Builder<>(initial.rivers);
            riverSystemsBuilder = new ZonePartition.Builder<>(initial.riverSystems);
        }

        public void addTile(Tile tile) {}
        public void connectSides(TileSide s1, TileSide s2) {}
        public void addInitialOccupant(PlayerColor player, Occupant.Kind occupantKind,
                                       Zone occupiedZone) {}
        public void removePawn(PlayerColor player, Zone occupiedZone){}
        public void clearGatherers(Area<Zone.Forest> forest) {}
        public void clearFishers(Area<Zone.River> river) {}
        public ZonePartitions build() {}

    }
}
