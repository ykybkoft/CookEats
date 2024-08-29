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
import java.util.List;

@Getter
@Setter
@Entity
@ToString
public class BoardNormal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100, nullable = false)
    private String title;

    @JsonBackReference(value = "boardNormal-member")
    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "member_id") // Foreign key column name
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Member member;

    @Column(name = "sys_date", updatable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    @CreatedDate
    private LocalDateTime sysDate = LocalDateTime.now(); // 기본값 설정

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    @Column(name = "count")
    @ColumnDefault("0")
    private int count;

    @Column(name = "likes") // 'llike' 필드명을 'likes'로 수정
    @ColumnDefault("0")
    private int likes;

    @Column(name = "views")
    @ColumnDefault("0") // 기본값 설정
    private int views;

    @JsonManagedReference(value = "boardNormal-comments")
    @OneToMany(mappedBy = "boardNormal", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BoardNormalComment> commentList = new ArrayList<>();

    // 'formattedSysDate'는 데이터베이스 컬럼이 아니므로, 쿼리 메서드와 관련되지 않음
    // 포맷된 날짜를 저장하기 위한 계산된 필드
    @Transient
    private String formattedSysDate;

    // 조회수 증가 메서드
    public void increaseViews() {
        this.views++;
    }
}
