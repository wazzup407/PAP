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

import java.util.Optional;

public class MainController {

    @FXML private ListView<Object> itemListView;
    @FXML private Label detailsTitleLabel;
    @FXML private TextArea detailsContentArea;
    @FXML private TextField searchField;
    @FXML private Button addBtn;
    @FXML private Button deleteBtn;

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
        if (item instanceof Recipe) {
            Recipe r = (Recipe) item;
            detailsTitleLabel.setText(r.getName());
            
            String content = String.format("Czas: %d min | Porcje: %d\n\nKroki Przygotowania:\n%s",
                    r.getPrepTime(), r.getPortions(), r.getInstructions());
            
            // Tutaj w przyszłości będzie wyświetlanie obrazka i obliczanie makro
            detailsContentArea.setText(content);

        } else if (item instanceof Ingredient) {
            Ingredient i = (Ingredient) item;
            detailsTitleLabel.setText(i.getName());
            
            String content = String.format("Jednostka bazowa: %s\n\nWartości Odżywcze (na jednostkę):\nKalorie: %.2f kcal\nBiałko: %.2f g\nTłuszcze: %.2f g\nWęglowodany: %.2f g",
                    i.getUnit(), i.getCalories(), i.getProtein(), i.getFat(), i.getCarbs());
            
            detailsContentArea.setText(content);
        }
    }

    private void clearDetails() {
        detailsTitleLabel.setText("Wybierz element z listy");
        detailsContentArea.setText("");
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
                stage.setScene(new Scene(root, 600, 500)); // Ustawiamy domyślny rozmiar okienka
                
                // Ustawienie Modality sprawia, że nowe okno blokuje to pod spodem (nie można klikać w główne okno, dopóki nie zamkniemy formularza)
                stage.initModality(Modality.APPLICATION_MODAL);
                
                // Pokazuje okno i "zatrzymuje" ten kod do momentu jego zamknięcia
                stage.showAndWait();
                
                // Tutaj w przyszłości dodam odświeżanie listy przepisów po dodaniu
                // showRecipes(); 
                
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
