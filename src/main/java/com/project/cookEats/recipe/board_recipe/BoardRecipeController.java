package com.project.cookEats.recipe.board_recipe;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/boardrecipe")
public class BoardRecipeController {

    @Autowired
    private BoardRecipeService boardRecipeService;

    // 모든 게시글 가져오기
    @GetMapping
    public ResponseEntity<List<BoardRecipe>> getAllBoardRecipes() {
        List<BoardRecipe> boardRecipes = boardRecipeService.getAllBoardRecipes();
        return ResponseEntity.ok(boardRecipes);
    }

    // 게시글 ID로 조회
    @GetMapping("/{id}")
    public ResponseEntity<BoardRecipe> getBoardRecipeById(@PathVariable Long id) {
        BoardRecipe boardRecipe = boardRecipeService.getBoardRecipeById(id);
        if (boardRecipe != null) {
            return ResponseEntity.ok(boardRecipe);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // 새로운 게시글 생성
    @PostMapping
    public ResponseEntity<BoardRecipe> createBoardRecipe(@RequestBody BoardRecipe boardRecipe) {
        BoardRecipe createdRecipe = boardRecipeService.createBoardRecipe(boardRecipe);
        return ResponseEntity.ok(createdRecipe);
    }

    // 게시글 업데이트
    @PutMapping("/{id}")
    public ResponseEntity<BoardRecipe> updateBoardRecipe(@PathVariable Long id, @RequestBody BoardRecipe boardRecipe) {
        BoardRecipe updatedRecipe = boardRecipeService.updateBoardRecipe(id, boardRecipe);
        if (updatedRecipe != null) {
            return ResponseEntity.ok(updatedRecipe);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // 게시글 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBoardRecipe(@PathVariable Long id) {
        boardRecipeService.deleteBoardRecipe(id);
        return ResponseEntity.noContent().build();
    }
}
