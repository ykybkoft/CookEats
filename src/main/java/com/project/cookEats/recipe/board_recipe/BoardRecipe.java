package com.project.cookEats.recipe.board_recipe;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.project.cookEats.member.Member;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "board_recipe") // 만약 Table 어노테이션이 없을 경우 테이터베이스 테이블 이름은 클래스의 이름과 유사하게 만들어짐.
@ToString // Hash코드가 아닌 데이터를 반환
@EntityListeners(AuditingEntityListener.class)
public class BoardRecipe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty("id")
    private Long id;

    // 게시글 제목 varchar(100)
    @Column(length = 100, nullable = false)
    @JsonProperty("title")
    private String title;

    @Column(columnDefinition = "TEXT", nullable = false)
    @JsonProperty("content")
    private String content;

    @JsonBackReference
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Member member;

    @Column(name = "sysDate", updatable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    @CreatedDate
    private LocalDateTime sysDate;

    @Column(name = "cntView")
    @ColumnDefault("0")
    private int cntView;

    @Column(name = "cntLike")
    @ColumnDefault("0")
    private int cntLike;

    @JsonManagedReference
    @OneToMany(mappedBy = "boardRecipe", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BoardRecipeComment> boardRecipeComments;

    @JsonBackReference
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "recipe_db_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private RecipeDb recipeDb;
}