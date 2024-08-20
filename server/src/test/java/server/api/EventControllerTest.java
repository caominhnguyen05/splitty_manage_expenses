package server.api;

import commons.Event;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

public class EventControllerTest {

    private TestEventRepository repo;
    private TestTagRepository tagRepository;
    private TestEventWebSocketHandler webSocketHandler;
    private EventController sut;

    @BeforeEach
    public void setup() {
        repo = new TestEventRepository();
        tagRepository = new TestTagRepository();

        webSocketHandler = new TestEventWebSocketHandler();
        sut = new EventController(repo, tagRepository, webSocketHandler);
    }

    @Test
    public void cannotAddNullName() {
        var actual = sut.add(getEvent(null));
        assertEquals(BAD_REQUEST, actual.getStatusCode());
    }

    @Test
    public void databaseIsUsed() {
        sut.add(getEvent("name"));
        repo.calledMethods.contains("save");
    }

    @Test
    public void addTest() {
        var response1 = sut.add(getEvent("name"));
        var response2 = sut.add(getEvent("otherName"));

        assertNotEquals(response1.getBody(), sut.getById(response1.getBody().id));
        assertNotEquals(response2.getBody(), sut.getById(response2.getBody().id));
    }

    @Test
    public void deleteTest() {
        var response = sut.add(getEvent("name"));

        var id = response.getBody().id;

        assertNotNull(id);

        assertNotEquals(response.getBody(), sut.getById(id));
        sut.deleteById(id);
        assertEquals(ResponseEntity.badRequest().build(), sut.getById(id));
    }

    private static Event getEvent(String name) {
        return new Event(name);
    }
}