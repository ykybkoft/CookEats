package com.project.cookEats.member;

import com.project.cookEats.board_normal.BoardNormal;
import com.project.cookEats.board_normal.BoardNormalRepository;
import com.project.cookEats.board_share.entityClasses.Board_share;
import com.project.cookEats.board_share.repositories.Board_shareRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class MemberService {

    private final Board_shareRepository bsr;
    private final BoardNormalRepository bnr;
    @Autowired
    private final MemberRepository mr;
    private final PasswordEncoder pe;
    public int join(Member row) {
        row.setPassword(pe.encode(row.getPassword()));
        mr.save(row);
        return 0;
    }

    public Member findMember(Authentication auth) {

        return mr.findById(findId(auth)).get();
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

        mr.deleteById(findId(auth));

        return 1;
    }

    public Long findId(Authentication auth){
        CustomUser user = (CustomUser) auth.getPrincipal();
        return user.getId();
    }
    public List<BoardNormal> findBoardNormal(Authentication auth) {
        Member member =new Member();
        member.setId(findId(auth));

        return bnr.findAllByMember(member);
    }
    public List<Board_share> findBoardShare(Authentication auth) {
        Member member =new Member();
        member.setId(findId(auth));

        return bsr.findAllByMember(member);
    }

    // 현주 write
    public Member getMemberByUsername(String username) {
        Optional<Member> memberOpt = mr.findByUsername(username);
        if (memberOpt.isPresent()) {
            return memberOpt.get(); // 값이 존재하면 반환
        } else {
            throw new RuntimeException("Member not found"); // 값이 없으면 예외를 던짐
        }
    }
}
