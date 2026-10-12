package bot.domain;

import java.time.Instant;

public class Card {
    private final long id;
    private long deckId;

    private String front;
    private String back;

    // The time when the card is scheduled for its next review.
    private Instant nextReviewAt;

    // The current interval between card reviews.
    private int intervalDays;

    // The number of times the card has been reviewed.
    private int repetitions;

    public enum CardState {
        NEW,
        LEARNING,
        REVIEW,
        RELEARNING
    }

    private CardState state;

    public Card(long id, long deckId, String front, String back) {
        this.id = id;
        this.deckId = deckId;
        this.front = front;
        this.back = back;
        this.state = CardState.NEW;
    }

    public long getId() {
        return id;
    }

    public long getDeckId() {
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

    public CardState getCardState() {
        return state;
    }

    public void setDeckId(long deckId) {
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

    public void setCardState(CardState cardState) {
        this.state = cardState;
    }
}
