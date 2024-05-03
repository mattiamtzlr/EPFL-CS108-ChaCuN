package ch.epfl.chacun.stage10;

import ch.epfl.chacun.Base32;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;


public class MyBase32Test {

    @Test
    void isValidForTrivialCase(){
        for (char c : Base32.ALPHABET.toCharArray()) {
            for (char d : Base32.ALPHABET.toCharArray()) {
                assertTrue(Base32.isValid(String.join("", Character.toString(c), Character.toString(d))));
            }
        }
        assertFalse(Base32.isValid("88"));
        assertFalse(Base32.isValid("??"));
        assertFalse(Base32.isValid(".E"));
        assertFalse(Base32.isValid("-2"));
    }

    @Test
    void encodeBits5WorksForTrivialCase(){
        for (int i = 0; i < 32; i++) {
            assertEquals(Character.toString(Base32.ALPHABET.toCharArray()[i]), Base32.encodeBits5(i));
            int j = -(i+1);
            assertThrows(IllegalArgumentException.class, () -> Base32.encodeBits5(j));
        }
        for (int i = 32; i < 100; i++) {
            int finalI = i;
            assertThrows(IllegalArgumentException.class, () -> Base32.encodeBits5(finalI));
        }

    }

    @Test
    void encodeBits10WorksForTrivialCase(){
        Map<Integer, String> testCases = new HashMap<>();
        int i = 0;
        for (char c : Base32.ALPHABET.toCharArray()) {
            for (char d : Base32.ALPHABET.toCharArray()) {
                testCases.put(i++, String.join("", Character.toString(c), Character.toString(d)));

            }
        }
        for (int j = 0; j < i; j++) {
            assertEquals(testCases.get(j), Base32.encodeBits10(j));
        }

    }

    @Test
    void decodeWorksForTrivialCase(){

        Map<Integer, String> testCases = new HashMap<>();
        int i = 0;
        for (char c : Base32.ALPHABET.toCharArray()) {
            for (char d : Base32.ALPHABET.toCharArray()) {
                testCases.put(i++, String.join("", Character.toString(c), Character.toString(d)));

            }
        }
        for (int j = 0; j < i; j++) {
            assertEquals(testCases.get(j), Base32.encodeBits10(j));
        }

    }



}