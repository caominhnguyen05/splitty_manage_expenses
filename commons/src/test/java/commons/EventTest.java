package commons;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EventTest {
    private Event e1;
    private Event e2;
    private Event e3;
    @BeforeEach
    void setup(){
        e1 = new Event("Holiday");
        e2 = new Event("Holiday");
        e3 = new Event("Party");
    }
    @Test
    void checkConstuctor(){

        assertEquals("Holiday", e1.name);
        assertNotNull(e1.participants);
        assertNotNull(e1.expenses);
        assertNotNull(e1.payments);
    }

    @Test
    void testEquals() {
        assertEquals(e1, e2);
        assertNotEquals(e1, e3);
    }

    @Test
    void testHashCode() {
        assertEquals(e1.hashCode(), e2.hashCode());
        assertNotEquals(e1.hashCode(), e3. hashCode());
    }

    @Test
    void hasToString() {
        String s = e1.toString();
        assertTrue(s.contains(Event.class.getSimpleName()));
        assertTrue(s.contains("\n"));
        assertTrue(s.contains("name"));
    }

    @Test
    void participantsString() {
        assertEquals("no participants", e1.participantsString());
    }

    @Test
    void participantsString2() {
        e1.participants.add(new Participant("Jack", "ABC", "4BC", "a@gmail.com"));
        e1.participants.add(new Participant("James", "ABC", "4BC", "a@gmail.com"));
        assertEquals("Jack, James", e1.participantsString());
    }

    @Test
    void emptyEvent() {
        Event e = new Event();
        Event e2=  new Event();
        assertEquals(e, e2);
    }

}