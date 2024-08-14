package com.project.cookEats.board_share.controller;

import com.project.cookEats.board_share.repositories.Board_shareRepository;
import com.project.cookEats.board_share.entityClasses.Board_share;
import com.project.cookEats.board_share.service.Borad_shareService;
import com.project.cookEats.member.Member;
import com.project.cookEats.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/board_share")
public class BoardController {
    // 의존성 주입
    private final Board_shareRepository br;
    private final Borad_shareService bs;

    // 게시글 리스트 페이지
    @GetMapping("/list")
    String title(){
        // br로 선언된 리포지토리 DB데이터 불러옴.
        List<Board_share> result = br.findAll();
        return "board_share/list.html";
    }

    // 게시판 글 내용 보기 페이지 매핑
    @GetMapping("/detail")
    String view(){
        List<Board_share> result = br.findAll();
        System.out.println(result.get(0));
        return "board_share/detail.html";
    }

    // 게시판 글 작성 페이지 매핑
    @GetMapping("/b_write")
    String write(Authentication auth, Model model){
        //게시글 로그인 된 유저의 아이디 가져옴 -my page- 활용
        Member result = bs.findMember(auth);
        model.addAttribute("nick",result.getNick());

        return "board_share/b_write.html";
    }

    @PostMapping("/b_write")
    public String addPost(@ModelAttribute Board_share data) {

        bs.savePost(data);

        return "redirect:/board_share/list";
    }
}
