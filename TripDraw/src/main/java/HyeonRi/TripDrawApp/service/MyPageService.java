// src/main/java/HyeonRi/TripDrawApp/service/MyPageService.java
package HyeonRi.TripDrawApp.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import HyeonRi.TripDrawApp.dto.DrawingResult;
import HyeonRi.TripDrawApp.dto.board.drawing.DrawingDto;
import HyeonRi.TripDrawApp.dto.tripPlan.TripPlanWithRouteDto;
import HyeonRi.TripDrawApp.mapper.MyPageMapper;
import HyeonRi.TripDrawApp.mapper.board.tripPlan.TripPlanMapper;
import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class MyPageService {

    private final MyPageMapper myPageMapper;
    private final TripPlanMapper tripPlanMapper;

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
    
    public List<TripPlanWithRouteDto> getMyPlans(Long userId) {
        return tripPlanMapper.selectMyPlansWithRoute(userId);
    }
    
    public void deleteMyPlan(Long userId, String planCode) {
        // 2) 일정 장소 먼저 삭제
    	myPageMapper.deleteLocationsByPlanCode(planCode);
        // 3) 계획 자체 삭제 (userId 조건 추가로 본인만 삭제)
        int deleted = myPageMapper.deletePlan(planCode, userId);
        if (deleted == 0) {
          throw new IllegalArgumentException("해당 계획이 없거나 권한이 없습니다.");
        }
      }
}
