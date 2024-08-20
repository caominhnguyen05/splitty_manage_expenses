package commons;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class ExpenseTest {
    private Expense e1;
    private Expense e2;
    private Expense e3;

    @BeforeEach
    void setup(){
        e1 = new Expense("party", 20, "EUR", new Tag("Food", "#fff"), LocalDate.of(2024, 1, 1));
        e2 = new Expense("party", 20, "EUR", new Tag("Food", "#fff"), LocalDate.of(2024, 1, 1));
        e3 = new Expense("restaurant", 20, "EUR", new Tag("Food", "#fff"), LocalDate.of(2024, 1, 1));
    }

    @Test
    void checkConstructor(){
        assertEquals("party", e1.title);
        assertEquals(20, e1.amount);
        assertEquals("EUR", e1.currency);
        assertEquals("Food", e1.type.getName());
        assertEquals(LocalDate.of(2024, 1, 1), e1.date);
        assertNotNull(e1.debtors);
    }
    @Test
    void testEquals() {
        assertEquals(e1, e2);
        assertNotEquals(e1,e3);
    }

    @Test
    void testHashCode() {
        assertEquals(e1.hashCode(), e2.hashCode());
        assertNotEquals(e1.hashCode(), e3. hashCode());
    }

    @Test
    void hasToString() {
        String s = e1.toString();
        assertTrue(s.contains(Expense.class.getSimpleName()));
        assertTrue(s.contains("\n"));
        assertTrue(s.contains("title"));
    }

    @Test
    void emptyExpense() {
        Expense e = new Expense();
        Expense e2 = new Expense();
        assertEquals(e, e2);
    }
}