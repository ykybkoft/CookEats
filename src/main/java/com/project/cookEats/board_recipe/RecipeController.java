package com.project.cookEats.board_recipe;

import com.project.cookEats.member.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.format.DateTimeFormatter;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/boardrecipe")
public class RecipeController {

    @Autowired
    private RecipeService recipeService;
    @Autowired
    private final MemberService memberService;

    @GetMapping("/home")
    String home(Model model){
        List<RecipeDB> result = recipeService.findAll();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        for (RecipeDB board : result) {
            if (board.getSYSDATE() != null) {
                board.setFormattedSysDate(board.getSYSDATE().format(formatter));
            } else {
                board.setFormattedSysDate(null);
            }
        }
        model.addAttribute("list", result);
        return "boardrecipe/home"; // home.html
    }

}
