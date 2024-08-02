package com.project.cookEats.board_share;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
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

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String nick;

    @CreatedDate // 시간 자동 설정, java.sql.Date = date 타입으로 매핑
    @Column(name = "sys_date", nullable = false, updatable = false)
    private Date sysDate;

    @Column(length = 255, nullable = false)
    private String comment;

    @Column(name = "cmt_like")
    private int cmtLike;
}
