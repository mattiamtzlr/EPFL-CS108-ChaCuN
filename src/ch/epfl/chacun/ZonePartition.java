package ch.epfl.chacun;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * ZonePartition record used to represent a partition of zones that form areas.
 *
 * @param areas (Set<Area<Z>>) the areas to be used for the partition
 * @param <Z>   a generic Zone
 * @author Mattia Metzler (372025)
 * @author Leoluca Bernardi (374107)
 */
public record ZonePartition<Z extends Zone>(Set<Area<Z>> areas) {
    /**
     * Constructs a new ZonePartition with a given set of areas.
     *
     * @param areas (Set<Area<Z>>) the areas to be used for the partition
     */
    public ZonePartition {
        areas = Set.copyOf(areas);
    }

    /**
     * Constructs a new ZonePartition with an empty set of areas
     */
    public ZonePartition() {
        this(Collections.emptySet());
    }

    /**
     * Returns the area containing the given zone
     *
     * @param zone (Z) the zone to be found
     * @return (Area < Z >) the area containing the given zone
     * @throws IllegalArgumentException if zone is not found in partition
     */
    public Area<Z> areaContaining(Z zone) {

        for (Area<Z> area : areas) {
            if (area.zones().contains(zone)) {
                return area;
            }
        }
        throw new IllegalArgumentException(String.format("Partition does not contain %s", zone));
    }

    /**
     * ZonePartition builder to construct a new ZonePartition object
     *
     * @param <Z> a generic Zone
     */
    public static final class Builder<Z extends Zone> {
        private final Set<Area<Z>> areas = new HashSet<>();

        /**
         * Constructs a new Builder for ZonePartition
         *
         * @param partition the partition to modify
         */
        public Builder(ZonePartition<Z> partition) {
            areas.addAll(partition.areas());
        }

        /**
         * Adds a new area with only the given zone and no occupants
         *
         * @param zone            the zone to be added
         * @param openConnections the number of open connections of the area
         */
        public void addSingleton(Z zone, int openConnections) {
            areas.add(new Area<>(
                    Collections.singleton(zone), Collections.emptyList(),
                    openConnections));
        }

        private Area<Z> areaContaining(Z zone) {
            for (Area<Z> area : areas) {
                if (area.zones().contains(zone)) {
                    return area;
                }
            }
            throw new IllegalArgumentException(
                    String.format("Partition does not contain %s", zone));
        }

        /**
         * Adds an occupant to the given zone of the given color
         *
         * @param zone  the zone to add the occupant to
         * @param color the color of the occupant
         */
        public void addInitialOccupant(Z zone, PlayerColor color) {
            Area<Z> area = areaContaining(zone);
            areas.add(area.withInitialOccupant(color));
            areas.remove(area);
        }

        /**
         * Removes an occupant from the given zone of the given color.
         * The occupant has to exist in the zone and of the color specified.
         *
         * @param zone  the zone to remove the occupant from
         * @param color the color of the occupant
         * @throws IllegalArgumentException if the zone does not contain an occupant of the color
         */
        public void removeOccupant(Z zone, PlayerColor color) {
            Area<Z> area = areaContaining(zone);
            areas.add(area.withoutOccupant(color));
            areas.remove(area);
        }

        /**
         * Removes all occupant from the given area
         *
         * @param area the area to remove the occupant from, has to be in the partition
         * @throws IllegalArgumentException if the partition does not contain the area
         */
        public void removeAllOccupantsOf(Area<Z> area) {
            Preconditions.checkArgument(areas.remove(area));
            areas.add(area.withoutOccupants());
        }

        /**
         * Connects the two areas containing the two given zones, the zones may be of the same
         * area
         *
         * @param zone1 the zone of the first area
         * @param zone2 the zone of the second area
         */
        public void union(Z zone1, Z zone2) {
            Area<Z> area1 = areaContaining(zone1);
            Area<Z> area2 = areaContaining(zone2);

            if (area1.equals(area2)) {
                areas.add(area1.connectTo(area2));
                areas.remove(area1);
            } else {
                areas.add(area1.connectTo(area2));
                areas.removeAll(Set.of(area1, area2));
            }
        }

        /**
         * builds the ZonePartition
         *
         * @return the ZonePartition that was constructed using this builder.
         */
        public ZonePartition<Z> build() {
            return new ZonePartition<>(areas);
        }
    }
}