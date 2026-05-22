package pl.dobrepapu.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import pl.dobrepapu.dao.IngredientDAO;
import pl.dobrepapu.dao.RecipeDAO;
import pl.dobrepapu.model.Ingredient;
import pl.dobrepapu.model.Recipe;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.Label;
import javafx.stage.FileChooser;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class RecipeFormController {

    @FXML private TextField nameField;
    @FXML private TextField portionsField;
    @FXML private TextField timeField;
    @FXML private TextArea instructionsArea;
    
    @FXML private ComboBox<Ingredient> ingredientComboBox; 
    @FXML private TextField ingredientQuantityField;
    @FXML private ListView<String> recipeIngredientsList;

    @FXML private ImageView recipeImageView;
    @FXML private Label imagePathLabel;
    private String savedImagePath = "";

    @FXML private Button star1;
    @FXML private Button star2;
    @FXML private Button star3;
    @FXML private Button star4;
    @FXML private Button star5;
    private int recipeRating = 3;

    private RecipeDAO recipeDAO = new RecipeDAO();
    private IngredientDAO ingredientDAO = new IngredientDAO();
    // lista która automatycznie aktualizuje interfejs użytkownika
    private ObservableList<String> temporaryIngredients = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // Ładowanie składników z bazy do ComboBoxa
        ingredientComboBox.getItems().addAll(ingredientDAO.getAllIngredients());
        
        // Połączenie tymczasowej listy z kontrolką ListView na ekranie
        recipeIngredientsList.setItems(temporaryIngredients);
    }

    @FXML
    public void handleAddIngredient() {
        Ingredient selectedIngredient = ingredientComboBox.getValue();
        String quantityStr = ingredientQuantityField.getText();

        // Prosta walidacja - czy wybrano składnik i wpisano ilość
        if (selectedIngredient != null && quantityStr != null && !quantityStr.trim().isEmpty()) {
            try {
                // przecinek na kropkę (wygoda dla użytkownika) i parsujemy
                double quantity = Double.parseDouble(quantityStr.replace(",", "."));
                
                // Tworzymy tekst, np.: "Jajo - 2.0 x 100g"
                String displayString = selectedIngredient.getName() + " - " + quantity + " x " + selectedIngredient.getUnit();
                
                // Dodajemy do listy (widok zaktualizuje się automatycznie)
                temporaryIngredients.add(displayString);
                
                // Czyścimy formularz składnika
                ingredientComboBox.getSelectionModel().clearSelection();
                ingredientQuantityField.clear();
                
            } catch (NumberFormatException e) {
                System.out.println("Błąd: Wpisana ilość nie jest poprawną liczbą!");
            }
        } else {
            System.out.println("Błąd: Wybierz składnik i wpisz ilość!");
        }
    }

    @FXML
    public void handleAddImage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Wybierz zdjęcie potrawy");
        
        // Filtrujemy, żeby użytkownik mógł wybrać tylko obrazki
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Obrazy", "*.png", "*.jpg", "*.jpeg")
        );
        
        Stage stage = (Stage) nameField.getScene().getWindow();
        File selectedFile = fileChooser.showOpenDialog(stage);
        
        if (selectedFile != null) {
            try {
                // Dodajemy timestamp do nazwy pliku, aby uniknąć nadpisania dwóch plików o tej samej nazwie
                String fileName = System.currentTimeMillis() + "_" + selectedFile.getName();
                
                // Folder "images" utworzy się sam
                Path destPath = Paths.get("images", fileName);
                
                // Kopiujemy plik z komputera do folderu projektu
                Files.copy(selectedFile.toPath(), destPath, StandardCopyOption.REPLACE_EXISTING);
                
                // Zapisujemy ścieżkę i odświeżamy etykietę w oknie
                savedImagePath = destPath.toString();
                imagePathLabel.setText(selectedFile.getName());
                
                // Ładujemy podgląd zdjęcia do ImageView
                Image image = new Image(destPath.toUri().toString());
                recipeImageView.setImage(image);
                
            } catch (IOException e) {
                System.err.println("Błąd podczas wgrywania pliku: " + e.getMessage());
            }
        }
    }

    @FXML public void setRating1() { updateStars(1); }
    @FXML public void setRating2() { updateStars(2); }
    @FXML public void setRating3() { updateStars(3); }
    @FXML public void setRating4() { updateStars(4); }
    @FXML public void setRating5() { updateStars(5); }

    // zamienia tekst na przyciskach w zależności od wybranej oceny
    private void updateStars(int rating) {
        this.recipeRating = rating;
        
        // Znak ★ (U+2605) to pełna gwiazdka, a ☆ (U+2606) to pusta
        star1.setText(rating >= 1 ? "★" : "☆");
        star2.setText(rating >= 2 ? "★" : "☆");
        star3.setText(rating >= 3 ? "★" : "☆");
        star4.setText(rating >= 4 ? "★" : "☆");
        star5.setText(rating >= 5 ? "★" : "☆");
    }

    @FXML
    public void handleSave() {
        try {
            // Pobieranie danych z pól tekstowych
            String name = nameField.getText();
            String portionsStr = portionsField.getText();
            String timeStr = timeField.getText();
            String instructions = instructionsArea.getText();

            // Prosta walidacja: czy nazwa nie jest pusta
            if (name == null || name.trim().isEmpty()) {
                System.out.println("Błąd: Nazwa przepisu nie może być pusta!");
                return;
            }

            // Zamiana tekstu na liczby (domyślnie 0, jeśli ktoś zostawi puste)
            int portions = portionsStr.isEmpty() ? 0 : Integer.parseInt(portionsStr);
            int time = timeStr.isEmpty() ? 0 : Integer.parseInt(timeStr);

            // Tworzymy nowy obiekt Recipe (id = 0, bo baza SQLite sama nada mu unikalne ID dzięki AUTOINCREMENT)
            Recipe newRecipe = new Recipe(0, name, portions, time, instructions, recipeRating, savedImagePath);

            // Zapisujemy do bazy
            recipeDAO.addRecipe(newRecipe);
            
            System.out.println("Pomyślnie zapisano przepis: " + name);
            closeWindow();

        } catch (NumberFormatException e) {
            System.out.println("Błąd: Porcje i czas muszą być liczbami!");
        }
    }

    @FXML
    public void handleCancel() {
        closeWindow();
    }

    private void closeWindow() {
        Stage stage = (Stage) nameField.getScene().getWindow();
        stage.close();
    }
}