package com.project.cookEats.board_normal;

import com.project.cookEats.member.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BoardNormalRepository extends JpaRepository<BoardNormal, Long> {

    // 특정 회원이 작성한 모든 게시글 조회
    List<BoardNormal> findAllByMember(Member member);

    // 최신순 내림차순 정렬
    Page<BoardNormal> findByTitleContainingOrderBySysDateDesc(String keyword, Pageable pageable);

    // 조회수 기준으로 내림차순 정렬
    Page<BoardNormal> findByTitleContainingOrderByViewsDesc(String keyword, Pageable pageable);

    // 추천수 기준으로 내림차순 정렬
    Page<BoardNormal> findByTitleContainingOrderByLikesDesc(String keyword, Pageable pageable);

    //혜정 코드
    @Query(nativeQuery = true, value = "select * from board_normal where title like %?1% or content like %?1% order by id desc limit 5")
    List<BoardNormal> findTotalSearch(String search);
}
