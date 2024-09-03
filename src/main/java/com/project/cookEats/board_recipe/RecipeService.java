package com.project.cookEats.board_recipe;

import com.project.cookEats.common_module.exception.ResourceNotFoundException;
import com.project.cookEats.member.Member;
import com.project.cookEats.member.CustomUser;
import com.project.cookEats.member.MemberRepository;
import com.project.cookEats.common_module.file.FileUpLoadService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
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
    private final FileUpLoadService fileUpLoadService;

    public Page<RecipeDB> findAll(int page, String search, String sortType) {
        int pageSize = 15;
        Pageable pageable = PageRequest.of(page - 1, pageSize, Sort.by(Sort.Direction.DESC, "LLIKE"));

        if (sortType != null) {
            pageable = PageRequest.of(page - 1, pageSize, Sort.by(Sort.Direction.DESC, sortType));
        }

        if (search == null || search.isEmpty()) {
            return recipeDBRepository.findAll(pageable);
        } else {
            return recipeDBRepository.findAllSearch(search, pageable);
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

    @Transactional
    public void deleteRecipe(Long id, Member loggedInMember) {
        RecipeDB recipe = recipeDBRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Recipe not found"));

        if (!recipe.getMember().equals(loggedInMember)) {
            throw new SecurityException("You are not authorized to delete this recipe");
        }

        for (String fileUrl : recipe.getManualImages()) {
            fileUpLoadService.deleteFile(fileUrl);
        }

        recipeDBRepository.delete(recipe);
    }

    @Transactional
    public void updateRecipe(RecipeDto recipeDto, MultipartFile[] manualImages) throws IOException {
        RecipeDB recipe = recipeDBRepository.findById(recipeDto.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Recipe not found"));

        recipe.setRCP_NM(recipeDto.getRCP_NM());
        recipe.setRCP_PARTS_DTLS(recipeDto.getRCP_PARTS_DTLS());
        recipe.setManuals(recipeDto.getManuals());

        List<String> currentImages = recipe.getManualImages();
        List<String> newImages = new ArrayList<>();

        if (manualImages != null) {
            for (MultipartFile file : manualImages) {
                if (!file.isEmpty()) {
                    // Save the file and get the URL
                    String fileName = fileUpLoadService.saveFile(file, "recipe");
                    newImages.add(fileName);
                }
            }
        }

        // If no new images were uploaded, keep the current images
        if (newImages.isEmpty()) {
            // No new images, so no need to delete existing images
            recipe.setManualImages(currentImages);
        } else {
            // Find images that are in currentImages but not in newImages and delete them
            for (String imageUrl : currentImages) {
                if (!newImages.contains(imageUrl)) {
                    // Log the image being deleted
                    System.out.println("Deleting Image: " + imageUrl);
                    fileUpLoadService.deleteFile(imageUrl);
                }
            }

            // Update the recipe with new image URLs
            recipe.setManualImages(newImages);
        }

        // Save the updated recipe
        recipeDBRepository.save(recipe);
    }


    public void upLike(Long id) {
        RecipeDB recipe = recipeDBRepository.findById(id).orElseThrow(() -> new RuntimeException("Recipe not found"));
        recipe.setLLIKE(recipe.getLLIKE() + 1);
        recipeDBRepository.save(recipe);
    }

    public int write(RecipeDB recipe, Authentication auth) {
        CustomUser user = (CustomUser) auth.getPrincipal();
        recipe.setMember(memberRepository.findById(user.getId()).orElseThrow(() -> new RuntimeException("User not found")));
        recipeDBRepository.save(recipe);
        return 1;
    }

    public void viewCount(Long id) {
        RecipeDB recipe = recipeDBRepository.findById(id).orElseThrow(() -> new RuntimeException("Recipe not found"));
        recipe.setCCOUNT(recipe.getCCOUNT() + 1);
        recipeDBRepository.save(recipe);
    }

    public int saveComment(RecipeComment comment) {
        recipeCommentRepository.save(comment);
        return 1;
    }

    public List<RecipeComment> commentList(Long id) {
        return recipeCommentRepository.findAllByRecipeDB(recipeDBRepository.findById(id).orElseThrow(() -> new RuntimeException("Recipe not found")));
    }

    public RecipeComment upCommentLike(Long id) {
        RecipeComment comment = recipeCommentRepository.findById(id).orElseThrow(() -> new RuntimeException("Comment not found"));
        comment.setLLIKE(comment.getLLIKE() + 1);
        recipeCommentRepository.save(comment);
        return comment;
    }

    public int commentDelete(Long id) {
        recipeCommentRepository.deleteById(id);
        return 1;
    }

    public void updateComment(Long id, String content) {
        RecipeComment comment = recipeCommentRepository.findById(id).orElseThrow(() -> new RuntimeException("Comment not found"));
        comment.setComment_contents(content);
        recipeCommentRepository.save(comment);
    }

    public Model getNutrition(Model model, Long id) {
        Optional<RecipeDBSubInfo> recipeInfo = recipeDBSubInfoRepository.findById(id);
        if (recipeInfo.isPresent()) {
            model.addAttribute("car", recipeInfo.get().getINFO_CAR());
            model.addAttribute("fat", recipeInfo.get().getINFO_FAT());
            model.addAttribute("pro", recipeInfo.get().getINFO_PRO());
        }
        return model;
    }
}
