package ch.epfl.chacun;

import java.util.Map;
import java.util.Set;

/**
 * TODO DESCRIPTION
 *
 * @author Mattia Metzler (372025)
 * @author Leoluca Bernardi (374107)
 */
public final class TextMakerFr implements TextMaker {
    Map<PlayerColor, String> playerNames;

    public TextMakerFr(Map<PlayerColor, String> playerNames) {
        this.playerNames = playerNames;
    }

    @Override
    public String playerName(PlayerColor playerColor) {
        return this.playerNames.getOrDefault(playerColor,"");
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
