package com.project.cookEats.board_share.controller;

import com.amazonaws.services.neptunedata.model.IllegalArgumentException;
import com.project.cookEats.board_share.entityClasses.Board_share_comment;
import com.project.cookEats.board_share.repositories.Board_shareRepository;
import com.project.cookEats.board_share.entityClasses.Board_share;
import com.project.cookEats.board_share.repositories.CommentRepository;
import com.project.cookEats.board_share.service.Borad_shareService;
import com.project.cookEats.board_share.service.CommentService;
import com.project.cookEats.member.Member;
import jakarta.persistence.Id;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


@Controller
@RequiredArgsConstructor
@RequestMapping("/boardShare")
public class BoardController {
    // 의존성 주입
    private final Borad_shareService bs;
    private final CommentRepository cr;
    private final CommentService cs;

    // 게시글 목록 기능
    // page = 페이징 값, search = 검색 값, sortType = 정렬 값
    @GetMapping("/home")
    String page(@RequestParam(value = "page", defaultValue = "0") Integer page,
                @RequestParam(required = false) String search,
                @RequestParam(required = false) String sortType,
                Model model){

        // 서비스에서 목록, 검색, 정렬 기능 구현 후 가공된 데이터를 한번에 묶어 페이징 처리함.
        Page<Board_share> resultPage = bs.pageFunction(page, search, sortType);
        bs.pagination(resultPage, model);

        model.addAttribute("search", search);
        model.addAttribute("sortType", sortType);

        return "boardShare/home.html";
    }

    // 조회수 업데이트
    // 응답을 JSON으로 반환하기 위해 ResponseBody사용
    @PostMapping("/updateCount/{id}")
    @ResponseBody
    public ResponseEntity<String> updateCount(@PathVariable Long id) {

        bs.updateCount(id);

        return new ResponseEntity<>("success", HttpStatus.OK);
    }

    // 게시글 좋아요 기능
    @PostMapping("/contentsLike/{id}")
    @ResponseBody
    public ResponseEntity<String> contentsLike(@PathVariable Long id) {

        bs.contentsLike(id);

        return new ResponseEntity<>("success", HttpStatus.OK);
    }

    // 댓글 좋아요 기능
    @PostMapping("/upLike/{id}")
    @ResponseBody
    public ResponseEntity<String> upLike(@PathVariable Long id) {

        cs.upLike(id);

        return new ResponseEntity<>("success", HttpStatus.OK);
    }

    // 게시판 글 및 댓글 내용 보기
    @GetMapping("/view/{id}")
    String detail(@PathVariable Long id, Model model){
        bs.getContents(id, model);

        return "boardShare/view.html";
    }

    // 1. html에서 사용자가 url의 게시글 pk아이디 정보와 댓글 정보를 controller에 post요청
    @PostMapping("/comment/{id}")
    String setComments(@PathVariable Long id, @ModelAttribute Board_share_comment data){
        cs.setComments(id, data);

        return "redirect:/boardShare/view/{id}";
    }

    // 댓글 수정 기능
    @PostMapping("/editComment/{id}")
    String editComment(@PathVariable Long id, @ModelAttribute Board_share_comment data){
        cs.editComments(id, data);

        // 해당 댓글이 있는 게시글로 리다이렉트하기 위해 게시글 id를 불러옴
        Board_share number = cr.findById(id).get().getBoardShare();

        return "redirect:/boardShare/view/" + number.getId();
    }
    // 댓글 삭제
    @DeleteMapping("/deleteComment/")
    ResponseEntity<String> deleteComment(Long id){
        cs.deleteComment(id);

        return ResponseEntity.ok("댓글이 삭제 되었습니다.");
    }
    // 게시판 글 작성 페이지 불러오기
    @GetMapping("/write")
    String write(Authentication auth, Model model){
        //게시글 로그인 된 유저의 아이디 가져옴 -my page- 활용
        Member result = bs.findMember(auth);
        model.addAttribute("nick",result.getNick());

        return "boardShare/write.html";
    }

    // 게시판 글 작성 기능
    @PostMapping("/write")
    public String addPost(@ModelAttribute Board_share data) {

        bs.savePost(data);

        return "redirect:/boardShare/home";
    }

    // 게시글 수정 데이터 불러오기
    @GetMapping("/edit/{id}")
    String edit(@PathVariable Long id, Model model){
        model.addAttribute("data", bs.getEdit(id));

        return "boardShare/edit.html";
    }

    //게시글 수정
    @PostMapping("/edit")
    String editPost(@ModelAttribute Board_share data){

        bs.editPost(data);

        // 리턴 뒤에 게시글 id를 붙여줘야 해당 수정된 게시글로 이동 됨.
        return "redirect:/boardShare/view/" + data.getId();
    }

    // 게시글 삭제
    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id){

        bs.deleteContents(id);
        return "redirect:/boardShare/home";
    }

}
