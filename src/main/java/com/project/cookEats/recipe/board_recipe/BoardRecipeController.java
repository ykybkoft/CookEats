package com.project.cookEats.recipe.board_recipe;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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

    @GetMapping("/board_recipe")
    String home(Model model){
        List<BoardRecipe> result = boardRecipeRepository.findAll();
        System.out.println(result);
        model.addAttribute("list", result);

        return "boardrecipe/home.html";
    }

    //특정 재료로 레시피 검색
    @GetMapping("/by-ingredient")
    public ResponseEntity<List<RecipeDb>> findByIngredientName(@RequestParam("ingredientName") String ingredientName) {
        List<RecipeDb> recipes = recipeDbRepository.findByIngredientName(ingredientName);
        return ResponseEntity.ok(recipes);
    }

    // 모든 게시글 가져오기
    @GetMapping
    public ResponseEntity<List<BoardRecipe>> getAllBoardRecipes() {
        List<BoardRecipe> boardRecipes = boardRecipeService.getAllBoardRecipes();
        return ResponseEntity.ok(boardRecipes);
    }

    // 글작성
    @GetMapping("/write")
    String board() { return "boardrecipe/write.html"; }


}
