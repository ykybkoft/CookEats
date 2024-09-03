package com.project.cookEats.board_share.service;

import com.project.cookEats.board_share.entityClasses.Board_share;
import com.project.cookEats.board_share.entityClasses.Board_share_comment;
import com.project.cookEats.board_share.repositories.Board_shareRepository;
import com.project.cookEats.board_share.repositories.CommentRepository;
import com.project.cookEats.member.CustomUser;
import com.project.cookEats.member.Member;
import com.project.cookEats.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final MemberRepository mr;
    private final Board_shareRepository br;
    private final CommentRepository cr;

    // member ID get
    public Member findMember(Authentication auth) {
        CustomUser user = (CustomUser) auth.getPrincipal();

        return mr.findById(user.getId()).get();
    }

    // 게시글 댓글 작성 기능
    public void setComments(Long id, Board_share_comment data) {
        // 2. 로그인 된 사용자만 게시글에 댓글 달 수 있게 함.
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth != null){
            // 3. 현재 게시글 아이디와 댓글 작성하는 현재 사용자 아이디
            Board_share boardArray = br.findById(id).get();
            Member member = findMember(auth);
            // insert가 update로 트랜젝션이 자동 변경되는 것을 막기위해
            // pk부분의 버퍼를 비워줘 null로 set해줌.
            Board_share_comment comment = new Board_share_comment();

            // 4. 현재 댓글을 쓸 게시글의 아이디 정보를 data에 추가한다.
            data.setId(comment.getId());
            data.setBoardShare(boardArray); // boardArray는 결국 현재 게시글 id이므로 boardShare 컬럼에 넣으면 된다.
            data.setMember(member);         // 이하 동문

            LocalDate now = LocalDate.now();    // 댓글 작성 시간 추가
            data.setSysdate(now);

            // 5. 이제 data에는 html에서 입력한 댓글과 4.의 데이터를 저장하면 끝난다.
            cr.save(data);
        }
    }

    // 댓글 수정 기능
    public void editComments(Long id, Board_share_comment data) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Member member = findMember(auth);

        Board_share_comment comment = cr.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("댓글을 찾을 수 없습니다."));

        // 로그인된 사용자와 댓글 작성자의 ID가 동일한지 확인
        if (comment.getMember().getId().equals(member.getId())) {
            comment.setComment(data.getComment());
            cr.save(comment);
        } else {
            throw new IllegalArgumentException("댓글 작성자만 수정할 수 있습니다.");
        }
    }

    // 댓글 삭제 기능
    public void deleteComment(Long id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Member member = findMember(auth);

        Board_share_comment userId = cr.findById(id).get();

        // 현재 로그인 유저와 댓글 작성자 유저가 맞는지 확인
        if (member.getId().equals(userId.getMember().getId())){
            cr.deleteById(id);
        } else {
            throw new IllegalArgumentException("댓글 작성자만 삭제할 수 있습니다.");
        }
    }

    // 댓글 좋아요 업데이트 메서드
    public void upLike(Long id){
        Board_share_comment like = cr.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("댓글을 찾을 수 없습니다."));

        like.setCmtlike(like.getCmtlike() + 1);
        cr.save(like);
    }

}
