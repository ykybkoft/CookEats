package com.project.cookEats.common_module.open_api;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.cookEats.board_recipe.RecipeDB;
import com.project.cookEats.board_recipe.RecipeDBSubInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@Service
public class OpenApiService {

    private static final Logger logger = LoggerFactory.getLogger(OpenApiService.class);

    @Autowired
    private OpenApiRepository openApiRepository;

    private final WebClient webClient;
    private final ObjectMapper objectMapper;

    @Value("${api.openapi.key}")
    private String apiKey;

    @Value("${api.openapi.serviceName}")
    private String serviceName;

    @Value("${api.openapi.delayMillis}")
    private long delayMillis;

    @Value("${api.openapi.start}")
    private int start;

    @Value("${api.openapi.end}")
    private int end;

    @Autowired
    public OpenApiService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("http://openapi.foodsafetykorea.go.kr/api/").build();
        this.objectMapper = new ObjectMapper();
    }

    // 데이터 수집 시작
    public void fetchData() {
        logger.info("Data fetch initiated.");
        fetchDataRecursive(start, end).subscribe();
    }

    // 재귀적으로 데이터를 수집하는 메서드
    private Mono<Void> fetchDataRecursive(int start, int end) {
        int currentEnd = start + 99; // 현재 데이터 범위 끝
        String url = String.format("%s/%s/%d/%d", apiKey, serviceName, start, currentEnd);
        logger.info("Fetching data from URL: {}", url);

        return webClient.get().uri(url).retrieve().bodyToMono(String.class)
                .flatMap(json -> {
                    try {
                        JsonNode rootNode = objectMapper.readTree(json);
                        JsonNode dataList = rootNode.path("COOKRCP01").path("row");

                        // 더 이상 데이터가 없을 경우
                        if (!dataList.isArray() || dataList.isEmpty()) {
                            logger.info("No more data to fetch.");
                            return Mono.empty();
                        }

                        List<RecipeDB> recipeDBS = new ArrayList<>();
                        for (JsonNode node : dataList) {
                            RecipeDB recipeDb = new RecipeDB();

                            // RecipeDB에 저장될 데이터 설정
                            recipeDb.setRCP_SEQ(node.path("RCP_SEQ").asLong());
                            recipeDb.setRCP_NM(node.path("RCP_NM").asText());
                            recipeDb.setHASH_TAG(node.path("HASH_TAG").asText());
                            recipeDb.setATT_FILE_NO_MAIN(node.path("ATT_FILE_NO_MAIN").asText());
                            recipeDb.setATT_FILE_NO_MK(node.path("ATT_FILE_NO_MK").asText());
                            recipeDb.setRCP_PARTS_DTLS(node.path("RCP_PARTS_DTLS").asText());

                            // Manuals와 Manual Images 설정
                            List<String> manuals = new ArrayList<>();
                            List<String> manualImages = new ArrayList<>();

                            for (int i = 1; i <= 20; i++) {
                                String manualKey = "MANUAL" + String.format("%02d", i);
                                String manualImageKey = "MANUAL_IMG" + String.format("%02d", i);

                                String manual = node.path(manualKey).asText();
                                String manualImage = node.path(manualImageKey).asText();

                                if (!manual.isEmpty()) {
                                    manuals.add(manual);
                                }
                                if (!manualImage.isEmpty()) {
                                    manualImages.add(manualImage);
                                }
                            }

                            recipeDb.setManuals(manuals);
                            recipeDb.setManualImages(manualImages);

                            // RecipeDBSubInfo에 저장될 데이터 설정
                            RecipeDBSubInfo subInfo = new RecipeDBSubInfo();
                            subInfo.setRecipe(recipeDb); // RecipeDB와 연관 설정
                            subInfo.setRCP_WAY2(node.path("RCP_WAY2").asText());
                            subInfo.setRCP_PAT2(node.path("RCP_PAT2").asText());
                            subInfo.setINFO_WGT(node.path("INFO_WGT").asDouble());
                            subInfo.setINFO_ENG(node.path("INFO_ENG").asDouble());
                            subInfo.setINFO_CAR(node.path("INFO_CAR").asDouble());
                            subInfo.setINFO_PRO(node.path("INFO_PRO").asDouble());
                            subInfo.setINFO_FAT(node.path("INFO_FAT").asDouble());
                            subInfo.setINFO_NA(node.path("INFO_NA").asDouble());

                            // subInfo를 recipeDb에 추가
                            recipeDb.getSubInfoList().add(subInfo);

                            recipeDBS.add(recipeDb);
                        }

                        // DB에 저장
                        openApiRepository.saveAll(recipeDBS);
                        logger.info("Data saved to database: {} records", recipeDBS.size());

                        // 다음 범위의 데이터를 수집
                        if (currentEnd < end) {
                            return Mono.delay(Duration.ofMillis(delayMillis))
                                    .then(fetchDataRecursive(currentEnd + 1, end + 100));
                        } else {
                            logger.info("Data fetch completed.");
                            return Mono.empty();
                        }
                    } catch (Exception e) {
                        logger.error("Error processing data", e);
                        return Mono.error(e);
                    }
                })
                .onErrorResume(e -> {
                    logger.error("Error fetching data", e);
                    return Mono.empty();
                });
    }
}
