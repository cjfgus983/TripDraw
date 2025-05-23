package HyeonRi.TripDrawApp.dto.board.free;

import HyeonRi.TripDrawApp.domain.ReactionType;

public record ReactionResultDto(
	    boolean active,          // 지금 눌렀다면 true, 취소라면 false
	    ReactionType type,       // LIKE or DISLIKE
	    int likeCount,           // 업데이트된 좋아요 수
	    int dislikeCount         // 업데이트된 싫어요 수
	) {}
