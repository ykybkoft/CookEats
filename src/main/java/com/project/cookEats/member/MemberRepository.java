package com.project.cookEats.member;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByUsername(String username);


    Optional<Member> findByEmail(String email);

    Optional<Member> findByNick(String nick);

    Optional<Member> findByPhone(String phone);



}
