package com.project.cookEats.board_normal;

import com.project.cookEats.member.Member;
import com.project.cookEats.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
public class UserService {
    private final MemberRepository memberRepository;
    @Autowired
    public UserService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public String getCurrentUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            return userDetails.getUsername(); // 또는 다른 사용자 정보를 반환
        }
        return null;
    }

    // 현재 로그인된 사용자의 Member 객체를 반환
    public Member getCurrentMember() {
        String username = getCurrentUsername();
        if (username == null || username.isEmpty()) {
            System.out.println("No authenticated user found");
            return null;
        }
        return memberRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
    }
}
