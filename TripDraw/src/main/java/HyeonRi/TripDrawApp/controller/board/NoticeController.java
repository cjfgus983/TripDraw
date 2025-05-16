package HyeonRi.TripDrawApp.controller.board;

import HyeonRi.TripDrawApp.dto.board.notice.NoticeDto;
import HyeonRi.TripDrawApp.service.board.NoticeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notice")
@RequiredArgsConstructor
public class NoticeController {

    private final NoticeService noticeService;

    @PostMapping
    public ResponseEntity<Void> createNotice(@RequestBody NoticeDto dto) {
        noticeService.createNotice(dto);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{noticeId}")
    public ResponseEntity<NoticeDto> getNotice(@PathVariable Long noticeId) {
        return ResponseEntity.ok(noticeService.getNotice(noticeId));
    }

    @GetMapping
    public ResponseEntity<List<NoticeDto>> getAllNotices() {
        return ResponseEntity.ok(noticeService.getAllNotices());
    }

    @PutMapping("/{noticeId}")
    public ResponseEntity<Void> updateNotice(@PathVariable Long noticeId, @RequestBody NoticeDto dto) {
        dto.setNoticeId(noticeId);
        noticeService.updateNotice(dto);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{noticeId}")
    public ResponseEntity<Void> deleteNotice(@PathVariable Long noticeId) {
        noticeService.deleteNotice(noticeId);
        return ResponseEntity.ok().build();
    }
}
