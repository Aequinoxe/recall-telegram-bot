package bot.domain;

import java.time.Instant;

// Immutable class
public class Review {
    private final long id;
    private final long cardId;
    private final ReviewResult result;
    private final Instant reviewedAt;

    public enum ReviewResult {
        AGAIN,
        HARD,
        GOOD,
        EASY,
    }

    public Review(long id, long cardId, ReviewResult result, Instant reviewedAt) {
        this.id = id;
        this.cardId = cardId;
        this.result = result;
        this.reviewedAt = reviewedAt;
    }

    public long getId() {
        return id;
    }

    public long getCardId() {
        return cardId;
    }

    public ReviewResult getResult() {
        return result;
    }

    public Instant getReviewedAt() {
        return reviewedAt;
    }
}
