package com.example.expensetracker.service;

import com.example.expensetracker.entity.Income;
import com.example.expensetracker.entity.User;
import com.example.expensetracker.repository.IncomeRepository;
import com.example.expensetracker.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class IncomeService {
    private final IncomeRepository incomeRepository;
    private final UserRepository userRepository;

    public IncomeService(IncomeRepository incomeRepository, UserRepository userRepository) {
        this.incomeRepository = incomeRepository;
        this.userRepository = userRepository;
    }

    public Income createIncome(Income income, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        income.setUser(user);
        return incomeRepository.save(income);
    }

    public List<Income> findIncomesByUsername(String username) {
        return incomeRepository.findByUser_Username(username);
    }

    public List<Income> findIncomesByDateRange(String username, LocalDate startDate, LocalDate endDate) {
        return incomeRepository.findByUser_UsernameAndDateBetween(username, startDate, endDate);
    }

    public Optional<Income> findIncomeById(Long incomeId, String username) {
        Optional<Income> income = incomeRepository.findById(incomeId);

        if (income.isEmpty()) {
            throw new RuntimeException("Income does not exist");
        }

        if (!income.get().getUser().getUsername().equals(username)) {
            throw new RuntimeException("Unauthorized access");
        }

        return income;
    }

    public void deleteIncome(Long incomeId, String username) {
        Optional<Income> income = findIncomeById(incomeId, username);

        if (income.isEmpty()) {
            throw new RuntimeException("Income does not exist");
        }

        incomeRepository.delete(income.get());
    }
}
