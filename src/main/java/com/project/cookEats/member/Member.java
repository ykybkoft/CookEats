package com.project.cookEats.member;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.project.cookEats.board_share.entityClasses.Board_share;
import com.project.cookEats.board_share.entityClasses.Board_share_comment;
import com.project.cookEats.recipe.board_recipe.BoardRecipe;
import com.project.cookEats.recipe.board_recipe.BoardRecipeComment;
import com.project.cookEats.recipe.board_recipe.RecipeDb;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

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
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Board_share> boardShare;

    @JsonManagedReference("member-boardComment")
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Board_share_comment> board_comment;

    @JsonManagedReference("member-boardRecipe")
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BoardRecipe> boardRecipeList;

    @JsonManagedReference("member-boardRecipeComment")
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BoardRecipeComment> boardRecipeCommentList;

    @JsonManagedReference("member-recipeDb")
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RecipeDb> recipeDbList;
}
