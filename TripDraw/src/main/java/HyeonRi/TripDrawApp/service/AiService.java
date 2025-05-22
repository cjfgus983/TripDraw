package HyeonRi.TripDrawApp.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import HyeonRi.TripDrawApp.dto.PlaceInfoDto;
import HyeonRi.TripDrawApp.dto.tripPlan.ItineraryItemDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class AiService {

    @Value("${openai.api.key}")
    private String apiKey;

    @Value("${google.places.api.key}")
    private String googleApiKey;



    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    
    private static final Map<String,String> COUNTRY_CODE = Map.of(
    	    "한국", "KR",
    	    "일본", "JP",
    	    "미국", "US",
    	    "프랑스", "FR"
    	    // 필요하신 나라들 추가...
    	);

    /**
     * 1) 이미지 설명 → 2) 그 설명으로 “새 이미지 생성” (edit이 아니라 generation 사용)
     */
    public String transformWithDallE(MultipartFile file) throws IOException {
        // Base64로 dataUri 준비
        String base64 = Base64.getEncoder().encodeToString(file.getBytes());
        String dataUri = "data:" + file.getContentType() + ";base64," + base64;

        // 1) GPT vision에 이미지 넣어 설명 받기
        String description = describeImageByDataUri(dataUri);

        // 2) 받은 설명으로 DALL·E 3 호출해 새 이미지 생성
        return generateImageFromPrompt(description);
    }


    /** GPT에게 데이터 URI로 전달된 이미지를 설명하도록 요청 */
    private String describeImageByDataUri(String dataUri) throws IOException {
        String chatUrl = "https://api.openai.com/v1/chat/completions";

        // 1) payload 생성
        ObjectNode payload = objectMapper.createObjectNode();
        payload.put("model", "gpt-4o-mini");        // vision-enabled 모델
        payload.put("max_tokens", 500);             // 필요한 만큼 토큰 제한

        ArrayNode messages = payload.putArray("messages");
        // 시스템 프롬프트
        messages.addObject()
                .put("role", "system")
                .put("content", "You are an assistant that provides vivid, detailed descriptions in Korean.");

        // 사용자 메시지: 텍스트 + 이미지
        ObjectNode userMsg = messages.addObject();
        userMsg.put("role", "user");
        ArrayNode contentArr = userMsg.putArray("content");

        // 1-1) 텍스트 파트
        contentArr.addObject()
                .put("type", "text")
                .put("text", "이 이미지를 아주 구체적으로 설명해주세요.");

        // 1-2) 이미지 파트 (여기에 dataUri 또는 public URL)
        ObjectNode img = contentArr.addObject()
                .put("type", "image_url")
                .putObject("image_url");
        img.put("url", dataUri);   // dataUri 대신 외부 업로드된 URL을 써도 됩니다.

        // 2) 호출
        HttpHeaders h = new HttpHeaders();
        h.setContentType(MediaType.APPLICATION_JSON);
        h.setBearerAuth(apiKey);

        String resp = restTemplate
                .postForObject(chatUrl, new HttpEntity<>(payload.toString(), h), String.class);

        // 3) 결과 파싱
        String content = objectMapper.readTree(resp)
                .path("choices").get(0).path("message").path("content").asText().trim();

        System.out.println(">>>GPT가 바라본 사진 설명" + content);

        return content;
    }


    /** 설명(prompt)으로 DALL·E 생성 API를 호출 */
    private String generateImageFromPrompt(String prompt) throws IOException {
        String url = "https://api.openai.com/v1/images/generations";

        JsonNode body = objectMapper.createObjectNode()
                .put("model", "dall-e-3")
                .put("prompt", prompt)
                .put("n", 1)
                .put("size", "1024x1024");

        HttpHeaders h = new HttpHeaders();
        h.setContentType(MediaType.APPLICATION_JSON);
        h.setBearerAuth(apiKey);

        String resp = restTemplate.postForObject(url, new HttpEntity<>(body.toString(), h), String.class);
        return objectMapper.readTree(resp)
                .path("data").get(0).path("url").asText();
    }

/// ////////////////////////////////////////////////////////////////////////////////////////////

    // (1) GPT에 이미지 URL 보내서 장소명 3개 뽑기 (기존 로직과 동일)
    public List<String> fetchPlaceNamesByDescription(String description) throws IOException {
        String url = "https://api.openai.com/v1/chat/completions";

        // 2) 사용자 프롬프트: 이미지 설명 기반 3개 명소 제안, JSON 배열로 반환
        String system = "당신은 여행 전문가입니다. 오타 없이 정확한 한국어 명소 이름을 반환해주세요.";
        String user = String.format(
                "다음은 이미지 설명입니다:\n\"%s\"\n" +
                        "이 설명을 바탕으로 전 세계의 관련 명소 3곳을 제안해주세요. " +
                        "반환 형식은 JSON 배열로, 예시:\n" +
                        "[\"후지산, 일본\", \"타임스퀘어, 미국 뉴욕\", \"시드니 오페라하우스, 호주\"]",
                description
        );


        ObjectNode payload = objectMapper.createObjectNode()
                .put("model", "gpt-4o-mini")
                .set("messages", objectMapper.createArrayNode()
                        .add(objectMapper.createObjectNode().put("role","system").put("content",system))
                        .add(objectMapper.createObjectNode().put("role","user").put("content",user))
                );

        System.out.println("▶ [fetchPlaceNames] payload: " + payload);
        String resp = restTemplate.postForObject(
                url,
                new HttpEntity<>(payload.toString(), buildJsonHeaders(apiKey)),
                String.class
        );
        System.out.println("▶ [fetchPlaceNames] raw GPT response: " + resp);

        String content = stripFences(objectMapper.readTree(resp)
                .path("choices").get(0).path("message").path("content").asText()
        );
        System.out.println("▶ [fetchPlaceNames] stripped content: " + content);

        JsonNode arr = objectMapper.readTree(content);
        List<String> names = new ArrayList<>();
        arr.forEach(n -> {
            String s = n.asText();
            System.out.println("▶ [fetchPlaceNames] parsed item: " + s);
            names.add(s);
        });
        System.out.println("▶ [fetchPlaceNames] final list: " + names);
        return names;
    }



    // (2) GPT에 장소명 보내서 한 문장 설명 뽑기
    private String fetchDescription(String placeName) throws IOException {
        String url = "https://api.openai.com/v1/chat/completions";
        String system = "넌 전세계 장소 전문가야.";
        String user = String.format(
                "'%s' 장소의 특징에 대해 한국어 한 문장으로 간단하게 설명해줘.",
                placeName
        );

        JsonNode payload = objectMapper.createObjectNode()
                .put("model", "gpt-4o-mini")
                .set("messages", objectMapper.createArrayNode()
                        .add(objectMapper.createObjectNode().put("role","system").put("content",system))
                        .add(objectMapper.createObjectNode().put("role","user").put("content",user))
                );

        HttpHeaders h = new HttpHeaders();
        h.setContentType(MediaType.APPLICATION_JSON);
        h.setBearerAuth(apiKey);

        String resp = restTemplate.postForObject(url, new HttpEntity<>(payload.toString(), h), String.class);
        String content = objectMapper.readTree(resp)
                .path("choices").get(0).path("message").path("content").asText();
        return stripFences(content);
    }
    
    /**
     * (3) 지정된 문장을 n개의 문장으로 요약해서 돌려줍니다.
     */
    public String summarizeInSentences(String text, int sentences) throws IOException {
        String system = "당신은 뛰어난 한국어 글 요약 전문가입니다.";
        String user = String.format("다음 글을 한국어 %d문장으로 간단히 요약해줘:\n\n%s", sentences, text);

        ArrayNode messages = objectMapper.createArrayNode()
            .add(objectMapper.createObjectNode()
                .put("role","system")
                .put("content",system))
            .add(objectMapper.createObjectNode()
                .put("role","user")
                .put("content",user));

        ObjectNode payload = objectMapper.createObjectNode()
            .put("model","gpt-4o-mini")
            .set("messages", messages);

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(apiKey);
        headers.setContentType(MediaType.APPLICATION_JSON);

        String resp = restTemplate.postForObject(
            "https://api.openai.com/v1/chat/completions",
            new HttpEntity<>(payload.toString(), headers),
            String.class
        );

        JsonNode choice = objectMapper.readTree(resp)
            .path("choices").get(0)
            .path("message").path("content");

        return choice.asText().strip();
    }
 // 결과 포맷
 // 내부 static 클래스
    public static class PlanResult {
        // 기존 일정 (unchanged)
        public List<List<ItineraryItemDto>> itinerary;
        public List<ItineraryItemDto> nearbyCafes;
        public List<ItineraryItemDto> nearbyRestaurants;
        public List<ItineraryItemDto> nearbyTours;
    }
    
    public List<List<ItineraryItemDto>> recalculateItineraryTimes(
    	    List<List<ItineraryItemDto>> itinerary
    	) throws IOException {
    	// ─── 1) 기존 start/end 시간 제거 ───────────────────────────────────────────
    	List<List<ItineraryItemDto>> stripped = itinerary.stream()
    	        .map(day -> day.stream()
    	            .map(item -> {
    	                ItineraryItemDto copy = new ItineraryItemDto();
    	                copy.setName(item.getName());
    	                copy.setCategory(item.getCategory());
    	                copy.setStartTime("");
    	                copy.setEndTime("");
    	                return copy;
    	            })
    	            .collect(Collectors.toList())
    	        )
    	        .collect(Collectors.toList());

    	    // 2) 프롬프트 생성 — **순서 유지** 지시 추가
    	    String system = "당신은 여행 일정 전문가입니다. 각 장소 간 이동 시간을 고려하여 HH:mm 형식으로 startTime과 endTime을 채워주세요.";
    	    String user = String.format(
    	        "다음은 기존 일정입니다. *시간 정보는 모두 무시하세요*, " +
    	        "*배열의 순서는 절대 변경하지 마시고*, 이 순서 안에서만 새로 시간을 채워주세요. " +
    	        "일정은 하루 09:00~21:00 사이에만 배치되도록 해주세요. " +
    	        "결과는 순수한 JSON 배열로, 설명 없이 반환해주세요:\n\n%s",
    	        objectMapper.writeValueAsString(stripped)
    	    );

    	    ObjectNode payload = objectMapper.createObjectNode()
    	        .put("model", "gpt-4o-mini")
    	        .set("messages", objectMapper.createArrayNode()
    	            .add(objectMapper.createObjectNode().put("role","system").put("content", system))
    	            .add(objectMapper.createObjectNode().put("role","user").put("content", user)));

    	    // 이하 호출 → fence 제거 → 파싱 로직은 그대로...
    	    String resp = restTemplate.postForObject(
    	        "https://api.openai.com/v1/chat/completions",
    	        new HttpEntity<>(payload.toString(), buildJsonHeaders(apiKey)),
    	        String.class
    	    );

    	    // fence 제거 & JSON 배열만 추출
    	    String content = stripFences(objectMapper.readTree(resp)
    	        .path("choices").get(0).path("message").path("content").asText());
    	    int start = content.indexOf('['), end = content.lastIndexOf(']');
    	    if (start < 0 || end <= start) {
    	        throw new IOException("유효한 JSON 배열을 찾을 수 없습니다:\n" + content);
    	    }
    	    String jsonArray = content.substring(start, end + 1)
    	        .replaceAll("(?m)^\\s*//.*$", "")      // 주석 제거
    	        .replaceAll(",\\s*(?=\\]|\\})", "");   // 트레일링 콤마 제거

    	    JsonNode root = objectMapper.readTree(jsonArray);
    	    List<List<ItineraryItemDto>> result = new ArrayList<>();

    	    if (root.isArray() && root.size() > 0 && root.get(0).isArray()) {
    	        // [[...], [...], ...] (중첩 배열)
    	        for (JsonNode dayNode : root) {
    	            List<ItineraryItemDto> dayList = objectMapper.convertValue(
    	                dayNode, new TypeReference<List<ItineraryItemDto>>() {}
    	            );
    	            result.add(dayList);
    	        }
    	    } else if (root.isArray()) {
    	        // [..., ..., ...] 한 단계 배열 → 단일 일차로 간주
    	        List<ItineraryItemDto> dayList = objectMapper.convertValue(
    	            root, new TypeReference<List<ItineraryItemDto>>() {}
    	        );
    	        result.add(dayList);
    	    } else {
    	        throw new IOException("지원되지 않는 JSON 구조입니다: " + root.getNodeType());
    	    }

    	    return result;
    	}




    /**
     * (최종 호출용)
     * 1) 기존 core 일정 생성
     * 2) 출발지 기준 반경 약 100km 이내의 카페/식당/관광지 15개씩 추가 검색
     */
    public List<List<ItineraryItemDto>> planItinerary(List<String> places, int days) throws IOException {
        // 1) 기존 코어 일정 생성
        List<List<ItineraryItemDto>> itinerary = planItineraryCore(places, days);

//        // 2) 출발지 좌표 얻기 (예외 발생 시 첫 일정 아이템으로 재시도, 그래도 실패하면 빈 string 사용)
//        String coords = "";
//        System.out.println(places.get(0));
//        try {
//            coords = getCoordsString(places.get(0));
//        } catch (IOException e) {
//            // Places 검색결과가 없거나 API 오류 시 첫 번째 추천 장소를 써보자
//            if (!itinerary.isEmpty() && !itinerary.get(0).isEmpty()) {
//                //String firstPlace = itinerary.get(0).get(0).getName().split(",")[0];
//            	String fallbackPlace = itinerary.get(0).get(0).getName(); // "후지산, 일본"
//                try {
//                    coords = getCoordsString(fallbackPlace);
//                } catch (IOException ex) {
//                    // 그래도 안 나오면 로그만 찍고 넘어감
//                    log.warn("Fallback coords failed for {}: {}", fallbackPlace, ex.getMessage());
//                }
//            } else {
//                log.warn("No itinerary items to fallback coords for origin {}", places.get(0));
//            }
//        }
//
//        System.out.println(coords);
//        
////        // 3) 주변 장소 수집 (coords가 비어있으면 빈 리스트 반환하도록 내부 로직 조정)
////        List<ItineraryItemDto> cafes       = coords.isEmpty() ? Collections.emptyList() : fetchNearbyItineraryItems(coords, "cafe", 15);
////        List<ItineraryItemDto> restaurants = coords.isEmpty() ? Collections.emptyList() : fetchNearbyItineraryItems(coords, "restaurant", 15);
////        List<ItineraryItemDto> tours       = coords.isEmpty() ? Collections.emptyList() : fetchNearbyItineraryItems(coords, "tourist_attraction", 15);
////
////        // 4) 결과 포장
//        PlanResult result = new PlanResult();
        //result.itinerary          = itinerary;
//        result.nearbyCafes        = cafes;
//        result.nearbyRestaurants  = restaurants;
//        result.nearbyTours        = tours;
        return itinerary;
    }


    /**
     * 주어진 장소 목록과 일수로 일차별 추천 코스를 짜서
     * JSON 배열(List<List<ItineraryItemDto>>)으로 반환합니다.
     * 프롬프트 생성 → OpenAI 호출 → JSON 파싱까지의 내용을 이 메서드에 두세요.
     */
    private List<List<ItineraryItemDto>> planItineraryCore(List<String> places, int days) throws IOException {
    	// 1) 역할과 요청 메시지 구성
        String system = "당신은 최고의 한국어 여행 일정 전문가입니다. " +
                        "식당과 카페는 Google Maps에 실제 등록된, 리뷰 수 많고 평점 높은 단일(unique) 매장만 골라주세요.";
        String user = String.format(
        	    "출발지: %s\n" +
        	    "%d일 동안, 각 일차별로 순서대로 HH:mm-HH:mm 타임라인으로 출력해주세요. " +
        	    "각 장소 간 이동시간은 절대 60분을 넘지 않도록 하고, 모두 같은 국가 내에서만 이동할 수 있게 해주세요. " +
        	    // ← 아래 한 줄만 추가된 부분입니다!
        	    "특히 CAFE, LUNCH, TOUR(관광지) 카테고리는 반드시 출발지와 동일한 국가 내에서 선택해주세요. " +
        	    "8단계 카테고리 순서: TOUR, BREAKFAST, TOUR, LUNCH, CAFE, TOUR, DINNER, EVENING_TOUR 또는 EVENING_CAFE. " +
        	    "장소 이름은 Google Maps에서 단일 지점으로 검색되는 정확한 명칭과 주소를 포함하고, " +
        	    "예시처럼 순수 JSON 배열만 반환해주세요.\n" +
        	    "예시 출력:\n" +
        	    "[\n" +
        	    "  [ {\"name\":\"경복궁, 서울\",\"category\":\"TOUR\",\"startTime\":\"09:00\",\"endTime\":\"11:00\"}, … ],\n" +
        	    "  [ /* Day2 items */ ], …\n" +
        	    "]",
        	    objectMapper.writeValueAsString(places), days
        	);

        ArrayNode messages = objectMapper.createArrayNode()
            .add(objectMapper.createObjectNode().put("role","system").put("content",system))
            .add(objectMapper.createObjectNode().put("role","user").put("content",user));

        ObjectNode payload = objectMapper.createObjectNode()
            .put("model","gpt-4o-mini")
            .set("messages", messages);

        // 2) OpenAI 호출
        HttpHeaders headers = buildJsonHeaders(apiKey);
        String resp = restTemplate.postForObject(
            "https://api.openai.com/v1/chat/completions",
            new HttpEntity<>(payload.toString(), headers),
            String.class
        );

        // 3) 응답에서 content 추출 및 펜스 제거
        JsonNode root = objectMapper.readTree(resp);
        String content = root.path("choices").get(0).path("message").path("content").asText();
        String stripped = stripFences(content);

        // 4) JSON 배열 부분만 추출
        int start = stripped.indexOf('[');
        int end = stripped.lastIndexOf(']');
        if (start < 0 || end < 0 || end <= start) {
            throw new IOException("유효한 JSON 배열을 찾을 수 없습니다:\n" + stripped);
        }
        String jsonArray = stripped.substring(start, end + 1)
            .replaceAll("(?m)^\\s*//.*$", "")      // 주석 제거
            .replaceAll(",\\s*(?=\\]|\\})", ""); // 끝 쉼표 제거

        // 5) 파싱: 배열 또는 객체 모두 처리
        JsonNode arrayNode = objectMapper.readTree(jsonArray);
        List<List<ItineraryItemDto>> result = new ArrayList<>();
        if (arrayNode.isArray()) {
            for (JsonNode dayNode : arrayNode) {
                List<ItineraryItemDto> dayList = objectMapper.convertValue(
                    dayNode,
                    new TypeReference<List<ItineraryItemDto>>() {}
                );
                result.add(dayList);
            }
        } else if (arrayNode.isObject()) {
            // 키 순서대로 Day1, Day2... 처리
            arrayNode.fieldNames().forEachRemaining(field -> {
                JsonNode dayNode = arrayNode.get(field);
                List<ItineraryItemDto> dayList = objectMapper.convertValue(
                    dayNode,
                    new TypeReference<List<ItineraryItemDto>>() {}
                );
                result.add(dayList);
            });
        } else {
            throw new IOException("지원되지 않는 JSON 형식입니다: " + jsonArray);
        }

        return result;
    }

    /**
     * 위도·경도 기준으로 주변 카페/음식점/관광지를 최소 minCount개 이상 가져옵니다.
     * 초기 반경(initialRadius)을 넣고, 결과가 부족하면 maxRadius까지 단계별로 반경을 늘려가며 검색합니다.
     */
    public NearbyPlacesResult fetchFlexibleNearbyPlaces(double lat, double lng, int minCount) throws IOException {
        // 초기 반경(m), 최대 반경(m), 단계별 확대율
        int initialRadius = 3000,
            maxRadius     = 20000;
        double factor  = 1.5;

        NearbyPlacesResult result = new NearbyPlacesResult();
        result.cafes       = fetchWithDynamicRadius(lat, lng, "cafe", minCount, initialRadius, maxRadius, factor);
        result.restaurants = fetchWithDynamicRadius(lat, lng, "restaurant", minCount, initialRadius, maxRadius, factor);
        result.tours       = fetchWithDynamicRadius(lat, lng, "tourist_attraction", minCount, initialRadius, maxRadius, factor);
        return result;
    }

    /**
     * type별로 dynamic radius를 적용해 최소 count개의 결과를 가져옵니다.
     */
    private List<PlaceInfoDto> fetchWithDynamicRadius(
            double lat, double lng,
            String type,
            int minCount,
            int radius, int maxRadius, double factor
    ) throws IOException {
        List<PlaceInfoDto> places;
        while (true) {
            places = fetchNearbyPlaces(lat + "," + lng, type, minCount, radius);
            if (places.size() >= minCount || radius >= maxRadius) break;
            radius = Math.min((int)(radius * factor), maxRadius);
        }
        return places;
    }

    /**
     * Google Places Nearby Search API 호출 (원본 fetchNearbyItineraryItems와 비슷하되
     * DTO를 PlaceInfoDto로 반환하고, count 대신 radius를 인자로 받습니다.)
     */
    public List<PlaceInfoDto> fetchNearbyPlaces(
            String coords, String type, int count, int radius
    ) throws IOException {
        String url = UriComponentsBuilder
            .fromHttpUrl("https://maps.googleapis.com/maps/api/place/nearbysearch/json")
            .queryParam("location", coords)
            .queryParam("radius", radius)
            .queryParam("type", type)
            .queryParam("key", googleApiKey)
            .toUriString();

        JsonNode root = restTemplate.getForObject(url, JsonNode.class);
        JsonNode results = root.path("results");
        if (!results.isArray() || results.size() == 0) {
            return Collections.emptyList();
        }

        List<PlaceInfoDto> out = new ArrayList<>();
        for (int i = 0; i < results.size() && out.size() < count; i++) {
            JsonNode node = results.get(i);
            String name    = node.path("name").asText("");
            String address = node.path("vicinity").asText("");
            String photoRef = null;
            JsonNode photos = node.path("photos");
            if (photos.isArray() && photos.size() > 0) {
                photoRef = photos.get(0).path("photo_reference").asText(null);
            }
            out.add(new PlaceInfoDto(name, address, photoRef));
        }
        return out;
    }

    /** API 호출 결과를 담아 반환할 간단한 래퍼 클래스 */
    public static class NearbyPlacesResult {
        public List<PlaceInfoDto> cafes;
        public List<PlaceInfoDto> restaurants;
        public List<PlaceInfoDto> tours;
    }

    /**
     * place 이름을 받아 lat,lng 문자열("37.579617,126.977041")을 반환합니다.
     * 결과가 없으면 IOException을 던집니다.
     */
    private String getCoordsString(String place) throws IOException {
        // 1) place 를 “,” 기준으로 분리
        String[] parts = place.split(",");
        String name = parts[0].trim();                  // "후지산"
        String countryKorean = parts.length > 1
            ? parts[1].trim()
            : null;                                      // "일본"
        
        // 2) Text Search URL 빌더
        UriComponentsBuilder b = UriComponentsBuilder
            .fromHttpUrl("https://maps.googleapis.com/maps/api/place/textsearch/json")
            .queryParam("query", name)
            .queryParam("key", googleApiKey);
        
        // 3) country 파라미터 추가
        if (countryKorean != null) {
            String iso = COUNTRY_CODE.get(countryKorean);
            if (iso != null) {
                // components 파라미터로 제한
                //b.queryParam("components", "country:" + iso.toLowerCase());
                b.queryParam("region", iso.toLowerCase());
            }
        }
        
        String tsUrl = b.toUriString();
        JsonNode root = restTemplate.getForObject(tsUrl, JsonNode.class);
        JsonNode results = root.path("results");
        if (!results.isArray() || results.isEmpty()) {
            throw new IOException("검색 결과가 없습니다 for place: " + place);
        }
        
        JsonNode loc = results.get(0)
                              .path("geometry")
                              .path("location");
        return loc.path("lat").asText() + "," + loc.path("lng").asText();
    }
    
 


    // 펜스(```json … ```) 제거 헬퍼
    private String stripFences(String text) {
        if (text.startsWith("```")) {
            String[] lines = text.split("\\r?\\n");
            int start = 1, end = lines.length;
            if (lines[lines.length-1].trim().equals("```")) end--;
            return String.join("\n", Arrays.copyOfRange(lines, start, end)).trim();
        }
        return text.trim();
    }


    private String fetchPhotoReference(String place) throws IOException {
        // 2) 폴백: textsearch/json 호출
        String tsUrl = UriComponentsBuilder
                .fromHttpUrl("https://maps.googleapis.com/maps/api/place/textsearch/json")
                .queryParam("query", place)
                .queryParam("key", googleApiKey)
                .toUriString();

        System.out.println("▶ [fetchPhotoReference][textsearch] URL: " + tsUrl);

        JsonNode tsRoot = restTemplate.getForObject(tsUrl, JsonNode.class);
        JsonNode results = tsRoot.path("results");
        if (results.isArray() && results.size() > 0) {
            JsonNode photos = results.get(0).path("photos");
            if (photos.isArray() && photos.size() > 0) {
                return photos.get(0).path("photo_reference").asText();
            }
        }
        // 모두 실패하면 null
        return null;
    }


    /** Google Text Search로 첫 번째 photo_reference 가져오기 */
    private String fetchPhotoUrl(String photoReference) {
        String url = UriComponentsBuilder
                .fromHttpUrl("https://maps.googleapis.com/maps/api/place/photo")
                .queryParam("maxwidth", 400)
                .queryParam("photoreference", photoReference)
                .queryParam("key", googleApiKey)
                .toUriString();
        System.out.println("▶ [fetchPhotoUrl] url    : " + url);
        return url;
    }




    /**
    * vkakf
    * */
    public List<PlaceInfoDto> recommendPlacesByGpt(String imageUrl) throws IOException {
        System.out.println(">>> start recommend, imageUrl=" + imageUrl);

        String desc = describeImageByDataUri(imageUrl);
        System.out.println("▶ image description = " + desc);

        List<String> names = fetchPlaceNamesByDescription(desc);
        List<PlaceInfoDto> out = new ArrayList<>();

        for (String nc : names) {
            System.out.println("\n--- processing: " + nc + " ---");
            String shortDesc = fetchDescription(nc);
            System.out.println("▶ place desc = " + shortDesc);

            String ref = fetchPhotoReference(nc);
            String imgUrl = (ref != null) ? fetchPhotoUrl(ref) : null;
            System.out.println("▶ final imgUrl = " + imgUrl);

            out.add(new PlaceInfoDto(nc, shortDesc, ref));
        }

        System.out.println(">>> done: " + out);
        return out;
    }


    private HttpHeaders buildJsonHeaders(String bearerToken) {
        HttpHeaders h = new HttpHeaders();
        h.setContentType(MediaType.APPLICATION_JSON);
        h.setBearerAuth(bearerToken);
        return h;
    }


    //====================================================================
    /**
     +     * Google Photo API로부터 실제 이미지 바이트를 가져오는 프록시용 메서드
     +     */
    public ByteArrayResource downloadPhotoBytes(String photoReference) {
        String url = UriComponentsBuilder
                .fromHttpUrl("https://maps.googleapis.com/maps/api/place/photo")
                .queryParam("maxwidth", 400)
                .queryParam("photoreference", photoReference)
                .queryParam("key", googleApiKey)
                .toUriString();
        System.out.println("▶ [downloadPhotoBytes] proxy URL = " + url);
        return restTemplate.getForObject(url, ByteArrayResource.class);
    }
}
