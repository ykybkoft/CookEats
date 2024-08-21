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


    // 게시판 상세페이지 정보
    public Object getContents(Long id) {
        if (id.equals(null)) {
            return "없는 게시글 입니다";
        }
        return br.findById(id).get();
    }

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

            // 입력된 게시글 정보와 날짜정보를 DB에 저장
            data.setSysDate(date);
            br.save(data);
        }
        else {
            // 회원 로그인 인증 정보가 없을 경우, null 포인트 예외를 발생시켜 예외메세지 출력
            throw new IllegalArgumentException("게시글을 작성하려면 로그인이 필요합니다");
        }
    }

    public Object getEdit(Long id) {
        // 현재 로그인 된 사용자 세션정보
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        // 게시글 아이디 'i' 호출 해당 게시글에 저장된 memberId 'i'를 가져온다.
        // 궁극적으로 게시글 게시글 테이블 컬럼 데이터도 모두 포함함.
        Board_share memberId = br.findById(id).get();

        // 현재 로그인 된 사용자 아이디를 가져옴.
        if (auth != null) {
            Member member = findMember(auth);

            // 현재 게시글 테이블에 저장된 memberId와 현재 세션에 저장된 memberId을 비교
            if (member.getId().equals(memberId.getMember().getId())) {
                // 두 사용자 아이디가 동일하다면 게시글 아이디를 리턴
                return memberId;
            }
            else {
                throw new IllegalArgumentException ("해당 글은 작성자만 수정 가능합니다");
            }
        }
        else {
            return "redirect:/member/login.html";
        }
    }

    // 게시글 저장
    public void editPost(Board_share data) {
        // 현재 사용자 세션 아이디 정보
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Member member = findMember(auth);

        // not null 제약조건을 피하기 위해 보드 데이터를 불러와서 set에 넣어줌
        Board_share board = br.findById(data.getId()).get();

        // 개발자 도구로 POST 될 게시물 아이디를 변경해서 다른 글을 수정하는 것을 막기 위한 목적의 로직
        // 현재 로그인 된 사용자 아이디와 현재 게시물에 저장된 아이디가 맞다면
        if (member.getId().equals(board.getMember().getId())) {
            data.setTitle(board.getTitle());
            data.setContent(data.getContent());
            data.setBoard_comment(board.getBoard_comment());
            br.save(data);
        }
        else {
            throw new IllegalArgumentException("해당 글은 작성자만 수정 가능합니다");
        }

    }

    public void deleteContents(Long id) {
        // 현재 사용자 세션 아이디 정보
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Member member = findMember(auth);
        // 게시글에 저장 된 사용자 아이디 정보
        Board_share memberId = br.findById(id).get();

        if (member.getId().equals(memberId.getMember().getId())) {
            // 현재 로그인 된 사용자와 게시글 작성자의 아이디가 일치하면 삭제
            br.deleteById(id);
        }
        else {
            throw new IllegalArgumentException("해당 글은 작성자만 삭제 가능합니다");
        }
    }
}
