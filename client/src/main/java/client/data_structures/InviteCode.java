package client.data_structures;

import java.util.Objects;
import java.util.Random;

public class InviteCode {
    private String code;

    /**
     * Constructor for InviteCode, immediately generates a code and sets it
     */
    public InviteCode() {
        code = generateCode();
    }

    /**
     * Generates a 6 character or digit code
     * @return      a random 6 character string
     */
    private static String generateCode () {

        // Create an instance of Random class
        Random random = new Random();

        StringBuilder code = new StringBuilder();
        char randomChar;

        // Generate 6 random characters and append them
        for (int i = 0; i < 6; i++) {
            randomChar = generateRandomCharacter(random);
            code.append(randomChar);
        }
        // Return string
        return code.toString();
    }

    /**
     * Generates a random character or digit
     * @param random    an instance of the Random class
     * @return          returns a single random character (lower or upper case) or digit
     */
    private static char generateRandomCharacter(Random random) {
        // All possible characters in a code
        String lettersAndDigits = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

        // Generate random character of lettersAndDigits and return it
        int randomIndex = random.nextInt(lettersAndDigits.length());
        return lettersAndDigits.charAt(randomIndex);
    }

    /**
     * Getter for the invite code
     * @return  the code of the current object
     */
    public String getCode() {return code;}

    /**
     * Reset the code associated to the object
     */
    public void resetCode() {
        this.code = generateCode();
    }

    /**
     * Equals method for two InviteCode objects
     * @param o     the object to compare with
     * @return      a boolean value
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || o.getClass() != this.getClass()) return false;

        InviteCode that = (InviteCode) o;
        return Objects.equals(code, that.code);
    }

    /**
     * Hashcode method for an InviteCode
     * @return  an integer hashcode
     */
    @Override
    public int hashCode() {
        return Objects.hash(code);
    }
}
