package com.project.cookEats.board_share.controller;

import com.project.cookEats.board_share.repositories.Board_shareRepository;
import com.project.cookEats.board_share.entityClasses.Board_share;
import com.project.cookEats.board_share.service.Borad_shareService;
import com.project.cookEats.member.Member;
import jakarta.persistence.Id;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/boardShare")
public class BoardController {
    // 의존성 주입
    private final Board_shareRepository br;
    private final Borad_shareService bs;

    // 게시글 리스트 페이지
    @GetMapping("/home")
    String title(Model model){
        List<Board_share> result = br.findAll();
        model.addAttribute("home", result);

        return "boardShare/home.html";
    }

    // 게시판 글 내용 보기 페이지 매핑
    @GetMapping("/view/{id}")
    String detail(@PathVariable Long id, Model model){
        model.addAttribute("data", bs.getContents(id));

        return "boardShare/view.html";
    }

    // 게시판 글 작성 페이지 매핑
    @GetMapping("/write")
    String write(Authentication auth, Model model){
        //게시글 로그인 된 유저의 아이디 가져옴 -my page- 활용
        Member result = bs.findMember(auth);
        model.addAttribute("nick",result.getNick());

        return "boardShare/write.html";
    }

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

        // 리턴 뒤에 게시글 id를 붙여줘야 해당 수정된 개시글로 이동 됨.
        return "redirect:/boardShare/view/" + data.getId();
    }

    // 게시글 삭제
    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id){

        bs.deleteContents(id);
        return "redirect:/boardShare/home";
    }
}
