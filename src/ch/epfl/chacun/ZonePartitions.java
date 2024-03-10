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
        public void addTile(Tile tile) {
            /* Step 1: Determine the number of open connections for each zone
                - I am assuming that by "zone" they are referring to the zones on the tile that is passed
                - If a river would be connected to a lake, consider this connection to be open
             */
             int[] openConnections = new int[10];
             //==============================================================
             // Frog nid was das isch oder was das macht, es funktioniert glaub villicht eventuell e bizeli maybe :D

            for (TileSide side : tile.sides()) {
                if (side instanceof TileSide.River) {
                    openConnections[((TileSide.River) side).river().localId()] += 1;

                    if (((TileSide.River) side).river().hasLake())
                        openConnections[((TileSide.River) side).river().lake().localId()] += 1;
                }
                for (Zone zone : side.zones()) {
                    openConnections[zone.localId()] += 1;
                }
            }
            //==============================================================

            /* Stage 2: Electric Bogaloo, add all of the zones to their correct partition
                 - We have to add them as singleton areas without occupants I think
                 - The number of open connections is needed and will be correct, except if we are adding
                   a river or a lake (I have no idea why we needed to do the weird thing that causes this :D)
            */
        }
        public void connectSides(TileSide s1, TileSide s2) {}
        public void addInitialOccupant(PlayerColor player, Occupant.Kind occupantKind,
                                       Zone occupiedZone) {}
        public void removePawn(PlayerColor player, Zone occupiedZone){}
        public void clearGatherers(Area<Zone.Forest> forest) {}
        public void clearFishers(Area<Zone.River> river) {}
        public void build() {}

    }
}
