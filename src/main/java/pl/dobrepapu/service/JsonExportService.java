package pl.dobrepapu.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import pl.dobrepapu.dto.RecipeExportDTO;

import java.io.File;
import java.io.IOException;

public class JsonExportService {

    private final ObjectMapper mapper;

    public JsonExportService() {
        this.mapper = new ObjectMapper();
        this.mapper.enable(SerializationFeature.INDENT_OUTPUT);
    }

    public void exportRecipeToFile(RecipeExportDTO recipeData, String filePath) throws IOException {
        File file = new File(filePath);
        mapper.writeValue(file, recipeData);
    }

    public RecipeExportDTO importRecipeFromFile(String filePath) throws IOException {
        File file = new File(filePath);
        return mapper.readValue(file, RecipeExportDTO.class);
    }
}