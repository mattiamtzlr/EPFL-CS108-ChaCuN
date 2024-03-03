package ch.epfl.chacun;

import java.util.*;

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
        List<PlayerColor> sortedOccupants = new ArrayList<>(occupants);
        Collections.sort(sortedOccupants);
        occupants = sortedOccupants;
    }

    // ================================================================== Static Methods

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

    /**
     * TODO JavaDoc
     * @param riverSystem
     * @return
     */
    public static int riverSystemFishCount(Area<Zone.Water> riverSystem) {
        // TODO: Doesn't this just do the same thing as riverFishCount() ?
        int count = 0;
        for (Zone.Water zone : riverSystem.zones()) {
            count += zone.fishCount();
        }
        return count;
    }

    /**
     * TODO JavaDoc
     * @param riverSystem
     * @return
     */
    public static int lakeCount(Area<Zone.Water> riverSystem) {
        int count = 0;
        for (Zone.Water zone : riverSystem.zones()) {
            if (zone instanceof Zone.Lake) count += 1;
        }

        return count;
    }

    // ================================================================== Non-static methods

    /**
     * Returns true when an area is closed, meaning there are no more tile-sides of this area,
     * to which another tile could be connected.
     * @return true when the area is closed, false if not.
     */
    public boolean isClosed() {
        return openConnections == 0;
    }

    /**
     * Returns true when there is at least one occupant in this area.
     * @return true when there is >= 1 occupant.
     */
    public boolean isOccupied() {
        return !occupants.isEmpty();
    }

    /**
     * TODO JavaDoc, also this might be really inefficient.
     * @return
     */
    public Set<PlayerColor> majorityOccupants() {
        Set<PlayerColor> majorityOccupants = new HashSet<>();
        int[] counts = new int[5];

        for (PlayerColor occupant : occupants) {
            counts[occupant.ordinal()] += 1;
        }
        int max = Arrays.stream(counts).max().getAsInt();

        for (int i = 0; i < counts.length; i++) {
            if (counts[i] == max)
                majorityOccupants.add(PlayerColor.ALL.get(i));
        }

        return majorityOccupants;
    }

    /**
     * TODO JavaDoc
     * @param that
     * @return
     */
    public Area<Z> connectTo(Area<Z> that) {
        // maybe this needs to be removed later, depending on how the open connections are handled
        if (this.openConnections() == 0 || that.openConnections() == 0)
            return this;

        // I think this is all that needs to be done, but TODO test extensively
        if (this.equals(that))
            return new Area<Z>(
                    this.zones, this.occupants, this.openConnections - 2
            );
        else {
            this.zones.addAll(that.zones);
            this.occupants.addAll(that.occupants);

            return new Area<>(
                    this.zones,
                    this.occupants,
                    this.openConnections + that.openConnections - 2
            );
        }
    }

    /**
     * Returns the same area as the current instance, with the given occupant added to its occupant
     * list. Throws IllegalArgumentException if the area already has at least one occupant.
     * @param occupant (PlayerColor) the color of occupant to be added
     * @return (Area) the new area with the added occupant
     */
    public Area<Z> withInitialOccupant(PlayerColor occupant) {
        Preconditions.checkArgument(!this.isOccupied());
        this.occupants.add(occupant);
        return new Area<Z>(this.zones, this.occupants, this.openConnections);
    }

    /**
     * Returns the same area as the current instance, with one less occupant of the given color.
     * @param occupant (PlayerColor) the color of occupant of which one needs to be removed
     * @return (Area) the new area with the new occupants list
     */
    public Area<Z> withoutOccupant(PlayerColor occupant) {
        Preconditions.checkArgument(this.occupants.contains(occupant));
        this.occupants.remove(occupant);
        return new Area<Z>(this.zones, this.occupants, this.openConnections);
    }

    /**
     * Returns the same area as the current instance without any occupants.
     * @return
     */
    public Area<Z> withoutOccupants() {
        return new Area<>(this.zones, Collections.emptyList(), this.openConnections);
    }

    /**
     * Returns the set of tile ids of the tiles containing the zone.
     * @return (Set<Integer>) set containing the tile ids
     */
    public Set<Integer> tileIds() {
        Set<Integer> ids = new HashSet<>();
        for (Z z : zones) {
            if (z instanceof Zone zone)
                ids.add(zone.tileId());
        }

        return ids;
    }

    /**
     * Returns the zone in the area with the given special power, or null if there is no such zone
     * @param specialPower (Zone.SpecialPower) the special power to be retrieved
     * @return (Zone) the zone containing the special power.
     */
    public Zone zoneWithSpecialPower(Zone.SpecialPower specialPower) {
        for (Z z : zones) {
            if (z instanceof Zone zone && zone.specialPower() != null)
                return zone;
        }

        return null;
    }
}
