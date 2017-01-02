package manage;

import com.google.common.collect.Maps;
import controller.*;
import org.apache.commons.collections.MapUtils;

import java.util.Map;

/**
 * Created by silvereyes10 on 2016-11-14.
 */
public class UrlManager {
    private static Map<String, Controller> URL_MANAGE_MAP;
    static {
        URL_MANAGE_MAP = Maps.newHashMap();
        URL_MANAGE_MAP.put("/user/create", new CreateUserController());
        URL_MANAGE_MAP.put("/user/login", new LoginUserController());
        URL_MANAGE_MAP.put("/user/list", new ListUserController());
    }

    public static <T extends Controller> T getController(String url) {
        return (T) MapUtils.getObject(URL_MANAGE_MAP, url, new ResourceManageController());
    }
}
