package ch.epfl.chacun;

/**
 * Animal record
 *
 * @author Mattia Metzler (372025)
 * @author Leoluca Bernardi (374107)
 *
 * @param id (int) the id of the animal
 * @param kind (Animal.Kind) the kind of the animal
 */
public record Animal(int id, Kind kind) {
    /**
     * Enumerator for the animal kind
     */
    public enum Kind {
        MAMMOTH, AUROCHS, DEER, TIGER
    }

    /**
     * Returns the identifier of the tile that the animal lives on.
     * @return (int) id of the tile of the animal
     */
    public int tileId() {
        return Zone.tileId(Zone.tileId(this.id));
    }
}
