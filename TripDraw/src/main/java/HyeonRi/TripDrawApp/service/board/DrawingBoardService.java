package HyeonRi.TripDrawApp.service.board;

import HyeonRi.TripDrawApp.domain.DrawingBoard;
import HyeonRi.TripDrawApp.dto.board.drawing.CreateDrawingBoardRequest;
import HyeonRi.TripDrawApp.dto.board.drawing.DrawingBoardDto;
import HyeonRi.TripDrawApp.mapper.board.DrawingBoardMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DrawingBoardService {
    private final DrawingBoardMapper mapper;
    private static final int DEFAULT_PAGE_SIZE = 9;

    /** 전체 리스트 조회 (비추천, 페이징 사용 권장) */
    public List<DrawingBoardDto> listAll() {
        return mapper.selectAll();
    }

    /** 페이징된 리스트 조회 */
    public List<DrawingBoardDto> listPage(int page) {
        int offset = (page - 1) * DEFAULT_PAGE_SIZE;
        return mapper.selectPage(offset, DEFAULT_PAGE_SIZE);
    }

    /** 지난 7일간 좋아요 상위 인기 리스트 */
    public List<DrawingBoardDto> listPopular() {
        return mapper.selectPopular();
    }

    /** 상세 조회 (조회수 증가) */
    public DrawingBoardDto get(Long id) {
        mapper.incrementViewCount(id);
        return mapper.selectById(id);
    }

    /** 게시글 생성 */
    public DrawingBoardDto create(DrawingBoardDto dto) {
        mapper.insert(dto);
        return dto;
    }

    /** 게시글 수정 */
    public void update(DrawingBoardDto dto) {
        mapper.update(dto);
    }

    /**
     * 게시물 삭제
     * -> 연관된 좋아요(user_drawing_like) 먼저 삭제 후 drawing_board 삭제
     */
    @Transactional
    public void delete(Long boardId) {
        // 1) 연관된 좋아요 기록 삭제
        mapper.deleteLikesByBoardId(boardId);
        // 2) 게시물 본체 삭제
        mapper.deleteById(boardId);
    }

    /** 좋아요 처리 (중복 방지) */
    @Transactional
    public void like(Long userId, Long boardId) {
        boolean exists = mapper.existsLike(userId, boardId);
        if (!exists) {
            mapper.insertLike(userId, boardId);
            mapper.incrementLikeCount(boardId);
        }
    }

    /** 도메인 객체 기반 생성 (추가 요청용) */
    @Transactional
    public void createDrawingBoard(Long userId, CreateDrawingBoardRequest req) {
        DrawingBoard board = new DrawingBoard();
        board.setDrawingId(req.getDrawingId());
        board.setUserId(userId);
        board.setTitle(req.getTitle());
        board.setContent(req.getContent());
        mapper.insertDrawingBoard(board);
    }
}
