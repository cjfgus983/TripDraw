package HyeonRi.TripDrawApp.service.board;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import HyeonRi.TripDrawApp.dto.board.free.FreeCommentDto;
import HyeonRi.TripDrawApp.mapper.board.FreeMapper;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class FreeCommentService {
    private final FreeMapper freeMapper;

    /** 댓글 목록 조회 */
    @Transactional(readOnly = true)
    public List<FreeCommentDto> getComments(Long freeId) {
        return freeMapper.getCommentsByFreeId(freeId);
    }

    /** 댓글 등록 */
    public FreeCommentDto addComment(Long freeId, Long userId, String content) {
        FreeCommentDto dto = new FreeCommentDto();
        dto.setFreeId(freeId);
        dto.setUserId(userId);
        dto.setContent(content);
        freeMapper.insertComment(dto);
        return freeMapper.getComment(dto.getCommentId(), freeId);
    }

    /** 댓글 수정 */
    public void updateComment(Long freeId, Long commentId, String newContent) {
        FreeCommentDto dto = new FreeCommentDto();
        dto.setFreeId(freeId);
        dto.setCommentId(commentId);
        dto.setContent(newContent);
        freeMapper.updateComment(dto);
    }

    /** 댓글 삭제 */
    public void deleteComment(Long freeId, Long commentId) {
        freeMapper.deleteComment(commentId, freeId);
    }
}
