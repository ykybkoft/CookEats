package com.project.cookEats.member;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;


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

    public Member findMember(Authentication auth) {
        CustomUser user = (CustomUser) auth.getPrincipal();

        return mr.findById(user.getId()).get();
    }


    public boolean check(String value, String type) {


        switch (type){
            case "username":
                return mr.findByUsername(value).isPresent();
            case "email":
                return mr.findByEmail(value).isPresent();
            case "nick":
                return mr.findByNick(value).isPresent();
            case "phone":
                return mr.findByPhone(value).isPresent();
        }
        return true;
    }

    public boolean checkPW(String password, Authentication auth) {
        Member user = findMember(auth);
        if(pe.matches(password,user.getPassword())){
            return true;
        }
        return false;
    }
}
