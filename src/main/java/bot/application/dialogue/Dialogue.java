package bot.application.dialogue;

import bot.application.repository.CardRepository;
import bot.application.repository.ReviewRepository;
import bot.domain.Card;
import bot.domain.Deck;
import bot.domain.Review;
import java.time.Instant;
import java.util.List;
import java.util.function.LongSupplier;

public class Dialogue {
    private final Deck deck;
    private final CardRepository cardRepository;
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
        this.deck = deck;
        this.cardRepository = cardRepository;
        this.reviewRepository = reviewRepository;

        this.reviewIdGenerator = reviewIdGenerator;

        this.cards = cardRepository.findByDeckId(deck.getId());
    }

    public void start() {
        if (currentState != DialogueState.NOT_STARTED) {
            throw new IllegalStateException("Сессия уже началась");
        System.out.println("Добро пожаловать в Recall Bot!");
        help();
        toNextCard();
    }

    public void help() {
        System.out.println("""
                Правила тренировки:
                1. Прочитайте английское слово.
                2. Попробуйте вспомнить перевод.
                3. Нажмите Enter, чтобы открыть ответ.
                4. Оцените свой ответ:
                   1 — AGAIN
                   2 — HARD
                   3 — GOOD
                   4 — EASY
                """);
    }

    public void flipCard() {
        if (currentState != DialogueState.WAITING_FOR_FLIP) {
            throw new IllegalStateException("Сейчас нельзя перевернуть карточку");
        }
        System.out.println("Перевод: " + currentCard.getBack());
        currentState = DialogueState.WAITING_FOR_GRADE;
        showGradeOptions();
    }

    private void showGradeOptions() {
        System.out.println("""
                Оцените, насколько хорошо вы вспомнили ответ:
                1 — AGAIN
                2 — HARD
                3 — GOOD
                4 — EASY
                """);
    }

    public void processGrade(Review.ReviewResult result) {
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
        toNextCard();
    }

    private void toNextCard() {
        if (currentCardIndex >= cards.size()) {
            currentCard = null;
            currentState = DialogueState.FINISHED;
            System.out.println("Тренировка завершена!");
            return;
        }
        currentCard = cards.get(currentCardIndex);
        currentCardIndex++;
        currentState = DialogueState.WAITING_FOR_FLIP;
        System.out.println("Карточка " + currentCardIndex + " из " + cards.size());
        System.out.println("Слово: " + currentCard.getFront());
        System.out.println("Нажмите Enter, чтобы открыть ответ.");
    }
}