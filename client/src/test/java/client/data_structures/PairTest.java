package client.data_structures;

import commons.Participant;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PairTest {

    private Pair<Participant, Double> pair;
    private Participant participant;
    @BeforeEach
    void setup(){
        participant = new Participant("John","123123",
            "ABC123","j@emial.com");
        pair = new Pair<>(participant, 20.0);
    }

    @Test
    void getParticipant() {
        assertEquals(participant,pair.getParticipant());
    }

    @Test
    void setParticipant() {
        Participant participant1 = new Participant("Eddy","123123",
            "ABC123","j@emial.com");
        pair.setParticipant(participant1);
        assertEquals(participant1,pair.getParticipant());
    }

    @Test
    void getBalance() {
        assertEquals(20.0,pair.getBalance());
    }

    @Test
    void setBalance() {
        pair.setBalance(30.0);
        assertEquals(30.0,pair.getBalance());
    }
}