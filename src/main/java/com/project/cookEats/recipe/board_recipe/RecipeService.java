package com.project.cookEats.recipe.board_recipe;

import com.project.cookEats.member.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RecipeService {

    @Autowired
    private BoardRecipeRepository boardRecipeRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private RecipeDbRepository recipeDbRepository;

    public List<RecipeDb> getRecipesByIngredient(String ingredientName) {
        return recipeDbRepository.findByIngredientName(ingredientName);
    }
}
