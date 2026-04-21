package pl.dobrepapu.model;

public class RecipeIngredient {
    private int recipeId;
    private int ingredientId;
    private double quantity; // ilość jednostek (np. 2 * 100g)

    public RecipeIngredient(int recipeId, int ingredientId, double quantity) {
        this.recipeId = recipeId;
        this.ingredientId = ingredientId;
        this.quantity = quantity;
    }

    public int getRecipeId() { return recipeId; }
    public void setRecipeId(int recipeId) { this.recipeId = recipeId; }
    public int getIngredientId() { return ingredientId; }
    public void setIngredientId(int ingredientId) { this.ingredientId = ingredientId; }
    public double getQuantity() { return quantity; }
    public void setQuantity(double quantity) { this.quantity = quantity; }
}
