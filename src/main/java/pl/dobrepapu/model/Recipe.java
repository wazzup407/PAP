package pl.dobrepapu.model;

public class Recipe {
    private int id;
    private String name;
    private int portions;
    private int prepTime; // w minutach
    private String instructions;
    private int rating; // 1-5 gwiazdek
    private String imagePath;

    public Recipe(int id, String name, int portions, int prepTime, String instructions, int rating, String imagePath) {
        this.id = id;
        this.name = name;
        this.portions = portions;
        this.prepTime = prepTime;
        this.instructions = instructions;
        this.rating = rating;
        this.imagePath = imagePath;
    }

    // Gettery i Settery
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public int getPortions() { return portions; }
    public void setPortions(int portions) { this.portions = portions; }
    public int getPrepTime() { return prepTime; }
    public void setPrepTime(int prepTime) { this.prepTime = prepTime; }
    public String getInstructions() { return instructions; }
    public void setInstructions(String instructions) { this.instructions = instructions; }
    public int getRating() { return rating; }
    public void setRating(int rating) { this.rating = rating; }
    public String getImagePath() { return imagePath; }
    public void setImagePath(String imagePath) { this.imagePath = imagePath; }

    @Override
    public String toString() {
        return name + " (" + rating + "⭐)"; // Wyświetlane na liście w interfejsie
    }
}
