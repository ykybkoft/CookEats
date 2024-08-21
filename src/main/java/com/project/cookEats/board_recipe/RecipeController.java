package com.project.cookEats.board_recipe;

import com.project.cookEats.member.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class RecipeController {

    private final RecipeService recipeService;
    private final MemberService memberService;

    @GetMapping("/home")
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            if (board.getSYSDATE() != null) {
                board.setFormattedSysDate(board.getSYSDATE().format(formatter));
            } else {
                board.setFormattedSysDate(null);
            }
        }
    }

    @GetMapping("/recipe/{id}")
    public String getRecipeDetail(@PathVariable("id") Long id, Model model) {
        RecipeDB recipe = recipeService.getRecipeById(id);
        if (recipe != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String formattedDate = recipe.getSYSDATE() != null ? recipe.getSYSDATE().format(formatter) : "";
            model.addAttribute("recipe", recipe);
            model.addAttribute("formattedDate", formattedDate);

        } else {
            model.addAttribute("errorMessage", "게시글을 찾을 수 없습니다.");
            return "error";
        }
    }


}
