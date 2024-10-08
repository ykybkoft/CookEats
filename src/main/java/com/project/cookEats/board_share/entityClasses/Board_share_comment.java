package com.project.cookEats.board_share.entityClasses;

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

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "board_share_comment")
@ToString
@EntityListeners(AuditingEntityListener.class)
public class Board_share_comment {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 댓글 작성자
    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Member member;

    @Column(length = 255, nullable = false)
    private String comment;

    @Column(name = "sysdate", nullable = false, updatable = false,  columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDate sysdate;

    @Column(name = "cmtlike")
    @ColumnDefault("0")
    private int cmtlike;

    // 탈퇴시 정보 삭제를 위한 컬럼
    // 코멘트는 많은 사용자가 가질 수 있다. 즉, n:1 양방향 관계 설정
    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_share_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Board_share boardShare;
}
