package com.project.cookEats.board_share.controller;

import com.project.cookEats.board_share.repositories.Board_shareRepository;
import com.project.cookEats.board_share.entityClasses.Board_share;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/board_share")
public class BoardController {
    private final Board_shareRepository br;

        List<Board_share> result = br.findAll();
    }
        List<Board_share> result = br.findAll();
        System.out.println(result.get(0));
    }
}
