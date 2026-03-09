package repository;

import database.DBConnection;
import model.Income;

import java.sql.Connection;
import java.sql.PreparedStatement;

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
}