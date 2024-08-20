package server.api;

import commons.Expense;
import commons.Participant;
import commons.Tag;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.http.HttpStatus.*;

public class ExpenseControllerTest {

    private TestExpenseRepository expenseRepo;
    private TestEventWebSocketHandler webSocketHandler;
    private ExpenseController expenseController;

    @BeforeEach
    public void setup() {
        expenseRepo = new TestExpenseRepository();
        webSocketHandler = new TestEventWebSocketHandler();
        expenseController = new ExpenseController(expenseRepo, webSocketHandler);
    }

    @Test
    public void testAddExpense() {
        Expense expense = new Expense();
        expense.title = "Test";
        expense.amount = 100;
        expense.currency = "Euro";
        expense.type = new Tag("Food", "White");
        expense.date = LocalDate.of(2003, 1, 1);
        expense.creditor = new Participant();
        expense.debtors = new ArrayList<>();

        ResponseEntity<Expense> response = expenseController.add(expense);

        assertEquals(OK, response.getStatusCode());
        assertEquals(expense, response.getBody());
    }

    @Test
    public void testAddExpenseWithNullTitle() {
        Expense expense = new Expense();
        expense.title = null;
        expense.amount = 100;
        expense.currency = "Euro";
        expense.type = new Tag("Food", "White");
        expense.date = LocalDate.of(2003, 1, 1);
        expense.creditor = new Participant();
        expense.debtors = new ArrayList<>();

        ResponseEntity<Expense> response = expenseController.add(expense);

        assertEquals(BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void testDeleteExpense(){

        Expense expense = new Expense();
        expense.title = "Test";
        expense.amount = 100;
        expense.currency = "Euro";
        expense.type = new Tag("Food", "White");
        expense.date = LocalDate.of(2003, 1, 1);
        expense.creditor = new Participant();
        expense.debtors = new ArrayList<>();

        Expense savedExpense = expenseRepo.save(expense);

        ResponseEntity<Expense> response = expenseController.deleteById(savedExpense.id);

        assertEquals(NO_CONTENT, response.getStatusCode());
    }

    @Test
    public void testGetAll() {
        Expense expense1 = new Expense();
        expense1.title = "Test1";
        expenseRepo.save(expense1);

        Expense expense2 = new Expense();
        expense2.title = "Test2";
        expenseRepo.save(expense2);

        var response = expenseController.getAll();

        assertEquals(2, response.size());
        assertTrue(response.contains(expense1));
        assertTrue(response.contains(expense2));
    }

    @Test
    public void testGetById() {
        Expense expense = new Expense();
        expense.title = "Test";
        Expense savedExpense = expenseRepo.save(expense);
        ResponseEntity<Expense> response = expenseController.getById(savedExpense.id);

        assertEquals(OK, response.getStatusCode());
        assertEquals(savedExpense, response.getBody());
    }

    @Test
    public void testGetByIdWithInvalidId() {
        ResponseEntity<Expense> response = expenseController.getById(-1);

        assertEquals(BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void testUpdateExpense() {

        Expense expense = new Expense();
        expense.title = "Existing expense";
        expense.amount = 100;
        expense.currency = "Euro";
        expense.type = new Tag("Food", "White");
        expense.date = LocalDate.of(2003, 1, 1);
        expense.creditor = new Participant();
        expense.debtors = new ArrayList<>();


        Expense updatedExpense = new Expense();
        updatedExpense.amount = 100;
        updatedExpense.currency = "Euro";
        updatedExpense.type = new Tag("Food", "White");
        updatedExpense.creditor = new Participant();
        updatedExpense.date = LocalDate.of(2003, 1, 1);
        updatedExpense.debtors = new ArrayList<>();
        updatedExpense.title = "New expense";

        expenseRepo.save(expense);

        ResponseEntity<Expense> response = expenseController.update(1L, updatedExpense, null);

        assertEquals(OK, response.getStatusCode());
        assertEquals("New expense", response.getBody().title);
    }


}

