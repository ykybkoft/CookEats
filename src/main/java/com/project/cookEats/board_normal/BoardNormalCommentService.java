package com.project.cookEats.board_normal;

import com.project.cookEats.member.Member;
import com.project.cookEats.member.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardNormalCommentService {

    private final BoardNormalCommentRepository commentRepository; // 댓글 저장소
    private final BoardNormalRepository boardRepository; // 게시글 저장소

    // 댓글 추가 메소드
    public void addComment(Long articleId, String contents, Member member) {
        BoardNormal board = boardRepository.findById(articleId).orElse(null); // 게시글 조회
        if (board != null) {
            BoardNormalComment comment = new BoardNormalComment();
            comment.setContents(contents);
            comment.setBoardNormal(board);
            comment.setMember(member);
            commentRepository.save(comment); // 댓글 저장
        } else {
            throw new IllegalArgumentException("게시글이 존재하지 않습니다.");
        }
    }

    // 댓글 삭제 메소드
    public void deleteComment(Long commentId) {
        commentRepository.deleteById(commentId);
    }

    // 게시글 ID로 댓글 조회
    public List<BoardNormalComment> getCommentsByArticleId(Long articleId) {
        return commentRepository.findByBoardNormalId(articleId);
    }

    // 댓글 업데이트 (저장)
    public void updateComment(BoardNormalComment comment) {
        commentRepository.save(comment);
    }

    // 댓글 ID로 댓글 조회
    public BoardNormalComment getCommentById(Long id) {
        return commentRepository.findById(id).orElse(null);
    }


    // 댓글 좋아요
    public void incrementCommentLikes(Long commentId) {
        BoardNormalComment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("댓글을 찾을 수 없습니다."));
        comment.setComment_like(comment.getComment_like() + 1);
        commentRepository.save(comment);
    }
}
