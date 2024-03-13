package ch.epfl.chacun;

import java.util.Map;
import java.util.Set;
import java.util.StringJoiner;

public class TextMakerTEST implements TextMaker{
    @Override
    public String playerName(PlayerColor playerColor) {
        return switch (playerColor) {
            case RED -> "Red";
            case BLUE -> "Blue";
            case GREEN -> "Green";
            case YELLOW -> "Yellow";
            case PURPLE -> "Purple";
        };
    }

    @Override
    public String points(int points) {
        StringJoiner sj = new StringJoiner(" ");
        return sj.add(Integer.toString(points)).add("points").toString();
    }

    @Override
    public String playerClosedForestWithMenhir(PlayerColor player) {
        StringJoiner sj = new StringJoiner(" ");
        return sj.add("Player").add(this.playerName(player)).add("closed a forest with a menhir.").toString();
    }

    @Override
    public String playersScoredForest(Set<PlayerColor> scorers, int points, int mushroomGroupCount, int tileCount) {
        StringJoiner sj = new StringJoiner(" ");

        StringJoiner playerSJ = new StringJoiner(", ");
        for (PlayerColor scorer : scorers) {
            playerSJ.add(this.playerName(scorer));
        }

        return sj.add("Players").add(playerSJ.toString()).add("have gained").add(this.points(points))
            .add("for closing a forest containing").add(Integer.toString(mushroomGroupCount))
            .add("mushroom groups and consisting of").add(Integer.toString(tileCount)).add("tiles.").toString();
    }

    @Override
    public String playersScoredRiver(Set<PlayerColor> scorers, int points, int fishCount, int tileCount) {
        StringJoiner sj = new StringJoiner(" ");

        StringJoiner playerSJ = new StringJoiner(", ");
        for (PlayerColor scorer : scorers) {
            playerSJ.add(this.playerName(scorer));
        }

        return sj.add("Players").add(playerSJ.toString()).add("have gained").add(this.points(points))
            .add("for closing a river containing").add(Integer.toString(fishCount))
            .add("fishes and consisting of").add(Integer.toString(tileCount)).add("tiles.").toString();
    }

    @Override
    public String playerScoredHuntingTrap(PlayerColor scorer, int points, Map<Animal.Kind, Integer> animals) {
        StringJoiner sj = new StringJoiner(" ");
        return sj.add("Player").add(this.playerName(scorer)).add("has gained").add(this.points(points))
            .add("by placing the hunting trap in a meadow with").add(Integer.toString(animals.size()))
            .add("animals in it.").toString();
    }

    @Override
    public String playerScoredLogboat(PlayerColor scorer, int points, int lakeCount) {
        StringJoiner sj = new StringJoiner(" ");
        return sj.add("Player").add(this.playerName(scorer)).add("has gained").add(this.points(points))
            .add("by placing the logboat in a river system with").add(Integer.toString(lakeCount))
            .add("lakes.").toString();
    }

    @Override
    public String playersScoredMeadow(Set<PlayerColor> scorers, int points, Map<Animal.Kind, Integer> animals) {
        StringJoiner sj = new StringJoiner(" ");

        StringJoiner playerSJ = new StringJoiner(", ");
        for (PlayerColor scorer : scorers) {
            playerSJ.add(this.playerName(scorer));
        }

        return sj.add("Players").add(playerSJ.toString()).add("have gained").add(this.points(points))
            .add("by occupying a meadow with").add(Integer.toString(animals.size()))
            .add("animals in it.").toString();
    }

    @Override
    public String playersScoredRiverSystem(Set<PlayerColor> scorers, int points, int fishCount) {
        StringJoiner sj = new StringJoiner(" ");

        StringJoiner playerSJ = new StringJoiner(", ");
        for (PlayerColor scorer : scorers) {
            playerSJ.add(this.playerName(scorer));
        }

        return sj.add("Players").add(playerSJ.toString()).add("have gained").add(this.points(points))
            .add("for closing river system with").add(Integer.toString(fishCount))
            .add("fishes.").toString();
    }

    @Override
    public String playersScoredPitTrap(Set<PlayerColor> scorers, int points, Map<Animal.Kind, Integer> animals) {
        StringJoiner sj = new StringJoiner(" ");

        StringJoiner playerSJ = new StringJoiner(", ");
        for (PlayerColor scorer : scorers) {
            playerSJ.add(this.playerName(scorer));
        }

        return sj.add("Players").add(playerSJ.toString()).add("have gained").add(this.points(points))
            .add("by occupying a meadow containing the pit trap with").add(Integer.toString(animals.size()))
            .add("animals in it.").toString();
    }

    @Override
    public String playersScoredRaft(Set<PlayerColor> scorers, int points, int lakeCount) {
        StringJoiner sj = new StringJoiner(" ");

        StringJoiner playerSJ = new StringJoiner(", ");
        for (PlayerColor scorer : scorers) {
            playerSJ.add(this.playerName(scorer));
        }

        return sj.add("Players").add(playerSJ.toString()).add("have gained").add(this.points(points))
            .add("by occupying a river system containing the raft with").add(Integer.toString(lakeCount))
            .add("lakes.").toString();
    }

    @Override
    public String playersWon(Set<PlayerColor> winners, int points) {
        StringJoiner sj = new StringJoiner(" ");

        StringJoiner playerSJ = new StringJoiner(", ");
        for (PlayerColor winner : winners) {
            playerSJ.add(this.playerName(winner));
        }

        return sj.add("Players").add(playerSJ.toString()).add("have won the game! Congratulations!").toString();
    }

    @Override
    public String clickToOccupy() {
        return "Click on the occupant you want to place in the area, or this message to not place any occupant.";
    }

    @Override
    public String clickToUnoccupy() {
        return "Click on the pawn you want to remove from the area, or this message to not remove any pawns.";
    }
}
