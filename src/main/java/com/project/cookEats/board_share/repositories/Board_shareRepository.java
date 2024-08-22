package com.project.cookEats.board_share.repositories;

import com.project.cookEats.board_normal.BoardNormal;
import com.project.cookEats.board_share.entityClasses.Board_share;
import com.project.cookEats.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface Board_shareRepository extends JpaRepository<Board_share, Long> {



    List<Board_share> findAllByMember(Member member);

    //혜정 코드
    @Query(nativeQuery = true, value = "select * from board_share where title like %?1% or content like %?1% order by id desc limit 5")
    List<Board_share> findTotalSearch(String search);
}
