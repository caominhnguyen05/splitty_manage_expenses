package client.data_structures;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DebtTest {

    private Debt d1;
    private Debt d2;
    private Debt d3;

    @BeforeEach
    void setup() {
        d1 = new Debt("Hank", "Frank", 30);
        d2 = new Debt("Hank", "Frank", 30);
        d3 = new Debt("Frank", "Hank", 30);
    }

    @Test
    void getCreditorTest() {
        assertEquals("Hank", d1.getCreditor());
    }

    @Test
    void getDebtorTest() {
        assertEquals("Frank", d1.getDebtor());
    }

    @Test
    void getAmountTest() {
        assertEquals(30, d1.getAmount());
    }

    @Test
    void addAmountTest() {
        d1.addAmount(10);
        assertEquals(40, d1.getAmount());
        d1.addAmount(-10);
    }

    @Test
    void toStringTest() {
        String expected = "Frank owes 30.0 to Hank";
        assertEquals(expected, d1.toString());
    }

    @Test
    void equalsTest() {
        assertEquals(d1, d2);
    }

    @Test
    void notEqualsTest() {
        assertNotEquals(d1, d3);
    }

    @Test
    void hashTest() {
        assertEquals(d1.hashCode(), d2.hashCode());
    }
}