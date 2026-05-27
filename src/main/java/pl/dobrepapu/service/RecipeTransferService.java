package pl.dobrepapu.service;

import pl.dobrepapu.dao.IngredientDAO;
import pl.dobrepapu.dao.RecipeDAO;
import pl.dobrepapu.dao.RecipeIngredientDAO;
import pl.dobrepapu.dto.IngredientExportDTO;
import pl.dobrepapu.dto.RecipeExportDTO;
import pl.dobrepapu.model.Ingredient;
import pl.dobrepapu.model.Recipe;
import pl.dobrepapu.model.RecipeIngredient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RecipeTransferService {

    private final JsonExportService jsonExportService = new JsonExportService();
    private final RecipeDAO recipeDAO = new RecipeDAO();
    private final IngredientDAO ingredientDAO = new IngredientDAO();
    private final RecipeIngredientDAO recipeIngredientDAO = new RecipeIngredientDAO();

    public void exportRecipe(int recipeId, String filePath) throws IOException {
        Recipe recipe = recipeDAO.getRecipeById(recipeId);
        if (recipe == null) {
            throw new IllegalArgumentException("Nie znaleziono przepisu o podanym ID.");
        }

        RecipeExportDTO dto = new RecipeExportDTO();
        dto.setName(recipe.getName());
        dto.setPortions(recipe.getPortions());
        dto.setPrepTime(recipe.getPrepTime());
        dto.setInstructions(recipe.getInstructions());
        dto.setRating(recipe.getRating());
        dto.setTags(recipe.getTags());

        List<RecipeIngredient> recipeIngredients = recipeIngredientDAO.getIngredientsForRecipe(recipeId);
        List<IngredientExportDTO> ingredientsDtoList = new ArrayList<>();

        for (RecipeIngredient ri : recipeIngredients) {
            Ingredient ing = ri.getIngredient();
            ingredientsDtoList.add(new IngredientExportDTO(
                    ing.getName(),
                    ing.getUnit(),
                    ing.getCalories(),
                    ing.getProtein(),
                    ing.getFat(),
                    ing.getCarbs(),
                    ri.getQuantity()
            ));
        }
        dto.setIngredients(ingredientsDtoList);

        jsonExportService.exportRecipeToFile(dto, filePath);
    }

    public void importRecipe(String filePath) throws IOException {
        RecipeExportDTO dto = jsonExportService.importRecipeFromFile(filePath);

        Recipe newRecipe = new Recipe(
                0, dto.getName(), dto.getPortions(), dto.getPrepTime(), 
                dto.getInstructions(), dto.getRating(), ""
        );
        newRecipe.setTags(dto.getTags());
        
        int newRecipeId = recipeDAO.addRecipe(newRecipe);

        if (dto.getIngredients() != null) {
            for (IngredientExportDTO ingDto : dto.getIngredients()) {

                Ingredient existingIngredient = ingredientDAO.getIngredientByName(ingDto.getName());
                int ingredientId;

                if (existingIngredient != null) {
                    ingredientId = existingIngredient.getId();
                } else {
                    Ingredient newIng = new Ingredient(
                            0, ingDto.getName(), ingDto.getUnit(), ingDto.getCalories(),
                            ingDto.getProtein(), ingDto.getFat(), ingDto.getCarbs()
                    );
                    ingredientId = ingredientDAO.addIngredient(newIng);
                }

                RecipeIngredient newRelation = new RecipeIngredient(newRecipeId, ingredientId, ingDto.getQuantity());
                recipeIngredientDAO.addRecipeIngredient(newRelation);
            }
        }
    }
}
