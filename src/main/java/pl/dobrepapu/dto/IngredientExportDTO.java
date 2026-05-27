package pl.dobrepapu.dto;

public class IngredientExportDTO {
    private String name;
    private String unit;
    private double calories;
    private double protein;
    private double fat;
    private double carbs;
    private double quantity;

    public IngredientExportDTO() {}

    public IngredientExportDTO(String name, String unit, double calories, double protein, double fat, double carbs, double quantity) {
        this.name = name;
        this.unit = unit;
        this.calories = calories;
        this.protein = protein;
        this.fat = fat;
        this.carbs = carbs;
        this.quantity = quantity;
    }

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
    public double getQuantity() { return quantity; }
    public void setQuantity(double quantity) { this.quantity = quantity; }
}