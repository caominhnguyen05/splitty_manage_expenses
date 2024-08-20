package commons;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ParticipantTest {

    private Participant p1;
    private Participant p2;
    private Participant p3;

    @BeforeEach
    void setup(){
        p1 = new Participant("John", "123456", "ABC123", "j@gmail.com");
        p2 = new Participant("John", "123456", "ABC123", "j@gmail.com");
        p3 = new Participant("Anna", "123456", "ABC123", "j@gmail.com");
    }
    @Test
    void checkConstructor(){
        assertEquals("John", p1.name);
        assertEquals("123456", p1.iban);
        assertEquals("ABC123", p1.bic);
        assertEquals("j@gmail.com", p1.email);
    }
    @Test
    void testEquals() {
        assertEquals(p1,p2);
        assertNotEquals(p1,p3);
    }

    @Test
    void testHashCode() {
        assertEquals(p1.hashCode(),p2.hashCode());
        assertNotEquals(p1.hashCode(),p3.hashCode());
    }

    @Test
    void hasToString() {
        String s = p1.toString();
        assertTrue(s.contains(Participant.class.getSimpleName()));
        assertTrue(s.contains("\n"));
        assertTrue(s.contains("name"));
    }

    @Test
    void testToString() {
        Participant p = new Participant();
        Participant p2 = new Participant();
        assertEquals(p, p2);
    }

}