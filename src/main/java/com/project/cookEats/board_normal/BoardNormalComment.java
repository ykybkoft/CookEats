

package com.project.cookEats.board_normal;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.project.cookEats.member.Member;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.Date;

@Getter
@Setter
@Entity
public class BoardNormalComment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonBackReference(value = "member-boardComment")
    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn()
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Member member;

    @JsonBackReference(value = "comment-boardNormal")
    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "board_normal_id")
    private BoardNormal boardNormal;

    @Column(name = "sysDate", updatable = false,  columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Date sys_date;

    @Column(nullable = false)
    private String contents;

    @Column(name = "cmtLike")
    @ColumnDefault("0")
    private long comment_like;

    @Override
    public String toString() {
        return "BoardNormalComment{id=" + id +
                ", contents='" + contents + '\'' +
                ", boardNormalId=" + (boardNormal != null ? boardNormal.getId() : null) +
                ", memberId=" + (member != null ? member.getId() : null) + '}';
    }
}