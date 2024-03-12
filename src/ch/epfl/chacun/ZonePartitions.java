package ch.epfl.chacun;

import java.util.List;

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
            // Not sure if this is sufficiently careful with immutable things involved
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
            for (TileSide side : tile.sides()) {
                if (side instanceof TileSide.River && ((TileSide.River) side).river().hasLake()) {
                    openConnections[((TileSide.River) side).river().localId()] += 1;
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
            for (Zone zone : tile.zones()) {
                // redo this using the pattern matching tricks from connectSides

                switch (zone) {
                    case Zone.Forest(int id, Zone.Forest.Kind kind) -> {
                        forestBuilder.addSingleton((Zone.Forest) zone,
                                openConnections[zone.localId()]);
                    }
                    case Zone.Meadow(
                            int id, List<Animal> animals, Zone.SpecialPower specialPower
                    ) -> meadowBuilder.addSingleton((Zone.Meadow) zone,
                            openConnections[zone.localId()]);
                    case Zone.River(int id, int fishCount, Zone.Lake lake) -> {
                        riverBuilder.addSingleton((Zone.River) zone, (((Zone.River)zone).hasLake())
                                ? openConnections[zone.localId()]-1
                                : openConnections[zone.localId()]);

                        riverSystemsBuilder.addSingleton((Zone.Water) zone,
                                openConnections[zone.localId()]);
                    }
                    case Zone.Lake(int id, int fishCount, Zone.SpecialPower specialPower) ->
                            riverSystemsBuilder.addSingleton((Zone.Water) zone,
                                    openConnections[zone.localId()]);
                }
            }

            for (Zone zone : tile.zones()) {
                if (zone instanceof Zone.River && ((Zone.River) zone).hasLake())
                    riverSystemsBuilder.union((Zone.Water) zone, ((Zone.River) zone).lake());
            }
        }
        public void connectSides(TileSide s1, TileSide s2) {
            switch (s1) {
                case TileSide.Forest(Zone.Forest f1)
                        when s2 instanceof TileSide.Forest(Zone.Forest f2) -> {
                    forestBuilder.union(f1, f2);
                }
                case TileSide.Meadow(Zone.Meadow m1)
                        when s2 instanceof TileSide.Meadow(Zone.Meadow m2) -> {
                    meadowBuilder.union(m1, m2);
                }
                case TileSide.River(Zone.Meadow m11, Zone.River r1, Zone.Meadow m12)
                        when s2 instanceof TileSide.River(
                                Zone.Meadow m21, Zone.River r2, Zone.Meadow m22) -> {
                    meadowBuilder.union(m11, m22);
                    riverBuilder.union(r1, r2);
                    meadowBuilder.union(m12, m21);

                }
                default -> {
                    throw new IllegalArgumentException("Not matching TileSide");
                }

            }
        }
        public void addInitialOccupant(PlayerColor player, Occupant.Kind occupantKind,
                                       Zone occupiedZone) {
            switch (occupantKind) {
                case PAWN -> {
                    switch (occupiedZone) {
                        case Zone.Forest(int id, Zone.Forest.Kind kind) -> {
                            forestBuilder.addInitialOccupant((Zone.Forest) occupiedZone, player);
                        }
                        case Zone.Meadow(
                                int id, List<Animal> animals, Zone.SpecialPower specialPower) -> {
                            meadowBuilder.addInitialOccupant((Zone.Meadow) occupiedZone, player);
                        }
                        case Zone.River(int id, int fishCount, Zone.Lake lake) -> {
                            riverBuilder.addInitialOccupant((Zone.River) occupiedZone, player);
                        }
                        default -> throw new IllegalArgumentException("Cannot add pawn here");
                    }
                }
                case HUT -> {
                    switch (occupiedZone) {
                        /* This code should be irrelevant
                        case Zone.River(int id, int fishCount, Zone.Lake lake) -> {

                            if (!((Zone.River) occupiedZone).hasLake())
                                riverBuilder.addInitialOccupant((Zone.River) occupiedZone, player);
                        }*/
                        case Zone.Lake(int id, int fishCount, Zone.SpecialPower specialPower) -> {
                            riverSystemsBuilder.addInitialOccupant(
                                    (Zone.Water) occupiedZone, player);
                        }
                        default -> throw new IllegalArgumentException("Cannot add Hutt here");
                    }
                }
                default -> throw new IllegalArgumentException("Illegal occupant");
            }
        }

        public void removePawn(PlayerColor player, Zone occupiedZone){
            switch (occupiedZone) {
                case Zone.Forest(int id, Zone.Forest.Kind kind) -> {
                    forestBuilder.removeOccupant((Zone.Forest) occupiedZone, player);
                }
                case Zone.Meadow(
                        int id, List<Animal> animals, Zone.SpecialPower specialPower) -> {
                    meadowBuilder.removeOccupant((Zone.Meadow) occupiedZone, player);
                }
                case Zone.River(int id, int fishCount, Zone.Lake lake) -> {
                    riverBuilder.removeOccupant((Zone.River) occupiedZone, player);
                }
                default -> throw new IllegalArgumentException("Lake cannot be occupied by pawn");
            }
        }
        public void clearGatherers(Area<Zone.Forest> forest) {
            forestBuilder.removeAllOccupantsOf(forest);
        }
        public void clearFishers(Area<Zone.River> river) {
            riverBuilder.removeAllOccupantsOf(river);
        }
        public ZonePartitions build() {
            return new ZonePartitions(
                    forestBuilder.build(), meadowBuilder.build(),
                    riverBuilder.build(), riverSystemsBuilder.build()
            );
        }

    }
}
