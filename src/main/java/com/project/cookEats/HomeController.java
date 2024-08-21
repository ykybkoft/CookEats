package com.project.cookEats;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
        return "search.html";
    }





}
