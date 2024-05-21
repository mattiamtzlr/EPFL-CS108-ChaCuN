package ch.epfl.chacun;

import java.util.*;
import java.util.stream.Collectors;

import static ch.epfl.chacun.Animal.Kind.*;

/**
 * TextMaker implementation which generates french message texts for the messages.
 *
 * @author Mattia Metzler (372025)
 * @author Leoluca Bernardi (374107)
 */
public final class TextMakerFr implements TextMaker {

    /* ======================================= IMPORTANT ===========================================
    | To keep lines under a 100 characters long, user triple quote """ strings for string          |
    | templates. When doing so, make sure to add a backslash at the end of each line to ignore the |
    | newline. If the newline is between two words, add a space before the next word on the next   |
    | line. See the playerClosedForestWithMenhir method for an example.                            |
    ========================================== IMPORTANT ======================================== */

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
     *
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
     *
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
                strings.add(amountWord(count, ANIMAL_NAMES.get(kind)));
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

    /**
     * Helper method which returns the correct form of the verb "avoir" for a given number of people
     *
     * @param amount the number of people to apply the verb to
     * @return the correct form
     */
    private String getVerb(int amount) {
        return amount > 1 ? "ont" : "a";
    }

    /**
     * Helper method which returns the correct ending for a word given an amount of the word and
     * whether it is a person or something else.
     *
     * @param amount the amount of the thing described by the word
     * @param person true if "·s" should be returned instead of "s"
     * @return the correct ending
     */
    private String getEnding(int amount, boolean person) {
        return amount > 1 ? (person ? "·s" : "s") : "";
    }

    /**
     * Helper method which returns a word and its quantity correctly formatted. Should NOT be used
     * for people.
     *
     * @param amount the amount of the thing described by the word
     * @param word   the word which needs to get the ending
     * @return the correctly formatted string
     */
    private String amountWord(int amount, String word) {
        return STR."\{amount} \{word}\{getEnding(amount, false)}";
    }

    /**
     * Helper method which returns the correct form of the ubiquitous phrase
     * "qu'occupant·e(·s) majoritaire(s)" depending on the amount given.
     *
     * @param amount the amount of people which need to be described
     * @return the correct form of the above phrase
     */
    private String occupants(int amount) {
        return STR."""
        qu'occupant·e\{getEnding(amount, true)} majoritaire\
        \{getEnding(amount, false)}\
        """;
    }

    @Override
    public String playerName(PlayerColor playerColor) {
        return this.playerNames.getOrDefault(playerColor, null);
    }

    @Override
    public String points(int points) {
        return amountWord(points, "point");
    }

    @Override
    public String playerClosedForestWithMenhir(PlayerColor player) {
        return STR."""
        \{this.playerNames.get(player)} a fermé une forêt contenant un menhir et peut\
         donc placer une tuile menhir.\
        """;
    }

    @Override
    public String playersScoredForest(
            Set<PlayerColor> scorers, int points, int mushroomGroupCount, int tileCount
    ) {
        return STR."""
        \{formatPlayers(scorers)} \{getVerb(scorers.size())} remporté \{points(points)} en tant\
         \{occupants(scorers.size())} d'une forêt composée de\
         \{amountWord(tileCount, "tuile")}\{mushroomGroupCount > 0
                ? STR." et de \{amountWord(mushroomGroupCount, "groupe")} de champignons."
                : "."}\
        """;
    }

    @Override
    public String playersScoredRiver(
            Set<PlayerColor> scorers, int points, int fishCount, int tileCount
    ) {
        return STR."""
        \{formatPlayers(scorers)} \{getVerb(scorers.size())} remporté \{points(points)} en tant\
         \{occupants(scorers.size())} d'une rivière composée de\
         \{amountWord(tileCount, "tuile")}\{fishCount > 0
                ? STR." et contenant \{amountWord(fishCount, "poisson")}."
                : "."}\
        """;
    }

    @Override
    public String playerScoredHuntingTrap(
            PlayerColor scorer, int points, Map<Animal.Kind, Integer> animals
    ) {
        return STR."""
        \{this.playerNames.get(scorer)} a remporté \{points(points)} en plaçant la fosse à pieux\
         dans un pré dans lequel elle est entourée de \{formatAnimals(animals)}.\
        """;
    }

    @Override
    public String playerScoredLogboat(PlayerColor scorer, int points, int lakeCount) {
        // no check for lake count needs to be made as that is done in MessageBoard
        return STR."""
        \{this.playerNames.get(scorer)} a remporté \{points(points)} en plaçant la pirogue dans un\
         réseau hydrographique contenant \{amountWord(lakeCount, "lac")}.\
        """;
    }

    @Override
    public String playersScoredMeadow(
            Set<PlayerColor> scorers, int points, Map<Animal.Kind, Integer> animals
    ) {
        return STR."""
        \{formatPlayers(scorers)} \{getVerb(scorers.size())} remporté \{points(points)} en tant\
         \{occupants(scorers.size())} d'un pré contenant \{formatAnimals(animals)}.\
        """;
    }

    @Override
    public String playersScoredRiverSystem(Set<PlayerColor> scorers, int points, int fishCount) {
        // no check for fish count needs to be made as that is done in MessageBoard
        return STR."""
        \{formatPlayers(scorers)} \{getVerb(scorers.size())} remporté \{points(points)} en tant\
         \{occupants(scorers.size())} d'un réseau hydrographique contenant\
         \{amountWord(fishCount, "poisson")}.\
        """;
    }

    @Override
    public String playersScoredPitTrap(
            Set<PlayerColor> scorers, int points, Map<Animal.Kind, Integer> animals
    ) {
        return STR."""
        \{formatPlayers(scorers)} \{getVerb(scorers.size())} remporté \{points(points)} en tant\
         \{occupants(scorers.size())} d'un pré contenant la grande fosse à pieux entourée de\
         \{formatAnimals(animals)}.\
        """;
    }

    @Override
    public String playersScoredRaft(Set<PlayerColor> scorers, int points, int lakeCount) {
        // no check for fish count needs to be made as that is done in MessageBoard
        return STR."""
        \{formatPlayers(scorers)} \{getVerb(scorers.size())} remporté \{points(points)} en tant\
         \{occupants(scorers.size())} d'un réseau hydrographique contenant le radeau et\
         \{amountWord(lakeCount, "lac")}.\
        """;
    }

    @Override
    public String playersWon(Set<PlayerColor> winners, int points) {
        return STR."""
        \{formatPlayers(winners)} \{getVerb(winners.size())} remporté la partie avec\
         \{points(points)} !\
        """;
    }

    @Override
    public String clickToOccupy() {
        return
        "Cliquez sur le pion ou la hutte que vous désirez placer, ou ici pour ne pas en placer.";
    }

    @Override
    public String clickToUnoccupy() {
        return "Cliquez sur le pion que vous désirez reprendre, ou ici pour ne pas en reprendre.";
    }
}
