package com.project.cookEats.board_share;

import com.project.cookEats.member.Member;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.ColumnDefault;
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

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "memberId", foreignKey = @ForeignKey(name = "fk_member_order1",
            foreignKeyDefinition = "FOREIGN KEY (id) REFERENCES member(id) " +
                    "ON DELETE CASCADE ON UPDATE CASCADE"))
    private Member member;

    @CreatedDate // 시간 자동 설정, java.sql.Date = date 타입으로 매핑
    @Column(name = "sysDate", nullable = false, updatable = false)
    private Date sysDate;

    @Column(length = 255, nullable = false)
    private String comment;

    @Column(name = "cmtLike")
    @ColumnDefault("0")
    private int cmtLike;
}
