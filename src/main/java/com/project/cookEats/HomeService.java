package com.project.cookEats;

import com.project.cookEats.board_recipe.RecipeDB;
import com.project.cookEats.board_recipe.RecipeDBRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class HomeService {

    private final RecipeDBRepository rdbr;
    public List<RecipeDB> bestRecipe() {

        return rdbr.findTop5ByOrderByLLIKEDesc();
    }
}
