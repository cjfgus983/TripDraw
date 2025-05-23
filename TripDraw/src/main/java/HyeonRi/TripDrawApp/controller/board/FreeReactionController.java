package HyeonRi.TripDrawApp.controller.board;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import HyeonRi.TripDrawApp.domain.ReactionType;
import HyeonRi.TripDrawApp.dto.board.free.ReactionResultDto;
import HyeonRi.TripDrawApp.service.board.FreeReactionService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/free")
@RequiredArgsConstructor
public class FreeReactionController {
    private final FreeReactionService reactionService;

    /**
     * POST /api/free/{freeId}/reaction?type=LIKE
     * Body 없음, 사용자 토큰에서 userId 추출
     */
    @PostMapping("/{freeId}/reaction")
    public ResponseEntity<ReactionResultDto> react(
            @PathVariable Long freeId,
            @RequestParam ReactionType type,
            @RequestParam Long userId
    ) {
        ReactionResultDto result = reactionService.toggleReaction(freeId, userId, type);
        return ResponseEntity.ok(result);
    }
}
