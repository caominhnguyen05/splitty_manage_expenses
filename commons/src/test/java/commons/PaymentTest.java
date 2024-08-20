package commons;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class PaymentTest {

    private Payment p1;
    private Payment p2;
    private Payment p3;
    @BeforeEach
    void setup(){
        p1 = new Payment("Frank", "Hank", 10, "EUR");
        p2 = new Payment("Frank", "Hank", 10, "EUR");
        p3 = new Payment("Frank", "Hank", 20, "EUR");
    }
    @Test
    void checkConstructor(){
        assertEquals("Frank", p1.payer);
        assertEquals("Hank", p2.receiver);
        assertEquals(10, p1.amount);
        assertEquals("EUR", p1.currency);
    }

    @Test
    void testEquals() {
        assertEquals(p1,p2);
        assertNotEquals(p1,p3);
    }

    @Test
    void testHashCode() {
        assertEquals(p1.hashCode(),p2.hashCode());
        assertNotEquals(p1.hashCode(),p3.hashCode());
    }

    @Test
    void testToString() {
        Date dateNow = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        String expectedDate = formatter.format(dateNow);

        String expected = "Frank paid Hank 10.00 EUR on " + expectedDate;
        assertEquals(expected, p1.toString());
    }

    @Test
    void emptyPayment() {
        Payment p = new Payment();
        Payment p2 = new Payment();
        assertEquals(p, p2);
    }
}