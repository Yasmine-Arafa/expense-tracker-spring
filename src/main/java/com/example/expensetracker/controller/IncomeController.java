package com.example.expensetracker.controller;

import com.example.expensetracker.entity.Income;
import com.example.expensetracker.service.IncomeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/incomes")
public class IncomeController {
    private final IncomeService incomeService;

    public IncomeController(IncomeService incomeService) {
        this.incomeService = incomeService;
    }

    @PostMapping
    public ResponseEntity<?> createIncome(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody Income income) {
        try {
            Income createdIncome = incomeService.createIncome(income, userDetails.getUsername());
            return new ResponseEntity<>(createdIncome, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping
    public ResponseEntity<List<Income>> getIncomesByUser(@AuthenticationPrincipal UserDetails userDetails) {
        List<Income> incomes = incomeService.findIncomesByUsername(userDetails.getUsername());
        return new ResponseEntity<>(incomes, HttpStatus.OK);
    }

    @GetMapping("/date-range")
    public ResponseEntity<List<Income>> getIncomesByDateRange(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam("startDate") LocalDate startDate,
            @RequestParam("endDate") LocalDate endDate) {
        List<Income> incomes = incomeService.findIncomesByDateRange(userDetails.getUsername(), startDate, endDate);
        return new ResponseEntity<>(incomes, HttpStatus.OK);
    }

    @GetMapping("/{incomeId}")
    public ResponseEntity<?> getIncomeById(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long incomeId) {
        try {
            Optional<Income> income = incomeService.findIncomeById(incomeId, userDetails.getUsername());
            return income.map(ResponseEntity::ok)
                    .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{incomeId}")
    public ResponseEntity<Void> deleteIncome(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long incomeId) {
        try {
            incomeService.deleteIncome(incomeId, userDetails.getUsername());
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
