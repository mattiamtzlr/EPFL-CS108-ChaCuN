package ch.epfl.chacun.week6;

import ch.epfl.chacun.Animal;
import ch.epfl.chacun.PlayerColor;
import ch.epfl.chacun.TextMaker;

import java.util.Map;
import java.util.Set;
import java.util.StringJoiner;

public class TextMakerImplementedForTests implements TextMaker {

    /**
     * Retourne le nom du joueur de couleur donnée.
     *
     * @param playerColor la couleur du joueur
     * @return le nom du joueur
     */
    @Override
    public String playerName(PlayerColor playerColor) {
        return playerColor.name();
    }

    /**
     * Retourne la représentation textuelle du nombre de points donnés (p. ex. "3 points").
     *
     * @param points le nombre de points
     * @return la représentation textuelle du nombre de points
     */
    @Override
    public String points(int points) {
        return String.valueOf(points);
    }

    /**
     * Retourne le texte d'un message déclarant qu'un joueur a fermé une forêt avec un menhir.
     *
     * @param player le joueur ayant fermé la forêt
     * @return le texte du message
     */
    @Override
    public String playerClosedForestWithMenhir(PlayerColor player) {
        return this.playerName(player) + " has closed a forest with menhir";
    }

    /**
     * Retourne le texte d'un message déclarant que les occupants majoritaires d'une forêt nouvellement
     * fermée, constituée d'un certain nombre de tuiles et comportant un certain nombre de groupes de champignons,
     * ont remporté les points correspondants.
     *
     * @param scorers            les occupants majoritaires de la forêt
     * @param points             les points remportés
     * @param mushroomGroupCount le nombre de groupes de champignons que la forêt contient
     * @param tileCount          le nombre de tuiles qui constitue la forêt
     * @return le texte du message
     */
    @Override
    public String playersScoredForest(Set<PlayerColor> scorers, int points, int mushroomGroupCount, int tileCount) {
        return new StringJoiner(" ")
                .add(scorers.toString())
                .add(String.valueOf(points))
                .add(String.valueOf(mushroomGroupCount))
                .add(String.valueOf(tileCount))
                .add(" scored with forest")
                .toString();
    }

    /**
     * Retourne le texte d'un message déclarant que les occupants majoritaires d'une rivière nouvellement
     * fermée, constituée d'un certain nombre de tuiles et comportant un certain nombre de poissons,
     * ont remporté les points correspondants.
     *
     * @param scorers   les occupants majoritaires de la rivière
     * @param points    les points remportés
     * @param fishCount le nombre de poissons nageant dans la rivière ou les lacs adjacents
     * @param tileCount le nombre de tuiles qui constitue la rivière
     * @return le texte du message
     */
    @Override
    public String playersScoredRiver(Set<PlayerColor> scorers, int points, int fishCount, int tileCount) {

        return new StringJoiner(" ")
                .add(scorers.toString())
                .add(String.valueOf(points))
                .add(String.valueOf(fishCount))
                .add(String.valueOf(tileCount))
                .add(" scored with river")
                .toString();
    }

    /**
     * Retourne le texte d'un message déclarant qu'un joueur a déposé la fosse à pieux dans un pré contenant,
     * sur les 8 tuiles voisines de la fosse, certains animaux, et remporté les points correspondants.
     *
     * @param scorer  le joueur ayant déposé la fosse à pieux
     * @param points  les points remportés
     * @param animals les animaux présents dans le même pré que la fosse et sur les 8 tuiles voisines
     * @return le texte du message
     */
    @Override
    public String playerScoredHuntingTrap(PlayerColor scorer, int points, Map<Animal.Kind, Integer> animals) {
        return new StringJoiner(" ")
                .add(this.playerName(scorer))
                .add(String.valueOf(points))
                .add(animals.toString())
                .add(" scored with hunting trap")
                .toString();
    }

    /**
     * Retourne le texte d'un message déclarant qu'un joueur a déposé la pirogue dans un réseau hydrographique
     * comportant un certain nombre de lacs, et remporté les points correspondants.
     *
     * @param scorer    le joueur ayant déposé la pirogue
     * @param points    les points remportés
     * @param lakeCount le nombre de lacs accessibles à la pirogue
     * @return le texte du message
     */
    @Override
    public String playerScoredLogboat(PlayerColor scorer, int points, int lakeCount) {
        return new StringJoiner(" ")
                .add(this.playerName(scorer))
                .add(String.valueOf(points))
                .add(String.valueOf(lakeCount))
                .add(" scored with log boat")
                .toString();
    }


