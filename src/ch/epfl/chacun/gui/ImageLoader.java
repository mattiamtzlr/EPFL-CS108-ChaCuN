package ch.epfl.chacun.gui;

import ch.epfl.chacun.Preconditions;
import javafx.scene.image.Image;

/**
 * Class which handles the loading of images from the resource folder.
 *
 * @author Mattia Metzler (372025)
 * @author Leoluca Bernardi (374107)
 */
public class ImageLoader {
    private ImageLoader() {
    }

    public static final int LARGE_TILE_PIXEL_SIZE = 512;
    public static final int LARGE_TILE_FIT_SIZE = 256;
    public static final int NORMAL_TILE_PIXEL_SIZE = 256;
    public static final int NORMAL_TILE_FIT_SIZE = 128;
    public static final int MARKER_PIXEL_SIZE = 96;
    public static final int MARKER_FIT_SIZE = 48;

    /**
     * Returns the 256x256 image of the tile specified by the given tile id
     *
     * @param tileId the tile id of the wanted tile
     * @return a JavaFX Image object containing the wanted image
     */
    public static Image normalForTileId(int tileId) {
        Preconditions.checkArgument(tileId >= 0 && tileId <= 94);
        return new Image(STR."/256/\{tileId}.jpg");
    }

    /**
     * Returns the 512x512 image of the tile specified by the given tile id
     *
     * @param tileId the tile id of the wanted tile
     * @return a JavaFX Image object containing the wanted image
     */
    public static Image largeForTileId(int tileId) {
        Preconditions.checkArgument(tileId >= 0 && tileId <= 94);
        return new Image(STR."/512/\{tileId}.jpg");
    }
}
