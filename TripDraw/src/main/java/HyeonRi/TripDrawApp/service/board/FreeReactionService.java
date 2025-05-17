package HyeonRi.TripDrawApp.service.board;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import HyeonRi.TripDrawApp.domain.ReactionType;
import HyeonRi.TripDrawApp.dto.board.free.FreeDto;
import HyeonRi.TripDrawApp.dto.board.free.ReactionResultDto;
import HyeonRi.TripDrawApp.mapper.board.FreeMapper;
import HyeonRi.TripDrawApp.mapper.board.FreeReactionMapper;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class FreeReactionService {
    private final FreeReactionMapper reactionMapper;
    private final FreeMapper freeMapper; // 좋아요 카운트 업데이트용

    /**
     * 사용자가 freeId 게시글에 type 반응(‘LIKE’ or ‘DISLIKE’)을 토글합니다.
     * - 같은 타입을 이미 눌렀다면 취소(delete + count--)
     * - 다른 타입을 눌렀다면 기존 반응 삭제 + 새 반응 insert
     *   + free.like_count or free.dislike_count 증감
     */
    public ReactionResultDto toggleReaction(Long freeId, Long userId, ReactionType type) {
        String existing = reactionMapper.getUserReaction(freeId, userId);
        boolean nowActive;

        if (existing != null && existing.equals(type.name())) {
            // 이미 같은 반응 → 취소
            reactionMapper.deleteReaction(freeId, userId);
            adjustCount(freeId, type, -1);
            nowActive = false;
        } else {
            // 기존 반응이 다르면(반응 없음 또는 반대 반응이 있음)
            if (existing != null) {
                // 반대 반응 취소
                reactionMapper.deleteReaction(freeId, userId);
                adjustCount(freeId, type.opposite(), -1);
            }
            // 새 반응 추가
            reactionMapper.insertReaction(freeId, userId, type.name());
            adjustCount(freeId, type, +1);
            nowActive = true;
        }

        // 업데이트된 counts 를 읽어와서 리턴
        FreeDto dto = freeMapper.getFreeById(freeId);
        return new ReactionResultDto(nowActive, type, dto.getLikeCount(), dto.getDislikeCount());
    }

    private void adjustCount(Long freeId, ReactionType type, int delta) {
        if (type == ReactionType.LIKE) {
            freeMapper.updateLikeCount(freeId, delta);
        } else {
            freeMapper.updateDislikeCount(freeId, delta);
        }
    }
}
