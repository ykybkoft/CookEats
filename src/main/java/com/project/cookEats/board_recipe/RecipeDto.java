package com.project.cookEats.board_recipe;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
@Data
public class RecipeDto {

    private Long id;

    private String RCP_NM;  // 메뉴명

    private String RCP_PARTS_DTLS; // 재료정보

    private List<String> manuals;  // 조리순서 목록

    private List<MultipartFile> manualImages; // 조리순서 이미지 파일
}

