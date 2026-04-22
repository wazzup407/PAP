package pl.dobrepapu.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import pl.dobrepapu.dao.IngredientDAO;
import pl.dobrepapu.dao.RecipeDAO;
import pl.dobrepapu.model.Ingredient;
import pl.dobrepapu.model.Recipe;

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
            // Tymczasowe proste dodawanie - docelowo tu otworzy się formularz
            TextInputDialog dialog = new TextInputDialog("Nowy Przepis");
            dialog.setTitle("Dodaj Przepis");
            dialog.setHeaderText("Podaj nazwę nowego przepisu");
            Optional<String> result = dialog.showAndWait();
            
            result.ifPresent(name -> {
                Recipe r = new Recipe(0, name, 2, 30, "Zrób to i tamto...", 3, "");
                recipeDAO.addRecipe(r);
                showRecipes(); // Odświeżenie listy
            });

        } else {
            // Tymczasowe proste dodawanie dla składnika
            TextInputDialog dialog = new TextInputDialog("Nowy Składnik");
            dialog.setTitle("Dodaj Składnik");
            dialog.setHeaderText("Podaj nazwę nowego składnika");
            Optional<String> result = dialog.showAndWait();

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
