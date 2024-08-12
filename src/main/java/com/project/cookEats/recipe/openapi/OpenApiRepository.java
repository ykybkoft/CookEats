package com.project.cookEats.recipe.openapi;

import com.project.cookEats.recipe.board_recipe.RecipeDb;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OpenApiRepository extends JpaRepository<RecipeDb, Long> {
}