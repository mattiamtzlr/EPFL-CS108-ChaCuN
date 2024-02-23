package ch.epfl.chacun;

/**
 * Points class which contains static methods to handle points
 *
 * @author Mattia Metzler (372025)
 * @author Leoluca Bernardi (374107)
 */
public final class Points {
    private Points() {}

    /**
     * Method that calculates the point gain for closing a forest
     * @param tileCount
     * @param mushroomGroupCount
     * @return points for a closed forest
     */
    public static int forClosedForest(int tileCount, int mushroomGroupCount) {
        Preconditions.checkArgument(tileCount > 1);
        Preconditions.checkArgument(mushroomGroupCount >= 0);

        return (tileCount * 2) + (mushroomGroupCount * 3);
    }

    /**
     * Method that calculates the point gain for closing a River
     * @param tileCount
     * @param fishCount
     * @return points for closed river
     */
    public static int forClosedRiver(int tileCount, int fishCount) {
        Preconditions.checkArgument(tileCount > 1);
        Preconditions.checkArgument(fishCount >= 0);

        return tileCount + fishCount;
    }

    /**
     * Method that calculates the point gain for a meadow
     * > this is only relevant in the final point counting phase
     * @param mammothCount
     * @param aurochsCount
     * @param deerCount
     * @return points for a given meadow
     */
    public static int forMeadow(int mammothCount, int aurochsCount, int deerCount) {
        Preconditions.checkArgument(mammothCount >= 0);
        Preconditions.checkArgument(aurochsCount >= 0);
        Preconditions.checkArgument(deerCount >= 0);

        return (mammothCount * 3) + (aurochsCount * 2) + deerCount;
    }

    /**
     * Method that calculates the point gain for a river system
     * @param fishCount
     * @return points for a given river system
     */
    public static int forRiverSystem(int fishCount) {
        Preconditions.checkArgument(fishCount >= 0);
        return fishCount;
    }

    /**
     * Method that calculates the point gain for a Logboat
     * @param lakeCount
     * @return points for the logboat
     */
    public static int forLogboat(int lakeCount) {
        Preconditions.checkArgument(lakeCount > 0);
        return lakeCount * 2;
    }

    /**
     * Method that calculates the point gain for a Raft
     * @param lakeCount
     * @return points for the raft
     */
    public static int forRaft(int lakeCount) {
        Preconditions.checkArgument(lakeCount > 0);
        return lakeCount;
    }
}
