package telegram.bot.service;

import telegram.bot.model.User;
import telegram.bot.storage.UserStorage;

public class UserService {
    private static final UserStorage userStorage = new UserStorage();

    public static User createUser(Long userId) {
        User user = new User(userId);
        
        return user;
    }

    public static void save(User user) {
        userStorage.save(user);
    }

    public static User findUser(Long userId) {
        return userStorage.find(userId);
    }

    public static User computeIfAbsent(Long userId) {
        return userStorage.computeIfAbsent(userId, UserService::createUser);
    }
}