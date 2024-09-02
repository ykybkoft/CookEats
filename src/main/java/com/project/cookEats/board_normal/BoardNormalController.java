package com.project.cookEats.board_normal;

import com.project.cookEats.member.Member;
import com.project.cookEats.member.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.format.DateTimeFormatter;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/boardNormal")
public class BoardNormalController {

    private final BoardNormalService bs;
    private final UserService us;
    private final MemberService ms;
    private final BoardNormalCommentService commentService; // 댓글 서비스

    // 게시판 홈 화면
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

    // 검색 및 정렬
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
        model.addAttribute("page", page);
        model.addAttribute("keyword", keyword);
        model.addAttribute("sort", sort);
        return "boardNormal/searchResults"; // 검색 결과를 보여줄 HTML 템플릿
    }

    // 게시글 상세 페이지
    @GetMapping("/articles/{id}")
    public String getArticleDetail(@PathVariable("id") Long id, Model model, Authentication auth) {
        BoardNormal article = bs.getArticleById(id);
        if (article != null) {
            // 조회수 증가
            bs.increaseViewCount(id);

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String formattedDate = article.getSysDate() != null ? article.getSysDate().format(formatter) : "";

            // 게시글에 대한 댓글 목록을 조회하여 모델에 추가
            List<BoardNormalComment> comments = commentService.getCommentsByArticleId(id);
            model.addAttribute("comments", comments);

            model.addAttribute("article", article);
            model.addAttribute("formattedDate", formattedDate);

            //혜정 코드
            if(auth != null){model.addAttribute("member", ms.findMember(auth));}
            return "boardNormal/articleDetail"; // articleDetail.html
        } else {
            model.addAttribute("errorMessage", "게시글을 찾을 수 없습니다.");
            return "error"; // error.html
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
        return "boardNormal/write"; // write.html
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
        return "redirect:/boardNormal/home";
    }

    // 게시글 수정 폼
    @GetMapping("/update/{id}")
    public String showUpdateForm(@PathVariable("id") Long id, Model model) {
        BoardNormal article = bs.getArticleById(id);
        if (article != null) {
            model.addAttribute("article", article);
            return "boardNormal/update"; // update.html
        } else {
            model.addAttribute("errorMessage", "게시글을 찾을 수 없습니다.");
            return "error"; // error.html
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
            return "redirect:/boardNormal/articles/" + id;
        } else {
            return "error"; // error.html
        }
    }

    // 게시글 삭제
    @GetMapping("/delete/{id}")
    public String deleteArticle(@PathVariable("id") Long id) {
        bs.deleteById(id);
        return "redirect:/boardNormal/home";
    }

    // 좋아요 증가 처리
    @GetMapping("/articles/{id}/like")
    public String likeArticle(@PathVariable("id") Long id) {
        try {
            boolean result = bs.incrementLikes(id); // 좋아요 수 증가
            if (result) {
                return "redirect:/boardNormal/articles/" + id; // 성공 시 게시글 상세 페이지로 리다이렉트
            } else {
                return "error"; // 실패 시 에러 페이지로 리다이렉트
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "error"; // 예외 발생 시 에러 페이지로 리다이렉트
        }
    }

    // 댓글 추가 처리
    @PostMapping("/articles/{id}/comment")
    public String addComment(@PathVariable("id") Long id,
                             @RequestParam String contents) {
        // 현재 로그인된 사용자 정보 조회
        Member currentMember = us.getCurrentMember();
        if (currentMember == null) {
            return "redirect:/member/login"; // 로그인 페이지로 리다이렉트
        }

        // 댓글 작성
        BoardNormal article = bs.getArticleById(id);
        if (article != null) {
            BoardNormalComment comment = new BoardNormalComment();
            comment.setContents(contents);
            comment.setBoardNormal(article);
            comment.setMember(currentMember);

            // 댓글 저장
            commentService.addComment(id, contents, currentMember);
        }

        return "redirect:/boardNormal/articles/" + id; // 게시글 상세 페이지로 리다이렉트
    }

    // 댓글 삭제 처리
    @GetMapping("/comments/delete/{id}")
    public String deleteComment(@PathVariable("id") Long id, @RequestParam Long articleID) {
        commentService.deleteComment(id);
        return "redirect:/boardNormal/articles/" + articleID; // 게시글 ID로 리다이렉트
    }

    // 댓글 수정 폼 페이지
    @GetMapping("/comments/{id}/editForm")
    public String showCommentEditForm(@PathVariable("id") Long id, Model model) {
        BoardNormalComment comment = commentService.getCommentById(id);

        if (comment != null) {
            model.addAttribute("comment", comment);
            model.addAttribute("articleId", comment.getBoardNormal().getId());
            return "boardNormal/commentEditForm"; // 댓글 수정 폼 템플릿 반환
        } else {
            model.addAttribute("errorMessage", "댓글을 찾을 수 없습니다.");
            return "error"; // 댓글을 찾을 수 없을 때 에러 페이지 반환
        }
    }

    // 댓글 수정 처리
    @PostMapping("/comments/{id}/edit")
    public String updateComment(@PathVariable("id") Long id, @RequestParam String contents) {
        BoardNormalComment comment = commentService.getCommentById(id);

        if (comment != null) {
            comment.setContents(contents);
            commentService.updateComment(comment);

            Long boardId = comment.getBoardNormal().getId(); // 댓글이 속한 게시글 ID를 가져옴
            return "redirect:/boardNormal/articles/" + boardId; // 수정된 댓글이 포함된 게시글 상세 페이지로 리다이렉트
        } else {
            return "error"; // 댓글을 찾을 수 없을 때 에러 페이지 반환
        }
    }

}
