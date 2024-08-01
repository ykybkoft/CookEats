package com.project.cookEats.board_normal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;

import java.sql.Date;

@Getter
@Setter
@Entity
@ToString
public class BoardNormal {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String member;
    private Date sys_date;
    private String contents;
    private Integer cnt_like;
}
