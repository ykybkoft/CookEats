package com.project.cookEats.member;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;


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

    public int changePW(String newPW,Long id) {
        Optional<Member> tmp = mr.findById(id);

        if(tmp.isEmpty()){
            return 0;
        }
        Member member = tmp.get();
        member.setPassword(pe.encode(newPW));
        mr.save(member);
        return 1;
    }

    public int delete(Authentication auth) {
        CustomUser user = (CustomUser) auth.getPrincipal();
        mr.deleteById(user.getId());

        return 1;
    }
}
