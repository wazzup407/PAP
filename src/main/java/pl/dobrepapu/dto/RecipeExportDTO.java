package pl.dobrepapu.dto;

import java.util.List;

public class RecipeExportDTO {
    private String name;
    private int portions;
    private int prepTime;
    private String instructions;
    private int rating;
    private List<String> tags;
    private List<IngredientExportDTO> ingredients;

    public RecipeExportDTO() {}

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
    public List<String> getTags() { return tags; }
    public void setTags(List<String> tags) { this.tags = tags; }
    public List<IngredientExportDTO> getIngredients() { return ingredients; }
    public void setIngredients(List<IngredientExportDTO> ingredients) { this.ingredients = ingredients; }
}
