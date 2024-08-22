package com.example.expensetracker.repository;

import com.example.expensetracker.entity.Expense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense, Long> {
    List<Expense> findByUser_Username(String username);

    List<Expense> findByUser_UsernameAndDateBetween(String username, LocalDate startDate, LocalDate endDate);

    List<Expense> findByUser_UsernameAndCategory(String username, String category);

}
