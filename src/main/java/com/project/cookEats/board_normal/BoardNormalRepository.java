package com.project.cookEats.board_normal;

import com.project.cookEats.board_share.entityClasses.Board_share;
import com.project.cookEats.member.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;

import java.awt.print.Pageable;
import java.util.List;
import java.util.Optional;

public interface BoardNormalRepository extends JpaRepository<BoardNormal, Long> {

    List<BoardNormal> findAllByMember(Member member);
    //Optional<Member> findByUsername(String username);

//    Page<BoardNormal> findPageBy(Pageable page);
//    List<BoardNormal> findAllByTitleContains(String title);

}
