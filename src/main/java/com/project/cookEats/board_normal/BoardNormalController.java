package com.project.cookEats.board_normal;

import com.project.cookEats.member.Member;
import com.project.cookEats.member.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.format.DateTimeFormatter;

@Controller
@RequiredArgsConstructor
@RequestMapping("/boardNormal")
public class BoardNormalController {

    private final BoardNormalService bs; // 게시판 서비스
    private final UserService us; // 사용자 서비스 (로그인 사용자 정보 관리)
    private final MemberService ms; // 멤버 서비스 (회원 정보 관리)

    // 게시판 홈 화면 + 페이징 기능
    @GetMapping("/home")
    public String home(Model model, @PageableDefault(size = 10) Pageable pageable) {
        // 페이지별 게시글 목록을 불러옴
        Page<BoardNormal> page = bs.findAll(pageable);

        // 날짜 포맷 설정 (yyyy-MM-dd)
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        // 각 게시글의 작성일을 포맷팅하여 문자열로 변환
        page.getContent().forEach(board -> {
            if (board.getSysDate() != null) {
                board.setFormattedSysDate(board.getSysDate().format(formatter));
            } else {
                board.setFormattedSysDate(null);
            }
        });

        // 모델에 페이지 정보 추가
        model.addAttribute("page", page);
        return "boardNormal/home"; // home.html 템플릿 반환
    }

    @GetMapping("/search")
    public String search(@RequestParam(value = "keyword", required = false, defaultValue = "") String keyword,
                         @RequestParam(value = "sort", required = false, defaultValue = "likes") String sort,
                         @PageableDefault(size = 10) Pageable pageable, Model model) {
        Page<BoardNormal> page;

        // 정렬 기준에 따른 게시글 검색
        switch (sort) {
            case "sysDate":
                page = bs.findByKeywordOrderBySysDateDesc(keyword, pageable); // 최신순
                break;
            case "views":
                page = bs.findByKeywordOrderByViewsDesc(keyword, pageable); // 조회순
                break;
            case "likes":
            default:
                page = bs.findByKeywordOrderByLikesDesc(keyword, pageable); // 추천순 (기본값)
                break;
        }

        // 모델에 검색 결과와 옵션들 추가
        model.addAttribute("page", page);
        model.addAttribute("keyword", keyword);
        model.addAttribute("sort", sort);
        return "boardNormal/searchResults"; // 검색 결과를 보여줄 HTML 템플릿 반환
    }

    // 게시글 상세 페이지
    @GetMapping("/articles/{id}")
    public String getArticleDetail(@PathVariable("id") Long id, Model model) {
        // ID를 통해 특정 게시글 조회
        BoardNormal article = bs.getArticleById(id);
        if (article != null) {
            // 날짜 포맷 설정
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String formattedDate = article.getSysDate() != null ? article.getSysDate().format(formatter) : "";

            // 모델에 게시글과 포맷팅된 날짜 추가
            model.addAttribute("article", article);
            model.addAttribute("formattedDate", formattedDate);
            return "boardNormal/articleDetail"; // articleDetail.html 템플릿 반환
        } else {
            // 게시글을 찾을 수 없는 경우 에러 메시지 표시
            model.addAttribute("errorMessage", "게시글을 찾을 수 없습니다.");
            return "error"; // error.html 템플릿 반환
        }
    }

    // 글쓰기 폼 페이지
    @GetMapping("/write")
    public String showWriteForm(Model model) {
        // 현재 로그인된 사용자 정보 조회
        String username = us.getCurrentUsername();
        if (username == null) {
            return "redirect:/member/login"; // 로그인 페이지로 리다이렉트
        }
        // 모델에 사용자 이름 추가
        model.addAttribute("username", username);
        return "boardNormal/write"; // write.html 템플릿 반환
    }

    // 글쓰기 폼 제출 처리
    @PostMapping("/write")
    public String submitPost(@RequestParam String title, @RequestParam String content) {
        // 현재 로그인된 사용자 정보 조회
        String username = us.getCurrentUsername();
        if (username == null) {
            return "redirect:/member/login"; // 로그인 페이지로 리다이렉트
        }

        // 사용자 이름을 통해 멤버 정보 조회
        Member member = ms.getMemberByUsername(username);

        // 새 게시글 생성
        BoardNormal post = new BoardNormal();
        post.setTitle(title);
        post.setContent(content);
        post.setMember(member); // 게시글 작성자 설정

        // 게시글 저장
        bs.save(post);
        return "redirect:/boardNormal/home"; // 게시판 홈으로 리다이렉트
    }

    // 게시글 수정 폼 페이지
    @GetMapping("/update/{id}")
    public String showUpdateForm(@PathVariable("id") Long id, Model model) {
        // ID를 통해 특정 게시글 조회
        BoardNormal article = bs.getArticleById(id);
        if (article != null) {
            // 모델에 게시글 추가
            model.addAttribute("article", article);
            return "boardNormal/update"; // update.html 템플릿 반환
        } else {
            // 게시글을 찾을 수 없는 경우 에러 메시지 표시
            model.addAttribute("errorMessage", "게시글을 찾을 수 없습니다.");
            return "error"; // error.html 템플릿 반환
        }
    }

    // 게시글 수정 처리
    @PostMapping("/update")
    public String updateArticle(@RequestParam Long id, @RequestParam String title, @RequestParam String content) {
        // ID를 통해 특정 게시글 조회
        BoardNormal article = bs.getArticleById(id);
        if (article != null) {
            // 게시글 제목과 내용을 업데이트
            article.setTitle(title);
            article.setContent(content);

            // 게시글 저장
            bs.save(article);
            return "redirect:/boardNormal/articles/" + id; // 수정된 게시글 상세 페이지로 리다이렉트
        } else {
            return "error"; // 에러 페이지 반환
        }
    }

    // 게시글 삭제 처리
    @PostMapping("/delete/{id}")
    public String deleteArticle(@PathVariable("id") Long id) {
        // ID를 통해 게시글 삭제
        bs.deleteById(id);
        return "redirect:/boardNormal/home"; // 게시판 홈으로 리다이렉트
    }
}
