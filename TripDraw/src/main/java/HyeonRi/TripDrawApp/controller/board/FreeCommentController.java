package HyeonRi.TripDrawApp.controller.board;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import HyeonRi.TripDrawApp.dto.board.free.FreeCommentDto;
import HyeonRi.TripDrawApp.mapper.UserMapper;
import HyeonRi.TripDrawApp.service.board.FreeCommentService;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/free/{freeId}/comment")
@RequiredArgsConstructor
public class FreeCommentController {

    private final FreeCommentService commentService;
    private final UserMapper userMapper;

    /** 댓글 목록 조회 */
    @GetMapping
    public ResponseEntity<List<FreeCommentDto>> listComments(
            @PathVariable Long freeId
    ) {
    	List<FreeCommentDto> list = commentService.getComments(freeId);
    	for(FreeCommentDto f: list) {
    		f.setNickName(userMapper.findNicknameByUserId(f.getUserId()));
    	}
        return ResponseEntity.ok(list);
    }

    /** 댓글 등록 */
    @PostMapping
    public ResponseEntity<FreeCommentDto> addComment(
            @PathVariable Long freeId,
            @RequestBody CommentRequest body,
            @RequestParam Long userId
    ) {
    	//System.out.println(body.content);
        FreeCommentDto created = commentService.addComment(freeId, userId, body.getContent());

        return ResponseEntity.ok(created);
    }

    /** 댓글 수정 */
    @PutMapping("/{commentId}")
    public ResponseEntity<Void> updateComment(
            @PathVariable Long freeId,
            @PathVariable Long commentId,
            @RequestBody CommentRequest body
    ) {
        commentService.updateComment(freeId, commentId, body.getContent());
        return ResponseEntity.ok().build();
    }

    /** 댓글 삭제 */
    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteComment(
            @PathVariable Long freeId,
            @PathVariable Long commentId
    ) {
        commentService.deleteComment(freeId, commentId);
        return ResponseEntity.ok().build();
    }

    @Data
    static class CommentRequest {
        private String content;
    }
}
