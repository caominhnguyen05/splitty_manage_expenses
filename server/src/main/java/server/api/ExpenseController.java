package server.api;

import commons.Expense;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.database.ExpenseRepository;
import server.websockets.EventWebSocket;

import java.util.List;
@RestController
@RequestMapping("/api/expenses")
public class ExpenseController {


    private final ExpenseRepository expenseRepo;

    private final EventWebSocket eventWebSocketHandler;

    /**
     * initiates expense repository
     * @param expenseRepo repository with expenses
     * @param eventWebSocketHandler handler responsible for web socket communication
     */
    public ExpenseController(ExpenseRepository expenseRepo, EventWebSocket eventWebSocketHandler) {
        this.expenseRepo = expenseRepo;
        this.eventWebSocketHandler = eventWebSocketHandler;
    }

    /**
     * returns all stored expenses
     * @return list of all expenses
     */
    @GetMapping(path = {"", "/"})
    public List<Expense> getAll() {
        return expenseRepo.findAll();
    }

    /**
     * gets expense searched by id
     * @param id id of wanted expense
     * @return expense
     */
    @GetMapping("/{id}")
    public ResponseEntity<Expense> getById(@PathVariable("id") long id) {
        if (id < 0 || !expenseRepo.existsById(id)) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(expenseRepo.findById(id).get());
    }

    /**
     * adds expense to the list of all expense
     * @param expense expense to be added
     * @return response OK if everything went well, otherwise BAD_REQUEST
     */
    @PostMapping(path = {"", "/"})
    public ResponseEntity<Expense> add(@RequestBody Expense expense) {

        if (isNullOrEmpty(expense.title) || expense.amount <= 0 ||
            expense.currency == null || expense.type == null ||
            expense.creditor == null || expense.debtors == null ||
            expense.date == null) {
            return ResponseEntity.badRequest().build();
        }

        Expense saved = expenseRepo.save(expense);
        return ResponseEntity.ok(saved);
    }

    /**
     * updates an existing expense
     * @param id id of the expense to be updated
     * @param updatedExpense the updated expense
     * @return response OK if everything went well, otherwise BAD_REQUEST
     * @param eventId optional id of the expense to be broadcast
     */
    @PutMapping("/{id}")
    public ResponseEntity<Expense> update(@PathVariable("id") long id,
                                          @RequestBody Expense updatedExpense,
                                          @RequestHeader("parent-event-id") String eventId) {
        if (id < 0 || !expenseRepo.existsById(id) || isNullOrEmpty(updatedExpense.title) ||
                updatedExpense.amount <= 0 ||
                updatedExpense.currency == null || updatedExpense.type == null ||
                updatedExpense.creditor == null || updatedExpense.debtors == null ||
            updatedExpense.date == null) {
            return ResponseEntity.badRequest().build();
        }

        Expense existingExpense = expenseRepo.findById(id).get();
        existingExpense.title = updatedExpense.title;
        existingExpense.amount = updatedExpense.amount;
        existingExpense.currency = updatedExpense.currency;
        existingExpense.type = updatedExpense.type;
        existingExpense.date = updatedExpense.date;
        existingExpense.creditor = updatedExpense.creditor;
        existingExpense.debtors.clear();
        existingExpense.debtors.addAll(updatedExpense.debtors);

        Expense saved = expenseRepo.save(existingExpense);

        try {
            if (eventId != null)
                eventWebSocketHandler.broadcastUpdate(eventId);
        } catch (Exception e) {
            System.out.println(e);
        }

        return ResponseEntity.ok(saved);
    }


    /**
     * deletes expense by id
     * @param id id of expense to be deleted
     * @return NO_CONTENT if expense was found and deleted successfully, otherwise BAD_REQUEST
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Expense> deleteById(@PathVariable("id") long id) {
        if (id < 0 || !expenseRepo.existsById(id)) {
            return ResponseEntity.badRequest().build();
        }

        expenseRepo.deleteById(id);

        return ResponseEntity.noContent().build();
    }

    /**
     * checks whether string is null or empty (or not)
     * @param s string to be checked
     * @return null/empty or not
     */
    private static boolean isNullOrEmpty(String s) {
        return s == null || s.isEmpty();
    }

}
