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

    /** (기존) 페이징된 리스트 조회—isLiked 없이 */
    public List<DrawingBoardDto> listPage(int page) {
        int offset = (page - 1) * DEFAULT_PAGE_SIZE;
        return mapper.selectPage(offset, DEFAULT_PAGE_SIZE);
    }

    /** (신규) 페이징된 리스트 조회—isLiked 포함 */
    public List<DrawingBoardDto> listPage(int page, Long userId) {
        int offset = (page - 1) * DEFAULT_PAGE_SIZE;
        return mapper.selectPageWithLike(offset, DEFAULT_PAGE_SIZE, userId);
    }

    /** (기존) 인기 작품 조회—isLiked 없이 */
    public List<DrawingBoardDto> listPopular() {
        return mapper.selectPopular();
    }

    /** (신규) 인기 작품 조회—isLiked 포함 */
    public List<DrawingBoardDto> listPopular(Long userId) {
        return mapper.selectPopularWithLike(userId);
    }

    /** (기존) 상세 조회 (조회수 증가) */
    public DrawingBoardDto get(Long id) {
        mapper.incrementViewCount(id);
        return mapper.selectById(id);
    }

    /** (신규) 상세 조회 (조회수 증가 + isLiked 포함) */
    public DrawingBoardDto get(Long id, Long userId) {
        mapper.incrementViewCount(id);
        return mapper.selectByIdWithLike(id, userId);
    }

    /** 게시글 생성 */
    @Transactional
    public DrawingBoardDto create(DrawingBoardDto dto) {
        mapper.insert(dto);
        return dto;
    }

    /** 게시글 수정 */
    @Transactional
    public void update(DrawingBoardDto dto) {
        mapper.update(dto);
    }

    /** 게시글 삭제 */
    @Transactional
    public void delete(Long boardId) {
        mapper.deleteLikesByBoardId(boardId);
        mapper.deleteById(boardId);
    }

    /** 좋아요 */
    @Transactional
    public void like(Long userId, Long boardId) {
        if (!mapper.existsLike(userId, boardId)) {
            mapper.insertLike(userId, boardId);
            mapper.incrementLikeCount(boardId);
        }
    }

    /** 좋아요 취소 */
    @Transactional
    public void unlike(Long userId, Long boardId) {
        mapper.deleteLike(userId, boardId);
        mapper.decrementLikeCount(boardId);
    }

    /** (기존) 도메인 객체 기반 생성 */
    @Transactional
    public void createDrawingBoard(Long userId, CreateDrawingBoardRequest req) {
        // ...
    }
}
