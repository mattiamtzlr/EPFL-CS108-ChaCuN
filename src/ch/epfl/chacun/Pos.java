package ch.epfl.chacun;

/**
 * Position records
 * On the grid, x points to the right and y points down.
 *
 * @author Mattia Metzler (372025)
 * @author Leoluca Bernardi (374107)
 *
 * @param x (int) x-coordinate
 * @param y (int) y-coordinate
 */
public record Pos(int x, int y) {
    public static final Pos ORIGIN = new Pos(0, 0);

    /**
     * Returns a new position translated by dX in the x-direction and dY in the
     * y-direction
     * @param dX (int) translation in x
     * @param dY (int) translation in y
     * @return (Pos) the translated position
     */
    public Pos translated(int dX, int dY) {
        return new Pos(this.x + dX, this.y + dY);
    }

    /**
     * Returns the position of the neighboring cell specified by the given direction
     * @param direction (Direction) the direction of the neighboring cell of which the
     *                  position is to be determined
     * @return (Pos) position of the neighboring cell.
     */
    public Pos neighbor(Direction direction) {
        return switch (direction) {
            case N -> new Pos(this.x, this.y - 1);
            case E -> new Pos(this.x + 1, this.y);
            case S -> new Pos(this.x, this.y + 1);
            case W -> new Pos(this.x - 1, this.y);
        };
    }
}
