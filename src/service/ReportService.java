package service;

import database.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ReportService {

    private int userId;

    public ReportService(int userId) {
        this.userId = userId;
    }

    // Total income
    public double getTotalIncome() {
        String sql = "SELECT SUM(amount) AS total FROM income WHERE user_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getDouble("total");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    // Total expenses
    public double getTotalExpenses() {
        String sql = "SELECT SUM(amount) AS total FROM expenses WHERE user_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getDouble("total");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    // Category-wise expenses
    public void printCategoryExpenses() {
        String sql = "SELECT c.name, SUM(e.amount) AS total " +
                     "FROM expenses e " +
                     "JOIN categories c ON e.category_id = c.id " +
                     "WHERE e.user_id = ? " +
                     "GROUP BY c.name";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

            System.out.println("\n--- Category-wise Expenses ---");
            while (rs.next()) {
                String category = rs.getString("name");
                double total = rs.getDouble("total");
                System.out.println(category + ": " + total);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}