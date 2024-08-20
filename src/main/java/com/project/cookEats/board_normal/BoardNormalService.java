package com.project.cookEats.board_normal;

import com.project.cookEats.member.Member;
import com.project.cookEats.member.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardNormalService {

    private final BoardNormalRepository br;

    // 제목에 키워드가 포함된 게시글을 조회수 기준으로 내림차순 정렬
    public List<BoardNormal> findByKeywordOrderByCountDesc(String keyword) {
        return br.findByTitleContainingOrderByCountDesc(keyword);
    }

    // 제목에 키워드가 포함된 게시글을 추천수 기준으로 내림차순 정렬
    public List<BoardNormal> findByKeywordOrderByLikesDesc(String keyword) {
        return br.findByTitleContainingOrderByLikesDesc(keyword);
    }

    // 제목에 키워드가 포함된 게시글을 작성일 기준으로 내림차순 정렬
    public List<BoardNormal> findByKeywordOrderByDateDesc(String keyword) {
        return br.findByTitleContainingOrderBySysDateDesc(keyword);
    }

    // 모든 게시글을 반환
    public List<BoardNormal> findAll() {
        return br.findAll();
    }

    // 상세 페이지의 게시글을 찾는 메서드
    public BoardNormal getArticleById(Long id) {
        return br.findById(id).orElse(null);
    }

    // 게시글 저장 메서드
    public BoardNormal save(BoardNormal boardNormal) {
        return br.save(boardNormal);
    }

    // 게시글 수정 메서드
    @Transactional
    public void updateArticle(Long id, String title, String content) {
        BoardNormal article = br.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid article Id:" + id));

        article.setTitle(title);
        article.setContent(content);

        br.save(article);
    }

    // 게시글 삭제 메서드
    public void deleteById(Long id) {
        br.deleteById(id);
    }
}
