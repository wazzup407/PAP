package pl.dobrepapu.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import pl.dobrepapu.dao.IngredientDAO;
import pl.dobrepapu.model.Ingredient;

public class RecipeFormController {

    @FXML private TextField nameField;
    @FXML private TextField portionsField;
    @FXML private TextField timeField;
    @FXML private TextArea instructionsArea;
    
    // Zmienione na Ingredient
    @FXML private ComboBox<Ingredient> ingredientComboBox; 
    @FXML private TextField ingredientQuantityField;
    @FXML private ListView<String> recipeIngredientsList;

    private IngredientDAO ingredientDAO = new IngredientDAO();
    // Specjalna lista, która automatycznie aktualizuje interfejs użytkownika
    private ObservableList<String> temporaryIngredients = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // Ładowanie składników z bazy prosto do ComboBoxa
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
                // Zamieniamy przecinek na kropkę (wygoda dla użytkownika) i parsujemy
                double quantity = Double.parseDouble(quantityStr.replace(",", "."));
                
                // Tworzymy czytelny tekst, np.: "Jajo - 2.0 x 100g"
                String displayString = selectedIngredient.getName() + " - " + quantity + " x " + selectedIngredient.getUnit();
                
                // Dodajemy do listy (widok zaktualizuje się automatycznie)
                temporaryIngredients.add(displayString);
                
                // Czyścimy formularz składnika, żeby był gotowy na kolejny wpis
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
    public void handleSave() {
        System.out.println("Kliknięto zapisz!");
        // Tu na sam koniec docelowy zapis do bazy całego przepisu
        closeWindow();
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