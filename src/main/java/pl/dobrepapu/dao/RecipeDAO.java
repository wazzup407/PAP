package pl.dobrepapu.dao;

import pl.dobrepapu.model.Recipe;
import pl.dobrepapu.db.DatabaseManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RecipeDAO {

    public List<Recipe> getAllRecipes() {
        List<Recipe> list = new ArrayList<>();
        String sql = "SELECT * FROM recipes";
        try (Connection conn = DatabaseManager.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new Recipe(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getInt("portions"),
                        rs.getInt("prep_time"),
                        rs.getString("instructions"),
                        rs.getInt("rating"),
                        rs.getString("image_path")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public void addRecipe(Recipe r) {
        String sql = "INSERT INTO recipes (name, portions, prep_time, instructions, rating, image_path) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseManager.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, r.getName());
            pstmt.setInt(2, r.getPortions());
            pstmt.setInt(3, r.getPrepTime());
            pstmt.setString(4, r.getInstructions());
            pstmt.setInt(5, r.getRating());
            pstmt.setString(6, r.getImagePath());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteRecipe(int id) {
        String sql = "DELETE FROM recipes WHERE id = ?";
        try (Connection conn = DatabaseManager.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
