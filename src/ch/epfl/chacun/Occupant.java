package ch.epfl.chacun;

import java.util.Objects;

/**
 * Occupant Record
 *
 * @author Mattia Metzler (372025)
 * @author Leoluca Bernardi (374107)
 *
 * @param kind (Occupant.Kind) the kind of occupant
 * @param zoneId (int) the id of the zone in which the occupant lives
 */
public record Occupant(Kind kind, int zoneId) {
    /**
     * Enumerator for the occupant kind
     */
    public enum Kind {
        PAWN, HUT
    }

    /**
     * Compact Constructor for Occupant, which checks whether the params are valid.
     */
    public Occupant {
        Objects.requireNonNull(kind);
        Preconditions.checkArgument(zoneId >= 0);
    }

    /**
     * Returns the number of occupants for a given kind.
     * @param kind (Kind) the kind to be queried
     * @return (int) 5 for PAWN, 3 for HUT
     */
    public static int occupantsCount(Kind kind) {
        return switch (kind) {
            case PAWN -> 5;
            case HUT -> 3;
        };
    }
}
