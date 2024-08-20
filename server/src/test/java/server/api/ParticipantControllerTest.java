package server.api;

import commons.Expense;
import commons.Participant;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.OK;

class ParticipantControllerTest {
    private TestParticipantRepository repo;
    private TestEventWebSocketHandler webSocketHandler;
    private ParticipantController sut;

    @BeforeEach
    public void setup() {
        repo = new TestParticipantRepository();
        webSocketHandler = new TestEventWebSocketHandler();
        sut = new ParticipantController(repo, webSocketHandler);
    }

    @Test
    public void cannotAddNullName() {
        var actual = sut.add(getParticipant(null));
        assertEquals(BAD_REQUEST, actual.getStatusCode());
    }

    private static Participant getParticipant(String name) {
        return new Participant(name, null, null, null);
    }

    @Test
    void getAll() {
        Participant p1 = new Participant();
        p1.name = "James";
        Participant p2 = new Participant();
        p2.name = "Jack";

        repo.save(p1);
        repo.save(p2);

        var response = sut.getAll();
        assertEquals(2, response.size());
        assertTrue(response.contains(p1));
        assertTrue(response.contains(p2));
    }

    // Failed test
    @Test
    void getById() {
        Participant participant = new Participant();
        participant.name = "Minh";

        Participant saved = repo.save(participant);
        ResponseEntity<Participant> response = sut.getById(saved.id);

        assertEquals(OK, response.getStatusCode());
        assertEquals(saved, response.getBody());
    }

    @Test
    public void testGetByIdWithInvalidId() {
        ResponseEntity<Participant> response = sut.getById(-1);
        assertEquals(BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void add() {
        Participant participant = new Participant();
        participant.name = "Minh";
        participant.bic = "1234";
        participant.iban = "NL1240";
        participant.email = "minh@gmail.com";

        ResponseEntity<Participant> response = sut.add(participant);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(participant, response.getBody());
    }

    @Test
    void update() {
        Participant participant = new Participant();
        participant.name = "Anna";

        Participant p2 = new Participant();
        p2.name = "Jack";
        p2.iban = "ABC89";
        p2.bic = "455555";

        Participant saved = repo.save(participant);

        var response = sut.update(saved.id, p2, null);
        assertEquals(OK, response.getStatusCode());
        assertEquals("Jack", response.getBody().name);
    }

    @Test
    public void testUpdateWithNullName() {
        Participant p = new Participant();
        p.name = "Jones";

        Participant p2 = new Participant();
        p2.name = null;

        Participant saved = repo.save(p);

        var response = sut.update(saved.id, p2, null);
        assertEquals(BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void delete() {
        Participant existingParticipant = new
                Participant("John Doe", "123456789", "ABCDEF", "john.doe@example.com");

        Participant saved = repo.save(existingParticipant);

        ResponseEntity<Void> response = sut.delete(saved.id);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    void deleteInvalidId() {
        ResponseEntity<Void> response = sut.delete(-1);

        assertEquals(BAD_REQUEST, response.getStatusCode());
    }
}