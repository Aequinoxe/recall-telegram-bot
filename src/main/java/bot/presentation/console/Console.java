package bot.presentation.console;

import bot.application.Dialogue;
import bot.domain.Card;
import static bot.domain.Review.ReviewResult.*;

import java.util.Locale;
import java.util.Scanner;
import java.util.Optional;

public class Console {

    private final Dialogue dialogue;
    private final Scanner scanner = new Scanner(System.in);

    private Optional<Card> currentCard = Optional.empty();
    private boolean isFlipped = false;
    private String currentAnswer = "";

    public Console(Dialogue dialogue) {
        this.dialogue = dialogue;
    }

    private void showCard(Card card) {
        System.out.println("Слово: " + card.getFront());
        System.out.println("Нажмите Enter, чтобы открыть ответ.");
    }

    private void showNextCard(Optional<Card> card) {
        currentCard = card;
        isFlipped = false;
        currentAnswer = "";
        if (card.isPresent()) {
            showCard(card.get());
        } else {
            System.out.println(ConsoleMessages.TRAINING_FINISHED);
        }
    }

    public void run() {
        System.out.println(ConsoleMessages.WELCOME);
        System.out.println(ConsoleMessages.HELP);

        while (true) {

            System.out.print("> ");

            if (!scanner.hasNextLine()) {
                return;
            }

            String command = scanner.nextLine().trim().toLowerCase(Locale.ROOT);  //save and normalize user's input

            try {
                switch (command) {
                    case "/help" -> System.out.println(ConsoleMessages.HELP);

                    case "/continue" -> {
                        if (currentCard.isPresent()) {
                            if (!isFlipped) {
                                showCard(currentCard.get());
                            } else {
                                System.out.println("Слово: " + currentCard.get().getFront());
                                System.out.println("Перевод: " + currentAnswer);
                                System.out.println(ConsoleMessages.GRADE_OPTIONS);
                            }
                        } else {
                            System.out.println(ConsoleMessages.INVALID_ACTION);
                        }
                    }

                    case "/start" -> {
                        Optional<Card> firstCard = dialogue.start();
                        currentCard = firstCard;
                        isFlipped = false;
                        currentAnswer = "";

                        if (firstCard.isPresent()) {
                            showCard(firstCard.get());
                        } else {
                            System.out.println(ConsoleMessages.NO_CARDS);
                        }
                    }

                    case "" -> {
                        if (!isFlipped) {
                            // Если перевод ещё не открыт — открываем его
                            currentAnswer = dialogue.flipCard();
                            isFlipped = true;
                            System.out.println("Перевод: " + currentAnswer);
                            System.out.println(ConsoleMessages.GRADE_OPTIONS);
                        } else {
                            System.out.println(ConsoleMessages.GRADE_OPTIONS);
                        }
                    }


                    case "1", "again" -> showNextCard(dialogue.processGrade(AGAIN));
                    case "2", "hard" -> showNextCard(dialogue.processGrade(HARD));
                    case "3", "good" -> showNextCard(dialogue.processGrade(GOOD));
                    case "4", "easy" -> showNextCard(dialogue.processGrade(EASY));

                    case "/exit" -> {
                        System.out.println(ConsoleMessages.GOODBYE);
                        return;
                    }

                    default -> System.out.println(ConsoleMessages.UNKNOWN_COMMAND);
                }
            } catch (IllegalStateException exception) {
                System.out.println(ConsoleMessages.INVALID_ACTION);
            }
        }
    }
}
