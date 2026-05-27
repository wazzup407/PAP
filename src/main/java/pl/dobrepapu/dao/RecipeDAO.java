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
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            
            while (rs.next()) {
                Recipe recipe = new Recipe(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getInt("portions"),
                        rs.getInt("prep_time"),
                        rs.getString("instructions"),
                        rs.getInt("rating"),
                        rs.getString("image_path")
                );
                
                recipe.setTags(getTagsForRecipe(recipe.getId(), conn));
                
                list.add(recipe);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<Recipe> searchRecipes(String query) {
        List<Recipe> list = new ArrayList<>();
        String sql = "SELECT DISTINCT r.* FROM recipes r " +
                     "LEFT JOIN recipe_tags rt ON r.id = rt.recipe_id " +
                     "LEFT JOIN tags t ON rt.tag_id = t.id " +
                     "WHERE r.name LIKE ? OR t.name LIKE ?";
        
        try (Connection conn = DatabaseManager.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            String pattern = "%" + query + "%";
            pstmt.setString(1, pattern);
            pstmt.setString(2, pattern);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Recipe recipe = new Recipe(
                            rs.getInt("id"),
                            rs.getString("name"),
                            rs.getInt("portions"),
                            rs.getInt("prep_time"),
                            rs.getString("instructions"),
                            rs.getInt("rating"),
                            rs.getString("image_path")
                    );
                    recipe.setTags(getTagsForRecipe(recipe.getId(), conn));
                    list.add(recipe);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    private List<String> getTagsForRecipe(int recipeId, Connection conn) {
        List<String> tags = new ArrayList<>();
        String sql = "SELECT t.name FROM tags t INNER JOIN recipe_tags rt ON t.id = rt.tag_id WHERE rt.recipe_id = ?";
        
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, recipeId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    tags.add(rs.getString("name"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tags;
    }

    public int addRecipe(Recipe r) {
        String sql = "INSERT INTO recipes (name, portions, prep_time, instructions, rating, image_path) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseManager.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setString(1, r.getName());
            pstmt.setInt(2, r.getPortions());
            pstmt.setInt(3, r.getPrepTime());
            pstmt.setString(4, r.getInstructions());
            pstmt.setInt(5, r.getRating());
            pstmt.setString(6, r.getImagePath());
            pstmt.executeUpdate();

            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int recipeId = generatedKeys.getInt(1);
                    saveTagsForRecipe(recipeId, r.getTags(), conn);
                    return recipeId;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public void updateRecipe(Recipe r) {
        String sql = "UPDATE recipes SET name = ?, portions = ?, prep_time = ?, instructions = ?, rating = ?, image_path = ? WHERE id = ?";
        try (Connection conn = DatabaseManager.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, r.getName());
            pstmt.setInt(2, r.getPortions());
            pstmt.setInt(3, r.getPrepTime());
            pstmt.setString(4, r.getInstructions());
            pstmt.setInt(5, r.getRating());
            pstmt.setString(6, r.getImagePath());
            pstmt.setInt(7, r.getId());
            pstmt.executeUpdate();

            try (PreparedStatement delStmt = conn.prepareStatement("DELETE FROM recipe_tags WHERE recipe_id = ?")) {
                delStmt.setInt(1, r.getId());
                delStmt.executeUpdate();
            }
            saveTagsForRecipe(r.getId(), r.getTags(), conn);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    private void saveTagsForRecipe(int recipeId, List<String> tags, Connection conn) throws SQLException {
        if (tags == null || tags.isEmpty()) return;

        String insertTagSql = "INSERT OR IGNORE INTO tags (name) VALUES (?)";
        String selectTagIdSql = "SELECT id FROM tags WHERE name = ?";
        String insertRelationSql = "INSERT INTO recipe_tags (recipe_id, tag_id) VALUES (?, ?)";

        try (PreparedStatement insertTagStmt = conn.prepareStatement(insertTagSql);
             PreparedStatement selectTagStmt = conn.prepareStatement(selectTagIdSql);
             PreparedStatement insertRelStmt = conn.prepareStatement(insertRelationSql)) {

            for (String tagName : tags) {
                insertTagStmt.setString(1, tagName);
                insertTagStmt.executeUpdate();

                selectTagStmt.setString(1, tagName);
                int tagId = -1;
                try (ResultSet rs = selectTagStmt.executeQuery()) {
                    if (rs.next()) {
                        tagId = rs.getInt("id");
                    }
                }

                if (tagId != -1) {
                    insertRelStmt.setInt(1, recipeId);
                    insertRelStmt.setInt(2, tagId);
                    insertRelStmt.executeUpdate();
                }
            }
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

    public Recipe getRecipeById(int id) {
    String sql = "SELECT * FROM recipes WHERE id = ?";
    try (Connection conn = DatabaseManager.connect();
         PreparedStatement pstmt = conn.prepareStatement(sql)) {
        pstmt.setInt(1, id);
        try (ResultSet rs = pstmt.executeQuery()) {
            if (rs.next()) {
                Recipe recipe = new Recipe(
                        rs.getInt("id"), rs.getString("name"), rs.getInt("portions"),
                        rs.getInt("prep_time"), rs.getString("instructions"),
                        rs.getInt("rating"), rs.getString("image_path")
                );
                recipe.setTags(getTagsForRecipe(recipe.getId(), conn));
                return recipe;
            }
        }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}

