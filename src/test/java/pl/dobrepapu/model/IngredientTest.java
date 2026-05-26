package pl.dobrepapu.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class IngredientTest {

    @Test
    public void testNegativeCaloriesThrowException() {
        // Używamy assertThrows, by sprawdzić, czy stworzenie składnika z ujemnymi kaloriami (-50) wyrzuci błąd
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            new Ingredient(1, "Zły Składnik", "100g", -50, 10, 5, 2);
        });

        // Opcjonalnie: sprawdzamy, czy treść błędu jest dokładnie taka, jaką zaprogramowaliśmy
        assertEquals("Kalorie nie mogą być ujemne.", exception.getMessage());
    }

    @Test
    public void testEmptyNameThrowsException() {
        // Sprawdzamy, czy pusta nazwa zostanie zablokowana
        assertThrows(IllegalArgumentException.class, () -> {
            new Ingredient(2, "", "100g", 100, 10, 5, 2);
        });
    }
}