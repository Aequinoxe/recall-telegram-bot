package bot.application;

import bot.application.Dialogue;
import bot.application.repository.CardRepository;
import bot.application.repository.ReviewRepository;
import bot.domain.Card;
import bot.domain.Deck;
import bot.infrastructure.repository.InMemoryCardRepository;
import bot.infrastructure.repository.InMemoryReviewRepository;
import bot.presentation.console.Console;

import java.util.List;

public class RecallBotApplication {

    private RecallBotApplication() {
    }

    public static void main(String[] args) {
        Deck deck = new Deck(1L, "English", 1L);

        List<Card> cards = List.of(
                new Card(1L, 1L, "apple", "яблоко"),
                new Card(2L, 1L, "book", "книга"),
                new Card(3L, 1L, "house", "дом"),
                new Card(4L, 1L, "equation", "уравнение")
        );

        CardRepository cardRepository =
                new InMemoryCardRepository(cards);

        ReviewRepository reviewRepository =
                new InMemoryReviewRepository();

        IdGenerator idGenerator = new IdGenerator();

        Dialogue dialogue = new Dialogue(
                deck,
                cardRepository,
                reviewRepository,
                idGenerator::getNextId
        );

        Console console = new Console(dialogue);
        console.run();
    }
}
