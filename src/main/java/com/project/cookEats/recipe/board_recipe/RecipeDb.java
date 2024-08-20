
package com.project.cookEats.recipe.board_recipe;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.project.cookEats.member.Member;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Getter
@Setter
@Entity
@ToString
@Table(name = "recipedb")
public class RecipeDb {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty("id")
    private Long id;    //idx

    @JsonBackReference
    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn()
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonProperty("member")
    private Member member;

    @JsonProperty("RCP_SEQ")
    private Long RCP_SEQ;   // OpenApi 일련번호

    @JsonProperty("RCP_NM")
    private String RCP_NM;  // 메뉴명

    @JsonProperty("RCP_WAY2")
    private String RCP_WAY2; // 조리방법

    @JsonProperty("RCP_PAT2")
    private String RCP_PAT2; // 요리종류

    @JsonProperty("INFO_WGT")
    private Double INFO_WGT; // 중량(1인분)

    @JsonProperty("INFO_ENG")
    private Double INFO_ENG; // 열량

    @JsonProperty("INFO_CAR")
    private Double INFO_CAR; // 탄수화물

    @JsonProperty("INFO_PRO")
    private Double INFO_PRO; // 단백질

    @JsonProperty("INFO_FAT")
    private Double INFO_FAT; // 지방

    @JsonProperty("INFO_NA")
    private Double INFO_NA; // 나트륨

    @JsonProperty("HASH_TAG")
    private String HASH_TAG; // 해쉬태그

    @JsonProperty("ATT_FILE_NO_MAIN")
    private String ATT_FILE_NO_MAIN; // 이미지경로(소)

    @JsonProperty("ATT_FILE_NO_MK")
    private String ATT_FILE_NO_MK; // 이미지경로(대)

    @Column(columnDefinition = "TEXT")
    @JsonProperty("RCP_PARTS_DTLS")
    private String RCP_PARTS_DTLS; // 재료정보

    @JsonProperty("MANUAL01")
    private String MANUAL01; // 만드는법_01

    @JsonProperty("MANUAL_IMG01")
    private String MANUAL_IMG01; // 만드는법_이미지_01

    @JsonProperty("MANUAL02")
    private String MANUAL02; // 만드는법_02

    @JsonProperty("MANUAL_IMG02")
    private String MANUAL_IMG02; // 만드는법_이미지_02

    @JsonProperty("MANUAL03")
    private String MANUAL03; // 만드는법_03

    @JsonProperty("MANUAL_IMG03")
    private String MANUAL_IMG03; // 만드는법_이미지_03

    @JsonProperty("MANUAL04")
    private String MANUAL04; // 만드는법_04

    @JsonProperty("MANUAL_IMG04")
    private String MANUAL_IMG04; // 만드는법_이미지_04

    @JsonProperty("MANUAL05")
    private String MANUAL05; // 만드는법_05

    @JsonProperty("MANUAL_IMG05")
    private String MANUAL_IMG05; // 만드는법_이미지_05

    @JsonProperty("MANUAL06")
    private String MANUAL06; // 만드는법_06

    @JsonProperty("MANUAL_IMG06")
    private String MANUAL_IMG06; // 만드는법_이미지_06

    @JsonProperty("MANUAL07")
    private String MANUAL07; // 만드는법_07

    @JsonProperty("MANUAL_IMG07")
    private String MANUAL_IMG07; // 만드는법_이미지_07

    @JsonProperty("MANUAL08")
    private String MANUAL08; // 만드는법_08

    @JsonProperty("MANUAL_IMG08")
    private String MANUAL_IMG08; // 만드는법_이미지_08

    @JsonProperty("MANUAL09")
    private String MANUAL09; // 만드는법_09

    @JsonProperty("MANUAL_IMG09")
    private String MANUAL_IMG09; // 만드는법_이미지_09

    @JsonProperty("MANUAL10")
    private String MANUAL10; // 만드는법_10

    @JsonProperty("MANUAL_IMG10")
    private String MANUAL_IMG10; // 만드는법_이미지_10

    @JsonProperty("MANUAL11")
    private String MANUAL11; // 만드는법_11

    @JsonProperty("MANUAL_IMG11")
    private String MANUAL_IMG11; // 만드는법_이미지_11

    @JsonProperty("MANUAL12")
    private String MANUAL12; // 만드는법_12

    @JsonProperty("MANUAL_IMG12")
    private String MANUAL_IMG12; // 만드는법_이미지_12

    @JsonProperty("MANUAL13")
    private String MANUAL13; // 만드는법_13

    @JsonProperty("MANUAL_IMG13")
    private String MANUAL_IMG13; // 만드는법_이미지_13

    @JsonProperty("MANUAL14")
    private String MANUAL14; // 만드는법_14

    @JsonProperty("MANUAL_IMG14")
    private String MANUAL_IMG14; // 만드는법_이미지_14

    @JsonProperty("MANUAL15")
    private String MANUAL15; // 만드는법_15

    @JsonProperty("MANUAL_IMG15")
    private String MANUAL_IMG15; // 만드는법_이미지_15

    @JsonProperty("MANUAL16")
    private String MANUAL16; // 만드는법_16

    @JsonProperty("MANUAL_IMG16")
    private String MANUAL_IMG16; // 만드는법_이미지_16

    @JsonProperty("MANUAL17")
    private String MANUAL17; // 만드는법_17

    @JsonProperty("MANUAL_IMG17")
    private String MANUAL_IMG17; // 만드는법_이미지_17

    @JsonProperty("MANUAL18")
    private String MANUAL18; // 만드는법_18

    @JsonProperty("MANUAL_IMG18")
    private String MANUAL_IMG18; // 만드는법_이미지_18

    @JsonProperty("MANUAL19")
    private String MANUAL19; // 만드는법_19

    @JsonProperty("MANUAL_IMG19")
    private String MANUAL_IMG19; // 만드는법_이미지_19

    @JsonProperty("MANUAL20")
    private String MANUAL20; // 만드는법_20

    @JsonProperty("MANUAL_IMG20")
    private String MANUAL_IMG20; // 만드는법_이미지_20

    @JsonProperty("RCP_NA_TIP")
    private String RCP_NA_TIP; // 저감 조리법 TIP

    @Column(columnDefinition = "TEXT")
    @JsonProperty("MANUAL")
    private String MANUAL;

    @Column(columnDefinition = "TEXT")
    @JsonProperty("MANUAL_IMG")
    private String MANUAL_IMG;


    @ColumnDefault("0")
    private int LLIKE;
}

