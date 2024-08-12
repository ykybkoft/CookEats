package com.project.cookEats.recipe.openapi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OpenApiController {

    @Autowired
    private OpenApiService service;

    @GetMapping("/fetchData")
    public String fetchData() {
        service.fetchData();
        return "Data fetching initiated.";
    }
}