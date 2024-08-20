package server.api;

import commons.Participant;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.database.ParticipantRepository;
import server.websockets.EventWebSocket;

import java.util.List;

@RestController
@RequestMapping("api/participants")
public class ParticipantController {
    private final ParticipantRepository repo;

    private final EventWebSocket eventWebSocketHandler;

    /**
     * Constructs a new participant controller
     *
     * @param repo the participant repository
     * @param eventWebSocketHandler handler responsible for web socket communication
     */
    public ParticipantController(ParticipantRepository repo, EventWebSocket eventWebSocketHandler) {
        this.repo = repo;
        this.eventWebSocketHandler = eventWebSocketHandler;
    }

    /**
     * Endpoint for getting all participants for an event
     *
     * @return a list of all participants
     */
    @GetMapping(path = {"", "/"})
    public List<Participant> getAll() {
        return repo.findAll();
    }

    /**
     * API endpoint to get a participant by ID
     *
     * @param id the ID of the participant to get
     * @return a response entity containing the participant, with HTTP status
     */
    @GetMapping(path = {"/{id}"})
    public ResponseEntity<Participant> getById(@PathVariable("id") long id) {
        if (id < 0 || !repo.existsById(id)) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(repo.findById(id).get());
    }

    /**
     * Endpoint to create a new participant
     *
     * @param participant the new participant object to add
     * @return a response entity with HTTP status
     */
    @PostMapping(path = {"", "/"})
    public ResponseEntity<Participant> add(@RequestBody Participant participant) {
        if (participant.name == null || participant.name.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        Participant saved = repo.save(participant);
        return ResponseEntity.ok(saved);
    }

    /**
     * Endpoint for updating a participant.
     *
     * @param id                 the ID of the participant to update details about.
     * @param updatedParticipant the updated participant object
     * @return a response entity with HTTP status
     * @param eventId optional id of the expense to be broadcast
     */
    @PutMapping(path = {"/{id}"})
    public ResponseEntity<Participant> update(@PathVariable("id") long id,
                                              @RequestBody Participant updatedParticipant,
                                              @RequestHeader("parent-event-id") String eventId) {
        if (id < 0 || !repo.existsById(id) || updatedParticipant.name == null) {
            return ResponseEntity.badRequest().build();
        }

        Participant thisParticipant = repo.findById(id).get();

        thisParticipant.name = updatedParticipant.name;
        thisParticipant.email = updatedParticipant.email;
        if (updatedParticipant.iban != null) {
            thisParticipant.iban = updatedParticipant.iban;
        }
        if (updatedParticipant.bic != null) {
            thisParticipant.bic = updatedParticipant.bic;
        }

        Participant saved = repo.save(thisParticipant);

        try {
            if (eventId != null)
                eventWebSocketHandler.broadcastUpdate(eventId);
        } catch (Exception e) {
            System.out.println(e);
        }

        return ResponseEntity.ok(saved);
    }

    /**
     * Endpoint for deleting a participant.
     *
     * @param id the ID of the participant to delete
     * @return a response entity with HTTP status
     */
    @DeleteMapping(path = {"/{id}"})
    public ResponseEntity<Void> delete(@PathVariable("id") long id) {
        if (id < 0 || !repo.existsById(id)) {
            return ResponseEntity.badRequest().build();
        }

        repo.deleteById(id);
        return ResponseEntity.noContent().build();
    }

}
