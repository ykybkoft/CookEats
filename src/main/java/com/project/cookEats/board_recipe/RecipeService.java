package com.project.cookEats.board_recipe;


import com.project.cookEats.member.CustomUser;
import com.project.cookEats.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RecipeService {

    private final RecipeDBRepository recipeDBRepository;
    private final MemberRepository memberRepository;
    private final RecipeCommentRepository recipeCommentRepository;
    private final RecipeDBSubInfoRepository recipeDBSubInfoRepository;

     // 모든 게시글을 반환, Paging
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

    //혜정 코드, 좋아요 증가
    public void upLike(Long id) {
        RecipeDB recipe = recipeDBRepository.findById(id).get();
        recipe.setLLIKE(recipe.getLLIKE()+1);
        recipeDBRepository.save(recipe);
    }


    //레시피 저장
    public int write(RecipeDB recipe, Authentication auth) {
        CustomUser user = (CustomUser) auth.getPrincipal();
        recipe.setMember(memberRepository.findById(user.getId()).orElseThrow(() -> new RuntimeException("User not found")));
        recipeDBRepository.save(recipe);

        return 1;
    }

    //혜정 코드, 조회수 증가
    public void viewCount(Long id) {
        RecipeDB recipe = recipeDBRepository.findById(id).get();
        recipe.setCCOUNT(recipe.getCCOUNT()+1);
        recipeDBRepository.save(recipe);
    }

    //혜정 코드, 댓글 저장
    public int saveComment(RecipeComment comment) {
        recipeCommentRepository.save(comment);
        return 1;
    }
  
    //혜정 코드, 댓글 목록 조회
    public List<RecipeComment> commentList(Long id) {

        return recipeCommentRepository.findAllByRecipeDB(recipeDBRepository.findById(id).get());
    }
  
    //혜정 코드, 댓글 좋아요 증가
    public RecipeComment upCommentLike(Long id) {
        RecipeComment comment = recipeCommentRepository.findById(id).get();
        comment.setLLIKE(comment.getLLIKE()+1);
        recipeCommentRepository.save(comment);
        return comment;
    }
  
     //혜정 코드, 댓글 삭제
    public int commentDelete(Long id) {
        recipeCommentRepository.deleteById(id);
        return 1;
    }
  
    //혜정 코드, 댓글 내용 업데이트
    public void updateComment(Long id, String content) {
        RecipeComment comment = recipeCommentRepository.findById(id).get();
        comment.setComment_contents(content);
        recipeCommentRepository.save(comment);  // 변경된 내용 저장
    }

    //혜정 코드
    public Model getNutrition(Model model, Long id) {
        Optional<RecipeDBSubInfo> recipeInfo = recipeDBSubInfoRepository.findById(id);
        if(recipeInfo.isEmpty()){
            return model;
        }
        model.addAttribute("car", recipeInfo.get().getINFO_CAR());
        model.addAttribute("fat", recipeInfo.get().getINFO_FAT());
        model.addAttribute("pro", recipeInfo.get().getINFO_PRO());

        return model;
    }

}
