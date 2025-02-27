import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class NewBot extends TelegramLongPollingBot {
    private final Map<Long, Integer> gameData = new HashMap<>();
    private final Random random = new Random();

    public static void main(String[] args) throws TelegramApiException {
        TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
        botsApi.registerBot(new NewBot());
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            long chatId = update.getMessage().getChatId();
            String messageText = update.getMessage().getText();
            String response;

            if (messageText.equalsIgnoreCase("/start")) {
                int targetNumber = random.nextInt(100) + 1;
                gameData.put(chatId, targetNumber);
                response = "Я загадл число 1 до 100 не угадаешь удалю виндовс";
            } else if (gameData.containsKey(chatId)) {
                try {
                    int guess = Integer.parseInt(messageText);
                    int targetNumber = gameData.get(chatId);
                    if (guess == targetNumber) {
                        response = "Тебе повело ";
                        gameData.remove(chatId);
                    } else if (guess < targetNumber) {
                        response = "Я тебя прощяю число больше";
                    } else {
                        response = "Я тебя прощяю число меньше";
                    }
                } catch (NumberFormatException e) {
                    response = "Введи число чтоб я удалил тебе виндовс";
                }
            } else {
                response = "Кароч я обиделса";
            }

            sendTextMessage(chatId, response);
        }
    }

    private void sendTextMessage(long chatId, String text) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(text);
        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getBotUsername() {
        return "MyFirtsBotForJava";
    }

    @Override
    public String getBotToken() {
        return "7868973521:AAFbf-n0W6MUt6C6rG_FAMcjJsDcNsX1n0E";
    }
}
