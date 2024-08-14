package com.project.cookEats.board_normal;

import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardNormalRepository extends JpaRepository<BoardNormal, Long> {


//    Optional<BoardNormal> findById(String id);
//    Optional<BoardNormal> findByTitle(String title);
//    Optional<BoardNormal> findByMember(String member);
//    Optional<BoardNormal> findByContent(String content);

}
