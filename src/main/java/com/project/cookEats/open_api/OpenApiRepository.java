package com.project.cookEats.open_api;

import com.project.cookEats.board_recipe.RecipeDB;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OpenApiRepository extends JpaRepository<RecipeDB, Long> {
}