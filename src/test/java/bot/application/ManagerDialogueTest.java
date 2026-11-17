package bot.application;

import bot.application.repository.CardRepository;
import bot.application.repository.ReviewRepository;
import bot.domain.Card;
import bot.domain.Deck;
import bot.infrastructure.repository.InMemoryCardRepository;
import bot.infrastructure.repository.InMemoryReviewRepository;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ManagerDialogueTest {
    @Test
    void shouldIsolateTwoUsersDialogues() {

        Deck deck = new Deck(1L, "English", 1L);
        List<Card> cards = List.of(
                new Card(1L, 1L, "apple", "яблоко"),
                new Card(2L, 1L, "book", "книга")
        );

        CardRepository cardRepository = new InMemoryCardRepository(cards);
        ReviewRepository reviewRepository = new InMemoryReviewRepository();
        IdGenerator idGenerator = new IdGenerator();

        ManagerDialogue manager = new ManagerDialogue(
                deck, cardRepository, reviewRepository, idGenerator::getNextId
        );

        Long user1 = 100L; // Вася
        Long user2 = 200L; // Петя

        String replyUser1 = manager.handleMessage(user1, "/start");
        assertTrue(replyUser1.contains("apple"));

        manager.handleMessage(user1, "/flip");
        String nextCardUser1 = manager.handleMessage(user1, "3");
        assertTrue(nextCardUser1.contains("book"));

        String replyUser2 = manager.handleMessage(user2, "/start");
        assertTrue(replyUser2.contains("apple"));
    }

}