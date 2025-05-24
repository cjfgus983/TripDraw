package HyeonRi.TripDrawApp.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class DrawingResult {
    @JsonProperty("drawingId")    // JSON 으로 내려줄 때 drawingId 라는 이름으로
    private Long drawingId;

    private String originalUrl;
    private String gptUrl;
    private LocalDateTime createdAt;    // ← 추가

    public DrawingResult(String originalUrl,String gptUrl,LocalDateTime createdAt){
        this.originalUrl = originalUrl;
        this.gptUrl = gptUrl;
        this.createdAt = createdAt;

    }
}
