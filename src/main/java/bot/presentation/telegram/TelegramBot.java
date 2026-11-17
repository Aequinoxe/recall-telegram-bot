package bot.presentation.telegram;

import bot.application.ManagerDialogue;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class TelegramBot extends TelegramLongPollingBot {

    private final String botUsername;
    private final ManagerDialogue managerDialogue;

    public TelegramBot(String botToken, String botUsername, ManagerDialogue managerDialogue) {
        super(botToken);
        this.botUsername = botUsername;
        this.managerDialogue = managerDialogue;
    }

    @Override
    public String getBotUsername() {
        return botUsername;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            Long chatId = update.getMessage().getChatId();
            String incomingText = update.getMessage().getText();

            // Передаем сообщение в ManagerDialogue
            String responseText = managerDialogue.handleMessage(chatId, incomingText);

            sendReply(chatId, responseText);
        }
    }

    private void sendReply(Long chatId, String text) {
        if (text == null || text.isBlank()) {
            return;
        }
        SendMessage message = new SendMessage();
        message.setChatId(chatId.toString());
        message.setText(text);

        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
