package ch.epfl.chacun;

import java.util.Objects;

/**
 * Occupant Record
 *
 * @param kind   (Occupant.Kind) the kind of occupant
 * @param zoneId (int) the id of the zone in which the occupant lives
 * @author Mattia Metzler (372025)
 * @author Leoluca Bernardi (374107)
 */
public record Occupant(Kind kind, int zoneId) {
    /**
     * Compact Constructor for Occupant, which checks whether the params are valid.
     * @throws IllegalArgumentException if the zoneId is smaller than zero
     */
    public Occupant {
        Objects.requireNonNull(kind);
        Preconditions.checkArgument(zoneId >= 0);
    }

    /**
     * Returns the number of occupants for a given kind.
     *
     * @param kind (Kind) the kind to be queried
     * @return (int) 5 for PAWN, 3 for HUT
     */
    public static int occupantsCount(Kind kind) {
        return switch (kind) {
            case PAWN -> 5;
            case HUT -> 3;
        };
    }

    /**
     * Enumerator for the occupant kind
     */
    public enum Kind {
        PAWN, HUT
    }
}
