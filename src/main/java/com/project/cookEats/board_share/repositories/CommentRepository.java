
package com.project.cookEats.board_share.repositories;

import com.project.cookEats.board_share.entityClasses.Board_share;
import com.project.cookEats.board_share.entityClasses.Board_share_comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Board_share_comment, Long> {
    // 메서드 이름은 참조할 자료형의 엔티티 클래스의 필드명과 동일하게 설정해야됨. 안 그럼 존재하지 않는 필드 오류 = null가 생김.
    // findAllByBoardShare ->  findAllByBoard_share로 수정
    // 해당 게시글 아이디를 참조하고 있는 모든 댓글들을 찾아주는 메서드
    List<Board_share_comment> findAllByBoardShare(Board_share boardShare);
}