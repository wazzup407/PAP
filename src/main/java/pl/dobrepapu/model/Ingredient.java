package pl.dobrepapu.model;

public class Ingredient {
    private int id;
    private String name;
    private String unit; 
    private double calories;
    private double protein;
    private double fat;
    private double carbs;

    public Ingredient(int id, String name, String unit, double calories, double protein, double fat, double carbs) {
        this.id = id;
        setName(name);
        setUnit(unit);
        setCalories(calories);
        setProtein(protein);
        setFat(fat);
        setCarbs(carbs);
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public String getName() { return name; }
    public void setName(String name) { 
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Nazwa składnika nie może być pusta.");
        }
        this.name = name; 
    }
    
    public String getUnit() { return unit; }
    public void setUnit(String unit) { 
        if (unit == null || unit.trim().isEmpty()) {
            throw new IllegalArgumentException("Jednostka nie może być pusta.");
        }
        this.unit = unit; 
    }
    
    public double getCalories() { return calories; }
    public void setCalories(double calories) { 
        if (calories < 0) throw new IllegalArgumentException("Kalorie nie mogą być ujemne.");
        this.calories = calories; 
    }
    
    public double getProtein() { return protein; }
    public void setProtein(double protein) { 
        if (protein < 0) throw new IllegalArgumentException("Białko nie może być ujemne.");
        this.protein = protein; 
    }
    
    public double getFat() { return fat; }
    public void setFat(double fat) { 
        if (fat < 0) throw new IllegalArgumentException("Tłuszcze nie mogą być ujemne.");
        this.fat = fat; 
    }
    
    public double getCarbs() { return carbs; }
    public void setCarbs(double carbs) { 
        if (carbs < 0) throw new IllegalArgumentException("Węglowodany nie mogą być ujemne.");
        this.carbs = carbs; 
    }

    @Override
    public String toString() {
        return name;
    }
}