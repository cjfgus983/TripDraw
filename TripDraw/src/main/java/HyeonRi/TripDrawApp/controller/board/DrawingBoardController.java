package HyeonRi.TripDrawApp.controller.board;

import HyeonRi.TripDrawApp.dto.board.drawing.CreateDrawingBoardRequest;
import HyeonRi.TripDrawApp.dto.board.drawing.DrawingBoardDto;
import HyeonRi.TripDrawApp.security.CustomUserDetails;
import HyeonRi.TripDrawApp.service.board.DrawingBoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/drawingboard")
public class DrawingBoardController {

    private final DrawingBoardService service;

    /** 1. 이번 주 인기 작품 */
    @GetMapping("/popular")
    public List<DrawingBoardDto> popular(
            @AuthenticationPrincipal CustomUserDetails principal
    ) {
        Long userId = principal.getUser().getUserId();
        return service.listPopular(userId);
    }

    /** 2. 페이지 단위 전체 작품 조회 (기본 page=1) */
    @GetMapping
    public List<DrawingBoardDto> list(
            @RequestParam(defaultValue = "1") int page,
            @AuthenticationPrincipal CustomUserDetails principal
    ) {
        Long userId = principal.getUser().getUserId();
        return service.listPage(page, userId);
    }

    /** 3. 상세 조회 (조회수 증가 포함) */
    @GetMapping("/{id}")
    public DrawingBoardDto get(
            @PathVariable Long id,
            @AuthenticationPrincipal CustomUserDetails principal
    ) {
        Long userId = principal.getUser().getUserId();
        return service.get(id, userId);
    }

    /** 4. 게시글 생성 */
    @PostMapping
    public ResponseEntity<DrawingBoardDto> create(
            @RequestBody DrawingBoardDto dto,
            @AuthenticationPrincipal CustomUserDetails principal
    ) {
        dto.setUserId(principal.getUser().getUserId());
        service.create(dto);
        return ResponseEntity.ok(dto);
    }

    /** 5. 게시글 수정 */
    @PutMapping("/{id}")
    public ResponseEntity<Void> update(
            @PathVariable Long id,
            @RequestBody DrawingBoardDto dto,
            @AuthenticationPrincipal CustomUserDetails principal
    ) {
        dto.setDrawingBoardId(id);
        dto.setUserId(principal.getUser().getUserId());
        service.update(dto);
        return ResponseEntity.ok().build();
    }

    /** 6. 게시글 삭제 */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable Long id,
            @AuthenticationPrincipal CustomUserDetails principal
    ) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    /** 7. 좋아요 */
    @PostMapping("/{id}/like")
    public ResponseEntity<Void> like(
            @PathVariable("id") Long boardId,
            @AuthenticationPrincipal CustomUserDetails principal
    ) {
        service.like(principal.getUser().getUserId(), boardId);
        return ResponseEntity.ok().build();
    }

    /** 8. 좋아요 취소 */
    @DeleteMapping("/{id}/like")
    public ResponseEntity<Void> unlike(
            @PathVariable("id") Long boardId,
            @AuthenticationPrincipal CustomUserDetails principal
    ) {
        service.unlike(principal.getUser().getUserId(), boardId);
        return ResponseEntity.noContent().build();
    }
}
