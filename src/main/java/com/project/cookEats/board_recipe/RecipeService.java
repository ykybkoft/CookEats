package com.project.cookEats.board_recipe;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RecipeService {

    @Autowired
    private RecipeDBRepository recipeDBRepository;

    }

    }

    public List<RecipeDB> searchRecipes(String keyword, String sortBy) {
        return switch (sortBy) {
            // 제목에 키워드가 포함된 게시글을 추천수 기준으로 내림차순 정렬
            case "likes" -> recipeDBRepository.findByTitleContainingOrderByLikesDesc(keyword);
            // 제목에 키워드가 포함된 게시글을 작성일 기준으로 내림차순 정렬
            case "date" -> recipeDBRepository.findByTitleContainingOrderBySysDateDesc(keyword);
            // 제목에 키워드가 포함된 게시글을 조회수 기준으로 내림차순 정렬
            case "count" -> recipeDBRepository.findByKeywordOrderByCountDesc(keyword);
            default -> recipeDBRepository.findByTitleContainingOrderBySysDateDesc(keyword);
        };
    }
    public RecipeDB getRecipeById(Long id) {
        return recipeDBRepository.findById(id).orElse(null);
    }

    public void saveRecipe(RecipeDB recipe) {
        recipeDBRepository.save(recipe);
    }

    public void deleteRecipe(Long id) {
        recipeDBRepository.deleteById(id);
    }


}
