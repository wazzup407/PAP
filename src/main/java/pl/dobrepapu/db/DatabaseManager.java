package pl.dobrepapu.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseManager {
    // Lokalna baza SQLite tworzona w katalogu projektu
    private static final String URL = "jdbc:sqlite:dobrepapu.db";

    public static Connection connect() throws SQLException {
        return DriverManager.getConnection(URL);
    }

    public static void initDB() {
        String createIngredients = "CREATE TABLE IF NOT EXISTS ingredients ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "name TEXT NOT NULL,"
                + "unit TEXT NOT NULL,"
                + "calories REAL,"
                + "protein REAL,"
                + "fat REAL,"
                + "carbs REAL"
                + ");";

        String createRecipes = "CREATE TABLE IF NOT EXISTS recipes ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "name TEXT NOT NULL,"
                + "portions INTEGER,"
                + "prep_time INTEGER,"
                + "instructions TEXT,"
                + "rating INTEGER,"
                + "image_path TEXT"
                + ");";

        String createRecipeIngredients = "CREATE TABLE IF NOT EXISTS recipe_ingredients ("
                + "recipe_id INTEGER,"
                + "ingredient_id INTEGER,"
                + "quantity REAL,"
                + "FOREIGN KEY(recipe_id) REFERENCES recipes(id) ON DELETE CASCADE,"
                + "FOREIGN KEY(ingredient_id) REFERENCES ingredients(id) ON DELETE CASCADE,"
                + "PRIMARY KEY (recipe_id, ingredient_id)"
                + ");";

        try (Connection conn = connect(); Statement stmt = conn.createStatement()) {
            // Włączamy ograniczenia kluczy obcych (domyślnie w SQLite są wyłączone)
            stmt.execute("PRAGMA foreign_keys = ON;");
            
            stmt.execute(createIngredients);
            stmt.execute(createRecipes);
            stmt.execute(createRecipeIngredients);
            System.out.println("Baza danych zainicjalizowana pomyślnie.");
        } catch (SQLException e) {
            System.err.println("Błąd podczas inicjalizacji bazy: " + e.getMessage());
        }
    }
}
