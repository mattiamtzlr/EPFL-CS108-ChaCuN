package ch.epfl.chacun;

import java.util.List;
import java.util.Objects;

/**
 * Class that groups together multiple ZonePartitions
 *
 * @param forests      The Forest ZonePartition
 * @param meadows      The Meadow ZonePartition
 * @param rivers       The River ZonePartition
 * @param riverSystems The ZonePartition containing all water related zones
 * @author Mattia Metzler (372025)
 * @author Leoluca Bernardi (374107)
 */
public record ZonePartitions(ZonePartition<Zone.Forest> forests,
                             ZonePartition<Zone.Meadow> meadows,
                             ZonePartition<Zone.River> rivers,
                             ZonePartition<Zone.Water> riverSystems) {

    /**
     * A ZonePartitions object that contains four empty partitions
     */
    public final static ZonePartitions EMPTY = new ZonePartitions(
        new ZonePartition<>(), new ZonePartition<>(),
        new ZonePartition<>(), new ZonePartition<>()
    );

    /**
     * A Builder class for ZonePartitions objects
     */
    public static final class Builder {
        private final ZonePartition.Builder<Zone.Forest> forestBuilder;
        private final ZonePartition.Builder<Zone.Meadow> meadowBuilder;
        private final ZonePartition.Builder<Zone.River> riverBuilder;
        private final ZonePartition.Builder<Zone.Water> riverSystemsBuilder;

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
         *
         * @param tile The tile of which we want to add areas to the partitions
         */
        public void addTile(Tile tile) {
            /* Step 1: Determine the number of open connections for each zone
                - I am assuming that by "zone" they are referring to the zones on the tile that is
                  passed
                - If a river would be connected to a lake, consider this connection to be open
             */
            int[] openConnections = new int[10];
            for (TileSide side : tile.sides()) {
                if (side instanceof TileSide.River && ((TileSide.River) side).river().hasLake()) {
                    openConnections[((TileSide.River) side).river().localId()] += 1;
                    openConnections[((TileSide.River) side).river().lake().localId()] += 1;
                }
                for (Zone zone : side.zones()) {
                    openConnections[zone.localId()] += 1;
                }
            }

            /* Stage 2: Add all the zones to their correct partition
                 - The number of open connections will be correct, except if we are
                   adding a river or a lake.
            */
            for (Zone zone : tile.zones()) {

                switch (zone) {
                    case Zone.Forest forestZone ->
                            forestBuilder.addSingleton(forestZone,
                                    openConnections[forestZone.localId()]);

                    case Zone.Meadow meadowZone ->
                            meadowBuilder.addSingleton(meadowZone,
                                    openConnections[meadowZone.localId()]);

                    case Zone.River riverZone -> {
                        riverBuilder.addSingleton(riverZone,
                            (riverZone.hasLake())
                            ? openConnections[riverZone.localId()] - 1
                            : openConnections[riverZone.localId()]);

                        riverSystemsBuilder.addSingleton(riverZone,
                            openConnections[riverZone.localId()]);
                    }
                    case Zone.Lake lakeZone->
                        riverSystemsBuilder.addSingleton(lakeZone,
                            openConnections[lakeZone.localId()]);
                }
            }

            for (Zone zone : tile.zones()) {
                if (zone instanceof Zone.River riverZone && riverZone.hasLake())
                    riverSystemsBuilder.union(
                        riverZone, riverZone.lake());
            }

        }

        /**
         * A method to connect two sides, as in connect the areas that border each other
         *
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
         *
         * @param player       The player playing the occupant
         * @param occupantKind The kind of occupant we want to add
         * @param occupiedZone The zone to which we want to add the occupant
         * @throws IllegalArgumentException If an illegal combination of occupant and zone is passed
         */
        public void addInitialOccupant(PlayerColor player, Occupant.Kind occupantKind,
                                       Zone occupiedZone) {
            switch (occupantKind) {
                case PAWN -> {
                    switch (occupiedZone) {
                        case Zone.Forest forestZone -> {
                            forestBuilder.addInitialOccupant(
                                forestZone, player);
                        }
                        case Zone.Meadow meadowZone -> {
                            meadowBuilder.addInitialOccupant(
                                meadowZone, player);
                        }
                        case Zone.River riverZone -> {
                            riverBuilder.addInitialOccupant(
                                riverZone, player);
                        }
                        default -> throw new IllegalArgumentException("Cannot add pawn here");
                    }
                }
                case HUT -> {
                    if (Objects.requireNonNull(occupiedZone) instanceof Zone.Lake ||
                        Objects.requireNonNull(occupiedZone) instanceof Zone.River ) {
                        riverSystemsBuilder.addInitialOccupant(
                            (Zone.Water) occupiedZone, player);
                    } else {
                        throw new IllegalArgumentException("Cannot add Hut here");
                    }
                }
                default -> throw new IllegalArgumentException("Illegal occupant");
            }
        }

        /**
         * A method to remove a single pawn of a specific color from a given zone
         *
         * @param player       The color of which we want to remove a pawn
         * @param occupiedZone The zone where we want to remove the pawn
         * @throws IllegalArgumentException If a lake is passed as zone
         */
        public void removePawn(PlayerColor player, Zone occupiedZone) {
            switch (occupiedZone) {
                case Zone.Forest forestZone -> {
                    forestBuilder.removeOccupant(forestZone, player);
                }
                case Zone.Meadow meadowZone -> {
                    meadowBuilder.removeOccupant(meadowZone, player);
                }
                case Zone.River riverZone -> {
                    riverBuilder.removeOccupant(riverZone, player);
                }
                default -> throw new IllegalArgumentException("Lake cannot be occupied by pawn");
            }
        }

        /**
         * A method to clear all occupants from a given forest area inside the partition
         *
         * @param forest The forest we want to clear
         */
        public void clearGatherers(Area<Zone.Forest> forest) {
            forestBuilder.removeAllOccupantsOf(forest);
        }

        /**
         * A method to clear all occupants from a given river area inside the partition
         *
         * @param river The river we want to clear
         */
        public void clearFishers(Area<Zone.River> river) {
            riverBuilder.removeAllOccupantsOf(river);
        }

        /**
         * Build method in ZonePartitions.Builder
         *
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
