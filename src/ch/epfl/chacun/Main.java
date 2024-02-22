package ch.epfl.chacun;

public class Main {
    public static void main(String[] args) {
        Direction d1 = Direction.N;
        System.out.println(d1.rotated(Rotation.LEFT));
        System.out.println(d1.opposite());

        Occupant o1 = new Occupant(Occupant.Kind.HUT, 56);
        Animal a1 = new Animal(5600, Animal.Kind.MAMMOTH);

        Pos p1 = new Pos(2, 3);
        System.out.println(p1.neighbor(Direction.N));
        System.out.println(p1.neighbor(Direction.E));
        System.out.println(p1.neighbor(Direction.S));
        System.out.println(p1.neighbor(Direction.W));

        Zone z1 = new Zone.Forest(726, Zone.Forest.Kind.WITH_MENHIR);
        System.out.printf("z1:\n  global: %d\n  tile: %d\n  local: %d\n",
                z1.id(), z1.tileId(), z1.localId());
    }
}
