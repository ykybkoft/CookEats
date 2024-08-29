package com.project.cookEats.board_recipe;


import com.project.cookEats.board_normal.BoardNormal;
import com.project.cookEats.member.CustomUser;
import com.project.cookEats.member.MemberRepository;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RecipeService {


    private final RecipeDBRepository recipeDBRepository;
    private final MemberRepository memberRepository;
    private final RecipeCommentRepository recipeCommentRepository;

    // 모든 게시글을 반환, Paging
    public Page<RecipeDB> findAll(Pageable pageable, String search, String searchType, String sortType) {

        if(search ==null && sortType == null){
            return recipeDBRepository.findAll(pageable);
        }

        return recipeDBRepository.findAll(pageable);

    }

    public Page<RecipeDB> findByIngredientName(String ingredientName, Pageable pageable) {
        return recipeDBRepository.findByIngredientName(ingredientName, pageable);
    }

    public long getTotalItems() {
        return recipeDBRepository.count();

    }

//    public List<RecipeDB> searchRecipes(String keyword, String sortBy) {
//        return switch (sortBy) {
//            // 제목에 키워드가 포함된 게시글을 추천수 기준으로 내림차순 정렬
//            case "likes" -> recipeDBRepository.findByTitleContainingOrderByLikesDesc(keyword);
//            // 제목에 키워드가 포함된 게시글을 작성일 기준으로 내림차순 정렬
//            case "date" -> recipeDBRepository.findByTitleContainingOrderBySysDateDesc(keyword);
//            // 제목에 키워드가 포함된 게시글을 조회수 기준으로 내림차순 정렬
//            case "count" -> recipeDBRepository.findByKeywordOrderByCountDesc(keyword);
//            default -> recipeDBRepository.findByTitleContainingOrderBySysDateDesc(keyword);
//        };
//    }
    public RecipeDB getRecipeById(Long id) {
        return recipeDBRepository.findById(id).orElse(null);
    }

    public void saveRecipe(RecipeDB recipe) {
        recipeDBRepository.save(recipe);
    }

    public void deleteRecipe(Long id) {
        recipeDBRepository.deleteById(id);
    }


    //좋아요 증가 - 혜정 코드
    public void upLike(Long id) {
        RecipeDB recipe = recipeDBRepository.findById(id).get();
        recipe.setLLIKE(recipe.getLLIKE()+1);
        recipeDBRepository.save(recipe);
    }


    //레시피 저장 - 혜정 코드
    public int write(RecipeDB recipe, Authentication auth) {
        CustomUser user = (CustomUser) auth.getPrincipal();
        String tmp = "";
        String[] manual = recipe.getMANUAL().split("\n");
        for (int i = 0; i <manual.length ; i++) {
            tmp += (manual[i]+"%<");
        }
        recipe.setMANUAL(tmp);
        recipe.setMember(memberRepository.findById(user.getId()).get());
        recipeDBRepository.save(recipe);
        return 1;
    }

    //조회수 증가 - 혜정 코드
    public void viewCount(Long id) {
        RecipeDB recipe = recipeDBRepository.findById(id).get();
        recipe.setCCOUNT(recipe.getCCOUNT()+1);
        recipeDBRepository.save(recipe);
    }

    //혜정 코드
    public int saveComment(RecipeComment comment) {
        recipeCommentRepository.save(comment);
        return 1;
    }
  
    //혜정 코드
    public List<RecipeComment> commentList(Long id) {

        return recipeCommentRepository.findAllByRecipeDB(recipeDBRepository.findById(id).get());
    }
  
    //혜정 코드
    public RecipeComment upCommentLike(Long id) {
        RecipeComment comment = recipeCommentRepository.findById(id).get();
        comment.setLLIKE(comment.getLLIKE()+1);
        recipeCommentRepository.save(comment);
        return comment;
    }
  
     //혜정 코드
    public int commentDelete(Long id) {
        recipeCommentRepository.deleteById(id);
        return 1;
    }
  
    //혜정 코드
    public void updateComment(Long id, String content) {
        RecipeComment comment = recipeCommentRepository.findById(id).get();
        comment.setComment_contents(content);
        recipeCommentRepository.save(comment);  // 변경된 내용 저장
    }

}
