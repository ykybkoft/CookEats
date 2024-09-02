package com.project.cookEats.board_share.repositories;

import com.project.cookEats.board_share.entityClasses.Board_share;
import com.project.cookEats.member.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface Board_shareRepository extends JpaRepository<Board_share, Long> {

    List<Board_share> findAllByMember(Member member);

    //혜정 코드
    @Query(nativeQuery = true, value = "select * from board_share where title like %?1% or content like %?1% order by id desc limit 5")
    List<Board_share> findTotalSearch(String search);


    @Query(
            value = "SELECT * FROM board_share WHERE title LIKE CONCAT('%', :search, '%') OR content LIKE CONCAT('%', :search, '%')",
            countQuery = "SELECT COUNT(*) FROM board_share WHERE title LIKE CONCAT('%', :search, '%') OR content LIKE CONCAT('%', :search, '%')",
            nativeQuery = true
    )
    Page<Board_share> findAllSearch(@Param("search") String search, Pageable pageable);
 }