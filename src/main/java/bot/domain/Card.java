package bot.domain;

import java.time.Instant;

public class Card {
    private final long id;
    private Long deckId;
    private String front;
    private String back;

    // The time when the card is scheduled for its next review.
    private Instant nextReviewAt;

    // The current interval between card reviews.
    private int intervalDays;

    // The number of times the card has been reviewed.
    private int repetitions;  //

    public Card(long id, long deckId, String front, String back) {
        this.id = id;
        this.deckId = deckId;
        this.front = front;
        this.back = back;
    }

    public Card(long id, String front, String back) {
        this.id = id;
        this.deckId = null;
        this.front = front;
        this.back = back;
    }

    public long getId() {
        return id;
    }

    public Long getDeckId() {
        return deckId;
    }

    public String getFront() {
        return front;
    }

    public String getBack() {
        return back;
    }

    public Instant getNextReviewAt() {
        return nextReviewAt;
    }

    public int getIntervalDays() {
        return intervalDays;
    }

    public int getRepetitions() {
        return repetitions;
    }

    public void setDeckId(Long deckId) {
        this.deckId = deckId;
    }

    public void setFront(String front) {
        this.front = front;
    }

    public void setBack(String back) {
        this.back = back;
    }

    public void setNextReviewAt(Instant nextReviewAt) {
        this.nextReviewAt = nextReviewAt;
    }

    public void setIntervalDays(int intervalDays) {
        this.intervalDays = intervalDays;
    }

    public void setRepetitions(int repetitions) {
        this.repetitions = repetitions;
    }
}
