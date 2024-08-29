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

    // 회원
    List<BoardNormal> findAllByMember(Member member);

    // 최신순 내림차순 정렬
    Page<BoardNormal> findByTitleContainingOrderBySysDateDesc(String keyword, Pageable pageable);

    // 조회수 기준으로 내림차순 정렬
    Page<BoardNormal> findByTitleContainingOrderByViewsDesc(String keyword, Pageable pageable);

    // 추천수 기준으로 내림차순 정렬
    Page<BoardNormal> findByTitleContainingOrderByLikesDesc(String keyword, Pageable pageable);

    // 제목에 키워드가 포함된 게시글을 연관도 기준으로 내림차순 정렬
    // 연관도 기준으로 정렬하는 쿼리는 커스텀 쿼리를 사용해야 할 수 있습니다. 여기서는 예를 보여주기 위해 작성합니다.
    // 예를 들어, 연관도를 별도로 설정하거나, 별도의 필드를 추가하거나, 특정 로직을 구현할 필요가 있습니다.
    // List<BoardNormal> findByTitleContainingOrderByRelevanceDesc(String keyword);

    //혜정 코드
    @Query(nativeQuery = true, value = "select * from board_normal where title like %?1% or content like %?1% order by id desc limit 5")
    List<BoardNormal> findTotalSearch(String search);
}
