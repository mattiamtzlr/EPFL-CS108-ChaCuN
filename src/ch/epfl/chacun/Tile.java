package ch.epfl.chacun;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Tile record
 *
 * @param id   the id of the tile
 * @param kind the kind of the tile (start, normal or menhir)
 * @param n    the northern tile side
 * @param e    the eastern tile side
 * @param s    the southern tile side
 * @param w    the western tile side
 * @author Mattia Metzler (372025)
 * @author Leoluca Bernardi (374107)
 */
public record Tile(
        int id, Kind kind, TileSide n, TileSide e, TileSide s, TileSide w
) {
    /**
     * Method that creates a list containing all sides
     *
     * @return a list containing all sides of a tile
     */
    public List<TileSide> sides() {
        return List.of(n, e, s, w);
    }

    /**
     * Method that finds all zones that touch the border
     *
     * @return a set of all zones that touch the border
     */
    public Set<Zone> sideZones() {
        return sides().stream()
                .map(TileSide::zones)
                .flatMap(Collection::stream)
                .collect(Collectors.toSet());
    }

    /**
     * Method that finds all zones of a tile
     *
     * @return a set of all zones contained in the tile
     */
    public Set<Zone> zones() {
        HashSet<Zone> zones = (HashSet<Zone>) this.sideZones();

        for (Zone zone : this.sideZones()) {
            if (zone instanceof Zone.River river && river.hasLake())
                zones.add(river.lake());
        }
        return zones;
    }

    /**
     * enum for the kind of the tile
     */
    public enum Kind {
        START, NORMAL, MENHIR
    }
}
