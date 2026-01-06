package telegram.bot.model;

import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.function.Consumer;

public class ActionEvent {
    public final String extender, name;
    public final long cooldown;
    private final Consumer<Update> handler;

    public ActionEvent(String name, Consumer<Update> handler,
        String extender, long cooldown) {
        
        this.handler = handler; this.name = name;
        this.extender = extender; this.cooldown = cooldown;
    }

    public void once(Update update, User user) {
        if (!isExtends(user)) {return;}
        
        user.changeStatus(name);
        handler.accept(update);
    }

    public boolean isExtends(User user) {
        return (extender == null || user.chatStatus.equals(extender));
    }
}
