package com.project.cookEats.recipe.board_recipe;

import com.project.cookEats.member.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.format.DateTimeFormatter;
import java.util.List;


@Controller
@RequiredArgsConstructor
@RequestMapping("/boardrecipe")
public class BoardRecipeController {

    @Autowired
    private BoardRecipeService boardRecipeService;

    @Autowired
    private BoardRecipeRepository boardRecipeRepository;

    @Autowired
    private RecipeDbRepository recipeDbRepository;

    @Autowired
    private final MemberService memberService;

    @GetMapping("/recipehome")
    String home(Model model){
        List<BoardRecipe> result = boardRecipeRepository.findAll();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        for (BoardRecipe board : result) {
            if (board.getSys_date() != null) {
                board.setFormattedSysDate(board.getSys_date().format(formatter));
            } else {
                board.setFormattedSysDate(null);
            }
        }

        model.addAttribute("list", result);

        return "boardrecipe/home.html";
    }

    //특정 재료로 레시피 검색
    @GetMapping("/by-ingredient")
    public ResponseEntity<List<RecipeDb>> findByIngredientName(@RequestParam("ingredientName") String ingredientName) {
        List<RecipeDb> recipes = recipeDbRepository.findByIngredientName(ingredientName);
        return ResponseEntity.ok(recipes);
    }

}
