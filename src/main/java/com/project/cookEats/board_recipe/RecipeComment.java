package com.project.cookEats.board_recipe;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.project.cookEats.member.Member;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "recipe_comment")
@ToString
@EntityListeners(AuditingEntityListener.class)
public class RecipeComment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @JsonBackReference(value = "member-Comment")
    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "member_id")
    private Member member;

    @JsonBackReference(value = "RecipeDB-Comment")
    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "recipedb_id") // 컬럼 이름은 RecipeDB의 ID와 일치해야 합니다.
    private RecipeDB recipeDB;

    @Column(nullable = false)
    private String comment_contents;

    @Column(name = "LLIKE")
    @ColumnDefault("0")
    private long LLIKE;

    @CreationTimestamp
    @Column(name = "sysDate", updatable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime sys_date;
}
