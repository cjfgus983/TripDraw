package HyeonRi.TripDrawApp.domain;

public enum ReactionType {
    LIKE,
    DISLIKE;

    /**
     * 현재 타입의 반대값을 반환합니다.
     * LIKE.opposite()  → DISLIKE
     * DISLIKE.opposite() → LIKE
     */
    public ReactionType opposite() {
        return this == LIKE ? DISLIKE : LIKE;
    }
}
