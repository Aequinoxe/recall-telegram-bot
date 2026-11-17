package bot.application;

import bot.application.repository.CardRepository;
import bot.application.repository.ReviewRepository;
import bot.domain.Card;
import bot.domain.Deck;
import bot.domain.Review;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.LongSupplier;

public class ManagerDialogue {
    private final Deck deck;
    private final CardRepository cardRepository;
    private final ReviewRepository reviewRepository;
    private final LongSupplier reviewIdGenerator;

    private final Map<Long, Dialogue> dialogues = new ConcurrentHashMap<>();

    public ManagerDialogue(Deck deck, CardRepository cardRepository, ReviewRepository reviewRepository, LongSupplier reviewIdGenerator) {
        this.deck = deck;
        this.cardRepository = cardRepository;
        this.reviewRepository = reviewRepository;
        this.reviewIdGenerator = reviewIdGenerator;
    }

    public String handleMessage(Long chatId, String text) {
        String command = text.trim().toLowerCase();

        if (command.equals("/help")) {
            String helpText = """
                    Правила тренировки:
                    1. Прочитай слово и вспомни перевод.
                    2. Нажми /flip, чтобы открыть ответ.
                    3. Оцени себя:
                       1/AGAIN — не вспомнил
                       2/HARD — с трудом
                       3/GOOD — вспомнил
                       4/EASY — легко

                    Команды:
                    /start — начать тренировку
                    /flip — открыть ответ
                    /help — показать эту справку
                    /continue — продолжить работу
                    /exit — завершить работу
                    """;
            Dialogue current = dialogues.get(chatId);
            if (current != null && current.isActive()) {
                return helpText + "\nНажми /continue для возвращения к карточке.";
            }
            return helpText;
        }

        if (command.equals("/start")) {
            Dialogue newDialogue = new Dialogue(deck, cardRepository, reviewRepository, reviewIdGenerator);
            dialogues.put(chatId, newDialogue);

            Optional<Card> firstCard = newDialogue.start();
            if (firstCard.isPresent()) {
                return "Слово: " + firstCard.get().getFront() + "\nНажми /flip, чтобы открыть ответ.";
            } else {
                return "В этой колоде пока нет карточек.";
            }
        }

        Dialogue dialogue = dialogues.get(chatId);
        if (dialogue == null) {
            return "Сначала введи /start, чтобы начать тренировку.";
        }

        if (command.equals("/exit")) {
            dialogues.remove(chatId);
            return "Тренировка завершена! Спасибо за занятие и до встречи!";
        }

        try {
            if (command.equals("/continue")) {
                if (!dialogue.isActive()) {
                    return "Сейчас нет активной тренировки. Введи /start.";
                }
                Card card = dialogue.getCurrentCard().orElseThrow(IllegalStateException::new);
                if (dialogue.getCurrentState() == Dialogue.DialogueState.WAITING_FOR_FLIP) {
                    return "Слово: " + card.getFront() + "\nНажми /flip, чтобы открыть ответ.";
                } else {
                    return "Слово: " + card.getFront() + "\nПеревод: " + card.getBack() + "\nОцени: 1 (again), 2 (hard), 3 (good), 4 (easy)";
                }
            }

            if (command.equals("/flip")) {
                String answer = dialogue.flipCard();
                return "Перевод: " + answer + "\nОцени: 1 (again), 2 (hard), 3 (good), 4 (easy)";
            }

            Review.ReviewResult grade = switch (command) {
                case "1", "again" -> Review.ReviewResult.AGAIN;
                case "2", "hard" -> Review.ReviewResult.HARD;
                case "3", "good" -> Review.ReviewResult.GOOD;
                case "4", "easy" -> Review.ReviewResult.EASY;
                default -> null;
            };

            if (grade != null) {
                Optional<Card> nextCard = dialogue.processGrade(grade);
                if (nextCard.isPresent()) {
                    return "Слово: " + nextCard.get().getFront() + "\nНажми /flip, чтобы открыть ответ.";
                } else {
                    dialogues.remove(chatId);
                    return "Тренировка завершена! Введи /start, чтобы пройти еще раз.";
                }
            }

            return "Неизвестная команда. Введи /help для вызова списка доступных команд.";

        } catch (IllegalStateException e) {
            return "Сейчас это действие недоступно. Введи /help, чтобы узнать доступные команды.";
        }
    }
}
