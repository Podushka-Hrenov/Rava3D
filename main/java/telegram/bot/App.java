package telegram.bot;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.message.Message;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import telegram.bot.graphics.math.CFrame;
import telegram.bot.graphics.math.Vec2;
import telegram.bot.graphics.core.Camera;
import telegram.bot.graphics.core.Part;
import telegram.bot.graphics.core.Workspace;

public class App {
    public static final String token = "8222830320:AAHQlcBeeTbMYxlwyJqeJWhr0CYH_YXkR4M";
    public static TelegramBot telegramBot = TelegramBot.register(token);
    public static Camera camera = new Camera(new Vec2(1e3f, 1e3f));
    public static Workspace workspace = new Workspace();
    public static Part renderPart = new Part();
   
    public static void main(String[] args) throws IOException {
        renderPart.setParent(workspace);

        camera.settings(Math.PI/2, 10f, 100f);
        camera.setParent(workspace);
        camera.cframe = new CFrame(0, 0, -10f);

        telegramBot.listenCommand("/start", App::start, "Idle", 1L);
        telegramBot.listenInput(App::input, "/start", 0L);

        System.out.println("Telegram bot Inited!");
    }

    public static void start(Update update) {
        SendMessage sendMessage = SendMessage.builder()
            .chatId(update.getMessage().getChatId())
            .text("input rotation components").build();
        
        try {telegramBot.client.execute(sendMessage);
        } catch (TelegramApiException e) {}
    }

    public static double parseRotationComponent(String toParse) {
        try {
            double parsed = Double.parseDouble(toParse);
            return Math.toRadians(parsed);

        } catch (NumberFormatException e) {
            return 0;
        }
    }

    public static void input(Update update) {
        Message message = update.getMessage();
        if (!message.hasText()) return;
        
        double[] rotation = Arrays.stream(message.getText().split("\\s+"))
            .mapToDouble(App::parseRotationComponent).toArray();

        if (rotation.length < 3) return;

        renderPart.cframe = new CFrame(0, 0, 0,
            rotation[0], rotation[1], rotation[2]
        );
        System.out.println(renderPart.meshData());
        
        try {
            File render = camera.render();
            SendPhoto sendPhoto = SendPhoto.builder()
                .chatId(message.getChatId())
                .photo(new InputFile(render))
                .build();
            
            telegramBot.client.execute(sendPhoto);
        } catch (IOException | TelegramApiException e) {
            e.printStackTrace();
        }
    }
}