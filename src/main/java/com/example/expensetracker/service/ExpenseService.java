package com.example.expensetracker.service;

import com.example.expensetracker.entity.Expense;
import com.example.expensetracker.entity.User;
import com.example.expensetracker.repository.CategoryRepository;
import com.example.expensetracker.repository.ExpenseRepository;
import com.example.expensetracker.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class ExpenseService {
    private final ExpenseRepository expenseRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

    // Constructor injection
    public ExpenseService(ExpenseRepository expenseRepository, CategoryRepository categoryRepository, UserRepository userRepository) {
        this.expenseRepository = expenseRepository;
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
    }

    public Expense createExpense(Expense expense, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        expense.setUser(user);

        if(!categoryRepository.existsByName(expense.getCategory())) {
            throw new RuntimeException("Category does not exist");
        }

        return expenseRepository.save(expense);
    }


    public List<Expense> findExpenses(String username) {
        return expenseRepository.findByUser_Username(username);
    }

    public Optional<Expense> findExpenseById(Long expenseId, String username) {
        Optional<Expense> theExpense = expenseRepository.findById(expenseId);

        if(theExpense.isEmpty()) {
            throw new RuntimeException("Expense does not exist");
        }

        if(!theExpense.get().getUser().getUsername().equals(username)) {
            throw new RuntimeException("Unauthorized access");
        }

        return theExpense;
    }

    public List<Expense> findExpensesByDateRange(String username, LocalDate startDate, LocalDate endDate) {
        return expenseRepository.findByUser_UsernameAndDateBetween(username, startDate, endDate);
    }


    public List<Expense> findExpensesByCategory(String username, String category) {
        return expenseRepository.findByUser_UsernameAndCategory(username, category);
    }


    public void deleteExpense(Long expenseId, String username) {
        Optional<Expense> theExpense = findExpenseById(expenseId, username);

        if(theExpense.isEmpty()) {
            throw new RuntimeException("Expense does not exist");
        }

        expenseRepository.delete(theExpense.get());
    }

}
