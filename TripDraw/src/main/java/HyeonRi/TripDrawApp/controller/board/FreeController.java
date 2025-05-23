package HyeonRi.TripDrawApp.controller.board;

import java.io.IOException;
import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import HyeonRi.TripDrawApp.dto.board.free.FreeCommentDto;
import HyeonRi.TripDrawApp.dto.board.free.FreeDto;
import HyeonRi.TripDrawApp.dto.board.free.FreeImageDto;
import HyeonRi.TripDrawApp.service.board.FreeService;

@RestController
@RequestMapping("/api/free")
public class FreeController {

    private final FreeService freeService;

    public FreeController(FreeService freeService) {
        this.freeService = freeService;
    }

    // 게시글
    @PostMapping(
    	    value = "/createFree",
    	    consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    	)
    	public ResponseEntity<Long> createFree(
    	    @RequestParam Long userId,
    	    @RequestParam String title,
    	    @RequestParam String content,
    	    @RequestPart(value = "images", required = false) List<MultipartFile> images
    	) throws IOException {
    	    // FreeDto로 조립
    	    FreeDto dto = new FreeDto();
    	    dto.setUserId(userId);
    	    dto.setTitle(title);
    	    dto.setContent(content);

    	    Long freeId = freeService.createFreeWithImages(dto, images);
    	    return ResponseEntity.ok(freeId);
    	}


    @GetMapping("/{freeId}")
    public ResponseEntity<FreeDto> getFree(@PathVariable Long freeId) throws IOException {
        return ResponseEntity.ok(freeService.getFree(freeId));
    }

    @GetMapping()
    public ResponseEntity<List<FreeDto>> getAllFree() {
        List<FreeDto> freeList = freeService.getAllFree();
//        
//        System.out.println("=== Free 게시글 목록 ===");
//        if(freeList== null) System.out.println("hi");
//        for (FreeDto dto : freeList) {
//            System.out.println(dto);
//        }

        return ResponseEntity.ok(freeList);
    }


    @PutMapping("/{freeId}")
    public ResponseEntity<Void> updateFree(@PathVariable Long freeId, @RequestBody FreeDto dto) {
        dto.setFreeId(freeId);
        freeService.updateFree(dto);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{freeId}")
    public ResponseEntity<Void> deleteFree(@PathVariable Long freeId) {
        freeService.deleteFree(freeId);
        return ResponseEntity.ok().build();
    }

//    // 댓글
//    @PostMapping("/{freeId}/comment")
//    public ResponseEntity<Void> addComment(@PathVariable Long freeId, @RequestBody FreeCommentDto dto) {
//        dto.setFreeId(freeId);
//        freeService.addComment(dto);
//        return ResponseEntity.ok().build();
//    }
//
//    @GetMapping("/{freeId}/comment")
//    public ResponseEntity<List<FreeCommentDto>> getComments(@PathVariable Long freeId) {
//        return ResponseEntity.ok(freeService.getComments(freeId));
//    }
//
//    @GetMapping("/{freeId}/comment/{commentId}")
//    public ResponseEntity<FreeCommentDto> getComment(@PathVariable Long freeId, @PathVariable Long commentId) {
//        return ResponseEntity.ok(freeService.getComment(freeId, commentId));
//    }
//
//    @PutMapping("/{freeId}/comment/{commentId}")
//    public ResponseEntity<Void> updateComment(@PathVariable Long freeId, @PathVariable Long commentId, @RequestBody FreeCommentDto dto) {
//        dto.setFreeId(freeId);
//        dto.setCommentId(commentId);
//        freeService.updateComment(dto);
//        return ResponseEntity.ok().build();
//    }
//
//    @DeleteMapping("/{freeId}/comment/{commentId}")
//    public ResponseEntity<Void> deleteComment(@PathVariable Long freeId, @PathVariable Long commentId) {
//        freeService.deleteComment(freeId, commentId);
//        return ResponseEntity.ok().build();
//    }

    // 이미지
    @PostMapping("/{freeId}/image")
    public ResponseEntity<Void> addImage(@PathVariable Long freeId, @RequestBody FreeImageDto dto) {
        dto.setFreeId(freeId);
        freeService.addImage(dto);
        return ResponseEntity.ok().build();
    }
}