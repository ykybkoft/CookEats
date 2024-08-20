package com.project.cookEats.recipe.board_recipe;

import com.project.cookEats.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


public interface RecipeDbRepository extends JpaRepository<RecipeDb, Long> {

    //혜정
    List<RecipeDb> findTop5ByOrderByLLIKEDesc();
    List<RecipeDb> findAllByMember(Member member);
}