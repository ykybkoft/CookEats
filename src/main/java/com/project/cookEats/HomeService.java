package com.project.cookEats;

import com.project.cookEats.board_normal.BoardNormalRepository;
import com.project.cookEats.board_recipe.RecipeDB;
import com.project.cookEats.board_recipe.RecipeDBRepository;
import com.project.cookEats.board_share.repositories.Board_shareRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class HomeService {

    private final Board_shareRepository bsr;
    private final BoardNormalRepository bnr;
    private final RecipeDBRepository rdbr;
    public List<RecipeDB> bestRecipe() {

        return rdbr.findTop5ByOrderByLLIKEDesc();
    }

    public Model totalSearch(String search, Model model) {

        model.addAttribute("recipe",rdbr.findTotalSearch(search));
        model.addAttribute("normal",bnr.findTotalSearch(search));
        model.addAttribute("share",bsr.findTotalSearch(search));
        return model;
    }

    //혜정 코드 
    public RecipeDB recipeRecommend(String ingredient) {
        Optional<RecipeDB> recipe = rdbr.findRecommandRecipe(ingredient);
        return recipe.orElse(null);
    }
    //혜정 코드 
    public String[] manualList(RecipeDB recipe) {
        if(recipe != null){
            String[] manual = recipe.getManuals().toArray(new String[0]);
            return manual;
        }
        return null;
    }
}

