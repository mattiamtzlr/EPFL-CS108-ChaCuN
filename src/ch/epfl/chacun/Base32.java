package ch.epfl.chacun;

/**
 * Class to facilitate working with Base32.
 *
 * @author Mattia Metzler (372025)
 * @author Leoluca Bernardi (374107)
 */
public final class Base32 {
    /**
     * Constant that contains the base 32 alphabet.
     */
    public static final String ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ234567";
    private Base32(){}

    /**
     * Method to check the validity of a Base32 expression.
     * @param word string to check.
     * @return true if all characters can be represented in base 32.
     */
    public static boolean isValid(String word) {
        return word.chars().allMatch(c -> ALPHABET.contains(Character.toString(c)));
    }

    /**
     * Method to encode positive integers smaller than 32.
     * @param plain number to encode to base 32.
     * @return encoded integer in base 32, one character.
     */
    public static String encodeBits5(int plain) {
        Preconditions.checkArgument(plain < 32 && plain >= 0);
        return Character.toString(ALPHABET.charAt(plain));
    }

    /**
     * Method to encode positive integers smaller than 1024.
     * Less efficient than encodeBits5
     * @param plain number to encode to base 32.
     * @return encoded integer in base 32, two characters.
     */
    public static String encodeBits10(int plain) {
        Preconditions.checkArgument(plain < 1024 && plain >= 0);
        return String.join("",
                encodeBits5((plain & 0b1111100000) >> 5), encodeBits5(plain & 0b11111));
    }

    /**
     * Method to decode a nonempty string of length two or smaller from base 32 to integers.
     * @param cipher string to decode.
     * @return integer decoded from the passed string.
     */
    public static int decode(String cipher) {
        Preconditions.checkArgument(cipher.length() <= 2 && !cipher.isEmpty());
        int[] digits = cipher.chars().map(ALPHABET::indexOf).toArray();
        return digits.length == 1 ? digits[0] : digits[1] + digits[0]*32;
    }

}
