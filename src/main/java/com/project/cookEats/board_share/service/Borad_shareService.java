package com.project.cookEats.board_share.service;

import com.project.cookEats.board_share.entityClasses.Board_share;
import com.project.cookEats.board_share.repositories.Board_shareRepository;
import com.project.cookEats.member.CustomUser;
import com.project.cookEats.member.Member;
import com.project.cookEats.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor // 의존성 주입 어노테이션 @autowired
public class Borad_shareService {

    // 저장할 리포지토리
    private final Board_shareRepository br;
    private final MemberRepository mr;

    // member ID get
    public Member findMember(Authentication auth) {
            CustomUser user = (CustomUser) auth.getPrincipal();

            return mr.findById(user.getId()).get();
    }

    public void savePost(Board_share data) {
       // 현재 로그인 된 사용자 세션정보를 가져옴
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        // 세션정보가 있다면 findMember 함수를 통해 사용자 id를 가져옴
        if (auth != null) {
            Member member = findMember(auth);
            data.setMember(member); // 현재 로그인된 사용자 id를 타입에 맞춰 저장

            // 글작성 된 현재 날짜 데이터 추가
            LocalDate date = LocalDate.now();

            // 입력된 게시글 정보와 파싱된 날짜정보를 DB에 저장
            data.setSysDate(date);
            br.save(data);
        } else {
            // 회원 로그인 인증 정보가 없을 경우, 예외를 발생시켜 로그인 페이지로 리다이렉트
            throw new RuntimeException("redirect:/member/login.html");
        }
    }
}
