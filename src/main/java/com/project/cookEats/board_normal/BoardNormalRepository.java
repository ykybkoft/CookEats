package com.project.cookEats.board_normal;

import com.project.cookEats.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BoardNormalRepository extends JpaRepository<Member, Long> {


    Optional<Member> findByUsername(String username);
}
