package com.project.cookEats.board_share.service;

import com.project.cookEats.board_share.entityClasses.Board_share;
import com.project.cookEats.board_share.entityClasses.Board_share_comment;
import com.project.cookEats.board_share.repositories.Board_shareRepository;
import com.project.cookEats.board_share.repositories.CommentRepository;
import com.project.cookEats.member.CustomUser;
import com.project.cookEats.member.Member;
import com.project.cookEats.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor // 의존성 주입 어노테이션 @autowired
public class Borad_shareService {

    // 저장할 리포지토리
    private final Board_shareRepository br;
    private final MemberRepository mr;
    private final CommentRepository cr;

    // member ID get
    public Member findMember(Authentication auth) {
            CustomUser user = (CustomUser) auth.getPrincipal();

            return mr.findById(user.getId()).get();
    }

    // 게시판 상세페이지 정보
    public Object getContents(Long id) {
        if (id.equals(null)) {
            return "없는 게시글 입니다";
        }
        return br.findById(id).get();
    }

    // 게시글 좋아요 메서드
    public void contentsLike(Long id){
        Board_share like = br.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다."));

        like.setCntlike(like.getCntlike() + 1);
        br.save(like);
    }

    // 조회수 업데이트 메서드
    public void updateCount(Long id){
        Board_share count = br.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다."));

        count.setVcount(count.getVcount() + 1);
        br.save(count);
    }

    // 게시글 검색, 목록, 정렬 기능
    public Page<Board_share> pageFunction(int page, String search, String sortType){

        // 목록에 표시할 게시글 수
        int pageSize = 10;
        Pageable pageable = PageRequest.of(page, pageSize, Sort.by(Sort.Direction.DESC, "id"));

        // url의 쿼리파라미터로 전송되는 정렬 기준이 있을 경우
        if (sortType != null){
            // 항상 like 값으로 고정되어있으나, 유저가 sortType을 요청할 경우 변경되게 한다.
            pageable = PageRequest.of(page, pageSize, Sort.by(Sort.Direction.DESC, sortType));

            // 검색과 정렬 기능
            // 검색 값이 없거나 공백과 같이 둘 중 하나일 경우
            if (search == null || search.equals("")){
                System.out.println("yes sortType, no search value : " + sortType + ", " + search);

                return br.findAll(pageable);
            }
            else {
                // 검색 값이 있다면
                // 리포지토리 SQL LIKE 문 메서드 실행
                System.out.println("yes sortType, yes search value : " + sortType + ", " + search);
                return br.findAllSearch(search, pageable);  // 결과적으로 검색결과는 기본정렬 값인 like 순으로 정렬 됨.
            }
        }
        // 정렬기준이 없다면
        else {
            if (search == null || search.equals("")){
                System.out.println("no sortType, no search value : " + sortType + ", " + search);

                return br.findAll(pageable);
            }
            else {
                System.out.println("no sortType, yes search value : " + sortType + ", " + search);

                return br.findAllSearch(search, pageable);
            }
        }
    }

    // 게시글 페이징 기능
    public void pagination(Page<Board_share> resultPage, Model model) {
        // home 자바스크립트 글쓰기 예외에 필요
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Member member = null;

        if (auth != null && auth.isAuthenticated() && !(auth.getPrincipal() instanceof String)) {
            member = findMember(auth);
        }

        // 총 페이지 수 계산
        int totalPages = resultPage.getTotalPages();

        // 페이지 블록 계산
        int startPage = Math.max(0, resultPage.getNumber() - 2);
        int endPage = Math.min(resultPage.getTotalPages() - 1, resultPage.getNumber() + 2);

        // 모델에 데이터 추가
        model.addAttribute("home", resultPage);
        model.addAttribute("currentPage", resultPage.getNumber());
        model.addAttribute("totalPages", resultPage.getTotalPages());
        model.addAttribute("totalItems", resultPage.getTotalElements());
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        model.addAttribute("auth", member);
    }

    // 상세페이지 게시글 및 댓글 정보
    public Model getContents(Long id, Model model) {
        // ↓ 로직은 로그인 되야 수정삭제를 구현하기에 로그인 되지 않으면 사용자 데이터를 th:if에서 찾을 수 없게 되어 String 타입 파싱 오류가 발생했다.
        // 자바스크립트에서 수정삭제 버튼 on/off 기능 구현을 위해 세션유저 정보 제공
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        // 아래조건문 검사에서 false라면 그대로 null로 유지
        Member member = null;

        // 사용자가 null이 아니라면 그리고 사용자가 로그인 되었는지 확인한다. 그리고 만약, 로그인이 되지 않았다면(true) 로그인 한 것으로(false)로 변환시킨다.
        if (auth != null && auth.isAuthenticated() && !(auth.getPrincipal() instanceof String)) {   // 사용자 객체가 클래스 string(로그인 되지 않는 사용자 타입)타입이 맞다면 ! true를 false로 반전
            // 위 조건이 모두 true 일경우 아래 member 객체에 현재 사용자 정보를 담음
            member = findMember(auth);
        }

        // // 파라미터 id와 같은 게시글을 boardShareRepository에서 찾음
        Board_share contents = br.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다."));

        // 파라미터 id를 참조하고 있는 모든 댓글을 찾음
        List<Board_share_comment> comment = cr.findAllByBoardShare(contents);

        model.addAttribute("contents", contents);
        model.addAttribute("comments", comment);
        // member가 null일 경우가 존재함. 그럼으로 html에서 수정삭제에서 사용자 null 데이터가 추가 되었기에 로그인이 null어도 오류가 나지 않음
        // 예) auth.ge
        model.addAttribute("auth", member);

        return model;
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
            data.setSysdate(date);
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
            data.setMember(board.getMember());
            data.setTitle(data.getTitle());
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