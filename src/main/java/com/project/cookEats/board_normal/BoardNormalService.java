package com.project.cookEats.board_normal;

import com.project.cookEats.member.Member;
import com.project.cookEats.member.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BoardNormalService {

    private final BoardNormalRepository br;

    // 최신순 내림차순 정렬
    public Page<BoardNormal> findByKeywordOrderBySysDateDesc(String keyword, Pageable pageable) {
        return br.findByTitleContainingOrderBySysDateDesc(keyword, pageable);
    }

    // 조회수 기준으로 내림차순 정렬
    public Page<BoardNormal> findByKeywordOrderByViewsDesc(String keyword, Pageable pageable) {
        return br.findByTitleContainingOrderByViewsDesc(keyword, pageable);
    }

    // 추천수 기준으로 내림차순 정렬
    public Page<BoardNormal> findByKeywordOrderByLikesDesc(String keyword, Pageable pageable) {
        return br.findByTitleContainingOrderByLikesDesc(keyword, pageable);
    }

    // 모든 게시글을 페이징하여 조회
    public Page<BoardNormal> findAll(Pageable pageable) {
        return br.findAll(pageable);
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

    // 조회수 증가 메서드
    @Transactional
    public void increaseViewCount(Long id) {
        BoardNormal article = br.findById(id).orElseThrow(() -> new IllegalArgumentException("없는 게시글 입니다."));
        article.setViews(article.getViews() + 1);
        br.save(article);
    }

    // 게시글의 추천수만 증가시키는 메서드
    public boolean incrementLikes(Long id) {
        Optional<BoardNormal> optionalArticle = br.findById(id);
        if (optionalArticle.isPresent()) {
            BoardNormal article = optionalArticle.get();
            article.setLikes(article.getLikes() + 1); // 추천수 증가
            br.save(article);
            return true;
        }
        return false;
    }
}
