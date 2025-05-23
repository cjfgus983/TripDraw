package HyeonRi.TripDrawApp.controller.board.tripPlan;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import HyeonRi.TripDrawApp.dto.board.tripBoard.BoardWithLocationsDto;
import HyeonRi.TripDrawApp.dto.board.tripBoard.TripBoardDto;
import HyeonRi.TripDrawApp.dto.board.tripBoard.TripBoardWithRouteDto;
import HyeonRi.TripDrawApp.dto.board.tripBoard.TripCommentDto;
import HyeonRi.TripDrawApp.dto.tripPlan.TripLocationDto;
import HyeonRi.TripDrawApp.service.board.tripPlan.FavoriteService;
import HyeonRi.TripDrawApp.service.board.tripPlan.TripBoardService;
import HyeonRi.TripDrawApp.service.board.tripPlan.TripLocationService;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/trip/boards")
public class TripBoardController {

	private final TripLocationService locService;
	private final TripBoardService boardService;
    private final FavoriteService favoriteService;


    // Board CRUD
    @PostMapping
    public ResponseEntity<Long> createBoard(@RequestBody TripBoardDto dto) {
        Long id = boardService.createBoard(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(id);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TripBoardWithRouteDto> getBoard(@PathVariable Long id,  @RequestParam Long userId) {
    	TripBoardWithRouteDto dto = boardService.getBoard(id);
        dto.setFavorite(favoriteService.isFavorited(userId, id));
        return ResponseEntity.ok(dto);
    	
    }

    @GetMapping
    public ResponseEntity<List<TripBoardWithRouteDto>> listBoards(@RequestParam Long userId) {
    	 List<Long> favs = favoriteService.getUserFavorites(userId);
         List<TripBoardWithRouteDto> dtos = boardService.getAllBoardsWithRoute().stream()
           .peek(dto -> dto.setFavorite(favs.contains(dto.getPlanBoardId())))
           .collect(Collectors.toList());
         return ResponseEntity.ok(dtos);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateBoard(@PathVariable Long id,
                                            @RequestBody TripBoardDto dto) {
        dto.setPlanBoardId(id);
        boardService.updateBoard(dto);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBoard(@PathVariable Long id) {
    	boardService.deleteBoard(id);
        return ResponseEntity.noContent().build();
    }

    // Comment CRUD
    @PostMapping("/{boardId}/comments")
    public ResponseEntity<Long> addComment(@PathVariable Long boardId,
                                           @RequestBody TripCommentDto dto) {
        dto.setPlanBoardId(boardId);
        Long commentId = boardService.addComment(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(commentId);
    }

    @GetMapping("/{boardId}/comments")
    public ResponseEntity<List<TripCommentDto>> getComments(@PathVariable Long boardId) {
        return ResponseEntity.ok(boardService.getComments(boardId));
    }

    @PutMapping("/{boardId}/comments/{commentId}")
    public ResponseEntity<Void> updateComment(@PathVariable    Long boardId,
                                              @PathVariable    Long commentId,
                                              @RequestBody     TripCommentDto dto) {
        dto.setPlanBoardId(boardId);
        dto.setCommentId(commentId);
        boardService.updateComment(dto);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{boardId}/comments/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long boardId,
                                              @PathVariable Long commentId) {
    	boardService.deleteComment(boardId, commentId);
        return ResponseEntity.noContent().build();
    }


    // --- 즐겨찾기 ---
    @PostMapping("/{id}/favorite")
    public ResponseEntity<Void> favorite(@PathVariable Long id, @RequestParam Long userId) {
      favoriteService.addFavorite(userId, id);
      return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}/favorite")
    public ResponseEntity<Void> unfavorite(@PathVariable Long id, @RequestParam Long userId) {
      favoriteService.removeFavorite(userId, id);
      return ResponseEntity.noContent().build();
    }
    
    /** 상세 + 일차별 장소 전체 반환 */
    @GetMapping("/{id}/detail-with-locations")
    public ResponseEntity<BoardWithLocationsDto> getDetailWithLocations(
        @PathVariable Long id,
        @RequestParam Long userId
    ) {
      // 1) board 메타
      TripBoardWithRouteDto board = boardService.getBoard(id);
      board.setFavorite(favoriteService.isFavorited(userId, id));

      // 2) planCode 가져와서 locations 조회
      String planCode = board.getPlanCode();
      List<TripLocationDto> locs = locService.getByPlanCode(planCode);

      // 3) 그룹핑
      Map<Integer, List<TripLocationDto>> byDay = locs.stream()
        .collect(Collectors.groupingBy(TripLocationDto::getDayNo, TreeMap::new, Collectors.toList()));

      // 4) 합치기
      BoardWithLocationsDto dto = new BoardWithLocationsDto(board, byDay);
      return ResponseEntity.ok(dto);
    }


}
