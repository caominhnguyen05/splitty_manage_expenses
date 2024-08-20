package client.data_structures;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InviteCodeTest {

    private InviteCode c1;
    private InviteCode c2;

    @BeforeEach
    void setup() {
        c1 = new InviteCode();
        c2 = new InviteCode();
    }

    @Test
    void notNullTest() {
        assertNotNull(c1);
    }

    @Test
    void codeTest() {
        String lettersAndDigits = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        String[] characters = c1.getCode().split("");
        for (String cha : characters) {
            assertTrue(lettersAndDigits.contains(cha));
        }
    }

    @Test
    void equalsTest() {
        assertEquals(c1, c1);
        assertNotEquals(c1, c2);
    }

    @Test
    void hashTest() {
        assertEquals(c1.hashCode(), c1.hashCode());
        assertNotEquals(c1.hashCode(), c2.hashCode());
    }

    @Test
    void resetTest() {
        String oldCode = c1.getCode();
        System.out.println(oldCode);
        c1.resetCode();
        System.out.println(c1.getCode());
        assertNotEquals(oldCode, c1.getCode());
    }

}