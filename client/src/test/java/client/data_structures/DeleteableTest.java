package client.data_structures;

import commons.Participant;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DeleteableTest {

    private Participant p1;
    private Participant p2;
    private Deleteable d1;

    @BeforeEach
    void setup() {
        p1 = new Participant("Frank", "iban", "bic", "frank@example.com");
        p2 = new Participant("Hank", "iban", "bic", "hank@example.com");
        d1 = new Deleteable(List.of(p1, p2));
    }

    @Test
    void getParticipantListTest() {
        assertEquals(List.of(p1, p2), d1.getParticipantList());
    }

    @Test
    void getParticipantNamesTest() {
        assertEquals(List.of("Frank", "Hank"), d1.getPartNames());
    }

    @Test
    void getDeleteOrNotBooleansTest() {
        assertEquals(List.of(false, false), d1.getDeleteOrNotBooleans());
    }

    @Test
    void getDeleteTest() {
        assertFalse(d1.getDelete("Hank"));
    }

    @Test
    void setDeleteTest() {
        assertFalse(d1.getDelete("Frank"));
        d1.setDelete("Frank", true);
        assertTrue(d1.getDelete("Frank"));
        d1.setDelete("Frank", false);
    }
}