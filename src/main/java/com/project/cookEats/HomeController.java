package com.project.cookEats;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class HomeController {
    @GetMapping("/")
    String home(){
        return "home.html";
    }

    @GetMapping("/search")
    String search(@PathVariable String search){
        System.out.println(search);
        return "search.html";
    }



}
