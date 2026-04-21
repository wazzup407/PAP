package pl.dobrepapu.dao;

import pl.dobrepapu.model.Ingredient;
import pl.dobrepapu.db.DatabaseManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class IngredientDAO {

    public List<Ingredient> getAllIngredients() {
        List<Ingredient> list = new ArrayList<>();
        String sql = "SELECT * FROM ingredients";
        try (Connection conn = DatabaseManager.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new Ingredient(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("unit"),
                        rs.getDouble("calories"),
                        rs.getDouble("protein"),
                        rs.getDouble("fat"),
                        rs.getDouble("carbs")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public void addIngredient(Ingredient i) {
        String sql = "INSERT INTO ingredients (name, unit, calories, protein, fat, carbs) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseManager.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, i.getName());
            pstmt.setString(2, i.getUnit());
            pstmt.setDouble(3, i.getCalories());
            pstmt.setDouble(4, i.getProtein());
            pstmt.setDouble(5, i.getFat());
            pstmt.setDouble(6, i.getCarbs());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteIngredient(int id) {
        String sql = "DELETE FROM ingredients WHERE id = ?";
        try (Connection conn = DatabaseManager.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
