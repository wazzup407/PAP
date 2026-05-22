package pl.dobrepapu.controller;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;

public class RecipeFormController {

    @FXML private TextField nameField;
    @FXML private TextField portionsField;
    @FXML private TextField timeField;
    @FXML private TextArea instructionsArea;
    @FXML private ComboBox<String> ingredientComboBox; 
    @FXML private TextField ingredientQuantityField;
    @FXML private ListView<String> recipeIngredientsList;

    @FXML
    public void initialize() {
        // Tu później inicjalizacja listy składników
    }

    @FXML
    public void handleSave() {
        // Tu zapis do bazy
        System.out.println("Kliknięto zapisz!");
        closeWindow();
    }

    @FXML
    public void handleCancel() {
        closeWindow();
    }

    @FXML
    public void handleAddIngredient() {
        // Tymczasowy test, czy przycisk działa
        System.out.println("Kliknięto dodawanie składnika!");
    }

    private void closeWindow() {
        // Pobiera aktualne okno (Stage) z dowolnego elementu i zamyka je
        Stage stage = (Stage) nameField.getScene().getWindow();
        stage.close();
    }
}