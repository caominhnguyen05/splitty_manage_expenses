/*
 * Copyright 2021 Delft University of Technology
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package client.utils;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.WebSocket;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;
import java.util.concurrent.CompletableFuture;

import commons.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import commons.Event;
import commons.Participant;
import commons.Expense;

import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.GenericType;

import javafx.application.Platform;
import org.glassfish.jersey.client.ClientConfig;

import static jakarta.ws.rs.core.MediaType.*;

public class ServerUtils {

    private final String serverURL;
    private final String serverWSURL;

    private final CountDownLatch latch = new CountDownLatch(1);

    private WebSocket eventWebSocket;

    /**
     * Creates a new ServerUtils object with url from the config file.
     */
    public ServerUtils() {
        var bundle = ResourceBundle.getBundle("settings.config");

        var serverString = bundle.getString("server-url");

        serverURL = "http://" + serverString + "/";
        serverWSURL = "ws://" + serverString + "/";
    }

    /**
     * Adds a new event to the server.
     *
     * @param event the event to add
     * @return the added event returned from the server.
     */
    public Event addEvent(Event event) {
        return ClientBuilder.newClient(new ClientConfig())
                .target(serverURL).path("api/events")
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .post(Entity.entity(event, APPLICATION_JSON), Event.class);
    }

    /**
     * Retrieves event with given id.
     *
     * @param id id of the returned event.
     * @return event with given id.
     */
    public Event getEvent(long id) {
        try {
            return ClientBuilder.newClient(new ClientConfig())
                    .target(serverURL).path("api/events/" + id)
                    .request(APPLICATION_JSON)
                    .accept(APPLICATION_JSON)
                    .get(Event.class);
        }
        catch (Exception e) {
            return null;
        }
    }

    /**
     * Retrieves all events in the database
     *
     * @return List of events
     */
    public List<Event> getAllEvents() {
        return ClientBuilder.newClient(new ClientConfig())
                .target(serverURL).path("api/events")
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .get(new GenericType<List<Event>>() {});
    }

    /**
     * Updates event by using put method
     *
     * @param event input event
     * @return updated event retrieved from server
     */
    public Event updateEvent(Event event) {
        return ClientBuilder.newClient(new ClientConfig())
                .target(serverURL).path("api/events/" + event.id)
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .put(Entity.entity(event, APPLICATION_JSON), Event.class);
    }

    /**
     * API endpoint to update a participant.
     * @param participant the participant to be updated.
     * @return the participant after updating
     * @param eventId optional id of the expense to be broadcast
     */
    public Participant updateParticipant(Participant participant, long eventId) {
        return ClientBuilder.newClient(new ClientConfig())
                .target(serverURL).path("api/participants/" + participant.id)
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .header("parent-event-id", eventId)
                .put(Entity.entity(participant, APPLICATION_JSON), Participant.class);
    }

    /**
     * adds expense to server
     *
     * @param expense expense to be added
     * @return the added expense which is returned from the server
     */
    public Expense addExpense(Expense expense) {
        return ClientBuilder.newClient(new ClientConfig()) //
                .target(serverURL).path("api/expenses") //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .post(Entity.entity(expense, APPLICATION_JSON), Expense.class);
    }

    /**
     * Retrieves expense with an input id
     *
     * @param id id of expense
     * @return expense object
     */
    public Expense getExpense(long id) {
        return ClientBuilder.newClient(new ClientConfig())
                .target(serverURL).path("api/expenses/" + id)
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .get(Expense.class);
    }

    /**
     * updates expense using put method
     *
     * @param expense input expense
     * @return updated expense retrieved from server
     * @param eventId optional id of the expense to be broadcast
     */
    public Expense updateExpense(Expense expense, long eventId) {
        return ClientBuilder.newClient(new ClientConfig()) //
                .target(serverURL).path("api/expenses/" + expense.id) //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .header("parent-event-id", eventId)
                .put(Entity.entity(expense, APPLICATION_JSON), Expense.class);
    }
    /**
     *
     * @param expense the expense to remove
     */
    public void removeExpense(Expense expense){
        ClientBuilder.newClient(new ClientConfig())
            .target(serverURL).path("api/expenses/" + expense.id)
            .request(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .delete();
    }

    /**
     * Removes an event by id
     * @param id id of the event to be removed
     */
    public void removeEvent(Long id) {
        ClientBuilder.newClient(new ClientConfig())
                .target(serverURL).path("api/events/" + id)
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .delete();
    }

    /**
     * Retrieves participant with given id.
     *
     * @param id id of the returned participant
     * @return the participant with given id
     */
    public Participant getParticipant(long id) {
        return ClientBuilder.newClient(new ClientConfig())
                .target(serverURL).path("api/participants/" + id)
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .get(Participant.class);
    }

    /**
     * Adds a new participant to the server.
     *
     * @param participant the event to add
     * @return the added participant returned from the server.
     */
    public Participant addParticipant(Participant participant) {
        return ClientBuilder.newClient(new ClientConfig())
                .target(serverURL).path("api/participants")
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .post(Entity.entity(participant, APPLICATION_JSON), Participant.class);
    }

    /**
     * Removes a participant from the server.
     *
     * @param participant id of the participant to be removed.
     */
    public void removeParticipant(Participant participant) {
        ClientBuilder.newClient(new ClientConfig())
                .target(serverURL).path("api/participants/" + participant.id)
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .delete();
    }

    /**
     * Send an api request to send an email
     * @param toEmail email address of the recipient
     * @param event event to which the recipient is invited
     */
    public void sendMail(String toEmail, Event event) {
        ClientBuilder.newClient(new ClientConfig())
                .target(serverURL).path("api/mail/" + toEmail)
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .post(Entity.entity(event, APPLICATION_JSON), Event.class);
    }

    /**
     * Send an api request to send a reminder email
     * @param toEmail
     * @param participant
     */
    public void sendReminder(String toEmail, Participant participant){
        ClientBuilder.newClient(new ClientConfig())
            .target(serverURL).path("api/mail/reminder/" + toEmail)
            .request(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .post(Entity.entity(participant, APPLICATION_JSON), Participant.class);
    }

    /**
     * sends an api request to send a confirmation email
     * after adding new participant
     * @param email
     * @param event
     */
    public void sentConfirmation(String email, Event event){
        ClientBuilder.newClient(new ClientConfig())
            .target(serverURL).path("api/mail/confirmation/" + email)
            .request(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .post(Entity.entity(event, APPLICATION_JSON), Event.class);
    }

    /**
     * sends an api request to send a confirmation email
     * for credentials update
     * @param email
     * @param participant
     */

    public void sentConfirmationUpdate(String email, Participant participant){
        ClientBuilder.newClient(new ClientConfig())
            .target(serverURL).path("api/mail/confirmation/update/" + email)
            .request(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .post(Entity.entity(participant, APPLICATION_JSON), Participant.class);
    }

    /**
     * Checks if the provided server password is authenticated
     * @param password password to check
     * @return returns a boolean, based on the outcome of the api request
     */
    public boolean authenticate(String password) {
        try {
            ClientBuilder.newClient(new ClientConfig())
                    .target(serverURL).path("api/admin/authenticate")
                    .request(TEXT_PLAIN)
                    .accept(TEXT_PLAIN)
                    .post(Entity.entity(password, TEXT_PLAIN), String.class);
            return true;
        }
        catch (Exception e) {
            return false;
        }
    }

    /**
     * Adds a new payment to the server.
     *
     * @param payment the Payment to add
     * @return the added Payment returned from the server.
     */
    public Payment addPayment(Payment payment) {
        return ClientBuilder.newClient(new ClientConfig())
                .target(serverURL).path("api/payments")
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .post(Entity.entity(payment, APPLICATION_JSON), Payment.class);
    }

    /**
     * Gets a Tag with a given ID
     * @param id ID of the Tag to get
     * @return returns the retrieved Tag OR null if the Tag was not found
     */
    public Tag getTag(long id) {
        try {
            return ClientBuilder.newClient(new ClientConfig())
                    .target(serverURL).path("api/tags/" + id)
                    .request(APPLICATION_JSON)
                    .accept(APPLICATION_JSON)
                    .get(Tag.class);
        }
        catch (BadRequestException e){
            return null;
        }

    }

    /**
     * Deleted a Tag from the database given its ID
     * @param id id of the Tag to be deleted
     */
    public void deleteTag(long id) {
        ClientBuilder.newClient(new ClientConfig())
                .target(serverURL).path("api/tags/" + id)
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .delete();
    }

    /**
     * Updates a Tag object
     * @param tag tag object with the updated values
     * @return returns the updated Tag (or a possible error)
     */
    public Tag updateTag(Tag tag) {
        return ClientBuilder.newClient(new ClientConfig())
                .target(serverURL).path("api/tags/" + tag.getId())
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .put(Entity.entity(tag, APPLICATION_JSON), Tag.class);
    }

    /**
     * Adds a Tag object to the database
     * @param tag Tag to be added to the database
     * @return returns the Tag that was saved (or a possible error)
     */
    public Tag addTag(Tag tag) {
        return ClientBuilder.newClient(new ClientConfig())
                .target(serverURL).path("api/tags")
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .post(Entity.entity(tag, APPLICATION_JSON), Tag.class);
    }

    /**
     * Gets a list of all the Tags in the database
     * @return List of Tags
     */
    public List<Tag> getAllTags() {
        return ClientBuilder.newClient(new ClientConfig())
                .target(serverURL).path("api/tags")
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .get(new GenericType<List<Tag>>() {
                });
    }


    private ExecutorService executorService = Executors.newCachedThreadPool();
    private final Set<Long> listenedEventsIDs = new HashSet<>();

    /**
     * Adds a new http long polling listener
     *
     * @param eventId id of the event to listen to
     * @param consumer completion handler of the listener
     */
    public void registerForUpdates(long eventId, Consumer<Event> consumer) {
        if (executorService.isShutdown()) executorService = Executors.newCachedThreadPool();
        if (listenedEventsIDs.contains(eventId)) return;

        listenedEventsIDs.add(eventId);

        executorService.submit(() -> {
            while (!Thread.interrupted()) {
                try {
                    CompletableFuture.supplyAsync(() ->
                                    ClientBuilder.newClient()
                                            .target(serverURL)
                                            .path("api/events/" + eventId + "/updates")
                                            .request(APPLICATION_JSON)
                                            .accept(APPLICATION_JSON)
                                            .get(Response.class), executorService)
                            .thenAcceptAsync(response -> {
                                if (response.getStatus() == 200) {
                                    Event event = response.readEntity(Event.class);

                                    Platform.runLater(new Runnable() {
                                        @Override
                                        public void run() {
                                            consumer.accept(event);
                                        }
                                    });
                                }
                                response.close();
                            }, executorService)
                            .get();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Closes all long polling connections
     */
    public void closeHttpLongPolling() {
        executorService.shutdownNow();
        listenedEventsIDs.clear();
    }

    /**
     * Removes a payment from the server.
     *
     * @param payment id of the payment to be removed.
     */
    public void removePayment(Payment payment) {
        ClientBuilder.newClient(new ClientConfig())
                .target(serverURL).path("api/payments/" + payment.id)
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .delete();
    }

     /**
     * Establishes a new websocket communication.
     *
     * @param eventId
     * @param onReceive
     */
    public void openWebSocketEventListener(String eventId, Consumer<Event> onReceive) {
        eventWebSocket = HttpClient
                .newHttpClient()
                .newWebSocketBuilder()
                .buildAsync(URI.create(serverWSURL + "api/ws-events/" + eventId),
                        new WebSocketListener<Event>(latch, onReceive, Event.class))
                .join();
    }

    /**
     * Closes the web socket connection
     */
    public void closeWebSocketListener() {
        if (eventWebSocket == null) return;

        eventWebSocket.sendClose(WebSocket.NORMAL_CLOSURE, "ok");
    }

    /**
     * Updates event through the web socket connection
     *
     * @param event event to be updated
     */
    public void updateEventWS(Event event) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.findAndRegisterModules();

            String encoded = mapper.writeValueAsString(event);

            eventWebSocket.sendText(encoded, true);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    private static class WebSocketListener<T>
            implements WebSocket.Listener {
        private final CountDownLatch latch;

        private Consumer<T> onReceive;

        private Class<T> objectClass;

        ObjectMapper mapper = new ObjectMapper();

        public WebSocketListener(CountDownLatch latch,
                                 Consumer<T> onReceive,
                                 Class<T> objectClass) {
            this.latch = latch;
            this.onReceive = onReceive;
            this.objectClass = objectClass;

            this.mapper.findAndRegisterModules();
        }

        @Override
        public CompletionStage<?> onText(WebSocket webSocket, CharSequence data, boolean last) {
            try {
                T event = mapper.readValue(data.toString(), objectClass);

                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        onReceive.accept(event);
                    }
                });
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }

            latch.countDown();
            return WebSocket.Listener.super.onText(webSocket, data, last);
        }
    }

    /**
     * calls the api for conversion rate
     * @param from to be converted
     * @param to to be converted to
     * @param date date of conversion
     * @return rate
     */
    public Double getConversionRate(String from, String to, LocalDate date) {
        return ClientBuilder.newClient(new ClientConfig())
                .target(serverURL).path("api/currency/" + from + "/" + to + "/" + date)
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .get(Double.class);
    }
}
