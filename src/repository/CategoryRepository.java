package repository;

import database.DBConnection;
import model.Category;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

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

    public int getCategoryIdByName(String name, String type) {
        String sql = "SELECT id FROM categories WHERE name = ? AND type = ?";
        try (Connection conn = DBConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, name);
            stmt.setString(2, type);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("id");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return -1; // Not found
    }

    public List<Category> getCategoriesByType(String type) {

        List<Category> categories = new ArrayList<>();

        String sql = "SELECT * FROM categories WHERE type = ?";

        try (Connection conn = DBConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, type);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {

                Category category = new Category();

                category.setId(rs.getInt("id"));
                category.setName(rs.getString("name"));
                category.setType(rs.getString("type"));

                categories.add(category);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return categories;
    }

    public Category getCategoryById(int id) {

        Category category = null;

        try {

            String sql = "SELECT * FROM categories WHERE id = ?";
            PreparedStatement stmt = DBConnection.getConnection().prepareStatement(sql);

            stmt.setInt(1, id);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {

                category = new Category();

                category.setId(rs.getInt("id"));
                category.setName(rs.getString("name"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return category;
    }
}