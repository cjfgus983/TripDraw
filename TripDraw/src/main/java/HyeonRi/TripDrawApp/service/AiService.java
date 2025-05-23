package HyeonRi.TripDrawApp.service;

import HyeonRi.TripDrawApp.dto.PlaceInfoDto;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AiService {

    private final S3Service s3Service;
    @Value("${openai.api.key}")
    private String apiKey;

    @Value("${google.places.api.key}")
    private String googleApiKey;

    
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

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
                        "[\"후지산,일본\", \"타임스퀘어,미국,뉴욕\", \"오페라하우스,시드니,호주\"]",
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


//    private String fetchPhotoReference(String place) throws IOException {
//        // 2) 폴백: textsearch/json 호출
//        String tsUrl = UriComponentsBuilder
//                .fromHttpUrl("https://maps.googleapis.com/maps/api/place/textsearch/json")
//                .queryParam("query", place)
//                .queryParam("key", googleApiKey)
//                .toUriString();
//
//        System.out.println("▶ [fetchPhotoReference][textsearch] URL: " + tsUrl);
//
//        JsonNode tsRoot = restTemplate.getForObject(tsUrl, JsonNode.class);
//        JsonNode results = tsRoot.path("results");
//        if (results.isArray() && results.size() > 0) {
//            JsonNode photos = results.get(0).path("photos");
//            if (photos.isArray() && photos.size() > 0) {
//                return photos.get(0).path("photo_reference").asText();
//            }
//        }
//        // 모두 실패하면 null
//        return null;
//    }


    /** Google Text Search로 첫 번째 photo_reference 가져오기 */
//    private String fetchPhotoUrl(String photoReference) {
//        String url = UriComponentsBuilder
//                .fromHttpUrl("https://maps.googleapis.com/maps/api/place/photo")
//                .queryParam("maxwidth", 400)
//                .queryParam("photoreference", photoReference)
//                .queryParam("key", googleApiKey)
//                .toUriString();
//        System.out.println("▶ [fetchPhotoUrl] url    : " + url);
//        return url;
//    }


    /**
     * 외부 URL에서 바이트를 GET 요청으로 받아와
     * Spring의 MockMultipartFile로 감싸 MultipartFile 형태로 리턴.
     */
    private MultipartFile downloadAsMultipart(String imageUrl) throws IOException {
        System.out.println("▶ downloadAsMultipart 호출, imageUrl = " + imageUrl);

        // 1) URLConnection 으로 연결
        URL url = new URL(imageUrl);
        URLConnection conn = url.openConnection();

        // 2) Content-Type, InputStream 가져오기
        String contentType = conn.getContentType();
        System.out.println("    contentType = " + contentType);

        byte[] bytes;
        try (InputStream in = conn.getInputStream()) {
            bytes = in.readAllBytes();
            System.out.println("    bytes length = " + bytes.length);
        } catch (IOException e) {
            System.err.println("    ▶ URLConnection 에서 바이트 읽기 실패!");
            throw e;
        }

        // 3) 파일 이름 추출 (경로의 마지막 부분)
        String filename = Paths.get(url.getPath()).getFileName().toString();
        if (filename == null || filename.isBlank()) {
            filename = "generated.png";
        }
        System.out.println("    filename = " + filename);

        // 4) MockMultipartFile 생성 후 반환
        return new MockMultipartFile(
                "file",         // form field name
                filename,       // original filename
                contentType,    // content type
                bytes           // file content
        );
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

            // 1) S3 에 해당 이미지 있으면 URL 바로 사용
            String encoded = URLEncoder
                    .encode(nc, StandardCharsets.UTF_8.toString());
            String key = "places/" + encoded + ".png";


            String imgUrl;
            if (s3Service.exists(key)) {
                // S3Service가 제공하는 URL만 가져오기
                imgUrl = s3Service.getUrl(key);
            } else {
                // DALL·E로 이미지 생성
                String dalleUrl = generateImageFromPrompt(nc);
                System.out.println("여기까지 옴");
                MultipartFile fake = downloadAsMultipart(dalleUrl);
                // S3에 업로드 후 URL 반환
                imgUrl = s3Service.upload(fake, key);
            }

            out.add(new PlaceInfoDto(nc, shortDesc, imgUrl));
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
//    public ByteArrayResource downloadPhotoBytes(String photoReference) {
//        String url = UriComponentsBuilder
//                .fromHttpUrl("https://maps.googleapis.com/maps/api/place/photo")
//                .queryParam("maxwidth", 400)
//                .queryParam("photoreference", photoReference)
//                .queryParam("key", googleApiKey)
//                .toUriString();
//        System.out.println("▶ [downloadPhotoBytes] proxy URL = " + url);
//        return restTemplate.getForObject(url, ByteArrayResource.class);
//    }
}
