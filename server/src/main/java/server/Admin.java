package server;

import java.util.Random;

public class Admin {

    private final String password;
    private final int passwordLength = 20;

    /**
     * Constructor for this class, generates this.password, using this.passwordLength
     */
    public Admin() {
        this.password = generatePassword(passwordLength);
        System.out.println("\n\nServer password: " + password + "\n");
    }

    /**
     * Password generator. Generates a random password of a given length, using java.util.Random
     * @param length length that the generated password should be
     * @return returns the generated password
     */
    private String generatePassword(int length) {
        Random random = new Random();
        StringBuilder pass = new StringBuilder();
        // All possible characters in password
        String chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

        // Generate random characters and append them
        for (int i = 0; i < length; i++) {
            pass.append( chars.charAt(random.nextInt(chars.length())) );
        }

        return pass.toString();
    }

    /**
     * Password getter
     * @return returns this.password
     */
    public String getPassword() {
        return password;
    }
}
