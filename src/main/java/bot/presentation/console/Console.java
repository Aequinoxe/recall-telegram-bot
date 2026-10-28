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

    private void showNextCard(Optional<Card> cardOpt) {
        if (cardOpt.isPresent()) {
            showCard(cardOpt.get());
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
                    case "/help" -> {
                        System.out.println(ConsoleMessages.HELP);
                        if (dialogue.isActive()) {
                            System.out.println("Нажмите /continue для возвращения к карточке.");
                        }
                    }

                    case "/continue" -> {
                        if (dialogue.isActive()) {
                            // Консоль не хранит состояние, она просто запрашивает у диалога текущую карточку
                            Card card = dialogue.getCurrentCard().orElseThrow(IllegalStateException::new);

                            if (dialogue.getCurrentState() == Dialogue.DialogueState.WAITING_FOR_FLIP) {
                                showCard(card);
                            } else {
                                System.out.println("Слово: " + card.getFront());
                                System.out.println("Перевод: " + card.getBack());
                                System.out.println(ConsoleMessages.GRADE_OPTIONS);
                            }
                        } else {
                            System.out.println(ConsoleMessages.INVALID_ACTION);
                        }
                    }

                    case "/start" -> {
                        Optional<Card> firstCard = dialogue.start();
                        if (firstCard.isPresent()) {
                            showCard(firstCard.get());
                        } else {
                            System.out.println(ConsoleMessages.NO_CARDS);
                        }
                    }

                    case "" -> {
                        if (dialogue.getCurrentState() == Dialogue.DialogueState.WAITING_FOR_FLIP) {
                            String answer = dialogue.flipCard();
                            System.out.println("Перевод: " + answer);
                            System.out.println(ConsoleMessages.GRADE_OPTIONS);
                        } else if (dialogue.getCurrentState() == Dialogue.DialogueState.WAITING_FOR_GRADE) {
                            System.out.println(ConsoleMessages.GRADE_OPTIONS);
                        } else {
                            System.out.println(ConsoleMessages.INVALID_ACTION);
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
