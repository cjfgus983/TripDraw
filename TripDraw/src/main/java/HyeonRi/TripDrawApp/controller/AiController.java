package HyeonRi.TripDrawApp.controller;


import HyeonRi.TripDrawApp.dto.PlaceInfoDto;
import HyeonRi.TripDrawApp.service.AiService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;
@RestController
@RequestMapping("/api/ai")
@RequiredArgsConstructor
public class AiController {

    private final AiService aiService;

    /**
     * 1) DALL·E 편집 API 호출 후 URL 반환
     */
    @PostMapping(value = "/transform", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Map<String, String>> transformImage(
            @RequestPart("file") MultipartFile file
    ) {
        try {
            String transformedUrl = aiService.transformWithDallE(file);
            return ResponseEntity.ok(Map.of("transformedImageUrl", transformedUrl));
        } catch (IOException e) {
            // 로깅 추가 가능
            return ResponseEntity
                    .status(500)
                    .body(Map.of("error", "이미지 변환 중 오류가 발생했습니다."));
        }
    }

    /**
     * 2) GPT API 호출해 여행지 추천
     */
    @PostMapping("/recommend")
    public ResponseEntity<List<PlaceInfoDto>> recommendPlaces(@RequestBody Map<String,String> body) {
        try {
            List<PlaceInfoDto> places = aiService.recommendPlacesByGpt(body.get("imageUrl"));
            return ResponseEntity.ok(places);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(List.of());
        }
    }

}
