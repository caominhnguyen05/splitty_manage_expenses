package server.websockets;

import com.fasterxml.jackson.databind.ObjectMapper;
import commons.Event;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import server.database.EventRepository;

import java.io.IOException;
import java.util.*;

public class EventWebSocketHandler extends TextWebSocketHandler implements EventWebSocket {
    ObjectMapper mapper;
    Map<String, Set<WebSocketSession>> sessions = new HashMap<>();

    private final EventRepository repo;

    /**
     * Creates a new web socket handler
     * @param repo event repository to be used for persistence
     */
    public EventWebSocketHandler(EventRepository repo) {
        this.repo = repo;
        this.mapper = new ObjectMapper();
        this.mapper.findAndRegisterModules();
    }

    /**
     * Method called after new connection is established
     *
     * @param session new connected session
     * @throws Exception thrown by exception in communication
     */
    @Override
    public void afterConnectionEstablished(WebSocketSession session)
            throws Exception {
        super.afterConnectionEstablished(session);

        String eventID = (String) session.getAttributes().get("id");
        Set<WebSocketSession> associatedSessions =
                sessions.computeIfAbsent(eventID, k -> new HashSet<>());
        associatedSessions.add(session);
    }

    /**
     * Method called when received a new message
     *
     * @param session session that sent the message
     * @param message received message
     * @throws Exception possibly thrown during communication
     */
    @Override
    protected void handleTextMessage(WebSocketSession session,
                                     TextMessage message) throws Exception {
        Event event = mapper.readValue(message.getPayload(), Event.class);

        if (event.id < 0 || !repo.existsById(event.id) || event.name == null) {
            return;
        }

        Event thisEvent = repo.findById(event.id).get();

        thisEvent.name = event.name;
        thisEvent.participants = event.participants;
        thisEvent.expenses = event.expenses;
        thisEvent.payments = event.payments;

        Event saved = repo.save(thisEvent);

        broadcastUpdate(saved);
    }

    /**
     * Method called after a session is closed
     *
     * @param session closed session
     * @param status status of the closure
     * @throws Exception possibly thrown during communication
     */
    @Override
    public void afterConnectionClosed(WebSocketSession session,
                                      CloseStatus status) throws Exception {
        super.afterConnectionClosed(session, status);

        String eventID = (String) session.getAttributes().get("id");
        Set<WebSocketSession> associatedSessions = sessions.get(eventID);

        if (associatedSessions != null) {
            associatedSessions.remove(session);
        }
    }

    /**
     * Broadcast event of the specific id
     *
     * @param eventId id of the event to be broadcast
     * @throws IOException possibly thrown during communication
     */
    public void broadcastUpdate(String eventId) throws IOException {
        var thisEvent = repo.findById(Long.valueOf(eventId));

        if (thisEvent.isPresent()) {
            broadcastUpdate(thisEvent.get());
        }
    }

    private void broadcastUpdate(Event event) throws IOException {
        Set<WebSocketSession> associatedSessions = sessions.get(Long.toString(event.id));
        if (associatedSessions == null) return;

        TextMessage message = new TextMessage(mapper.writeValueAsString(event));

        for (WebSocketSession session : associatedSessions) {
            session.sendMessage(message);
        }
    }
}
