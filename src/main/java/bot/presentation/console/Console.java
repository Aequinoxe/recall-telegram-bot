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

    public Console(Dialogue dialogue) {
        this.dialogue = dialogue;
    }

    private void showCard(Card card) {
        System.out.println("Слово: " + card.getFront());
        System.out.println("Нажмите Enter, чтобы открыть ответ.");
    }

    private void showNextCard(Optional<Card> card) {
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

                    case "/start" -> {
                        Optional<Card> firstCard = dialogue.start();

                        if (firstCard.isPresent()) {
                            showCard(firstCard.get());
                        } else {
                            System.out.println(ConsoleMessages.NO_CARDS);
                        }
                    }

                    case "" -> {
                        String answer = dialogue.flipCard();
                        System.out.println("Перевод: " + answer);
                        System.out.println(ConsoleMessages.GRADE_OPTIONS);
                    }

                    case "again" -> showNextCard(dialogue.processGrade(AGAIN));
                    case "hard" -> showNextCard(dialogue.processGrade(HARD));
                    case "good" -> showNextCard(dialogue.processGrade(GOOD));
                    case "easy" -> showNextCard(dialogue.processGrade(EASY));


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
