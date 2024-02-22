package ch.epfl.chacun;

/**
 * Points class which contains static methods to handle points
 *
 * @author Mattia Metzler (372025)
 * @author Leoluca Bernardi (374107)
 */
public final class Points {
    private Points() {}

    public static int forClosedForest(int tileCount, int mushroomGroupCount) {
        Preconditions.checkArgument(tileCount > 1);
        Preconditions.checkArgument(mushroomGroupCount >= 0);

        return (tileCount * 2) + (mushroomGroupCount * 3);
    }

    public static int forClosedRiver(int tileCount, int fishCount) {
        Preconditions.checkArgument(tileCount > 1);
        Preconditions.checkArgument(fishCount >= 0);

        return tileCount + fishCount;
    }

    public static int forMeadow(int mammothCount, int aurochCount, int deerCount) {
        Preconditions.checkArgument(mammothCount >= 0);
        Preconditions.checkArgument(aurochCount >= 0);
        Preconditions.checkArgument(deerCount >= 0);

        return (mammothCount * 3) + (aurochCount * 2) + deerCount;
    }

    public static int forRiverSystem(int fishCount) {
        Preconditions.checkArgument(fishCount >= 0);

        return fishCount;
    }

    public static int forLogboat(int lakeCount) {
        Preconditions.checkArgument(lakeCount > 0);

        return lakeCount * 2;
    }

    public static int forRaft(int lakeCount) {
        Preconditions.checkArgument(lakeCount > 0);

        return lakeCount;
    }
}
