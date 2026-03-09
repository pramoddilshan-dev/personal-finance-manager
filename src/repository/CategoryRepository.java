package repository;

import database.DBConnection;
import model.Category;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class CategoryRepository {

    public boolean addCategory(Category category) {

        String sql = "INSERT INTO categories(name, type) VALUES (?,?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, category.getName());
            stmt.setString(2, category.getType());

            stmt.executeUpdate();
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}