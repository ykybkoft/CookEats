package com.project.cookEats.board_share.controller;

import com.project.cookEats.board_share.repositories.Board_shareRepository;
import com.project.cookEats.board_share.entityClasses.Board_share;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/board_share")
public class BoardController {
    // 리포지토리 등록
    private final Board_shareRepository br;

    // 게시판 목록 페이지 매핑
    @GetMapping("/title")
    String title(){
        // br로 선언된 리포지토리 DB데이터 불러옴.
        List<Board_share> result = br.findAll();
        System.out.println(result.get(0));
        return "board_share/title.html";

    // 게시판 글 작성 페이지 매핑
    @GetMapping("/write")
    String write(){
        // br로 선언된 리포지토리 DB데이터 불러옴.
        List<Board_share> result = br.findAll();
        System.out.println(result.get(0));
        return "board_share/write.html";
    }
}
