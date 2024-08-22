package com.project.cookEats.board_normal;

import com.project.cookEats.member.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BoardNormalRepository extends JpaRepository<BoardNormal, Long> {

    // 특정 회원이 작성한 모든 게시글 조회
    List<BoardNormal> findAllByMember(Member member);

    // 제목에 키워드가 포함된 게시글을 작성일 기준으로 내림차순 정렬
    Page<BoardNormal> findByTitleContainingOrderBySysDateDesc(String keyword, Pageable pageable);

    // 제목에 키워드가 포함된 게시글을 조회수 기준으로 내림차순 정렬
    Page<BoardNormal> findByTitleContainingOrderByCountDesc(String keyword, Pageable pageable);

    // 제목에 키워드가 포함된 게시글을 추천수 기준으로 내림차순 정렬
    Page<BoardNormal> findByTitleContainingOrderByLikesDesc(String keyword, Pageable pageable);
}

