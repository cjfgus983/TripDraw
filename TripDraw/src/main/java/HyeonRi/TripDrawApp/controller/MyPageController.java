// src/main/java/HyeonRi/TripDrawApp/controller/MyPageController.java
package HyeonRi.TripDrawApp.controller;

import HyeonRi.TripDrawApp.dto.DrawingResult;
import HyeonRi.TripDrawApp.security.CustomUserDetails;
import HyeonRi.TripDrawApp.service.MyPageService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/mypage")
public class MyPageController {

    private final MyPageService myPageService;

    // 로그인된 유저의 그림 목록 조회
    @GetMapping("/drawings")
    public List<DrawingResult> getMyDrawings(
            @AuthenticationPrincipal CustomUserDetails principal
    ) {
        Long userId = principal.getUser().getUserId();
        return myPageService.getDrawingsByUser(userId);
    }

    // ▶ 그림 삭제
    @DeleteMapping("/drawings/{drawingId}")
    public void deleteDrawing(
            @AuthenticationPrincipal CustomUserDetails principal,
            @PathVariable Long drawingId
    ) {
        Long userId = principal.getUser().getUserId();
        myPageService.deleteDrawing(userId, drawingId);
    }
}
