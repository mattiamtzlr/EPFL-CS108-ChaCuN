package ch.epfl.chacun;

import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * Class that groups together multiple ZonePartitions
 * @param forests The Forest ZonePartition
 * @param meadows The Meadow ZonePartition
 * @param rivers The River ZonePartition
 * @param riverSystems The ZonePartition containing all water related zones
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

        /**
         * Constructor for the builder of ZonePartitions
         * @param initial The ZonePartitions upon which we desire to build
         */
        public Builder(ZonePartitions initial) {
            forestBuilder = new ZonePartition.Builder<>(initial.forests);
            meadowBuilder = new ZonePartition.Builder<>(initial.meadows);
            riverBuilder = new ZonePartition.Builder<>(initial.rivers);
            riverSystemsBuilder = new ZonePartition.Builder<>(initial.riverSystems);
        }

        /**
         * A method to add a new tile. Adds all of the areas on said tile to the partitions
         * @param tile The tile of which we want to add areas to the partitions
         */
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

        /**
         * A method to connect two sides, as in connect the areas that border each other
         * @param s1 The First TileSide to join with the second
         * @param s2 The Second TileSide to join with the first
         * @throws IllegalArgumentException if the TileSides don't match
         */
        public void connectSides(TileSide s1, TileSide s2) {
            Preconditions.checkArgument(!s1.equals(s2));
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
                    riverSystemsBuilder.union(r1, r2);

                }
                default -> {
                    throw new IllegalArgumentException("Not matching TileSide");
                }

            }
        }

        /**
         * A method to add an occupant to an unoccupied area
         * @param player The player playing the occupant
         * @param occupantKind The kind of occupant we want to add
         * @param occupiedZone The zone to which we want to add the occupant
         * @throws IllegalArgumentException If an illegal combination of occupant and zone is passed
         */
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
                    if (Objects.requireNonNull(occupiedZone) instanceof Zone.Lake) {
                        riverSystemsBuilder.addInitialOccupant(
                                (Zone.Water) occupiedZone, player);
                    } else {
                        throw new IllegalArgumentException("Cannot add Hutt here");
                    }
                }
                default -> throw new IllegalArgumentException("Illegal occupant");
            }
        }

        /**
         * A method to remove a single pawn of a given color from a given zone
         * @param player The color of which we want to remove a pawn
         * @param occupiedZone The zone where we want to remove the pawn
         * @throws IllegalArgumentException If a lake is passed as zone
         */
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

        /**
         * A method to clear all occupants from a given forest area inside the partition
         * @param forest The forest we want to clear
         */
        public void clearGatherers(Area<Zone.Forest> forest) {
            forestBuilder.removeAllOccupantsOf(forest);
        }
        /**
         * A method to clear all occupants from a given river area inside the partition
         * @param river The river we want to clear
         */
        public void clearFishers(Area<Zone.River> river) {
            riverBuilder.removeAllOccupantsOf(river);
        }

        /**
         * Build method in ZonePartitions.Builder
         * @return A ZonePartitions object created from the sub builders in ZonePartitions.Builder
         */
        public ZonePartitions build() {
            return new ZonePartitions(
                    forestBuilder.build(), meadowBuilder.build(),
                    riverBuilder.build(), riverSystemsBuilder.build()
            );
        }

    }
}
