package com.project.cookEats.recipe.board_recipe;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.project.cookEats.member.Member;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.sql.Date;

@Getter
@Setter
@Entity
@Table(name = "board_recipe_comment")
@ToString
@EntityListeners(AuditingEntityListener.class)
public class BoardRecipeComment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonBackReference
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Member member;

    @Column(nullable = false)
    private String comment;

    @Column(name = "sysDate",  updatable = false,  columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Date sysDate;

    @Column(name = "cntcmtLike")
    @ColumnDefault("0")
    private int cntcmtLike;

    @JsonBackReference
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "board_recipe_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private BoardRecipe boardRecipe;
}
