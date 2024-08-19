package com.project.cookEats.member;


import com.project.cookEats.board_normal.BoardNormal;
import com.project.cookEats.board_normal.BoardNormalRepository;
import com.project.cookEats.board_share.entityClasses.Board_share;
import com.project.cookEats.board_share.repositories.Board_shareRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Optional;


@Controller
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {



    private final MemberService ms;



    @GetMapping("/join")
    String join(){

        return "member/join.html";
    }

    @PostMapping("/join")
    String joinProcess(@ModelAttribute Member row){
        int result =ms.join(row);
        return "redirect:/?result=success&type=join";
    }

    @GetMapping("/login")
    String login(){
        return "member/login.html";
    }


    @GetMapping("/logout") // Spring Security 는 로그아웃 방법이 기본적으로 POST 이기 때문에 추가한 코드
    public String logout(HttpServletRequest request, HttpServletResponse response){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if(authentication != null){
            new SecurityContextLogoutHandler().logout(request,response,authentication);
        }

        return "redirect:/";
    }

    @GetMapping("/myPage")
    String myPage(Authentication auth, Model model){
        Member result = ms.findMember(auth);
        model.addAttribute("user",result);
        List<BoardNormal> normal = ms.findBoardNormal(auth);
        model.addAttribute("normal", normal);
        List<Board_share> share = ms.findBoardShare(auth);
        model.addAttribute("share",share);

        return "member/myPage.html";
    }

    @PostMapping("/password")
    String password(@RequestParam String password,@RequestParam String type ,Authentication auth){
        if(type.equals("password")){
            if(ms.checkPW(password, auth)){
                return "member/password.html";
            }
            return "redirect:/member/myPage?result=false";
        }

        if(ms.checkPW(password,auth)){
            int result = ms.delete(auth);
            return "redirect:/?result=success&type=delete";
        }
        return "redirect:/member/myPage?result=false";



    }


    @PostMapping("/changePW")
    String changePW(@RequestParam String newPW, @RequestParam Long id){
        int result = ms.changePW(newPW, id);
        return "redirect:/";
    }

    @PostMapping("/check")
    @ResponseBody
    public int check(@RequestParam("value") String value, @RequestParam("type") String type) {
        if(ms.check(value, type)){
            return 1;
        }

        return 0;
    }




}