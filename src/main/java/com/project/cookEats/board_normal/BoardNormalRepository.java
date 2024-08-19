package com.project.cookEats.board_normal;


import com.project.cookEats.board_share.entityClasses.Board_share;
import com.project.cookEats.member.Member;
import org.springframework.data.domain.Page;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BoardNormalRepository extends JpaRepository<BoardNormal, Long> {

    // 회원
    List<BoardNormal> findAllByMember(Member member);

    // 검색
    List<BoardNormal> findAllByTitleContains(String keyword);


}
