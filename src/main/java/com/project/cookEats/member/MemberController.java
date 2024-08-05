package com.project.cookEats.member;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {


    private final MemberRepository mr;
    private final MemberService ms;

    @GetMapping("/join")
    String join(){
        return "member/join.html";
    }

    @PostMapping("/join")
    String joinProcess(@ModelAttribute Member row){
        int result =ms.join(row);
        return "redirect:/";
    }

    @GetMapping("/login")
    String login(){
        return "member/login.html";
    }
}
