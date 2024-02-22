package ch.epfl.chacun;

public class Main {
    public static void main(String[] args) {
        Direction d1 = Direction.N;
        System.out.println(d1.rotated(Rotation.LEFT));
        System.out.println(d1.opposite());
    }
}
