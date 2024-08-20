package server.websockets;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.HandshakeInterceptor;
import server.database.EventRepository;

import java.util.Map;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {
    private final EventWebSocketHandler eventWebSocketHandler;

    /**
     * Instantiate new web socket configuration
     *
     * @param repo event repository
     */
    public WebSocketConfig(EventRepository repo) {
        this.eventWebSocketHandler = new EventWebSocketHandler(repo);
    }

    /**
     * Get app web socket handler
     *
     * @return event web socket handler
     */
    @Bean
    public EventWebSocketHandler getEventUpdateHandler() {
        return eventWebSocketHandler;
    }

    /**
     * Registers web socket handlers to the server
     *
     * @param registry handler registry
     */
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry
                .addHandler(eventWebSocketHandler, "/api/ws-events/{id}")
                .addInterceptors(getInterceptor());
    }

    private HandshakeInterceptor getInterceptor() {
        return new HandshakeInterceptor() {
            @Override
            public boolean beforeHandshake(ServerHttpRequest request,
                                           ServerHttpResponse response,
                                           WebSocketHandler wsHandler,
                                           Map<String, Object> attributes) throws Exception {
                String path = request.getURI().getPath();
                String[] comp = path.split("/");
                attributes.put("id", comp[comp.length - 1]);
                return true;
            }

            @Override
            public void afterHandshake(ServerHttpRequest request,
                                       ServerHttpResponse response,
                                       WebSocketHandler wsHandler,
                                       Exception exception) {}
        };
    }
}
