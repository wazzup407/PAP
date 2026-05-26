package pl.dobrepapu.service;

import pl.dobrepapu.model.Ingredient;
import java.util.Map;

public class NutritionService {

    /**
     * Wewnętrzna klasa pełniąca rolę "raportu" podsumowującego całe danie.
     */
    public static class MacroSummary {
        private final double totalCalories;
        private final double totalProtein;
        private final double totalFat;
        private final double totalCarbs;

        public MacroSummary(double calories, double protein, double fat, double carbs) {
            this.totalCalories = calories;
            this.totalProtein = protein;
            this.totalFat = fat;
            this.totalCarbs = carbs;
        }

        public double getTotalCalories() { return totalCalories; }
        public double getTotalProtein() { return totalProtein; }
        public double getTotalFat() { return totalFat; }
        public double getTotalCarbs() { return totalCarbs; }
    }

    /**
     * Główna metoda licząca makroskładniki.
     * Używamy Mapy, gdzie kluczem jest fizyczny Składnik (Ingredient), 
     * a wartością (Double) jest jego mnożnik ilości z przepisu (np. 2 porcje po 100g).
     */
    public MacroSummary calculateMacros(Map<Ingredient, Double> ingredientsWithQuantities) {
        double totalCalories = 0;
        double totalProtein = 0;
        double totalFat = 0;
        double totalCarbs = 0;

        for (Map.Entry<Ingredient, Double> entry : ingredientsWithQuantities.entrySet()) {
            Ingredient ingredient = entry.getKey();
            Double quantityMultiplier = entry.getValue(); // Ilość zadeklarowana w przepisie

            totalCalories += ingredient.getCalories() * quantityMultiplier;
            totalProtein += ingredient.getProtein() * quantityMultiplier;
            totalFat += ingredient.getFat() * quantityMultiplier;
            totalCarbs += ingredient.getCarbs() * quantityMultiplier;
        }

        return new MacroSummary(totalCalories, totalProtein, totalFat, totalCarbs);
    }
}