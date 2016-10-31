package db;

import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class DataBase {
    private static Logger log = LoggerFactory.getLogger(DataBase.class);
    private static Map<String, User> users = new HashMap() {
        {
            put("admin", new User("admin", "1234", "administrator", "admin@test.com"));
        }
    };

    public static void addUser(User user) {
        users.put(user.getUserId(), user);
        log.info("[DataBase] UserId({}) is inserted.", user.getUserId());
    }

    public static User findUserById(String userId) {
        return users.get(userId);
    }

    public static Collection<User> findAll() {
        return users.values();
    }
}
