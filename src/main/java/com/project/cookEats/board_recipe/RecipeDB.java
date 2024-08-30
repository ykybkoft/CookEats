package com.project.cookEats.board_recipe;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.project.cookEats.member.Member;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
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
@Table(name = "recipedb")
public class RecipeDB {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty("id")
    private Long id;

    @JsonBackReference(value = "member-RecipeDB")
    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "member_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonProperty("member")
    private Member member;

    @JsonManagedReference(value = "RecipeDB-Comment")
    @OneToMany(mappedBy = "recipeDB", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<RecipeComment> recipeCommentList = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "MANUALS", joinColumns = @JoinColumn(name = "recipe_id"))
    @OrderColumn(name = "step_order")
    private List<String> manuals = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "MANUAL_IMAGES", joinColumns = @JoinColumn(name = "recipe_id"))
    @OrderColumn(name = "step_order")
    private List<String> manualImages = new ArrayList<>();

    @OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RecipeDBSubInfo> subInfoList = new ArrayList<>();

    @JsonProperty("RCP_SEQ")
    private Long RCP_SEQ;   // OpenApi 일련번호

    @JsonProperty("RCP_NM")
    private String RCP_NM;  // 메뉴명

    @JsonProperty("HASH_TAG")
    private String HASH_TAG; // 해쉬태그

    @JsonProperty("ATT_FILE_NO_MAIN")
    private String ATT_FILE_NO_MAIN; // 이미지경로(소)

    @JsonProperty("ATT_FILE_NO_MK")
    private String ATT_FILE_NO_MK; // 이미지경로(대)

    @Column(columnDefinition = "TEXT")
    @JsonProperty("RCP_PARTS_DTLS")
    private String RCP_PARTS_DTLS; // 재료정보

    @Column(name = "LLIKE")
    @ColumnDefault("0")
    private int LLIKE;

    @Column(name = "CCOUNT")
    @ColumnDefault("0")
    private int CCOUNT;

    @CreationTimestamp
    @Column(name = "SYSDATE", updatable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    @CreatedDate
    private LocalDateTime SYSDATE = LocalDateTime.now(); // 기본값 설정

    @Transient
    private String formattedSysDate;
}