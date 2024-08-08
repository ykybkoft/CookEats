package com.project.cookEats.board_normal;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/board")
public class BoardNormalController {

    private final BoardNormalRepository Br;
    private final BoardNormalService bs;

    @GetMapping("/board_normal")
    String home(){
        return "boardnormal/home.html";
    }

    @GetMapping("/write")
    String board() {
        return "boardnormal/write.html";
    }

    @GetMapping("/search")
    String postSearch(@RequestParam String searchText) {

        // var result = BoardNormalRepository.findAllByTitleContains(searchText)
        // System.out.println(result);

        return  "boardnormal/list.html";
    }
}
