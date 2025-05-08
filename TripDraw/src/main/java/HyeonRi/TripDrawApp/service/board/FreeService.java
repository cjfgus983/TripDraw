package HyeonRi.TripDrawApp.service.board;

import java.util.List;

import org.springframework.stereotype.Service;

import HyeonRi.TripDrawApp.dto.board.free.FreeCommentDto;
import HyeonRi.TripDrawApp.dto.board.free.FreeDto;
import HyeonRi.TripDrawApp.dto.board.free.FreeImageDto;
import HyeonRi.TripDrawApp.mapper.board.FreeMapper;

@Service
public class FreeService {

    private final FreeMapper freeMapper;

    public FreeService(FreeMapper freeMapper) {
        this.freeMapper = freeMapper;
    }

    // 게시글
    public Long createFree(FreeDto dto) {
        freeMapper.insertFree(dto);
        return dto.getFreeId();
    }

    public FreeDto getFree(Long id) {
        return freeMapper.getFreeById(id);
    }

    public List<FreeDto> getAllFree() {
        return freeMapper.getAllFree();
    }

    public void updateFree(FreeDto dto) {
        freeMapper.updateFree(dto);
    }

    public void deleteFree(Long id) {
        freeMapper.deleteFree(id);
    }

    // 댓글
    public void addComment(FreeCommentDto dto) {
        freeMapper.insertComment(dto);
    }

    public List<FreeCommentDto> getComments(Long freeId) {
        return freeMapper.getCommentsByFreeId(freeId);
    }

    public FreeCommentDto getComment(Long freeId, Long commentId) {
        return freeMapper.getComment(commentId, freeId);
    }

    public void updateComment(FreeCommentDto dto) {
        freeMapper.updateComment(dto);
    }

    public void deleteComment(Long freeId, Long commentId) {
        freeMapper.deleteComment(commentId, freeId);
    }

    // 이미지
    public void addImage(FreeImageDto dto) {
        freeMapper.insertImage(dto);
    }
}
