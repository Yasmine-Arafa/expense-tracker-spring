package com.example.expensetracker.controller;

import com.example.expensetracker.entity.Expense;
import com.example.expensetracker.service.ExpenseService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/expenses")
public class ExpenseController {

    private final ExpenseService expenseService;

    public ExpenseController(ExpenseService expenseService) {
        this.expenseService = expenseService;
    }

    @PostMapping
    public ResponseEntity<?> createExpense(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody Expense expense
    ) {
        try {
            Expense createdExpense = expenseService.createExpense(expense, userDetails.getUsername());
            return new ResponseEntity<>(createdExpense, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping
    public ResponseEntity<List<Expense>> getAllExpenses(@AuthenticationPrincipal UserDetails userDetails) {

        List<Expense> expenses = expenseService.findExpenses(userDetails.getUsername());
        if(expenses.isEmpty())
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        return new ResponseEntity<>(expenses, HttpStatus.OK);
    }

    @GetMapping("/{expenseId}")
    public ResponseEntity<Expense> getExpenseById(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long expenseId
    ) {
        Optional<Expense> expense = expenseService.findExpenseById(expenseId, userDetails.getUsername());
        return expense.map(ResponseEntity::ok).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }


    @GetMapping("/category")
    public ResponseEntity<List<Expense>> getExpensesByCategory(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam("category") String category) {
        List<Expense> expenses = expenseService.findExpensesByCategory(userDetails.getUsername(), category);

        if(expenses.isEmpty())
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        return new ResponseEntity<>(expenses, HttpStatus.OK);
    }

    @GetMapping("/date-range")
    public ResponseEntity<List<Expense>> getExpensesByDateRange(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam("startDate") LocalDate startDate,
            @RequestParam("endDate") LocalDate endDate) {

        List<Expense> expenses = expenseService.findExpensesByDateRange(userDetails.getUsername(), startDate, endDate);
        if(expenses.isEmpty())
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        return new ResponseEntity<>(expenses, HttpStatus.OK);
    }

    @DeleteMapping("/{expenseId}")
    public ResponseEntity<Void> deleteExpense(@PathVariable Long expenseId, @AuthenticationPrincipal UserDetails userDetails) {
        expenseService.deleteExpense(expenseId, userDetails.getUsername());
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
