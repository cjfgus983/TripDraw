package HyeonRi.TripDrawApp.controller.board.tripPlan;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import HyeonRi.TripDrawApp.dto.board.tripBoard.TripBoardDto;
import HyeonRi.TripDrawApp.dto.board.tripBoard.TripBoardWithRouteDto;
import HyeonRi.TripDrawApp.dto.board.tripBoard.TripCommentDto;
import HyeonRi.TripDrawApp.service.board.tripPlan.FavoriteService;
import HyeonRi.TripDrawApp.service.board.tripPlan.TripBoardService;
import HyeonRi.TripDrawApp.service.board.tripPlan.TripLocationService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/trip/boards")
public class TripBoardController {

    private final TripBoardService service;  
    private final FavoriteService favoriteService;


    // Board CRUD
    @PostMapping
    public ResponseEntity<Long> createBoard(@RequestBody TripBoardDto dto) {
        Long id = service.createBoard(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(id);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TripBoardWithRouteDto> getBoard(@PathVariable Long id,  @RequestParam Long userId) {
    	TripBoardWithRouteDto dto = service.getBoard(id);
        dto.setFavorite(favoriteService.isFavorited(userId, id));
        return ResponseEntity.ok(dto);
    	
    }

    @GetMapping
    public ResponseEntity<List<TripBoardWithRouteDto>> listBoards(@RequestParam Long userId) {
    	 List<Long> favs = favoriteService.getUserFavorites(userId);
         List<TripBoardWithRouteDto> dtos = service.getAllBoardsWithRoute().stream()
           .peek(dto -> dto.setFavorite(favs.contains(dto.getPlanBoardId())))
           .collect(Collectors.toList());
         return ResponseEntity.ok(dtos);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateBoard(@PathVariable Long id,
                                            @RequestBody TripBoardDto dto) {
        dto.setPlanBoardId(id);
        service.updateBoard(dto);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBoard(@PathVariable Long id) {
        service.deleteBoard(id);
        return ResponseEntity.noContent().build();
    }

    // Comment CRUD
    @PostMapping("/{boardId}/comments")
    public ResponseEntity<Long> addComment(@PathVariable Long boardId,
                                           @RequestBody TripCommentDto dto) {
        dto.setPlanBoardId(boardId);
        Long commentId = service.addComment(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(commentId);
    }

    @GetMapping("/{boardId}/comments")
    public ResponseEntity<List<TripCommentDto>> getComments(@PathVariable Long boardId) {
        return ResponseEntity.ok(service.getComments(boardId));
    }

    @PutMapping("/{boardId}/comments/{commentId}")
    public ResponseEntity<Void> updateComment(@PathVariable    Long boardId,
                                              @PathVariable    Long commentId,
                                              @RequestBody     TripCommentDto dto) {
        dto.setPlanBoardId(boardId);
        dto.setCommentId(commentId);
        service.updateComment(dto);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{boardId}/comments/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long boardId,
                                              @PathVariable Long commentId) {
        service.deleteComment(boardId, commentId);
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


}
