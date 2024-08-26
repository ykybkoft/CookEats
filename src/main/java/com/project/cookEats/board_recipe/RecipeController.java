package com.project.cookEats.board_recipe;

import com.project.cookEats.common_module.file.FileUpLoadService;
import com.project.cookEats.member.CustomUser;
import com.project.cookEats.member.Member;
import com.project.cookEats.member.MemberRepository;
import com.project.cookEats.member.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;


@Controller
@RequiredArgsConstructor
@RequestMapping("/boardRecipe")
public class RecipeController {

    private final RecipeService recipeService;
    private final RecipeDBRepository recipeDBRepository;
    private final MemberService memberService;
    private final MemberRepository memberRepository;
    private final FileUpLoadService fileUpLoadService;

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

        return "boardRecipe/home"; // home.html로 반환
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


            return "boardRecipe/recipeDetail";

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
        return "boardRecipe/write.html";
    }

    @PostMapping("/write")
    public String writePro(@ModelAttribute RecipeDB recipe,
                           Authentication auth,
                           @RequestParam("manual[]") String[] manuals,
                           @RequestParam("manualImage[]") MultipartFile[] manualImages) throws IOException {
        CustomUser user = (CustomUser) auth.getPrincipal();

        List<String> manualList = new ArrayList<>();
        List<String> manualImageList = new ArrayList<>();
        String manual = "";

        for (int i = 0; i < manuals.length; i++) {
            // 각각의 조리순서를 처리
            manualList.add(manuals[i]);

            // 파일 저장
            String imagePath = fileUpLoadService.saveFile(manualImages[i]);
            manualImageList.add(imagePath);

            // 필요한 경우 조리 순서와 이미지를 결합
            manual += manuals[i] + "%<";
        }

        // RecipeDB 객체에 수동으로 값 설정
        recipe.setManuals(manualList);
        recipe.setManualImages(manualImageList);
        recipe.setMANUAL(manual);
        recipe.setMember(memberRepository.findById(user.getId()).get());

        recipeDBRepository.save(recipe);
        return "redirect:/boardRecipe/home";
    }




}
