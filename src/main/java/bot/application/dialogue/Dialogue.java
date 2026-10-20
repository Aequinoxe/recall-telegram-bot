package bot.application.dialogue;

import bot.application.repository.CardRepository;
import bot.application.repository.ReviewRepository;
import bot.domain.Card;
import bot.domain.Deck;
import bot.domain.Review;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.function.LongSupplier;

public class Dialogue {
    private final ReviewRepository reviewRepository;
    private final LongSupplier reviewIdGenerator;
    private final List<Card> cards;
    private DialogueState currentState =
            DialogueState.NOT_STARTED;
    private Card currentCard;
    private int currentCardIndex = 0;

    private enum DialogueState {
        NOT_STARTED,
        WAITING_FOR_FLIP,
        WAITING_FOR_GRADE,
        FINISHED
    }

    public Dialogue(
            Deck deck,
            CardRepository cardRepository,
            ReviewRepository reviewRepository,
            LongSupplier reviewIdGenerator
    ) {
        this.reviewRepository = reviewRepository;

        this.reviewIdGenerator = reviewIdGenerator;

        this.cards = cardRepository.findByDeckId(deck.getId());
    }

    public Optional<Card> start() {
        if (currentState != DialogueState.NOT_STARTED) {
            throw new IllegalStateException("Сессия уже началась");
        }

        return toNextCard();
    }

    public String flipCard() {
        if (currentState != DialogueState.WAITING_FOR_FLIP) {
            throw new IllegalStateException(
                    "Сейчас нельзя перевернуть карточку"
            );
        }

        currentState = DialogueState.WAITING_FOR_GRADE;
        return currentCard.getBack();
    }

    public Optional<Card> processGrade(Review.ReviewResult result) {
        if (currentState != DialogueState.WAITING_FOR_GRADE) {
            throw new IllegalStateException("Сначала необходимо открыть ответ");
        }
        if (result == null) {
            throw new IllegalArgumentException("Оценка не может быть null");
        }

        Review review = new Review(
                reviewIdGenerator.getAsLong(),
                currentCard.getId(),
                result,
                Instant.now()
        );
        reviewRepository.save(review);
        return toNextCard();
    }

    private Optional<Card> toNextCard() {
        if (currentCardIndex >= cards.size()) {
            currentCard = null;
            currentState = DialogueState.FINISHED;
            return Optional.empty();
        }

        currentCard = cards.get(currentCardIndex);
        currentCardIndex++;
        currentState = DialogueState.WAITING_FOR_FLIP;

        return Optional.of(currentCard);
    }
}