package com.project.cookEats.board_recipe;

import com.project.cookEats.common_module.file.FileUpLoadService;
import com.project.cookEats.member.CustomUser;
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
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Paths;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
@RequestMapping("/boardRecipe")
public class RecipeController {

    private final RecipeService recipeService;
    private final MemberService memberService;
    private final FileUpLoadService fileUpLoadService;

    @GetMapping("/home")
    public String home(@RequestParam(value = "page", defaultValue = "1") int page, Model model, @RequestParam(required = false) String searchType, @RequestParam(required = false) String search, @RequestParam(required = false) String sortType) {

        int pageSize = 15;
        Pageable pageable = PageRequest.of(page - 1, pageSize);

        if(sortType==null ||sortType.equals("")){
            sortType="LLIKE";
        }

        Page<RecipeDB> resultPage = recipeService.findAll(page, search , sortType);

        int totalPages = resultPage.getTotalPages();
        int startPage = (page - 1) / 10 * 10 + 1;
        int endPage = Math.min(startPage + 9, totalPages);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        for (RecipeDB board : resultPage) {
            if (board.getSYSDATE() != null) {
                board.setFormattedSysDate(board.getSYSDATE().format(formatter));
            } else {
                board.setFormattedSysDate(null);
            }
        }

        model.addAttribute("list", resultPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        model.addAttribute("search", search);
        model.addAttribute("sortType", sortType);

        return "boardRecipe/home";
    }

    @GetMapping("/recipe/{id}")
    public String getRecipeDetail(@PathVariable("id") Long id, Model model, Authentication auth) {
        RecipeDB recipe = recipeService.getRecipeById(id);

        if (recipe.getMember() == null) {
            model = recipeService.getNutrition(model, id);
        }

        if (recipe != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String formattedDate = recipe.getSYSDATE() != null ? recipe.getSYSDATE().format(formatter) : "";
            model.addAttribute("recipe", recipe);
            model.addAttribute("formattedDate", formattedDate);

            List<String> manualImages = recipe.getManualImages();
            model.addAttribute("manualImages", manualImages);

            recipeService.viewCount(id);
            if (auth != null) {
                model.addAttribute("member", memberService.findMember(auth));
            }
            model.addAttribute("comments", recipeService.commentList(id));

            return "boardRecipe/recipeDetail";
        } else {
            model.addAttribute("errorMessage", "게시글을 찾을 수 없습니다.");
            return "error";
        }
    }

    @PostMapping("/update")
    public String updateRecipe(@ModelAttribute RecipeDto recipeDto,
                               @RequestParam(value = "manualImages", required = false) MultipartFile[] manualImages,
                               RedirectAttributes redirectAttributes) throws IOException {
        recipeService.updateRecipe(recipeDto, manualImages);

        redirectAttributes.addFlashAttribute("message", "레시피가 성공적으로 수정되었습니다.");
        return "redirect:/boardRecipe/recipe/" + recipeDto.getId();
    }

    @GetMapping("/update/{id}")
    public String updateRecipe(@PathVariable Long id, Model model) {
        RecipeDB recipe = recipeService.getRecipeById(id);
        if (recipe == null) {
            throw new ResourceAccessException("Recipe not found");
        }

        List<String> imageFileNames = recipe.getManualImages().stream()
                .map(url -> {
                    try {
                        URL urlObj = new URL(url);
                        String path = urlObj.getPath();
                        return path.substring(path.lastIndexOf('/') + 1);
                    } catch (MalformedURLException e) {
                        return "Invalid URL";
                    }
                })
                .collect(Collectors.toList());

        model.addAttribute("recipeDto", recipe);
        model.addAttribute("manualImages", imageFileNames);
        return "boardRecipe/update";
    }

    @GetMapping("/boardLike/{id}")
    public String like(@PathVariable Long id) {
        recipeService.upLike(id);
        return "redirect:/boardRecipe/recipe/" + id;
    }

    @GetMapping("/write")
    public String write(Authentication auth, Model model) {
        Member result = memberService.findMember(auth);
        model.addAttribute("user", result);
        return "boardRecipe/write";
    }

    @PostMapping("/write")
    public String writePro(@ModelAttribute RecipeDB recipe,
                           Authentication auth,
                           @RequestParam("manual[]") String[] manuals,
                           @RequestParam("manualImage[]") MultipartFile[] manualImages) throws Exception {
        CustomUser user = (CustomUser) auth.getPrincipal();

        List<String> manualList = new ArrayList<>();
        List<String> manualImageList = new ArrayList<>();

        for (int i = 0; i < manuals.length; i++) {
            manualList.add(manuals[i]);
            String boardType = "recipe";
            String imagePath = fileUpLoadService.saveFile(manualImages[i], boardType);
            manualImageList.add(imagePath);
        }

        recipe.setManuals(manualList);
        recipe.setManualImages(manualImageList);
        recipe.setMember(memberService.findMember(auth));

        recipeService.saveRecipe(recipe);

        return "redirect:/boardRecipe/home";
    }

    @PostMapping("/delete/{id}")
    public String deleteRecipe(@PathVariable Long id, Authentication auth) {
        try {
            Member loggedInMember = memberService.findMember(auth);
            recipeService.deleteRecipe(id, loggedInMember);
            return "redirect:/boardRecipe/home?type=delete&result=success";
        } catch (SecurityException e) {
            return "redirect:/boardRecipe/home?type=delete&result=error";
        } catch (Exception e) {
            return "redirect:/boardRecipe/home?type=delete&result=error";
        }
    }

    @PostMapping("/commentWrite")
    public String commentWrite(@ModelAttribute RecipeComment comment) {
        recipeService.saveComment(comment);
        return "redirect:/boardRecipe/recipe/" + comment.getRecipeDB().getId();
    }

    @GetMapping("/commentLike/{id}")
    public String commentLike(@PathVariable Long id) {
        RecipeComment comment = recipeService.upCommentLike(id);
        return "redirect:/boardRecipe/recipe/" + comment.getRecipeDB().getId();
    }

    @PostMapping("/commentModify/{commentId}")
    public ResponseEntity<String> modifyComment(@PathVariable Long commentId, @RequestParam String content) {
        recipeService.updateComment(commentId, content);
        return ResponseEntity.ok("댓글 수정 성공");
    }

    @GetMapping("/commentDelete/{id}")
    public String commentDelete(@PathVariable Long id, @RequestParam Long recipeID) {
        recipeService.commentDelete(id);
        return "redirect:/boardRecipe/recipe/" + recipeID + "?type=commentDelete&result=success";
    }
}
