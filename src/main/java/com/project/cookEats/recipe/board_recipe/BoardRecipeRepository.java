package com.project.cookEats.recipe.board_recipe;

import com.project.cookEats.board_normal.BoardNormal;
import com.project.cookEats.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BoardRecipeRepository extends JpaRepository<BoardRecipe, Long> {

    List<BoardNormal> findAllByMember(Member member);

}
