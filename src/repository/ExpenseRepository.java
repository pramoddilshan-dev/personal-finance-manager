package repository;

import database.DBConnection;
import model.Expense;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

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

    public List<Expense> getExpensesByUser(int userId) {

        List<Expense> expenses = new ArrayList<>();

        String sql = "SELECT * FROM expenses WHERE user_id = ?";

        try (Connection conn = DBConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {

                Expense expense = new Expense();

                expense.setId(rs.getInt("id"));
                expense.setUserId(rs.getInt("user_id"));
                expense.setCategoryId(rs.getInt("category_id"));
                expense.setAmount(rs.getDouble("amount"));
                expense.setDate(rs.getDate("date").toLocalDate());
                expense.setNote(rs.getString("note"));

                expenses.add(expense);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return expenses;
    }

    public void printExpenseTransactions(int userId) {

        String sql =
                "SELECT e.date, c.name AS category, e.amount, e.note " +
                "FROM expenses e " +
                "JOIN categories c ON e.category_id = c.id " +
                "WHERE e.user_id = ?";

        try (Connection conn = DBConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {

                String date = rs.getDate("date").toString();
                String category = rs.getString("category");
                double amount = rs.getDouble("amount");
                String note = rs.getString("note");

                System.out.printf("%-12s %-10s %-12s %-10.2f %-15s\n",
                        date,
                        "Expense",
                        category,
                        amount,
                        note);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}