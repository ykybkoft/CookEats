package com.project.cookEats.board_share.repositories;

import com.project.cookEats.board_share.entityClasses.Board_share;
import com.project.cookEats.member.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface Board_shareRepository extends JpaRepository<Board_share, Long> {

    List<Board_share> findAllByMember(Member member);
}
