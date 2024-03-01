package ch.epfl.chacun;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * TODO JavaDoc description
 *
 * @author Mattia Metzler (372025)
 * @author Leoluca Bernardi (374107)
 */
public record Area<Z>(Set<Z> zones, List<PlayerColor> occupants, int openConnections) {
    /**
     * TODO JavaDoc
     * @param zones
     * @param occupants
     * @param openConnections
     */
    public Area {
        Preconditions.checkArgument(openConnections >= 0);
        zones = Set.copyOf(zones);
        Collections.sort(occupants);
        occupants = List.copyOf(occupants);
    }

    /**
     * TODO JavaDoc
     * @param forest
     * @return
     */
    public static boolean hasMenhir(Area<Zone.Forest> forest) {
        for (Zone.Forest zone : forest.zones()) {
            if (zone.kind() == Zone.Forest.Kind.WITH_MENHIR)
                return true;
        }
        return false;
    }

    /**
     * TODO Javadoc
     * @param forest
     * @return
     */
    public static int mushroomGroupCount(Area<Zone.Forest> forest) {
        int count = 0;
        for (Zone.Forest zone : forest.zones()) {
            if (zone.kind() == Zone.Forest.Kind.WITH_MUSHROOMS)
                count += 1;
        }
        return count;
    }

    /**
     * TODO JavaDoc
     * @param meadow
     * @param cancelledAnimals
     * @return
     */
    public static Set<Animal> animals(Area<Zone.Meadow> meadow, Set<Animal> cancelledAnimals) {
        Set<Animal> animals = new HashSet<Animal>();
        for (Zone.Meadow zone : meadow.zones()) {
            animals.addAll(zone.animals());
        }
        animals.removeAll(cancelledAnimals);
        return animals;
    }

    /**
     * TODO JavaDoc
     * @param river
     * @return
     */
    public static int riverFishCount(Area<Zone.River> river) {
        Set<Zone.Lake> knownLakes = new HashSet<>();
        int count = 0;
        for (Zone.River zone : river.zones()) {
            if (zone.hasLake() && knownLakes.add(zone.lake()))
                count += zone.lake().fishCount();
            count += zone.fishCount();
        }
        return count;
    }

}
