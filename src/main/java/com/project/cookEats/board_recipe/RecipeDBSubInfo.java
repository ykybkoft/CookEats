package com.project.cookEats.board_recipe;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "recipe_db_sub_info")
public class RecipeDBSubInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "recipe_id")
    private RecipeDB recipe;

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
}
