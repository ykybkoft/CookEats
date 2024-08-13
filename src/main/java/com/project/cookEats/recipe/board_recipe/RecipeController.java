package com.project.cookEats.recipe.board_recipe;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class RecipeController {

    @Autowired
    private RecipeDbRepository recipeDbRepository;

    public List<RecipeDb> findByIngredientName(String ingredientName) {
        return recipeDbRepository.findByIngredientName(ingredientName);
    }
}
