package repository;

import database.DBConnection;
import model.Expense;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class ExpenseRepository {

    public boolean addExpense(Expense expense) {

        String sql = "INSERT INTO expenses(user_id, category_id, amount, date, note) VALUES (?,?,?,?,?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, expense.getUserId());
            stmt.setInt(2, expense.getCategoryId());
            stmt.setDouble(3, expense.getAmount());
            stmt.setDate(4, java.sql.Date.valueOf(expense.getDate()));
            stmt.setString(5, expense.getNote());

            stmt.executeUpdate();
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}