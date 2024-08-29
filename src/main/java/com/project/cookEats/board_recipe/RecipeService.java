package com.project.cookEats.board_recipe;


import com.project.cookEats.board_normal.BoardNormal;
import com.project.cookEats.member.CustomUser;
import com.project.cookEats.member.MemberRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RecipeService {
    //혜정 코드
    @PersistenceContext
    private EntityManager entityManager;


    private final RecipeDBRepository recipeDBRepository;
    private final MemberRepository memberRepository;
    private final RecipeCommentRepository recipeCommentRepository;


    //지훈 코드 -> 혜정 수정
    public Page<RecipeDB> findAll(int page,String search,String sortType) {


        int pageSize = 15; // 한 페이지에 표시할 레시피 수
        Pageable pageable = PageRequest.of(page - 1, pageSize, Sort.by(Sort.Direction.DESC, "LLIKE"));


        if(sortType!= null){
            pageable = PageRequest.of(page-1, pageSize, Sort.by(Sort.Direction.DESC, sortType));
            if(search == null || search.equals("")){

                return recipeDBRepository.findAll(pageable);
            }else{
                return recipeDBRepository.findAllSearch(search,pageable);
            }
        }else{
            if(search== null || search.equals("")){
                return recipeDBRepository.findAll(pageable);
            }else{
                return recipeDBRepository.findAllSearch(search,pageable);
            }

        }


    }


    public long getTotalItems() {
        return recipeDBRepository.count();

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
