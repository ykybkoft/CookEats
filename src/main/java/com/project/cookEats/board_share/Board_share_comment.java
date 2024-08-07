package com.project.cookEats.board_share;

import com.project.cookEats.member.Member;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.sql.Date;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "board_share_comment")
@ToString
@EntityListeners(AuditingEntityListener.class)
public class Board_share_comment {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn()
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Member member;

    @Column(name = "sysDate", nullable = false, updatable = false,  columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Date sysDate;

    @Column(length = 255, nullable = false)
    private String comment;

    @Column(name = "cmtLike")
    @ColumnDefault("0")
    private int cmtLike;


}
