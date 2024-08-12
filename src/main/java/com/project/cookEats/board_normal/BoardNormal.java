package com.project.cookEats.board_normal;

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

    @JsonManagedReference
    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn()
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Member member;

    @Column(name = "sysDate", nullable = false, updatable = false,  columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    @CreatedDate
    private Date sys_date;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    @Column(name = "count")
    @ColumnDefault("0")
    private int count;

    @Column(name = "llike")
    @ColumnDefault("0")
    private int llike;

    @JsonManagedReference
    @OneToMany()
    private List<BoardNormalComment> commentList = new ArrayList<>();
}
