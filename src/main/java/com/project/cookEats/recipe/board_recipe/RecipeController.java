package com.project.cookEats.recipe.board_recipe;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/recipes")
public class RecipeController {

    @Autowired
    private RecipeService recipeService;

    @GetMapping
    public String listRecipes(Model model) {
        model.addAttribute("recipes", recipeService.getAllRecipes());
        return "recipes/list";
    }

    @GetMapping("/{id}")
    public String viewRecipe(@PathVariable Long id, Model model) {
        Optional<BoardRecipe> recipe = recipeService.getRecipeById(id);
        if (recipe.isPresent()) {
            model.addAttribute("recipe", recipe.get());
            return "recipes/view";
        }
        return "redirect:/recipes";
    }

    @GetMapping("/new")
    public String createRecipeForm(Model model) {
        model.addAttribute("recipe", new RecipeDb());
        return "recipes/create";
    }

    @PostMapping
//    public String saveRecipe(@ModelAttribute RecipeDb recipeDb,
//                             @RequestParam Long memberId) {
//        BoardRecipe boardRecipe = convertToBoardRecipe(recipeDb);
//        recipeService.saveRecipe(boardRecipe, memberId);
//        return "redirect:/recipes";
//    }

    @GetMapping("/{id}/delete")
    public String deleteRecipe(@PathVariable Long id) {
        recipeService.deleteRecipe(id);
        return "redirect:/recipes";
    }
}
