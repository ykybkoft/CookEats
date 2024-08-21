package com.project.cookEats;

import com.project.cookEats.recipe.board_recipe.RecipeDb;
import com.project.cookEats.recipe.board_recipe.RecipeDbRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class HomeService {

    private final RecipeDbRepository rdbr;
    public List<RecipeDb> bestRecipe() {

        return rdbr.findTop5ByOrderByLLIKEDesc();
    }
}
