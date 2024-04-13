package ch.epfl.chacun;

import java.util.*;
import java.util.stream.Collectors;

import static ch.epfl.chacun.Animal.Kind.*;
import static ch.epfl.chacun.PlayerColor.*;

/**
 * TextMaker implementation which generates french message texts for the messages.
 *
 * @author Mattia Metzler (372025)
 * @author Leoluca Bernardi (374107)
 */
public final class TextMakerFr implements TextMaker {

    // TODO: Delete this
    public static void main(String[] args) {
        TextMakerFr textMakerFr = new TextMakerFr(Map.of(
                RED, "Mattia",
                BLUE, "Leo",
                GREEN, "Anthony",
                YELLOW, "Ali",
                PURPLE, "Michael"
        ));

        System.out.println(textMakerFr.formatAnimals(Map.of(
                DEER, 1,
                MAMMOTH, 2,
                AUROCHS, 3,
                TIGER, 2
        )));

        System.out.println(textMakerFr.formatAnimals(Map.of(
                DEER, 0,
                MAMMOTH, 4,
                AUROCHS, 3,
                TIGER, 0
        )));

        System.out.println(textMakerFr.formatAnimals(Map.of(
                DEER, 5,
                MAMMOTH, 1,
                AUROCHS, 0,
                TIGER, 2
        )));

        System.out.println(textMakerFr.formatAnimals(Map.of(
                DEER, 0,
                MAMMOTH, 0,
                AUROCHS, 1,
                TIGER, 2
        )));

        System.out.println(textMakerFr.formatAnimals(Map.of(
                DEER, 3,
                MAMMOTH, 0,
                AUROCHS, 3,
                TIGER, 0
        )));
    }

    Map<PlayerColor, String> playerNames;

    private static final Map<Animal.Kind, String> ANIMAL_NAMES = Map.of(
            MAMMOTH, "mammouth",
            AUROCHS, "auroch",
            DEER, "cerf"
    );

    public TextMakerFr(Map<PlayerColor, String> playerNames) {
        this.playerNames = playerNames;
    }

    /**
     * Helper method to format a set of players correctly
     * @param players the set of players, non-empty
     * @return correctly formatted string
     */
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

    /**
     * Helper method to format a map of animals and their counts correctly
     * @param animals the map of animals, at least one value > 0
     * @return correctly formatted string
     */
    private String formatAnimals(Map<Animal.Kind, Integer> animals) {
        Map<Animal.Kind, Integer> filteredAnimals = animals.entrySet().stream()
                .filter(e -> !e.getKey().equals(TIGER) && e.getValue() > 0)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        StringJoiner sjComma = new StringJoiner(", ");
        StringJoiner sjAnd = new StringJoiner(" et ");

        List<String> strings = new ArrayList<>();
        // values() returns them in order of declaration thus sorted
        for (Animal.Kind kind : Animal.Kind.values()) {
            int count = filteredAnimals.getOrDefault(kind, 0);
            if (!kind.equals(TIGER) && count > 0)
                strings.add(STR."\{filteredAnimals.get(kind)} \{ANIMAL_NAMES.get(kind)}\{getEnding(count, false)}");
        }

        // valid as a message only gets generated when there are > 0 points thus at least one animal
        Preconditions.checkArgument(!strings.isEmpty());
        strings.subList(0, strings.size() - 1).forEach(sjComma::add);

        return sjComma.toString().isEmpty()
                ? strings.getLast()
                : sjAnd.add(sjComma.toString())
                .add(strings.getLast())
                .toString();
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
