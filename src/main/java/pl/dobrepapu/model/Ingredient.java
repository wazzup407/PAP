package pl.dobrepapu.model;

public class Ingredient {
    private int id;
    private String name;
    private String unit; // np. 100g, 100ml, 1 sztuka
    private double calories;
    private double protein;
    private double fat;
    private double carbs;

    public Ingredient(int id, String name, String unit, double calories, double protein, double fat, double carbs) {
        this.id = id;
        this.name = name;
        this.unit = unit;
        this.calories = calories;
        this.protein = protein;
        this.fat = fat;
        this.carbs = carbs;
    }

    // Gettery i Settery
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getUnit() { return unit; }
    public void setUnit(String unit) { this.unit = unit; }
    public double getCalories() { return calories; }
    public void setCalories(double calories) { this.calories = calories; }
    public double getProtein() { return protein; }
    public void setProtein(double protein) { this.protein = protein; }
    public double getFat() { return fat; }
    public void setFat(double fat) { this.fat = fat; }
    public double getCarbs() { return carbs; }
    public void setCarbs(double carbs) { this.carbs = carbs; }

    @Override
    public String toString() {
        return name; // Wyświetlane na liście w interfejsie
    }
}
