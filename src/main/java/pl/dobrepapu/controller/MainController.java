package pl.dobrepapu.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.layout.GridPane;
import javafx.geometry.Insets;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import pl.dobrepapu.dao.IngredientDAO;
import pl.dobrepapu.dao.RecipeDAO;
import pl.dobrepapu.dao.RecipeIngredientDAO;
import pl.dobrepapu.model.Ingredient;
import pl.dobrepapu.model.Recipe;
import pl.dobrepapu.model.RecipeIngredient;
import pl.dobrepapu.service.NutritionService;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.Modality;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Optional;

public class MainController {

    @FXML private ListView<Object> itemListView;
    @FXML private TextField searchField;
    @FXML private Button addBtn;
    @FXML private Button deleteBtn;

    //elementy prawego panelu
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

        // Dynamiczne wyszukiwanie po stronie bazy danych
        searchField.textProperty().addListener((obs, oldVal, newVal) -> {
            filterList(newVal);
        });

        // Edycja przez dwukrotne kliknięcie na liście
        itemListView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                handleEdit();
            }
        });
    }

    @FXML
    public void showRecipes() {
        currentMode = "RECIPES";
        searchField.clear();
        searchField.setPromptText("Szukaj przepisu...");
        itemListView.getItems().clear();
        itemListView.getItems().addAll(recipeDAO.getAllRecipes());
        clearDetails();
    }

    @FXML
    public void showIngredients() {
        currentMode = "INGREDIENTS";
        searchField.clear();
        searchField.setPromptText("Szukaj składnika...");
        itemListView.getItems().clear();
        itemListView.getItems().addAll(ingredientDAO.getAllIngredients());
        clearDetails();
    }

    private void filterList(String query) {
        if ("RECIPES".equals(currentMode)) {
            itemListView.getItems().clear();
            if (query == null || query.trim().isEmpty()) {
                itemListView.getItems().addAll(recipeDAO.getAllRecipes());
            } else {
                itemListView.getItems().addAll(recipeDAO.searchRecipes(query));
            }
        } else {
            itemListView.getItems().clear();
            if (query == null || query.trim().isEmpty()) {
                itemListView.getItems().addAll(ingredientDAO.getAllIngredients());
            } else {
                itemListView.getItems().addAll(ingredientDAO.searchIngredients(query));
            }
        }
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
            
            // Dynamiczne wczytywanie składników przepisu
            RecipeIngredientDAO riDAO = new RecipeIngredientDAO();
            List<RecipeIngredient> recipeIngredients = riDAO.getIngredientsForRecipe(r.getId());
            
            StringBuilder instructionsBuilder = new StringBuilder();
            if (!recipeIngredients.isEmpty()) {
                instructionsBuilder.append("🍎 Składniki:\n");
                for (RecipeIngredient ri : recipeIngredients) {
                    if (ri.getIngredient() != null) {
                        instructionsBuilder.append("  • ")
                                .append(ri.getIngredient().getName())
                                .append(" - ")
                                .append(ri.getQuantity())
                                .append(" x ")
                                .append(ri.getIngredient().getUnit())
                                .append("\n");
                    }
                }
                instructionsBuilder.append("\n📝 Sposób przygotowania:\n");
            }
            instructionsBuilder.append(r.getInstructions());
            detailInstructionsArea.setText(instructionsBuilder.toString());
            
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

            // Liczenie makroskładników dynamicznie przy użyciu NutritionService
            Map<Ingredient, Double> ingredientsMap = new HashMap<>();
            for (RecipeIngredient ri : recipeIngredients) {
                if (ri.getIngredient() != null) {
                    ingredientsMap.put(ri.getIngredient(), ri.getQuantity());
                }
            }
            
            NutritionService service = new NutritionService();
            NutritionService.MacroSummary summary = service.calculateMacros(ingredientsMap);
            
            macroCaloriesLabel.setText(String.format("Kalorie: %.1f kcal", summary.getTotalCalories()));
            macroProteinLabel.setText(String.format("Białko: %.1f g", summary.getTotalProtein()));
            macroFatLabel.setText(String.format("Tłuszcze: %.1f g", summary.getTotalFat()));
            macroCarbsLabel.setText(String.format("Węglowodany: %.1f g", summary.getTotalCarbs()));

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
                stage.setScene(new Scene(root, 650, 680)); // domyślny rozmiar okienka
                
                // Ustawienie Modality sprawia, że nowe okno blokuje to pod spodem
                stage.initModality(Modality.APPLICATION_MODAL);
                
                // Pokazuje okno i zatrzymuje ten kod do momentu zamknięcia
                stage.showAndWait();
                
                showRecipes(); 
                
            } catch (Exception e) {
                System.err.println("Błąd podczas otwierania formularza: " + e.getMessage());
                e.printStackTrace();
            }

        } else {
            // Używamy profesjonalnego okna dialogowego z walidacją dla składnika
            Optional<Ingredient> result = showIngredientDialog(null);
            result.ifPresent(ingredient -> {
                ingredientDAO.addIngredient(ingredient);
                showIngredients(); // Odświeżenie listy
            });
        }
    }

    @FXML
    public void handleEdit() {
        Object selected = itemListView.getSelectionModel().getSelectedItem();
        if (selected == null) return;

        if (selected instanceof Recipe) {
            Recipe r = (Recipe) selected;
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/pl/dobrepapu/fxml/RecipeForm.fxml"));
                Parent root = loader.load();
                
                RecipeFormController controller = loader.getController();
                controller.setRecipe(r); // Pass the recipe to edit!
                
                Stage stage = new Stage();
                stage.setTitle("Edytuj Przepis");
                stage.setScene(new Scene(root, 650, 680));
                
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.showAndWait();
                
                showRecipes(); // Refresh recipes list after the dialog is closed
            } catch (Exception e) {
                System.err.println("Błąd podczas otwierania formularza edycji przepisu: " + e.getMessage());
                e.printStackTrace();
            }
        } else if (selected instanceof Ingredient) {
            Ingredient i = (Ingredient) selected;
            Optional<Ingredient> result = showIngredientDialog(i);
            result.ifPresent(updatedIngredient -> {
                ingredientDAO.updateIngredient(updatedIngredient);
                showIngredients(); // Odświeżenie listy
                itemListView.getSelectionModel().select(updatedIngredient);
            });
        }
    }

    private Optional<Ingredient> showIngredientDialog(Ingredient existing) {
        Dialog<Ingredient> dialog = new Dialog<>();
        dialog.setTitle(existing == null ? "Dodaj Składnik" : "Edytuj Składnik");
        dialog.setHeaderText(existing == null ? "Podaj parametry nowego składnika:" : "Modyfikuj parametry składnika:");

        // Set the button types
        ButtonType saveButtonType = new ButtonType("Zapisz", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

        // Grid layout
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField nameField = new TextField();
        nameField.setPromptText("Nazwa");
        TextField unitField = new TextField();
        unitField.setPromptText("np. 100g, sztuka");
        TextField caloriesField = new TextField();
        caloriesField.setPromptText("kcal");
        TextField proteinField = new TextField();
        proteinField.setPromptText("g");
        TextField fatField = new TextField();
        fatField.setPromptText("g");
        TextField carbsField = new TextField();
        carbsField.setPromptText("g");

        if (existing != null) {
            nameField.setText(existing.getName());
            unitField.setText(existing.getUnit());
            caloriesField.setText(String.valueOf(existing.getCalories()));
            proteinField.setText(String.valueOf(existing.getProtein()));
            fatField.setText(String.valueOf(existing.getFat()));
            carbsField.setText(String.valueOf(existing.getCarbs()));
        }

        grid.add(new Label("Nazwa:"), 0, 0);
        grid.add(nameField, 1, 0);
        grid.add(new Label("Jednostka:"), 0, 1);
        grid.add(unitField, 1, 1);
        grid.add(new Label("Kalorie (kcal):"), 0, 2);
        grid.add(caloriesField, 1, 2);
        grid.add(new Label("Białko (g):"), 0, 3);
        grid.add(proteinField, 1, 3);
        grid.add(new Label("Tłuszcze (g):"), 0, 4);
        grid.add(fatField, 1, 4);
        grid.add(new Label("Węglowodany (g):"), 0, 5);
        grid.add(carbsField, 1, 5);

        dialog.getDialogPane().setContent(grid);

        Platform.runLater(() -> nameField.requestFocus());

        // Dynamic validation filtering
        Button okButton = (Button) dialog.getDialogPane().lookupButton(saveButtonType);
        okButton.addEventFilter(ActionEvent.ACTION, event -> {
            String name = nameField.getText().trim();
            String unit = unitField.getText().trim();
            if (name.isEmpty() || unit.isEmpty()) {
                showErrorAlert("Nazwa i jednostka nie mogą być puste!");
                event.consume();
                return;
            }
            try {
                double cal = Double.parseDouble(caloriesField.getText().replace(',', '.'));
                double prot = Double.parseDouble(proteinField.getText().replace(',', '.'));
                double fat = Double.parseDouble(fatField.getText().replace(',', '.'));
                double carbs = Double.parseDouble(carbsField.getText().replace(',', '.'));
                if (cal < 0 || prot < 0 || fat < 0 || carbs < 0) {
                    showErrorAlert("Wartości odżywcze nie mogą być ujemne!");
                    event.consume();
                }
            } catch (NumberFormatException e) {
                showErrorAlert("Wartości odżywcze muszą być poprawnymi liczbami!");
                event.consume();
            }
        });

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == saveButtonType) {
                int id = existing == null ? 0 : existing.getId();
                return new Ingredient(
                        id,
                        nameField.getText().trim(),
                        unitField.getText().trim(),
                        Double.parseDouble(caloriesField.getText().replace(',', '.')),
                        Double.parseDouble(proteinField.getText().replace(',', '.')),
                        Double.parseDouble(fatField.getText().replace(',', '.')),
                        Double.parseDouble(carbsField.getText().replace(',', '.'))
                );
            }
            return null;
        });

        return dialog.showAndWait();
    }

    private void showErrorAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Błąd walidacji");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
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
