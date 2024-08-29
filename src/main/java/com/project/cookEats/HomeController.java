package com.project.cookEats;


import com.project.cookEats.board_recipe.RecipeDB;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Controller
public class HomeController {

    private final HomeService hs;
    @GetMapping("/")
    String home(Model model)
    {

        model.addAttribute("bestrecipe", hs.bestRecipe());
        return "home.html";
    }

    @GetMapping("/search")
    String search(@RequestParam String search, Model model){
        model = hs.totalSearch(search,model);

        return "search.html";
    }

    @GetMapping("/recommend")
    String recommend(Model model){
        model.addAttribute("recipe",null);
        return "recommend.html";
    }

    @PostMapping("/recommend")
    String recipeRecommend(@RequestParam String ingredient, Model model){
        RecipeDB recipe = hs.recipeRecommend(ingredient);
        model.addAttribute("recipe",recipe);
        model.addAttribute("reload", "yes");
        model.addAttribute("ingredient_reload",ingredient);
        model.addAttribute("manual", hs.manualList(recipe));

        return "recommend.html";
    }





}
