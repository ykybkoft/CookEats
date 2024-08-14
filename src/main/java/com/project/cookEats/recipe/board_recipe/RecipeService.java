package com.project.cookEats.recipe.board_recipe;

import com.project.cookEats.member.Member;
import com.project.cookEats.member.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RecipeService {

    @Autowired
    private BoardRecipeRepository boardRecipeRepository;

    @Autowired
    private MemberRepository memberRepository;

    // 모든 레시피를 조회
    public List<BoardRecipe> getAllRecipes() {
        return boardRecipeRepository.findAll();
    }

    // 특정 ID를 가진 레시피를 조회
    public Optional<BoardRecipe> getRecipeById(Long id) {
        return boardRecipeRepository.findById(id);
    }

    // 새로운 레시피를 저장
    public BoardRecipe saveRecipe(BoardRecipe boardRecipe, Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("Member not found"));

        boardRecipe.setMember(member);

        return boardRecipeRepository.save(boardRecipe);
    }

    // 특정 ID를 가진 레시피를 삭제
    public void deleteRecipe(Long id) {
        boardRecipeRepository.deleteById(id);
    }
}
