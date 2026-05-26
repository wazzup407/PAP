package pl.dobrepapu.model;

import java.util.ArrayList;
import java.util.List;

public class Recipe {
    private int id;
    private String name;
    private int portions;
    private int prepTime; 
    private String instructions;
    private int rating; 
    private String imagePath;

    private List<String> tags;

    public Recipe(int id, String name, int portions, int prepTime, String instructions, int rating, String imagePath) {
        this.id = id;
        setName(name);
        setPortions(portions);
        setPrepTime(prepTime);
        this.instructions = instructions;
        setRating(rating);
        this.imagePath = imagePath;
        
        this.tags = new ArrayList<>(); 
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public String getName() { return name; }
    public void setName(String name) { 
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Nazwa przepisu nie może być pusta.");
        }
        this.name = name; 
    }
    
    public int getPortions() { return portions; }
    public void setPortions(int portions) { 
        if (portions <= 0) {
            throw new IllegalArgumentException("Przepis musi mieć co najmniej 1 porcję.");
        }
        this.portions = portions; 
    }
    
    public int getPrepTime() { return prepTime; }
    public void setPrepTime(int prepTime) { 
        if (prepTime < 0) {
            throw new IllegalArgumentException("Czas przygotowania nie może być ujemny.");
        }
        this.prepTime = prepTime; 
    }
    
    public String getInstructions() { return instructions; }
    public void setInstructions(String instructions) { this.instructions = instructions; }
    
    public int getRating() { return rating; }
    public void setRating(int rating) { 
        if (rating < 1 || rating > 5) {
            throw new IllegalArgumentException("Ocena musi być w przedziale od 1 do 5 gwiazdek.");
        }
        this.rating = rating; 
    }
    
    public String getImagePath() { return imagePath; }
    public void setImagePath(String imagePath) { this.imagePath = imagePath; }

    
    public List<String> getTags() { return tags; }
    
    public void setTags(List<String> tags) { 
        if (tags == null) {
            this.tags = new ArrayList<>();
        } else {
            this.tags = tags; 
        }
    }
    
    public void addTag(String tag) {
        if (tag != null && !tag.trim().isEmpty() && !this.tags.contains(tag)) {
            this.tags.add(tag);
        }
    }

    @Override
    public String toString() {
        return name + " (" + rating + "⭐)"; 
    }
}