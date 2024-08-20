package server.websockets;

import java.io.IOException;

public interface EventWebSocket {
    /**
     * Sends an update to all the clients listening to that event id
     *
     * @param eventId id of the event to be broadcast
     * @throws IOException exception that could be thrown during communication
     */
    void broadcastUpdate(String eventId) throws IOException;
}
