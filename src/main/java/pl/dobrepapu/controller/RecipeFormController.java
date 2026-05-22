package pl.dobrepapu.controller;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class RecipeFormController {

    @FXML private TextField nameField;
    @FXML private TextField portionsField;
    @FXML private TextField timeField;
    @FXML private TextArea instructionsArea;

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

    private void closeWindow() {
        // Pobiera aktualne okno (Stage) z dowolnego elementu i zamyka je
        Stage stage = (Stage) nameField.getScene().getWindow();
        stage.close();
    }
}