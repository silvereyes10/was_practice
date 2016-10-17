package util;

import model.User;
import org.apache.commons.collections.MapUtils;

import java.util.Map;

/**
 * Created by silvereyes10 on 2016-10-17.
 */
public class MemberUtils {
    public static User createUserObject(String userId, String password, String name, String email) {
        return new User(userId, password, name, email);
    }

    public static User createUserObject(Map paramMap) {
        String userId = MapUtils.getString(paramMap, "userId");
        String password = MapUtils.getString(paramMap, "password");
        String name = MapUtils.getString(paramMap, "name");
        String email = MapUtils.getString(paramMap, "email");

        return new User(userId, password, name, email);
    }
}
