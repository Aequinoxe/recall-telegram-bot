package bot.application;

import bot.application.Dialogue;
import bot.application.repository.CardRepository;
import bot.application.repository.ReviewRepository;
import bot.domain.Card;
import bot.domain.Deck;
import bot.infrastructure.repository.InMemoryCardRepository;
import bot.infrastructure.repository.InMemoryReviewRepository;
import bot.presentation.console.Console;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import bot.presentation.telegram.TelegramBot;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

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

        if (args.length > 0 && args[0].equalsIgnoreCase("console")) {
            Dialogue dialogue = new Dialogue(
                    deck,
                    cardRepository,
                    reviewRepository,
                    idGenerator::getNextId
            );

            Console console = new Console(dialogue);
            console.run();
        } else {
            ManagerDialogue managerDialogue = new ManagerDialogue(
                    deck,
                    cardRepository,
                    reviewRepository,
                    idGenerator::getNextId
            );
            // потом задать тут токен
            String botToken = System.getenv().getOrDefault("BOT_TOKEN", "YOUR_BOT_TOKEN_HERE");
            String botUsername = System.getenv().getOrDefault("BOT_USERNAME", "YOUR_BOT_USERNAME_HERE");

            try {
                TelegramBotsApi botsApi =
                        new TelegramBotsApi(DefaultBotSession.class);
                botsApi.registerBot(new TelegramBot(botToken, botUsername, managerDialogue));
                System.out.println("Telegram-бот успешно запущен!");
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
        }
}
