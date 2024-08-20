package server.api;

import commons.Event;
import commons.Tag;

import org.antlr.v4.runtime.misc.Pair;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;
import server.database.EventRepository;
import server.database.TagRepository;
import server.websockets.EventWebSocket;

import java.util.*;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

@RestController
@RequestMapping("/api/events")
public class EventController {
    private final EventRepository repo;
    private final TagRepository tagRepository;

    private final EventWebSocket eventWebSocketHandler;

    /**
     * Initializes a new event controller endpoint.
     *
     * @param repo event repository that connects to the database.
     * @param tagRepository tag repository that connects to the database.
     * @param eventWebSocketHandler handler responsible for web socket communication
     */
    public EventController(EventRepository repo, TagRepository tagRepository,
                           EventWebSocket eventWebSocketHandler) {
        this.repo = repo;
        this.tagRepository = tagRepository;
        this.eventWebSocketHandler = eventWebSocketHandler;
    }

    /**
     * Returns event with given id.
     *
     * @param id id of the event
     * @return response with event with given id.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Event> getById(@PathVariable("id") long id) {
        if (id < 0 || !repo.existsById(id)) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(repo.findById(id).get());
    }

    /**
     * Deletes event with given id.
     *
     * @param id id of the event
     * @return http response.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Event> deleteById(@PathVariable("id") long id) {
        if (id < 0 || !repo.existsById(id)) {
            return ResponseEntity.badRequest().build();
        }

        repo.deleteById(id);

        return ResponseEntity.noContent().build();
    }

    /**
     * Updates event with given id.
     *
     * @param id    id of the event
     * @param event updated event
     * @return updated event with given id.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Event> updateById(@PathVariable("id") long id, @RequestBody Event event) {
        if (id < 0 || !repo.existsById(id) || event.name == null) {
            return ResponseEntity.badRequest().build();
        }

        Event thisEvent = repo.findById(id).get();

        thisEvent.name = event.name;
        thisEvent.participants = event.participants;
        thisEvent.expenses = event.expenses;
        thisEvent.payments = event.payments;
        thisEvent.tags = event.tags;

        Event saved = repo.save(thisEvent);

        try {
            eventWebSocketHandler.broadcastUpdate(String.valueOf(saved.id));
        } catch (Exception e) {
            System.out.println(e);
        }

        var associatedListeners = listeners.get(event.id);
        associatedListeners.forEach(pair -> {
            pair.b.accept(event);
        });

        return ResponseEntity.ok(saved);
    }

    /**
     * Creates new event.
     *
     * @param event event to be added
     * @return given event.
     */
    @PostMapping(path = {"", "/"})
    public ResponseEntity<Event> add(@RequestBody Event event) {
        if (event.name == null || event.name.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        // Create and persist Tag entities
        Tag entranceFees = new Tag("Entrance Fees", "#47c5ff");
        Tag food = new Tag("Food", "#19ff66");
        Tag travel = new Tag("Travel", "#ff7063");

        // Save tags to the database
        tagRepository.saveAll(Arrays.asList(entranceFees, food, travel));

        // Associate the persisted tags with the event
        event.setTags(Arrays.asList(entranceFees, food, travel));

        Event saved = repo.save(event);
        return ResponseEntity.ok(saved);
    }

    /**
     * Returns all events in the database
     *
     * @return response with events
     */
    @GetMapping(path = {"", "/"})
    public ResponseEntity<List<Event>> getAll() {
        return ResponseEntity.ok(repo.findAll());
    }

    private Map<Long, Set<Pair<Object, Consumer<Event>>>> listeners = new HashMap<>();

    /**
     * Http long polling end-point
     *
     * @param id identifier of the event
     * @return endpoint initial response
     */
    @GetMapping("/{id}/updates")
    public DeferredResult<ResponseEntity<Event>> getUpdate(@PathVariable("id") long id) {
        var noContent = ResponseEntity.noContent();
        var res = new DeferredResult<ResponseEntity<Event>>(5000L, noContent);

        var key = new Object();

        var associatedListeners = listeners.computeIfAbsent(id, k -> new HashSet<>());
        associatedListeners.add(new Pair<>(key, x -> {
            res.setResult(ResponseEntity.ok(x));
        }));
        res.onCompletion(() -> {
            associatedListeners.removeIf(p -> p.a == key);
        });

        return res;
    }
}