    /**
     * Retourne le texte d'un message déclarant que les occupants majoritaires d'un pré contenant certains
     * animaux ont remporté les points correspondants.
     *
     * @param scorers les occupants majoritaires du pré
     * @param points  les points remportés
     * @param animals les animaux présents dans le pré (sans ceux ayant été précédemment annulés)
     * @return le texte du message
     */
    @Override
    public String playersScoredMeadow(Set<PlayerColor> scorers, int points, Map<Animal.Kind, Integer> animals) {
        return new StringJoiner(" ")
                .add(scorers.toString())
                .add(String.valueOf(points))
                .add(animals.toString())
                .add(" scored with meadow")
                .toString();
    }

    /**
     * Retourne le texte d'un message déclarant que les occupants majoritaires d'un réseau hydrographique
     * comportant un certain nombre de poissons ont remporté les points correspondants.
     *
     * @param scorers   les occupants majoritaires du réseau hydrographique
     * @param points    les points remportés
     * @param fishCount le nombre de poissons nageant dans le réseau hydrographique
     * @return le texte du message
     */
    @Override
    public String playersScoredRiverSystem(Set<PlayerColor> scorers, int points, int fishCount) {
        return new StringJoiner(" ")
                .add(scorers.toString())
                .add(String.valueOf(points))
                .add(String.valueOf(fishCount))
                .add(" scored with river system")
                .toString();
    }

    /**
     * Retourne le texte d'un message déclarant que les occupants majoritaires d'un pré contenant la
     * grande fosse à pieux et, sur les 8 tuiles voisines d'elles, certains animaux, ont remporté les
     * points correspondants.
     *
     * @param scorers les occupants majoritaires du pré contenant la fosse à pieux
     * @param points  les points remportés
     * @param animals les animaux présents sur les tuiles voisines de la fosse (sans ceux ayant été précédemment annulés)
     * @return le texte du message
     */
    @Override
    public String playersScoredPitTrap(Set<PlayerColor> scorers, int points, Map<Animal.Kind, Integer> animals) {
        return new StringJoiner(" ")
                .add(scorers.toString())
                .add(String.valueOf(points))
                .add(animals.toString())
                .add(" scored with pit trap")
                .toString();
    }

    /**
     * Retourne le texte d'un message déclarant que les occupants majoritaires d'un réseau hydrographique
     * contenant le radeau ont remporté les points correspondants.
     *
     * @param scorers   les occupants majoritaires du réseau hydrographique comportant le radeau
     * @param points    les points remportés
     * @param lakeCount le nombre de lacs contenus dans le réseau hydrographique
     * @return le texte du message
     */
    @Override
    public String playersScoredRaft(Set<PlayerColor> scorers, int points, int lakeCount) {
        return new StringJoiner(" ")
                .add(scorers.toString())
                .add(String.valueOf(points))
                .add(String.valueOf(lakeCount))
                .add(" scored with raft")
                .toString();
    }

    /**
     * Retourne le texte d'un message déclarant qu'un ou plusieurs joueurs ont remporté la partie, avec un
     * certain nombre de points.
     *
     * @param winners l'ensemble des joueurs ayant remporté la partie
     * @param points  les points des vainqueurs
     * @return le texte du message
     */
    @Override
    public String playersWon(Set<PlayerColor> winners, int points) {
        return new StringJoiner(" ")
                .add(winners.toString())
                .add(" won with ")
                .add(String.valueOf(points))
                .add(" points")
                .toString();
    }

    /**
     * Retourne un texte demandant au joueur actuel de cliquer sur l'occupant qu'il désire placer, ou sur le texte
     * du message s'il ne désire placer aucun occupant.
     *
     * @return le texte en question
     */
    @Override
    public String clickToOccupy() {
        return "Click the occupant you wish to place, click this message otherwise";
    }

    /**
     * Retourne un texte demandant au joueur actuel de cliquer sur le pion qu'il désire reprendre, ou sur le texte
     * du message s'il ne désire reprendre aucun pion.
     *
     * @return le texte en question
     */
    @Override
    public String clickToUnoccupy() {
        return "Click the occupant you wish to take back, click this message otherwise";
    }
}
