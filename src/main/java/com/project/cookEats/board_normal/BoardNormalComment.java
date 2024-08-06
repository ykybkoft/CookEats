package com.project.cookEats.board_normal;

import com.project.cookEats.member.Member;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.annotation.Id;

import java.sql.Date;

@Getter
@Setter
@EntityScan
@ToString
public class BoardNormalComment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn()
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Member member;

    @Column(name = "sysDate", nullable = false, updatable = false,  columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Date sys_date;

    @Column(length = 255, nullable = false)
    private String comment_contents;

    @Column(name = "cmtLike")
    @ColumnDefault("0")
    private long comment_like;
}
