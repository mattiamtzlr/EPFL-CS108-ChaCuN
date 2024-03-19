package ch.epfl.chacun;

import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class BasicTextMaker implements TextMaker {
    private static String scorers(Set<PlayerColor> scorers) {
        return scorers.stream()
                .sorted()
                .map(Object::toString)
                .collect(Collectors.joining(",", "{", "}"));
    }

    private static String animals(Map<Animal.Kind, Integer> animals) {
        return Arrays.stream(Animal.Kind.values())
                .map(k -> animals.getOrDefault(k, 0) + "×" + k)
                .collect(Collectors.joining("/"));
    }

    @Override
    public String playerName(PlayerColor playerColor) {
        return playerColor.name();
    }

    @Override
    public String points(int points) {
        return String.valueOf(points);
    }

    @Override
    public String playerClosedForestWithMenhir(PlayerColor player) {
        return playerName(player);
    }

    @Override
    public String playersScoredForest(Set<PlayerColor> scorers,
                                      int points,
                                      int mushroomGroupCount,
                                      int tileCount) {
        return String.join("|",
                scorers(scorers),
                points(points),
                String.valueOf(mushroomGroupCount),
                String.valueOf(tileCount));
    }

    @Override
    public String playersScoredRiver(Set<PlayerColor> scorers,
                                     int points,
                                     int fishCount,
                                     int tileCount) {
        return String.join("|",
                scorers(scorers),
                points(points),
                String.valueOf(fishCount),
                String.valueOf(tileCount));
    }

    @Override
    public String playerScoredHuntingTrap(PlayerColor scorer,
                                          int points,
                                          Map<Animal.Kind, Integer> animals) {
        return String.join("|",
                playerName(scorer),
                String.valueOf(points),
                animals(animals));
    }

    @Override
    public String playerScoredLogboat(PlayerColor scorer, int points, int lakeCount) {
        return String.join("|",
                playerName(scorer),
                points(points),
                String.valueOf(lakeCount));
    }

    @Override
    public String playersScoredMeadow(Set<PlayerColor> scorers,
                                      int points,
                                      Map<Animal.Kind, Integer> animals) {
        return String.join(
                "|",
                scorers(scorers),
                points(points),
                animals(animals));
    }

    @Override
    public String playersScoredRiverSystem(Set<PlayerColor> scorers, int points, int fishCount) {
        return String.join(
                "|",
                scorers(scorers),
                points(points),
                String.valueOf(fishCount));
    }

    @Override
    public String playersScoredPitTrap(Set<PlayerColor> scorers,
                                       int points,
                                       Map<Animal.Kind, Integer> animals) {
        return String.join("|",
                scorers(scorers),
                String.valueOf(points),
                animals(animals));
    }

    @Override
    public String playersScoredRaft(Set<PlayerColor> scorers, int points, int lakeCount) {
        return String.join("|",
                scorers(scorers),
                String.valueOf(points),
                String.valueOf(lakeCount));
    }

    @Override
    public String playersWon(Set<PlayerColor> winners, int points) {
        return String.join("|",
                scorers(winners),
                points(points));
    }

    @Override
    public String clickToOccupy() {
        return "clickToOccupy";
    }

    @Override
    public String clickToUnoccupy() {
        return "clickToUnoccupy";
    }
}