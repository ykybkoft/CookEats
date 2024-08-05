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

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "board_share") // 만약 Table 어노테이션이 없을 경우 테이터베이스 테이블 이름은 클래스의 이름과 유사하게 만들어짐.
@ToString // Hash코드가 아닌 데이터를 반환
@EntityListeners(AuditingEntityListener.class)
public class Board_share {
    // @GeneratedValue로 게시글 시퀀스 자동 번호 증가 추가
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 게시글 제목 varchar(100)
    @Column(length = 100, nullable = false)
    private String title;

    // 1-1. ManyToOne : 다대일 테이블 외래키 연결 어노테이션
    // 1-2. cascade = CascadeType.ALL : 부모 엔티티의 CRUD 변경사항이 자식 엔티티에게 전달되도록 함
    // 1-3. orphanRemoval = true : 부모 엔티티에서 자식 엔티티를 제거할 때 삭제 되는 자식 엔티티를 자동으로 데이터베이스에서 제거하는 기능이다.
    @ManyToOne(cascade = CascadeType.MERGE)
    // 1. foreignKeyDefinition : 외래키 제약 조건을 입력하기 위해 사용되는 속성
    @JoinColumn() //foreign key 값을 채우기 위한 어노테이션
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Member member;

    // 1. 작성일자 - updateble = fales는 게시글 시간이 변경되지 않음을 의미.
    // 2-1 LsatModifiedDate - Spring Data JPA의 감사(auditing) 기능 사용하여 엔티티 작성 및 수정시 자동으로 현재 시간을 설정함.
    // 2-2 하지만, updateble = fales 조건을 통해 생성 시에만 자동으로 시간이 설정되고 이후는 글의 내용이 수정되어도 시간이 변하지 않음.
    // 2-3 이 기능을 사용하려면 @EntityListeners(AuditingEntityListener.class)를 클래스에 추가해야 됨.
    @Column(name = "sysDate", nullable = false, updatable = false,  columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    @CreatedDate // TimeStemp 데이터 타입
    private LocalDateTime sysDate;

    // 게시글
    // 1-1 columDefinition = "TEXT" = 이 설정을 통해 해당 컬럼의 데이터 타입을 TEXT로 지정함.
    // 1-2 데이터 타입을 TEXT로 설정한 이유는 글의 길이가 매우 길 수 있기에 이를 저장할 수 있게하기 위해서임.
    // 1-3 만약 length와 TEXT 둘 모두 사용하지 않을 경우 데이터 베이스 벤더의 기본 값 즉, MySQL 기본 길이 설정인 varchar(255)로 설정되어 255만큼의 텍스트를 게시글에 담을 수 있음.
    // MySQL = varchar(255), PostgerSQL = 기본설정이 "TEXT"
    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    // 조회수 view count의 약자
    // 1-1. ColumnDefault : 조회수 초기값을 0으로 설정하기 위해 사용
    // 1-2. 위의 방식을 사용하지 않을 경우 @Column(columnDefinition = "integer default -1")로 대체 이때 Wrapper class인 integer를 사용하는 이유는 null값이 들어갔을 경우의 에러를 방지하기 위함.
    @Column(name = "v_count")
    @ColumnDefault("0")
    private int vCount;

    // 좋아요 수
    @Column(name = "cntLike")
    @ColumnDefault("0")
    private int cntLike;

    @OneToMany()
    private List<Board_share_comment> commentList = new ArrayList<>();


    
}
