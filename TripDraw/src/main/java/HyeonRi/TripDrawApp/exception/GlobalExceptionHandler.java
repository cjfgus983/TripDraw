package HyeonRi.TripDrawApp.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<Map<String, String>> handleMaxSize(MaxUploadSizeExceededException ex) {
        return ResponseEntity
                .status(HttpStatus.PAYLOAD_TOO_LARGE)
                .body(Map.of("error", "파일 크기가 너무 큽니다. 최대 5MB 이하의 이미지를 업로드 해주세요."));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String,String>> handleAny(Exception e) {
        // 콘솔에 스택트레이스 찍기
        e.printStackTrace();
        // 클라이언트로는 JSON 에러 메시지만
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", e.getMessage()));
    }
}
