package com.project.cookEats.board_normal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class BoardNormalService {
    @Autowired
    private BoardNormalRepository br;

    public List<BoardNormal> searchArticlesByTitle;

    // 검색
    public List<BoardNormal> searchArticlesByTitle(String keyword) {
        return br.findAllByTitleContains(keyword);
    }
    public List<BoardNormal> findAll() {
        return br.findAll();
    }

    // 상세 페이지
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
        // 게시글 찾기
        BoardNormal article = br.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid article Id:" + id));

        // 게시글 정보 업데이트
        article.setTitle(title);
        article.setContent(content);

        // 게시글 저장
        br.save(article);
    }

    // 삭제
    public void deleteById(Long id) {
        br.deleteById(id);
    }

}
