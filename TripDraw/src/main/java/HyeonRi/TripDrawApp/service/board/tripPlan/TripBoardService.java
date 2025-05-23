package HyeonRi.TripDrawApp.service.board.tripPlan;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import HyeonRi.TripDrawApp.dto.board.tripBoard.TripBoardDto;
import HyeonRi.TripDrawApp.dto.board.tripBoard.TripBoardWithRouteDto;
import HyeonRi.TripDrawApp.dto.board.tripBoard.TripCommentDto;
import HyeonRi.TripDrawApp.mapper.board.tripPlan.TripBoardMapper;
import HyeonRi.TripDrawApp.mapper.board.tripPlan.TripCommentMapper;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TripBoardService {

    private final TripBoardMapper   boardMapper;
    private final TripCommentMapper commentMapper;

    /* ==== Board CRUD ==== */

    @Transactional
    public Long createBoard(TripBoardDto dto) {
        dto.setCreatedAt(LocalDateTime.now());
        boardMapper.insertBoard(dto);
        return dto.getPlanBoardId();
    }
    
    public List<TripBoardWithRouteDto> getAllBoardsWithRoute() {
        // Mapper 에서 routeConcat → DTO.setRouteConcat 으로 List<String> route 로 변환됨
        return boardMapper.selectBoardsWithRoute();
    }

    public TripBoardWithRouteDto getBoard(Long id) {
        return boardMapper.selectBoardById(id);
    }

    public List<TripBoardDto> getAllBoards() {
        return boardMapper.selectAllBoards();
    }

    @Transactional
    public void updateBoard(TripBoardDto dto) {
        boardMapper.updateBoard(dto);
    }

    @Transactional
    public void deleteBoard(Long id) {
        boardMapper.deleteBoard(id);
    }

    /* ==== Comment CRUD ==== */

    @Transactional
    public Long addComment(TripCommentDto dto) {
        dto.setCreatedAt(LocalDateTime.now());
        dto.setIsDeleted(false);
        commentMapper.insertComment(dto);
        return dto.getCommentId();
    }

    public List<TripCommentDto> getComments(Long boardId) {
        return commentMapper.selectCommentsByBoardId(boardId);
    }

    @Transactional
    public void updateComment(TripCommentDto dto) {
        commentMapper.updateComment(dto);
    }

    @Transactional
    public void deleteComment(Long boardId, Long commentId) {
        commentMapper.deleteComment(boardId, commentId);
    }
}