package com.project.cookEats.board_normal;

import com.project.cookEats.member.Member;
import com.project.cookEats.member.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.format.DateTimeFormatter;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/boardnormal")
public class BoardNormalController {

    private final BoardNormalService bs;
    private final UserService us;
    private final MemberService ms;

    // 게시판 홈 화면
    @GetMapping("/home")
    public String home(Model model) {
        List<BoardNormal> result = bs.findAll();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        for (BoardNormal board : result) {
            if (board.getSysDate() != null) {
                board.setFormattedSysDate(board.getSysDate().format(formatter));
            } else {
                board.setFormattedSysDate(null);
            }
        }

        model.addAttribute("list", result);
        return "boardnormal/home"; // home.html
    }

    // 검색 및 정렬
    @GetMapping("/search")
    public String search(@RequestParam(value = "keyword", required = false, defaultValue = "") String keyword,
                         @RequestParam(value = "sort", required = false, defaultValue = "recommend") String sort,
                         Model model) {
        List<BoardNormal> articles;
        switch (sort) {
            case "latest":
                articles = bs.findByKeywordOrderByDateDesc(keyword);
                break;
            case "views":
                articles = bs.findByKeywordOrderByCountDesc(keyword);
                break;
            case "recommend":
            default:
                articles = bs.findByKeywordOrderByLikesDesc(keyword);
                break;
        }
        model.addAttribute("list", articles);
        model.addAttribute("keyword", keyword);
        model.addAttribute("sort", sort);
        return "boardnormal/searchResults"; // 검색 결과를 보여줄 HTML 템플릿
    }

    // 게시글 상세 페이지
    @GetMapping("/articles/{id}")
    public String getArticleDetail(@PathVariable("id") Long id, Model model) {
        BoardNormal article = bs.getArticleById(id);
        if (article != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String formattedDate = article.getSysDate() != null ? article.getSysDate().format(formatter) : "";
            model.addAttribute("article", article);
            model.addAttribute("formattedDate", formattedDate);
            return "boardnormal/articleDetail";
        } else {
            model.addAttribute("errorMessage", "게시글을 찾을 수 없습니다.");
            return "error";
        }
    }

    // 글쓰기 폼
    @GetMapping("/write")
    public String showWriteForm(Model model) {
        String username = us.getCurrentUsername();
        if (username == null) {
            return "redirect:/member/login"; // 로그인 페이지로 리다이렉트
        }
        model.addAttribute("username", username);
        return "boardnormal/write"; // write.html
    }

    @PostMapping("/write")
    public String submitPost(@RequestParam String title, @RequestParam String content) {
        String username = us.getCurrentUsername();
        if (username == null) {
            return "redirect:/member/login"; // 로그인 페이지로 리다이렉트
        }

        Member member = ms.getMemberByUsername(username);

        BoardNormal post = new BoardNormal();
        post.setTitle(title);
        post.setContent(content);
        post.setMember(member);

        bs.save(post);
        return "redirect:/board/board_normal";
    }

    // 게시글 수정 폼
    @GetMapping("/update/{id}")
    public String showUpdateForm(@PathVariable("id") Long id, Model model) {
        BoardNormal article = bs.getArticleById(id);
        if (article != null) {
            model.addAttribute("article", article);
            return "boardnormal/update"; // update.html
        } else {
            model.addAttribute("errorMessage", "게시글을 찾을 수 없습니다.");
            return "error";
        }
    }

    // 게시글 수정 처리
    @PostMapping("/update")
    public String updateArticle(@RequestParam Long id, @RequestParam String title, @RequestParam String content) {
        BoardNormal article = bs.getArticleById(id);
        if (article != null) {
            article.setTitle(title);
            article.setContent(content);
            bs.save(article);
            return "redirect:/board/articles/" + id;
        } else {
            return "error";
        }
    }

    // 게시글 삭제
    @PostMapping("/delete/{id}")
    public String deleteArticle(@PathVariable("id") Long id) {
        bs.deleteById(id);
        return "redirect:/board/board_normal";
    }
}
