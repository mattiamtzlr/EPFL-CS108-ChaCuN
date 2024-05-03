package ch.epfl.chacun.stage10;

import ch.epfl.chacun.Base32;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;


public class MyBase32Test {

    @Test
    void isValidForTrivialCase(){
        for (char c : Base32.ALPHABET.toCharArray()) {
            for (char d : Base32.ALPHABET.toCharArray()) {
                assertTrue(Base32.isValid(String.join("", String.valueOf(c), String.valueOf(d))));
            }
        }
    }

}