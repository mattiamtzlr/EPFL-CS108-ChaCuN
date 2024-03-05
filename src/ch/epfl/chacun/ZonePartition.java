package ch.epfl.chacun;

import java.util.Collections;
import java.util.Set;

/**
 * TODO Description
 *
 * @author Mattia Metzler (372025)
 * @author Leoluca Bernardi (374107)
 */
public record ZonePartition<Z>(Set<Area<Z>> areas) {
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


}
