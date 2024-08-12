package com.project.cookEats.board_normal;


import com.project.cookEats.board_share.entityClasses.Board_share;
import com.project.cookEats.member.Member;
import org.springframework.data.domain.Page;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BoardNormalRepository extends JpaRepository<BoardNormal, Long> {

    List<BoardNormal> findAllByMember(Member member);


}
