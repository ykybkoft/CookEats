package com.project.cookEats.common_module.open_api;

import com.project.cookEats.board_recipe.RecipeDB;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OpenApiRepository extends JpaRepository<RecipeDB, Long> {
}