package com.project.cookEats.member;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository mr;
    private final PasswordEncoder pe;
    public int join(Member row) {
        row.setPassword(pe.encode(row.getPassword()));
        mr.save(row);
        return 0;
    }
}
