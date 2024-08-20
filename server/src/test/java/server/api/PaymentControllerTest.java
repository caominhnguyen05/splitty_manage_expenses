package server.api;

import commons.Participant;
import commons.Payment;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.HttpStatus.*;

import java.util.Date;

public class
PaymentControllerTest {
    private TestPaymentRepository repo;
    private TestEventWebSocketHandler webSocketHandler;
    private PaymentController ctrl;

    @BeforeEach
    public void setup(){
        repo = new TestPaymentRepository();
        webSocketHandler = new TestEventWebSocketHandler();
        ctrl = new PaymentController(repo, webSocketHandler);
    }

    @Test
    public void addValidPayment(){
        Payment p = new Payment();
        p.amount=100;
        p.currency="EUR";
        p.date = new Date();
        p.payer  = "John";
        p.receiver = "Eddy";

        assertEquals(OK, ctrl.add(p).getStatusCode());
        assertEquals(p, ctrl.add(p).getBody());
    }
    @Test
    public void addInvalidPayment(){
        Payment p = new Payment();
        p.amount=100;
        p.currency="EUR";
        p.date = new Date();
        p.payer = "John";
        //no receiver ~ null attribute
        assertEquals(BAD_REQUEST, ctrl.add(p).getStatusCode());
    }
    @Test
    public void getAllTest(){
        Payment p1 = new Payment();
        p1.amount=100;
        p1.currency="EUR";
        p1.date = new Date();
        p1.payer  = "John";
        p1.receiver = "Eddy";

        Payment p2 = new Payment();
        p2.amount = 200;
        p2.currency = "USD";
        p2.date = new Date();
        p2.payer  = "Eddy";
        p2.receiver = "John";

        repo.save(p1);
        repo.save(p2);

        assertEquals(2,ctrl.getAll().size());
        assertTrue(ctrl.getAll().contains(p1));
        assertTrue(ctrl.getAll().contains(p2));

    }

    @Test
    public void getByIDTest(){
        Payment p1 = new Payment();
        p1.amount=100;
        p1.currency="EUR";
        p1.date = new Date();
        p1.payer  = "John";
        p1.receiver = "Eddy";

        Payment pay1 = repo.save(p1);

        assertEquals(OK, ctrl.getById(pay1.id).getStatusCode());
        assertEquals(p1, ctrl.getById(pay1.id).getBody());

    }

    @Test
    public void updateTest(){
        Payment p1 = new Payment();
        p1.amount = 100;
        p1.currency = "EUR";
        p1.date = new Date();
        p1.payer = "John";
        p1.receiver = "Eddy";

        Payment p2 = new Payment();
        p2.amount = 200;
        p2.currency=  "USD";
        p2.date = new Date();
        p2.payer  = "John";
        p2.receiver = "Eddy";

        Payment payment = repo.save(p1);

        var response = ctrl.updatePayment(payment.id,p2, null);

        assertEquals(OK, response.getStatusCode());
        assertEquals(200, response.getBody().amount);

    }

    @Test
    public void deleteTest(){
        Payment p1 = new Payment();
        p1.amount=100;
        p1.currency="EUR";
        p1.date = new Date();
        p1.payer  = "John";
        p1.receiver = "Eddy";

        Payment payment = repo.save(p1);
        var response = ctrl.delete(payment.id);

        assertEquals(NO_CONTENT, response.getStatusCode());
        assertEquals(BAD_REQUEST, ctrl.getById(payment.id).getStatusCode());

    }
}
