package com.project.cookEats.board_normal;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface BoardNormalCommentRepository extends JpaRepository<BoardNormalComment, Long> {
   // List<BoardNormalComment> findByBoardNormalId(Long boardNormalId);

    List<BoardNormalComment> findAllByBoardNormal(BoardNormal boardNormal);

    List<BoardNormalComment> findByBoardNormalId(Long boardId);

}

