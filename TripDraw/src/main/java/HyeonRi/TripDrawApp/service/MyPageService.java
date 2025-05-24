// src/main/java/HyeonRi/TripDrawApp/service/MyPageService.java
package HyeonRi.TripDrawApp.service;

import HyeonRi.TripDrawApp.dto.DrawingResult;
import HyeonRi.TripDrawApp.dto.board.drawing.DrawingDto;
import HyeonRi.TripDrawApp.mapper.MyPageMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class MyPageService {

    private final MyPageMapper myPageMapper;

    public List<DrawingResult> getDrawingsByUser(Long userId) {
        List<DrawingDto> list = myPageMapper.selectByUserId(userId);
        return list.stream()
                .map(dto -> new DrawingResult(
                        dto.getDrawingId(),
                        dto.getOriginalImageUrl(),
                        dto.getGptImageUrl(),
                        dto.getCreatedAt()      // LocalDateTime 타입
                ))
                .collect(Collectors.toList());
    }

    public void deleteDrawing(Long userId, Long drawingId) {
        myPageMapper.deleteByUserIdAndDrawingId(userId, drawingId);
    }
}
