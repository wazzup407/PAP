package pl.dobrepapu;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import pl.dobrepapu.db.DatabaseManager;

import java.io.File;

public class App extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        // Inicjalizacja folderu na zdjęcia
        File imgDir = new File("images");
        if (!imgDir.exists()) {
            imgDir.mkdir();
        }

        // Inicjalizacja bazy danych i tabel
        DatabaseManager.initDB();

        // Wczytanie widoku
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("/pl/dobrepapu/fxml/Main.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1000, 650);
        
        stage.setTitle("DobrePAPu - Menedżer Przepisów");
        stage.setScene(scene);
        stage.setMinWidth(800);
        stage.setMinHeight(550);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
