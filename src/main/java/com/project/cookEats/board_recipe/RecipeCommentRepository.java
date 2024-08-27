package com.project.cookEats.board_recipe;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RecipeCommentRepository extends JpaRepository<RecipeComment,Long> {
    List<RecipeComment> findAllByRecipeDB(RecipeDB recipeDB);
}
