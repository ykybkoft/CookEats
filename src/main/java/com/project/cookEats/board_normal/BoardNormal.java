package com.project.cookEats.board_normal;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.project.cookEats.member.Member;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@Entity
@ToString
public class BoardNormal {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100, nullable = false)
    private String title;

    @JsonBackReference(value = "boardNormal-member")
    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn()
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Member member;

    @Column(name = "sysDate", updatable = false,  columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    @CreatedDate
    private LocalDateTime sys_date = LocalDateTime.now(); // 기본값 설정

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    @Column(name = "count")
    @ColumnDefault("0")
    private int count;

    @Column(name = "llike")
    @ColumnDefault("0")
    private int llike;

    @Column(name = "views")
    @ColumnDefault("0") // 기본값 설정
    private int views;

    @JsonManagedReference(value = "boardNormal-comments")
    @OneToMany(mappedBy = "boardNormal", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BoardNormalComment> commentList = new ArrayList<>();

    private String formattedSysDate; // 포맷된 날짜를 저장할 필드

}
