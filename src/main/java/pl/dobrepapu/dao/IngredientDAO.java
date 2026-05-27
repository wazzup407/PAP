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
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
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

    public List<Ingredient> searchIngredients(String query) {
        List<Ingredient> list = new ArrayList<>();
        String sql = "SELECT * FROM ingredients WHERE name LIKE ?";
        try (Connection conn = DatabaseManager.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, "%" + query + "%");
            try (ResultSet rs = pstmt.executeQuery()) {
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
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public void updateIngredient(Ingredient i) {
        String sql = "UPDATE ingredients SET name = ?, unit = ?, calories = ?, protein = ?, fat = ?, carbs = ? WHERE id = ?";
        try (Connection conn = DatabaseManager.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, i.getName());
            pstmt.setString(2, i.getUnit());
            pstmt.setDouble(3, i.getCalories());
            pstmt.setDouble(4, i.getProtein());
            pstmt.setDouble(5, i.getFat());
            pstmt.setDouble(6, i.getCarbs());
            pstmt.setInt(7, i.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    // Czy na pewno?
    public Ingredient getIngredientByName(String name) {
        String sql = "SELECT * FROM ingredients WHERE name = ?";
        try (Connection conn = DatabaseManager.connect();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new Ingredient(
                            rs.getInt("id"), rs.getString("name"), rs.getString("unit"),
                            rs.getDouble("calories"), rs.getDouble("protein"),
                            rs.getDouble("fat"), rs.getDouble("carbs")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    public int addIngredient(Ingredient i) {
        String sql = "INSERT INTO ingredients (name, unit, calories, protein, fat, carbs) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseManager.connect();
            PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, i.getName());
            pstmt.setString(2, i.getUnit());
            pstmt.setDouble(3, i.getCalories());
            pstmt.setDouble(4, i.getProtein());
            pstmt.setDouble(5, i.getFat());
            pstmt.setDouble(6, i.getCarbs());
            pstmt.executeUpdate();

            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
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
