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
    @JsonBackReference(value = "member-boardComment")
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Member member;

    // 탈퇴시 정보 삭제를 위한 컬럼
    // 코멘트는 많은 사용자가 가질 수 있다. 즉, n:1 양방향 관계 설정
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "board_share_id")
    private Board_share board_share;

    @Column(length = 255, nullable = false)
    private String comment;

    @Column(name = "sysDate", updatable = false,  columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDate sysDate;

    @Column(name = "cmtLike")
    @ColumnDefault("0")
    private int cmtLike;


}
