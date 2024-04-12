package ch.epfl.chacun;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringJoiner;
import java.util.stream.Collectors;

import static ch.epfl.chacun.PlayerColor.*;
import static ch.epfl.chacun.Animal.Kind.*;

/**
 * TextMaker implementation which generates french message texts for the messages.
 *
 * @author Mattia Metzler (372025)
 * @author Leoluca Bernardi (374107)
 */
public final class TextMakerFr implements TextMaker {

    public static void main(String[] args) {
        TextMakerFr textMakerFr = new TextMakerFr(Map.of(
                RED, "Mattia",
                BLUE, "Leo",
                GREEN, "Anthony",
                YELLOW, "Ali",
                PURPLE, "Michael"
        ));

        System.out.println(textMakerFr.formatPlayers(Set.of(
                BLUE
        )));
        System.out.println(textMakerFr.formatPlayers(Set.of(
                YELLOW, PURPLE
        )));
        System.out.println(textMakerFr.formatPlayers(Set.of(
                GREEN, BLUE, YELLOW
        )));
        System.out.println(textMakerFr.formatPlayers(Set.of(
                GREEN, BLUE, RED, PURPLE
        )));
        System.out.println(textMakerFr.formatPlayers(Set.of(
                PURPLE, GREEN, YELLOW, RED, BLUE
        )));
    }

    Map<PlayerColor, String> playerNames;

    public TextMakerFr(Map<PlayerColor, String> playerNames) {
        this.playerNames = playerNames;
    }

    private String formatPlayers(Set<PlayerColor> players) {
        Preconditions.checkArgument(!players.isEmpty());
        List<String> playerStrings = players.stream()
                .sorted()
                .map(this::playerName)
                .toList();

        StringJoiner sjComma = new StringJoiner(", ");
        StringJoiner sjAnd = new StringJoiner(" et ");

        playerStrings.subList(0, playerStrings.size() - 1).forEach(sjComma::add);

        return sjComma.toString().isEmpty()
                ? playerStrings.getLast()
                : sjAnd.add(sjComma.toString())
                .add(playerStrings.getLast())
                .toString();
    }

    private String formatAnimals(Map<Animal.Kind, Integer> animals) {
        Map<Animal.Kind, Integer> filteredAnimals = animals.entrySet().stream()
                .filter(e -> !e.getKey().equals(TIGER) && e.getValue() > 0)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        StringJoiner sjComma = new StringJoiner(", ");
        StringJoiner sjAnd = new StringJoiner(" et ");

        // TODO
        return null;
    }

    private String getVerb(int amount) {
        return amount > 1 ? "s" : "";
    }

    private String getEnding(int amount, boolean gendered) {
        return amount > 1 ? (gendered ? "·s" : "s") : "";
    }

    @Override
    public String playerName(PlayerColor playerColor) {
        return this.playerNames.getOrDefault(playerColor, "");
    }

    @Override
    public String points(int points) {
        // TODO
        return null;
    }

    @Override
    public String playerClosedForestWithMenhir(PlayerColor player) {
        // TODO
        return null;
    }

    @Override
    public String playersScoredForest(Set<PlayerColor> scorers, int points, int mushroomGroupCount, int tileCount) {
        // TODO
        return null;
    }

    @Override
    public String playersScoredRiver(Set<PlayerColor> scorers, int points, int fishCount, int tileCount) {
        // TODO
        return null;
    }

    @Override
    public String playerScoredHuntingTrap(PlayerColor scorer, int points, Map<Animal.Kind, Integer> animals) {
        // TODO
        return null;
    }

    @Override
    public String playerScoredLogboat(PlayerColor scorer, int points, int lakeCount) {
        // TODO
        return null;
    }

    @Override
    public String playersScoredMeadow(Set<PlayerColor> scorers, int points, Map<Animal.Kind, Integer> animals) {
        // TODO
        return null;
    }

    @Override
    public String playersScoredRiverSystem(Set<PlayerColor> scorers, int points, int fishCount) {
        // TODO
        return null;
    }

    @Override
    public String playersScoredPitTrap(Set<PlayerColor> scorers, int points, Map<Animal.Kind, Integer> animals) {
        // TODO
        return null;
    }

    @Override
    public String playersScoredRaft(Set<PlayerColor> scorers, int points, int lakeCount) {
        // TODO
        return null;
    }

    @Override
    public String playersWon(Set<PlayerColor> winners, int points) {
        // TODO
        return null;
    }

    @Override
    public String clickToOccupy() {
        // TODO
        return null;
    }

    @Override
    public String clickToUnoccupy() {
        // TODO
        return null;
    }
}
