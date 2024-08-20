package server.api;

import commons.Payment;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.database.PaymentRepository;
import server.websockets.EventWebSocket;

import java.util.*;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private final PaymentRepository repo;

    private final EventWebSocket eventWebSocketHandler;

    /**
     * Construct a new PaymentController
     * @param repo repository of all payments
     * @param eventWebSocketHandler handler responsible for web socket communication
     */
    public PaymentController(PaymentRepository repo, EventWebSocket eventWebSocketHandler){
        this.repo = repo;
        this.eventWebSocketHandler = eventWebSocketHandler;
    }

    /**
     * returns all stored payments
     * @return list of all payments
     */

    @GetMapping(path = {"", "/"})
    public List<Payment> getAll(){
        return repo.findAll();
    }

    /**
     * endpoint for getting a specified payment
     * @param id
     * @return the payment with the specified id
     */
    @GetMapping(path={"/{id}"})
    public ResponseEntity<Payment> getById(@PathVariable ("id") long id){
        if(id<0 || !repo.existsById(id)){
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(repo.findById(id).get());
    }

    /**
     * ednpoint for updating the specified payment if the new attributes are valid
     * @param id
     * @param newPayment
     * @return the updated payment
     * @param eventId optional id of the expense to be broadcast
     */
    @PutMapping(path = {"/{id}"})
    public ResponseEntity<Payment> updatePayment(@PathVariable ("id") long id,
                                                 @RequestBody Payment newPayment,
                                                 @RequestHeader("parent-event-id") String eventId){
        if(id<0 || !repo.existsById(id) ||
            newPayment.amount<0 || newPayment.currency == null ||
            newPayment.payer == null || newPayment.receiver == null ||
            newPayment.date == null){
            return ResponseEntity.badRequest().build();
        }

        Payment current = repo.findById(id).get();
        current.amount = newPayment.amount;
        current.currency = newPayment.currency;
        current.date = newPayment.date;
        current.payer = newPayment.payer;
        current.receiver = newPayment.receiver;

        try {
            if (eventId != null)
                eventWebSocketHandler.broadcastUpdate(eventId);
        } catch (Exception e) {
            System.out.println(e);
        }

        return ResponseEntity.ok(repo.save(current));
    }

    /**
     * endpoint for adding a new payment with valid attributes
     * @param newPayment
     * @return the added payment
     */
    @PostMapping(path = {"", "/"})
    public ResponseEntity<Payment> add(@RequestBody Payment newPayment){
        if(newPayment.amount<0 || newPayment.currency == null ||
            newPayment.payer == null || newPayment.receiver == null ||
            newPayment.date == null){
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(repo.save(newPayment));
    }

    /**
     * endpoint for deleting a certain payment if existent
     * @param id
     * @return a response enetity with HTTp status
     */
    @DeleteMapping(path={"/{id}"})
    public ResponseEntity<Void> delete(@PathVariable("id") long id){
        if(id<0 || !repo.existsById(id)){
            return ResponseEntity.badRequest().build();
        }
        repo.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
