package ch.epfl.chacun.gui;

import ch.epfl.chacun.Preconditions;
import javafx.scene.image.Image;

/**
 * Class which handles the loading of images from the resource folder.
 *
 * @author Mattia Metzler (372025)
 * @author Leoluca Bernardi (374107)
 */
public final class ImageLoader {
    /**
     * Pixel size for large tile faces
     */
    public static final int LARGE_TILE_PIXEL_SIZE = 512;

    /**
     * Fit size for large tile faces (display size)
     */
    public static final int LARGE_TILE_FIT_SIZE = LARGE_TILE_PIXEL_SIZE / 2;

    /**
     * Pixel size for normal tile faces
     */
    public static final int NORMAL_TILE_PIXEL_SIZE = 256;

    /**
     * Fit size for normal tile faces (display size)
     */
    public static final int NORMAL_TILE_FIT_SIZE = NORMAL_TILE_PIXEL_SIZE / 2;

    /**
     * Pixel size for cancelled animal markers
     */
    public static final int MARKER_PIXEL_SIZE = 96;

    /**
     * Fit size for cancelled animal markers
     */
    public static final int MARKER_FIT_SIZE = MARKER_PIXEL_SIZE / 2;
    private static final int TILES_AMOUNT = 94;

    private ImageLoader() {}

    /**
     * Returns the 256x256 image of the tile specified by the given tile id
     *
     * @param tileId the tile id of the wanted tile
     * @return a JavaFX Image object containing the wanted image
     * @throws IllegalArgumentException if the tile id is invalid i.e. not respecting
     *                                  0 <= id <= TILES_AMOUNT
     */
    public static Image normalForTileId(int tileId) {
        Preconditions.checkArgument(tileId >= 0 && tileId <= TILES_AMOUNT);
        String tileIdStr = tileId < 10 ? STR."0\{tileId}" : String.valueOf(tileId);
        return new Image(STR."/\{NORMAL_TILE_PIXEL_SIZE}/\{tileIdStr}.jpg");
    }

    /**
     * Returns the 512x512 image of the tile specified by the given tile id
     *
     * @param tileId the tile id of the wanted tile
     * @return a JavaFX Image object containing the wanted image
     * @throws IllegalArgumentException if the tile id is invalid i.e. not respecting
     *                                  0 <= id <= TILES_AMOUNT
     */
    public static Image largeForTileId(int tileId) {
        Preconditions.checkArgument(tileId >= 0 && tileId <= TILES_AMOUNT);
        String tileIdStr = tileId < 10 ? STR."0\{tileId}" : String.valueOf(tileId);
        return new Image(STR."/\{LARGE_TILE_PIXEL_SIZE}/\{tileIdStr}.jpg");
    }
}
