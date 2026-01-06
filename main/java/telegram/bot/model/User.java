package telegram.bot.model;

public class User {
    public final long userId;
    public String chatStatus;

    public User(Long userId) {
        this.userId = userId;
        this.chatStatus = "Idle";
    }

    public void changeStatus(String status) {
        chatStatus = status != null ? status : "Idle";
    }
}
