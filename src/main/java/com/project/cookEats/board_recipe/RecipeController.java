package com.project.cookEats.board_recipe;

import com.project.cookEats.member.Member;
import com.project.cookEats.member.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.time.format.DateTimeFormatter;
import java.util.List;


@Controller
@RequiredArgsConstructor
@RequestMapping("/boardrecipe")
public class RecipeController {

    private final RecipeService recipeService;
    private final MemberService memberService;

    @GetMapping("/home")
    public String home(@RequestParam(value = "page", defaultValue = "1") int page, Model model) {
        int pageSize = 15; // 한 페이지에 표시할 레시피 수
        Pageable pageable = PageRequest.of(page - 1, pageSize);
        Page<RecipeDB> resultPage = recipeService.findAll(pageable);

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

        return "boardrecipe/home"; // home.html로 반환
    }

    // 상세 레시피
    @GetMapping("/recipe/{id}")
    public String getRecipeDetail(@PathVariable("id") Long id, Model model) {
        RecipeDB recipe = recipeService.getRecipeById(id);

        if (recipe != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String formattedDate = recipe.getSYSDATE() != null ? recipe.getSYSDATE().format(formatter) : "";
            model.addAttribute("recipe", recipe);
            model.addAttribute("formattedDate", formattedDate);


            return "boardrecipe/recipeDetail";

        } else {
            model.addAttribute("errorMessage", "게시글을 찾을 수 없습니다.");
            return "error";
        }
    }


    //혜정 코드
    @GetMapping("/boardLike/{id}")
    String like(@PathVariable Long id){
        recipeService.upLike(id);
        return "redirect:/boardrecipe/recipe/"+id;
    }

    @GetMapping("/write")
    String write(Authentication auth, Model model){
        Member result = memberService.findMember(auth);
        model.addAttribute("user",result);
        return "boardrecipe/write.html";
    }

    @PostMapping("/write")
    String writePro(@ModelAttribute RecipeDB recipe, Authentication auth){
        int result = recipeService.write(recipe, auth);
        return "redirect:/boardrecipe/home";
    }



}
