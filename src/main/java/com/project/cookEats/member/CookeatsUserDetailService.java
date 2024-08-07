package com.project.cookEats.member;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CookeatsUserDetailService implements UserDetailsService {
    private final MemberRepository mr;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Member> tmp = mr.findByUsername(username);

        if(tmp.isEmpty()){
            throw new UsernameNotFoundException("해당 유저가 존재하지 않습니다.");
        }

        var user = tmp.get();
        List<GrantedAuthority> authList = new ArrayList<>();
        authList.add(new SimpleGrantedAuthority("normal"));

        CustomUser custom = new CustomUser(user.getUsername(), user.getPassword(),authList);
        custom.setId(user.getId());

        return custom;

    }
}
