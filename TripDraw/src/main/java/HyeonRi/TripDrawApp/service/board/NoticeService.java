package HyeonRi.TripDrawApp.service.board;

import HyeonRi.TripDrawApp.dto.board.notice.NoticeDto;
import HyeonRi.TripDrawApp.mapper.board.NoticeMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NoticeService {

    private final NoticeMapper noticeMapper;

    public int createNotice(NoticeDto dto) {
        return noticeMapper.insertNotice(dto);
    }

    public NoticeDto getNotice(Long id) {
        return noticeMapper.selectNotice(id);
    }

    public List<NoticeDto> getAllNotices() {
        return noticeMapper.selectAllNotices();
    }

    public int updateNotice(NoticeDto dto) {
        return noticeMapper.updateNotice(dto);
    }

    public int deleteNotice(Long id) {
        return noticeMapper.deleteNotice(id);
    }
}
