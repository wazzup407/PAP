package pl.dobrepapu.service;

import org.junit.jupiter.api.Test;
import pl.dobrepapu.model.Ingredient;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class NutritionServiceTest {

    @Test
    public void testCalculateMacros() {
        // 1. Przygotowanie danych (Arrange)
        NutritionService service = new NutritionService();
        
        // Tworzymy sztuczne składniki (zakładamy, że podajemy wartości odżywcze dla jednej jednostki, np. 100g)
        Ingredient chicken = new Ingredient(1, "Pierś z kurczaka", "100g", 165, 31, 3.6, 0);
        Ingredient rice = new Ingredient(2, "Ryż biały", "100g", 130, 2.7, 0.3, 28);

        // Dodajemy je do mapy: 2 "jednostki" kurczaka (200g) i 0.5 "jednostki" ryżu (50g)
        Map<Ingredient, Double> recipeIngredients = new HashMap<>();
        recipeIngredients.put(chicken, 2.0);
        recipeIngredients.put(rice, 0.5);

        // 2. Działanie (Act)
        NutritionService.MacroSummary result = service.calculateMacros(recipeIngredients);

        // 3. Sprawdzenie wyników (Assert)
        // Matematyka dla kurczaka (x2): 330 kcal, 62g białka, 7.2g tłuszczu, 0g węgli
        // Matematyka dla ryżu (x0.5): 65 kcal, 1.35g białka, 0.15g tłuszczu, 14g węgli
        // OCZEKIWANA SUMA: 395 kcal | 63.35g białka | 7.35g tłuszczu | 14g węgli
        
        // Parametr 0.01 na końcu to dopuszczalny margines błędu dla ułamków (tzw. delta)
        assertEquals(395.0, result.getTotalCalories(), 0.01, "Błąd w obliczeniach kalorii!");
        assertEquals(63.35, result.getTotalProtein(), 0.01, "Błąd w obliczeniach białka!");
        assertEquals(7.35, result.getTotalFat(), 0.01, "Błąd w obliczeniach tłuszczu!");
        assertEquals(14.0, result.getTotalCarbs(), 0.01, "Błąd w obliczeniach węglowodanów!");
    }
}