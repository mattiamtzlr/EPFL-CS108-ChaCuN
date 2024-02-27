package ch.epfl.chacun;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Tile record
 *
 * @author Mattia Metzler (372025)
 * @author Leoluca Bernardi (374107)
 *
 * @param id the id of the tile
 * @param kind the kind of the tile (start, normal or menhir)
 * @param n the northern tile side
 * @param e the eastern tile side
 * @param s the southern tile side
 * @param w the western tile side
 */
public record Tile(
        int id, Kind kind, TileSide n, TileSide e, TileSide s, TileSide w
) {
    /**
     * enum for the kind of the tile
     */
    enum Kind {
        START, NORMAL, MENHIR
    }

    /**
     * Method that creates a list containing all sides
     * @return a list containing all sides of a tile
     */
    public List<TileSide> sides(){
        return List.of(n, e, s, w);
    }

    /**
     * Method that finds all zones that touch the border
     * @return a set of all zones that touch the border
     */
    public Set<Zone> sideZones() {
        HashSet<Zone> sideZones = new HashSet<>();
        for (TileSide side : sides()) {
            sideZones.addAll(side.zones());
        }
        return sideZones;
    }

    /**
     * Method that finds all zones of a tile
     * @return a set of all zones contained in the tile
     */
    public Set<Zone> zones() {
        HashSet<Zone> zones = (HashSet<Zone>) this.sideZones();
        for (Zone zone : zones) {
            if (zone instanceof Zone.River river)
                if (river.hasLake())
                    zones.add(river.lake());
        }
        return zones;
    }
}
