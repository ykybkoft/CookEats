package com.project.cookEats.board_normal;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BoardNormalService {

    private final BoardNormalRepository br; // BoardNormalRepository 의존성 주입

    // 제목에 키워드가 포함된 게시글을 조회수 기준으로 내림차순 정렬
    public Page<BoardNormal> findByKeywordOrderByViewsDesc(String keyword, Pageable pageable) {
        return br.findByTitleContainingOrderByCountDesc(keyword, pageable);
    }

    // 제목에 키워드가 포함된 게시글을 추천수 기준으로 내림차순 정렬
    public Page<BoardNormal> findByKeywordOrderByLikesDesc(String keyword, Pageable pageable) {
        return br.findByTitleContainingOrderByLikesDesc(keyword, pageable);
    }

    // 제목에 키워드가 포함된 게시글을 작성일 기준으로 내림차순 정렬
    public Page<BoardNormal> findByKeywordOrderBySysDateDesc(String keyword, Pageable pageable) {
        return br.findByTitleContainingOrderBySysDateDesc(keyword, pageable);
    }

    // 모든 게시글을 페이징하여 조회
    public Page<BoardNormal> findAll(Pageable pageable) {
        return br.findAll(pageable);
    }

    // ID로 게시글 조회
    public BoardNormal getArticleById(Long id) {
        return br.findById(id).orElse(null);
    }

    // 게시글 저장
    public BoardNormal save(BoardNormal boardNormal) {
        return br.save(boardNormal);
    }

    // 게시글 수정
    @Transactional
    public void updateArticle(Long id, String title, String content) {
        BoardNormal article = br.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid article Id:" + id));

        article.setTitle(title);
        article.setContent(content);

        br.save(article);
    }

    // 게시글 삭제
    public void deleteById(Long id) {
        br.deleteById(id);
    }
}
