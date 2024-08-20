package server.api;

import server.websockets.EventWebSocket;

import java.io.IOException;

public class TestEventWebSocketHandler implements EventWebSocket {
    @Override
    public void broadcastUpdate(String EventId) throws IOException {

    }
}
