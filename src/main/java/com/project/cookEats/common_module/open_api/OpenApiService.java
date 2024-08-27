package com.project.cookEats.common_module.open_api;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.cookEats.board_recipe.RecipeDB;
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
    private OpenApiRepository repository;

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

    public void fetchData() {
        logger.info("Data fetch initiated.");
        fetchDataRecursive(start, end).subscribe();
    }

    private Mono<Void> fetchDataRecursive(int start, int end) {
        int currentEnd = start + 99;
        String url = String.format("%s/%s/%d/%d", apiKey, serviceName, start, currentEnd);
        logger.info("Fetching data from URL: {}", url);

        return webClient.get().uri(url).retrieve().bodyToMono(String.class)
                .flatMap(json -> {
                    try {
                        JsonNode rootNode = objectMapper.readTree(json);
                        JsonNode dataList = rootNode.path("COOKRCP01").path("row");

                        if (!dataList.isArray() || dataList.isEmpty()) {
                            logger.info("No more data to fetch.");
                            return Mono.empty();
                        }

                        List<RecipeDB> recipeDBS = new ArrayList<>();
                        for (JsonNode node : dataList) {
                            RecipeDB recipeDb = new RecipeDB();
                            recipeDb.setRCP_NM(node.path("RCP_NM").asText());
                            recipeDb.setRCP_PARTS_DTLS(node.path("RCP_PARTS_DTLS").asText());

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

                            recipeDBS.add(recipeDb);
                        }

                        repository.saveAll(recipeDBS);
                        logger.info("Data saved to database: {} records", recipeDBS.size());

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