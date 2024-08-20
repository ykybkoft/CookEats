package com.project.cookEats.member;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.project.cookEats.board_share.entityClasses.Board_share;
import com.project.cookEats.board_share.entityClasses.Board_share_comment;
import com.project.cookEats.board_recipe.RecipeComment;
import com.project.cookEats.board_recipe.RecipeDB;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Date;
import java.util.List;

@Getter
@Setter
@Entity
//@ToString
public class Member {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id ;

    @Column(length = 20, unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, unique = true, length = 20)
    private String nick;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false, unique = true)
    private String phone;

    @Column(nullable = false)
    private Date birth;

    // 회원탈퇴시 정보삭제를 위한 코드
    // 멤버가 사라지면 연관된 게시물과 코멘트를 삭제 해야됨으로 멤버에서 1:n 관계를 설정함
    // 멤버는 많은 보드 쉐어 게시물을 가질 수 있다. 즉, 1:n 양방향 관계 설정
    @JsonManagedReference("member-boardShare")
    //json~~reference는 무한 순환 참조를 방지하기 위해 사용
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Board_share> boardShare;

    @JsonManagedReference("member-boardShareComment")
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Board_share_comment> board_comment;

    @JsonManagedReference("member-RecipeDB")
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RecipeDB> recipeDBList;

    @JsonManagedReference("member-RecipeComment")
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RecipeComment> recipeCommentList;
}