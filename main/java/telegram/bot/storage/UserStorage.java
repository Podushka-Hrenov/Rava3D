package telegram.bot.storage;

import telegram.bot.model.User;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

public class UserStorage {
    private final Map<Long, User> storage;

    public UserStorage() {
        storage = new ConcurrentHashMap<>();
    }

    public void save(User user) {
        storage.put(user.userId, user);
    }

    public User find(Long userId) {
        return storage.get(userId);
    }

    public User computeIfAbsent(Long userId, Function<Long, User> mapper) {
        return storage.computeIfAbsent(userId, mapper);
    }
}
