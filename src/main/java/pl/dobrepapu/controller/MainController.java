package pl.dobrepapu.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import pl.dobrepapu.dao.IngredientDAO;
import pl.dobrepapu.dao.RecipeDAO;
import pl.dobrepapu.model.Ingredient;
import pl.dobrepapu.model.Recipe;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.Modality;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import java.io.File;

import java.util.Optional;

public class MainController {

    @FXML private ListView<Object> itemListView;
    @FXML private TextField searchField;
    @FXML private Button addBtn;
    @FXML private Button deleteBtn;

    // Nowe elementy prawego panelu
    @FXML private Label detailsTitleLabel;
    @FXML private Label detailRatingLabel;
    @FXML private Label detailInfoLabel;
    @FXML private TextArea detailInstructionsArea;
    @FXML private ImageView detailImageView;

    // Elementy kafelka z makroskładnikami
    @FXML private VBox macroBox;
    @FXML private Label macroCaloriesLabel;
    @FXML private Label macroProteinLabel;
    @FXML private Label macroFatLabel;
    @FXML private Label macroCarbsLabel;

    private IngredientDAO ingredientDAO = new IngredientDAO();
    private RecipeDAO recipeDAO = new RecipeDAO();

    private String currentMode = "RECIPES"; // Może być "RECIPES" albo "INGREDIENTS"

    @FXML
    public void initialize() {
        showRecipes();

        // Nasłuchiwanie wyboru elementu na liście
        itemListView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                showDetails(newSelection);
            }
        });
    }

    @FXML
    public void showRecipes() {
        currentMode = "RECIPES";
        searchField.setPromptText("Szukaj przepisu...");
        itemListView.getItems().clear();
        itemListView.getItems().addAll(recipeDAO.getAllRecipes());
        clearDetails();
    }

    @FXML
    public void showIngredients() {
        currentMode = "INGREDIENTS";
        searchField.setPromptText("Szukaj składnika...");
        itemListView.getItems().clear();
        itemListView.getItems().addAll(ingredientDAO.getAllIngredients());
        clearDetails();
    }

    private void showDetails(Object item) {
        macroBox.setVisible(true); // Pokazujemy panel makro

        if (item instanceof Recipe) {
            Recipe r = (Recipe) item;
            detailsTitleLabel.setText(r.getName());
            
            // Rysowanie odpowiedniej liczby gwiazdek
            StringBuilder stars = new StringBuilder();
            for (int i = 0; i < r.getRating(); i++) stars.append("★");
            for (int i = r.getRating(); i < 5; i++) stars.append("☆");
            detailRatingLabel.setText(stars.toString());
            
            detailInfoLabel.setText(String.format("⏱ Czas: %d min   |   🍽 Porcje: %d", r.getPrepTime(), r.getPortions()));
            detailInstructionsArea.setText(r.getInstructions());
            
            // Ładowanie zdjęcia (jeśli istnieje)
            detailImageView.setImage(null);
            if (r.getImagePath() != null && !r.getImagePath().isEmpty()) {
                try {
                    File imgFile = new File(r.getImagePath());
                    if (imgFile.exists()) {
                        detailImageView.setImage(new Image(imgFile.toURI().toString()));
                    }
                } catch (Exception e) {
                    System.out.println("Nie udało się załadować zdjęcia.");
                }
            }

            // Makro dla przepisu wyliczy Osoba 2, zostawiamy ładne "puste" wartości
            macroCaloriesLabel.setText("Kalorie: -- kcal");
            macroProteinLabel.setText("Białko: -- g");
            macroFatLabel.setText("Tłuszcze: -- g");
            macroCarbsLabel.setText("Węglowodany: -- g");

        } else if (item instanceof Ingredient) {
            Ingredient i = (Ingredient) item;
            detailsTitleLabel.setText(i.getName());
            detailRatingLabel.setText(""); // Składniki nie mają ocen
            detailImageView.setImage(null); // Składniki nie mają (na razie) zdjęć
            
            detailInfoLabel.setText("⚖ Jednostka bazowa: " + i.getUnit());
            detailInstructionsArea.setText("Brak dodatkowego opisu instrukcji dla samego składnika.");
            
            // Makro dla pojedynczego składnika znamy bezpośrednio z bazy
            macroCaloriesLabel.setText(String.format("Kalorie: %.1f kcal", i.getCalories()));
            macroProteinLabel.setText(String.format("Białko: %.1f g", i.getProtein()));
            macroFatLabel.setText(String.format("Tłuszcze: %.1f g", i.getFat()));
            macroCarbsLabel.setText(String.format("Węglowodany: %.1f g", i.getCarbs()));
        }
    }

    private void clearDetails() {
        detailsTitleLabel.setText("Wybierz element z listy");
        if(detailRatingLabel != null) detailRatingLabel.setText("");
        if(detailInfoLabel != null) detailInfoLabel.setText("");
        if(detailInstructionsArea != null) detailInstructionsArea.setText("");
        if(detailImageView != null) detailImageView.setImage(null);
        if(macroBox != null) macroBox.setVisible(false);
    }

    @FXML
    public void handleAdd() {
        if ("RECIPES".equals(currentMode)) {
            // Ładowanie nowego formularza
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/pl/dobrepapu/fxml/RecipeForm.fxml"));
                Parent root = loader.load();
                
                Stage stage = new Stage();
                stage.setTitle("Dodaj Nowy Przepis");
                stage.setScene(new Scene(root, 700, 800)); // Ustawiamy domyślny rozmiar okienka
                
                // Ustawienie Modality sprawia, że nowe okno blokuje to pod spodem (nie można klikać w główne okno, dopóki nie zamkniemy formularza)
                stage.initModality(Modality.APPLICATION_MODAL);
                
                // Pokazuje okno i "zatrzymuje" ten kod do momentu jego zamknięcia
                stage.showAndWait();
                
                // Tutaj w przyszłości dodam odświeżanie listy przepisów po dodaniu
                showRecipes(); 
                
            } catch (Exception e) {
                System.err.println("Błąd podczas otwierania formularza: " + e.getMessage());
                e.printStackTrace();
            }

        } else {
            // Tymczasowe proste dodawanie dla składnika - To jest zadanie dla Tymka
            TextInputDialog dialog = new TextInputDialog("Nowy Składnik");
            dialog.setTitle("Dodaj Składnik");
            dialog.setHeaderText("Podaj nazwę nowego składnika");
            java.util.Optional<String> result = dialog.showAndWait();

            result.ifPresent(name -> {
                Ingredient i = new Ingredient(0, name, "100g", 0, 0, 0, 0);
                ingredientDAO.addIngredient(i);
                showIngredients(); // Odświeżenie listy
            });
        }
    }

    @FXML
    public void handleDelete() {
        Object selected = itemListView.getSelectionModel().getSelectedItem();
        if (selected != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Potwierdzenie usunięcia");
            alert.setHeaderText("Czy na pewno chcesz usunąć wybrany element?");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                if (selected instanceof Recipe) {
                    recipeDAO.deleteRecipe(((Recipe) selected).getId());
                    showRecipes();
                } else if (selected instanceof Ingredient) {
                    ingredientDAO.deleteIngredient(((Ingredient) selected).getId());
                    showIngredients();
                }
            }
        }
    }
}
