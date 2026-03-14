package repository;

import database.DBConnection;
import model.Income;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;

public class IncomeRepository {

    public boolean addIncome(Income income) {

        String sql = "INSERT INTO income(user_id, category_id, amount, date, note) VALUES (?,?,?,?,?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, income.getUserId());
            stmt.setInt(2, income.getCategoryId());
            stmt.setDouble(3, income.getAmount());
            stmt.setDate(4, java.sql.Date.valueOf(income.getDate()));
            stmt.setString(5, income.getNote());

            stmt.executeUpdate();
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Income> getIncomeByUser(int userId) {

        List<Income> incomes = new ArrayList<>();

        String sql = "SELECT * FROM income WHERE user_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {

                Income income = new Income();

                income.setId(rs.getInt("id"));
                income.setUserId(rs.getInt("user_id"));
                income.setCategoryId(rs.getInt("category_id"));
                income.setAmount(rs.getDouble("amount"));
                income.setDate(rs.getDate("date").toLocalDate());
                income.setNote(rs.getString("note"));

                incomes.add(income);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return incomes;
    }

    /**
     * NEW METHOD
     * Get total income of a user
     */
    public double getTotalIncome(int userId) {

        String sql = "SELECT SUM(amount) AS total FROM income WHERE user_id = ?";
        double total = 0;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                total = rs.getDouble("total");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return total;
    }

    public void printIncomeTransactions(int userId) {

        String sql =
                "SELECT i.date, c.name AS category, i.amount, i.note " +
                        "FROM income i " +
                        "JOIN categories c ON i.category_id = c.id " +
                        "WHERE i.user_id = ?";

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
                        "Income",
                        category,
                        amount,
                        note);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean deleteIncome(int id) {

        String sql = "DELETE FROM incomes WHERE id=?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean updateIncome(int id, double amount, int categoryId, LocalDate date, String note) {

        String sql = "UPDATE incomes SET amount=?, category_id=?, date=?, note=? WHERE id=?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setDouble(1, amount);
            stmt.setInt(2, categoryId);
            stmt.setDate(3, java.sql.Date.valueOf(date));
            stmt.setString(4, note);
            stmt.setInt(5, id);

            return stmt.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }
}