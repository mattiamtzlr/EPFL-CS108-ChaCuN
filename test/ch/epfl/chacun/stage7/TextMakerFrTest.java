package ch.epfl.chacun.stage7;

import ch.epfl.chacun.TextMakerFr;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

import static ch.epfl.chacun.Animal.Kind.*;
import static ch.epfl.chacun.PlayerColor.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class TextMakerFrTest {

    /*
        This test file tests primarily the cases shown in the handout, should be enough.
     */

    static final TextMakerFr textMakerFr = new TextMakerFr(Map.of(
            RED, "Dalia",
            BLUE, "Claude",
            GREEN, "Bachir",
            YELLOW, "Alice"
    ));

    @Test
    void pointsFormatIsCorrect() {
        assertEquals("1 point", textMakerFr.points(1));

        for (int i = 2; i <= 10; i++) {
            assertEquals(STR."\{i} points", textMakerFr.points(i));
        }
    }

    @Test
    void playerClosedForestWithMenhirIsCorrect() {
        assertEquals(
                "Dalia a fermé une forêt contenant un menhir et peut donc placer une tuile menhir.",
                textMakerFr.playerClosedForestWithMenhir(RED)
        );
    }

    @Test
    void playerScoredForestIsCorrect() {
        assertEquals(
                "Claude a remporté 6 points en tant qu'occupant·e majoritaire d'une forêt composée de 3 tuiles.",
                textMakerFr.playersScoredForest(Collections.singleton(BLUE), 6, 0, 3)
        );

        assertEquals(
                "Dalia et Alice ont remporté 9 points en tant qu'occupant·e·s majoritaires d'une forêt composée de 3 tuiles et de 1 groupe de champignons.",
                textMakerFr.playersScoredForest(Set.of(RED, YELLOW), 9, 1, 3)
        );
    }

    @Test
    void playerScoredRiverIsCorrect() {
        assertEquals(
                "Claude et Bachir ont remporté 3 points en tant qu'occupant·e·s majoritaires d'une rivière composée de 3 tuiles.",
                textMakerFr.playersScoredRiver(Set.of(BLUE, GREEN), 3, 0, 3)
        );

        assertEquals(
                "Alice a remporté 8 points en tant qu'occupant·e majoritaire d'une rivière composée de 3 tuiles et contenant 5 poissons.",
                textMakerFr.playersScoredRiver(Collections.singleton(YELLOW), 8, 5, 3)
        );
    }

    @Test
    void playerScoredHuntingTrapIsCorrect() {
        assertEquals(
                "Bachir a remporté 10 points en plaçant la fosse à pieux dans un pré dans lequel elle est entourée de 1 mammouth, 2 aurochs et 3 cerfs.",
                textMakerFr.playerScoredHuntingTrap(GREEN, 10, Map.of(
                        MAMMOTH, 1,
                        AUROCHS, 2,
                        DEER, 3
                ))
        );
    }

    @Test
    void playerScoredLogboatIsCorrect() {
        assertEquals(
                "Alice a remporté 8 points en plaçant la pirogue dans un réseau hydrographique contenant 4 lacs.",
                textMakerFr.playerScoredLogboat(YELLOW, 8, 4)
        );

    }

    @Test
    void playersScoredMeadowIsCorrect() {
        assertEquals(
                "Dalia a remporté 1 point en tant qu'occupant·e majoritaire d'un pré contenant 1 cerf.",
                textMakerFr.playersScoredMeadow(Collections.singleton(RED), 1, Map.of(
                        DEER, 1
                ))
        );

        assertEquals(
                "Claude et Bachir ont remporté 5 points en tant qu'occupant·e·s majoritaires d'un pré contenant 1 mammouth et 2 cerfs.",
                textMakerFr.playersScoredMeadow(Set.of(GREEN, BLUE), 5, Map.of(
                        MAMMOTH, 1,
                        DEER, 2
                ))
        );
    }

    @Test
    void playersScoredRiverSystemIsCorrect() {
        assertEquals(
                "Alice a remporté 9 points en tant qu'occupant·e majoritaire d'un réseau hydrographique contenant 9 poissons.",
                textMakerFr.playersScoredRiverSystem(Collections.singleton(YELLOW), 9, 9)
        );

        assertEquals(
                "Dalia, Claude et Bachir ont remporté 1 point en tant qu'occupant·e·s majoritaires d'un réseau hydrographique contenant 1 poisson.",
                textMakerFr.playersScoredRiverSystem(Set.of(RED, BLUE, GREEN), 1, 1)
        );
    }

    @Test
    void playersScoredPitTrapIsCorrect() {
        assertEquals(
                "Bachir et Alice ont remporté 12 points en tant qu'occupant·e·s majoritaires d'un pré contenant la grande fosse à pieux entourée de 2 mammouths, 2 aurochs et 2 cerfs.",
                textMakerFr.playersScoredPitTrap(Set.of(GREEN, YELLOW), 12, Map.of(
                        MAMMOTH, 2,
                        AUROCHS, 2,
                        DEER, 2
                ))
        );

        assertEquals(
                "Dalia a remporté 2 points en tant qu'occupant·e majoritaire d'un pré contenant la grande fosse à pieux entourée de 1 auroch.",
                textMakerFr.playersScoredPitTrap(Collections.singleton(RED), 2, Map.of(
                        AUROCHS, 1
                ))
        );
    }

    @Test
    void playersScoredRaftIsCorrect() {
        assertEquals(
                "Dalia et Claude ont remporté 10 points en tant qu'occupant·e·s majoritaires d'un réseau hydrographique contenant le radeau et 10 lacs.",
                textMakerFr.playersScoredRaft(Set.of(RED, BLUE), 10, 10)
        );

        assertEquals(
                "Alice a remporté 1 point en tant qu'occupant·e majoritaire d'un réseau hydrographique contenant le radeau et 1 lac.",
                textMakerFr.playersScoredRaft(Collections.singleton(YELLOW), 1, 1)
        );
    }

    @Test
    void playersWonIsCorrect() {
        assertEquals(
                "Bachir a remporté la partie avec 111 points !",
                textMakerFr.playersWon(Collections.singleton(GREEN), 111)
        );

        assertEquals(
                "Dalia et Alice ont remporté la partie avec 123 points !",
                textMakerFr.playersWon(Set.of(RED, YELLOW), 123)
        );
    }

    @Test
    void clickToOccupyIsCorrect() {
        assertEquals(
                "Cliquez sur le pion ou la hutte que vous désirez placer, ou ici pour ne pas en placer.",
                textMakerFr.clickToOccupy()
        );
    }

    @Test
    void clickToUnoccupyIsCorrect() {
        assertEquals(
                "Cliquez sur le pion que vous désirez reprendre, ou ici pour ne pas en reprendre.",
                textMakerFr.clickToUnoccupy()
        );
    }
}