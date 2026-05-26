package pl.dobrepapu.dao;

import pl.dobrepapu.db.DatabaseManager;
import pl.dobrepapu.model.Ingredient;
import pl.dobrepapu.model.RecipeIngredient;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RecipeIngredientDAO {

    public List<RecipeIngredient> getIngredientsForRecipe(int recipeId) {
        List<RecipeIngredient> list = new ArrayList<>();
        String sql = "SELECT ri.recipe_id, ri.ingredient_id, ri.quantity, " +
                     "i.name, i.unit, i.calories, i.protein, i.fat, i.carbs " +
                     "FROM recipe_ingredients ri " +
                     "JOIN ingredients i ON ri.ingredient_id = i.id " +
                     "WHERE ri.recipe_id = ?";
        
        try (Connection conn = DatabaseManager.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, recipeId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Ingredient ingredient = new Ingredient(
                            rs.getInt("ingredient_id"),
                            rs.getString("name"),
                            rs.getString("unit"),
                            rs.getDouble("calories"),
                            rs.getDouble("protein"),
                            rs.getDouble("fat"),
                            rs.getDouble("carbs")
                    );
                    
                    RecipeIngredient ri = new RecipeIngredient(
                            rs.getInt("recipe_id"),
                            rs.getInt("ingredient_id"),
                            rs.getDouble("quantity")
                    );
                    ri.setIngredient(ingredient);
                    
                    list.add(ri);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public void addRecipeIngredient(RecipeIngredient ri) {
        String sql = "INSERT OR REPLACE INTO recipe_ingredients (recipe_id, ingredient_id, quantity) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseManager.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, ri.getRecipeId());
            pstmt.setInt(2, ri.getIngredientId());
            pstmt.setDouble(3, ri.getQuantity());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteRecipeIngredient(int recipeId, int ingredientId) {
        String sql = "DELETE FROM recipe_ingredients WHERE recipe_id = ? AND ingredient_id = ?";
        try (Connection conn = DatabaseManager.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, recipeId);
            pstmt.setInt(2, ingredientId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteIngredientsForRecipe(int recipeId) {
        String sql = "DELETE FROM recipe_ingredients WHERE recipe_id = ?";
        try (Connection conn = DatabaseManager.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, recipeId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
