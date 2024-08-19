package com.project.cookEats.recipe.board_recipe;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BoardRecipeService {

    @Autowired
    private BoardRecipeRepository boardRecipeRepository;

    @Autowired
    private RecipeDbRepository recipeDbRepository;

    // 모든 게시글 가져오기
    public List<BoardRecipe> getAllBoardRecipes() {
        return boardRecipeRepository.findAll();
    }

    // 게시글 ID로 조회
    public BoardRecipe getBoardRecipeById(Long id) {
        return boardRecipeRepository.findById(id).orElse(null);
    }

    // 새로운 게시글 생성
    public BoardRecipe createBoardRecipe(BoardRecipe boardRecipe) {
        // RecipeDb에서 데이터를 가져와서 연결
        if (boardRecipe.getRecipeDb() != null && boardRecipe.getRecipeDb().getId() != null) {
            RecipeDb recipeDb = recipeDbRepository.findById(boardRecipe.getRecipeDb().getId())
                    .orElseThrow(() -> new RuntimeException("RecipeDb not found"));
            boardRecipe.setRecipeDb(recipeDb);
        }
        return boardRecipeRepository.save(boardRecipe);
    }

    // 게시글 업데이트
    public BoardRecipe updateBoardRecipe(Long id, BoardRecipe boardRecipe) {
        BoardRecipe existingRecipe = boardRecipeRepository.findById(id).orElse(null);
        if (existingRecipe != null) {
            existingRecipe.setTitle(boardRecipe.getTitle());
            existingRecipe.setContent(boardRecipe.getContent());
            // 다른 필드들도 필요에 따라 업데이트

            // RecipeDb 연결 업데이트
            if (boardRecipe.getRecipeDb() != null && boardRecipe.getRecipeDb().getId() != null) {
                RecipeDb recipeDb = recipeDbRepository.findById(boardRecipe.getRecipeDb().getId())
                        .orElseThrow(() -> new RuntimeException("RecipeDb not found"));
                existingRecipe.setRecipeDb(recipeDb);
            }

            return boardRecipeRepository.save(existingRecipe);
        } else {
            return null;
        }
    }

    // 게시글 삭제
    public void deleteBoardRecipe(Long id) {
        boardRecipeRepository.deleteById(id);
    }
}
