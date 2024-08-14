package com.project.cookEats.board_normal;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/board")
public class BoardNormalController {

    private final BoardNormalRepository br;
    private final BoardNormalService bs;


    // 게시판 홈 화면
    @GetMapping("/board_normal")
    String home(){
        return "boardnormal/home.html";
    }

    // 글작성
    @GetMapping("/write")
    String board() {
        return "boardnormal/write.html";
    }
}