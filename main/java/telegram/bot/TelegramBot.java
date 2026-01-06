package telegram.bot;

import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.longpolling.TelegramBotsLongPollingApplication;
import org.telegram.telegrambots.longpolling.interfaces.LongPollingUpdateConsumer;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.objects.Document;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.message.Message;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import telegram.bot.model.ActionEvent;
import telegram.bot.model.User;
import telegram.bot.service.UserService;

import java.util.List;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class TelegramBot implements LongPollingUpdateConsumer {
    private final Map<String, ActionEvent> commandHandlers;
    private final List<ActionEvent> inputHandlers;

    public final String token;
    public final OkHttpTelegramClient client;

    private TelegramBot(String token) {
        client = new OkHttpTelegramClient(token);
        commandHandlers = new HashMap<>();
        inputHandlers = new ArrayList<>();

        this.token = token;
    }

    public static final TelegramBot register(String token) {
        TelegramBotsLongPollingApplication botApplication = new TelegramBotsLongPollingApplication();
        TelegramBot telegramBot = new TelegramBot(token);

        System.out.println("Telegram bot requests long polling application!");
       
        try {
            botApplication.registerBot(token, telegramBot);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }

        return telegramBot;
    }

    @Override
    public void consume(List<Update> updates) {
        System.out.println("new update!");

        for (Update update : updates) {
            try {
                handleUpdate(update);
            } catch(Exception e){e.printStackTrace();}
        }
    }

    private void handleUpdate(Update update) {
        if (!update.hasMessage()) return;
        Message message = update.getMessage();

        Long userId = message.getFrom().getId();
        User user = UserService.computeIfAbsent(userId);

        ActionEvent actionEvent = null;

        String text;
        if (message.hasText() && (text = message.getText()).startsWith("/")) {
            String command = text.substring(0, (text+" ").indexOf(" "));
            actionEvent = commandHandlers.get(command);

            if (actionEvent != null) actionEvent.once(update, user); return;
        }

        for (ActionEvent inputHandler : inputHandlers) {
            if (!inputHandler.isExtends(user)) continue;
            actionEvent = inputHandler; break;
        }

        if (actionEvent != null) actionEvent.once(update, user); return;
    }

    public void listenCommand(String name, Consumer<Update> handler,
        String extender, long cooldown) {
        
        commandHandlers.put(name, new ActionEvent(name, handler, extender, cooldown));
    }

    public void listenInput(Consumer<Update> handler,
        String extender, long cooldown) {
        
        inputHandlers.add(new ActionEvent(null, handler, extender, cooldown));
    }

    public File downloadFile(Document document) throws TelegramApiException {
        GetFile getFile = new GetFile(document.getFileId());
        String filePath = client.execute(getFile).getFilePath();
        
        return client.downloadFile(filePath);
    }
}