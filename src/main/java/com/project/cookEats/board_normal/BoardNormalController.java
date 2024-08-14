package com.project.cookEats.board_normal;

import com.project.cookEats.member.Member;
import com.project.cookEats.member.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;


import java.time.format.DateTimeFormatter;
import java.util.List;


@Controller
@RequiredArgsConstructor
@RequestMapping("/board")
public class BoardNormalController {

    @Autowired
    private final BoardNormalRepository br;
    @Autowired
    private final BoardNormalService bs;
    @Autowired
    private final UserService us;
    @Autowired
    private final MemberService ms;

    // 게시판 홈 화면
    @GetMapping("/board_normal")
    String home(Model model){
        List<BoardNormal> result = br.findAll();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        for (BoardNormal board : result) {
            if (board.getSys_date() != null) {
                board.setFormattedSysDate(board.getSys_date().format(formatter));
            } else {
                board.setFormattedSysDate(null);
            }
        }

        model.addAttribute("list", result);

        return "boardnormal/home.html";
    }


    // 검색
    @GetMapping("/search")
    public String searchArticles(@RequestParam(value = "keyword", required = false) String keyword, Model model) {
        List<BoardNormal> searchResults;
        if (keyword == null || keyword.trim().isEmpty()) {
            // 검색어가 없으면 전체 리스트를 보여줌
            searchResults = bs.findAll();
        } else {
            // 검색어가 있으면 제목에 해당 키워드가 포함된 게시글만 보여줌
            searchResults = bs.searchArticlesByTitle(keyword);
        }
        model.addAttribute("list", searchResults);
        return "boardnormal/searchResults"; // 검색 결과를 보여줄 HTML 템플릿
    }


    // 상세페이지
    @GetMapping("/articles/{id}")
    public String getArticleDetail(@PathVariable("id") Long id, Model model) {
        BoardNormal article = bs.getArticleById(id);
        if (article != null) {
            // 날짜 포맷팅
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String formattedDate = article.getSys_date() != null ? article.getSys_date().format(formatter) : "";
            model.addAttribute("article", article);
            model.addAttribute("formattedDate", formattedDate);
            return "boardnormal/articleDetail";
        } else {
            model.addAttribute("errorMessage", "게시글을 찾을 수 없습니다.");
            return "error";
        }
    }


    // 글쓰기
    @GetMapping("/write")
    public String showWriteForm(Model model) {
        String username = us.getCurrentUsername();
        if (username == null) {
            return "redirect:/member/login"; // 로그인 페이지로 리다이렉트
        }
        model.addAttribute("username", username);
        return "boardnormal/write"; // 확장자 제거
    }

    @PostMapping("/write")
    public String submitPost(@RequestParam String title, @RequestParam String content) {
        String username = us.getCurrentUsername();
        if (username == null) {
            return "redirect:/member/login"; // 로그인 페이지로 리다이렉트
        }

        Member member = ms.getMemberByUsername(username); // Member 객체 조회

        BoardNormal post = new BoardNormal();
        post.setTitle(title);
        post.setContent(content);
        post.setMember(member); // Member 객체 설정

        bs.save(post); // BoardNormalService의 save 메서드 호출
        return "redirect:/board/board_normal"; // 게시판 목록으로 리다이렉트
    }

    // 게시글 수정 폼 표시
    @GetMapping("/update/{id}")
    public String showUpdateForm(@PathVariable("id") Long id, Model model) {
        BoardNormal article = bs.getArticleById(id);
        if (article != null) {
            model.addAttribute("article", article);
            return "boardnormal/update"; // update.html 템플릿으로 이동
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
            bs.save(article); // 수정된 게시글 저장
            return "redirect:/board/articles/" + id; // 수정된 게시글 페이지로 리다이렉트
        } else {
            return "error"; // 게시글을 찾을 수 없는 경우
        }
    }

    // 삭제
    @PostMapping("/delete/{id}")
    public String deleteArticle(@PathVariable("id") Long id) {
        bs.deleteById(id);
        return "redirect:/board/board_normal"; // 게시판 목록으로 리다이렉트
    }

}
