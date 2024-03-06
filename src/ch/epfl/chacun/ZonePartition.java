package ch.epfl.chacun;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * TODO Description
 *
 * @author Mattia Metzler (372025)
 * @author Leoluca Bernardi (374107)
 */
public record ZonePartition<Z extends Zone>(Set<Area<Z>> areas) {
    public ZonePartition {
        areas = Set.copyOf(areas);
    }
    // TODO test if types are correct if we use this consturctor
    public ZonePartition() {
        this(Collections.emptySet());
    }

    public Area<Z> areaContaining(Z zone) {
        for (Area<Z> area : areas) {
            if (area.zones().contains(zone)){
                return area;
            }
        }
        throw new IllegalArgumentException(String.format("Partition does not contain %s", zone));
    }

    public static final class Builder<Z extends Zone> {
        private HashSet<Area<Z>> areas = new HashSet<>();

        public Builder(ZonePartition<Z> partition) {
            areas.addAll(partition.areas());
        }
        public void addSingleton(Z zone, int openConnections) {
            areas.add(new Area<Z>(
                    Collections.singleton(zone), Collections.emptyList() ,openConnections));
        }

        // TODO test the absolute shit out of the following two methods :)

        /**
         *
         * @param zone
         * @param color
         * @throws e illegalArgumentException
         */
        public void addInitialOccupant(Z zone, PlayerColor color) {
            Area<Z> area = new ZonePartition<>(areas).areaContaining(zone);
            areas.remove(area);
            areas.add(area.withInitialOccupant(color));
        }
        public void removeOccupant(Z zone, PlayerColor color) {
            Area<Z> area = new ZonePartition<>(areas).areaContaining(zone);
            areas.remove(area);
            areas.add(area.withoutOccupant(color));
        }
        public void removeAllOccupantsOf(Area<Z> area) {
            Preconditions.checkArgument(areas.contains(area));
            areas.remove(area);
            areas.add(area.withoutOccupants());
        }
    }

}
