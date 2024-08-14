package com.project.cookEats.board_normal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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


}
