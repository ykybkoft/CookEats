package com.project.cookEats.board_normal;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/board")
public class BoardNormalController {

    private final BoardNormalRepository Br;
    private final BoardNormalService bs;

    @GetMapping("/home")
    String home(){
        return "";
    }

    @GetMapping("/write")
    String board() {
        return "boardnormal/write.html";
    }
}
