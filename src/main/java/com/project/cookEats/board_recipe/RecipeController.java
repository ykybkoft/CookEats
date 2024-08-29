package com.project.cookEats.board_recipe;

import com.project.cookEats.member.Member;
import com.project.cookEats.member.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.time.format.DateTimeFormatter;
import java.util.List;


@Controller
@RequiredArgsConstructor
@RequestMapping("/boardRecipe")
public class RecipeController {

    private final RecipeService recipeService;
    private final MemberService memberService;
    private final RecipeCommentRepository recipeCommentRepository;

    @GetMapping("/home")
    public String home(@RequestParam(value = "page", defaultValue = "1") int page, Model model, @RequestParam(required = false) String searchType, @RequestParam(required = false) String search, @RequestParam(required = false) String sortType) {

        int pageSize = 15; // 한 페이지에 표시할 레시피 수
        Pageable pageable = PageRequest.of(page - 1, pageSize);
        Page<RecipeDB> resultPage = recipeService.findAll(pageable, search, searchType , sortType);

        // 총 페이지 수 계산
        int totalPages = resultPage.getTotalPages();

        // 페이지 블록의 시작과 끝을 계산
        int startPage = (page - 1) / 10 * 10 + 1;
        int endPage = Math.min(startPage + 9, totalPages);

        // 날짜 형식 설정
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        for (RecipeDB board : resultPage) {

            if (board.getSYSDATE() != null) {
                board.setFormattedSysDate(board.getSYSDATE().format(formatter));
            } else {
                board.setFormattedSysDate(null);
            }
        }

        // 모델에 데이터 추가
        model.addAttribute("list", resultPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);

        //혜정 코드
        if(searchType != null){
            model.addAttribute("searchType",searchType);
            model.addAttribute("search", search);
        }

        return "boardRecipe/home"; // home.html로 반환
    }

    // 상세 레시피
    @GetMapping("/recipe/{id}")
    public String getRecipeDetail(@PathVariable("id") Long id, Model model, Authentication auth) {
        RecipeDB recipe = recipeService.getRecipeById(id);


        if (recipe != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String formattedDate = recipe.getSYSDATE() != null ? recipe.getSYSDATE().format(formatter) : "";
            model.addAttribute("recipe", recipe);
            model.addAttribute("formattedDate", formattedDate);


            //혜정 코드
            recipeService.viewCount(id);
            if(auth != null){model.addAttribute("member", memberService.findMember(auth));};
            model.addAttribute("comments", recipeService.commentList(id));


            return "boardRecipe/recipeDetail";

        }else {
            model.addAttribute("errorMessage", "게시글을 찾을 수 없습니다.");
            return "error";
        }



    }


    //혜정 코드
    @GetMapping("/boardLike/{id}")
    String like(@PathVariable Long id){
        recipeService.upLike(id);
        return "redirect:/boardRecipe/recipe/"+id;
    }


    //혜정코드

    @GetMapping("/write")
    String write(Authentication auth, Model model){
        Member result = memberService.findMember(auth);
        model.addAttribute("user",result);
        return "boardRecipe/write.html";
    }

    //혜정코드

    @PostMapping("/write")
    String writePro(@ModelAttribute RecipeDB recipe, Authentication auth){
        int result = recipeService.write(recipe, auth);
        return "redirect:/boardRecipe/home";
    }


    //혜정코드
    @PostMapping("/commentWrite")
    String commentWrite(@ModelAttribute RecipeComment comment){

        int result = recipeService.saveComment(comment);
        return "redirect:/boardRecipe/recipe/"+comment.getRecipeDB().getId();
    }

    //혜정 코드
    @GetMapping("/commentLike/{id}")
    String commentLike(@PathVariable Long id){
        RecipeComment comment = recipeService.upCommentLike(id);
        return "redirect:/boardrecipe/recipe/"+comment.getRecipeDB().getId();
    }

    //혜정 코드
    @PostMapping("/commentModify/{commentId}")
    public ResponseEntity<String> modifyComment(@PathVariable Long commentId, @RequestParam String content) {
        recipeService.updateComment(commentId, content);
        return ResponseEntity.ok("댓글 수정 성공");
    }

    //혜정 코드
    @GetMapping("/commentDelete/{id}")
    String commentDelete(@PathVariable Long id, @RequestParam Long recipeID){

        int result = recipeService.commentDelete(id);
        return "redirect:/boardRecipe/recipe/"+recipeID+"?type=commentDelete&result=success";

    }



}
