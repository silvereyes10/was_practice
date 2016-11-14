package manage;

import bo.ResourceManageBOImpl;
import bo.UserManageBOImpl;
import com.google.common.collect.Maps;
import org.apache.commons.collections.MapUtils;

import java.util.Map;

/**
 * Created by silvereyes10 on 2016-11-14.
 */
public class UrlManager {
    private static Map<String, UrlConnector> URL_MANAGE_MAP;
    private static UserManageBOImpl userManageBO = new UserManageBOImpl();
    private static ResourceManageBOImpl resourceManageBO = new ResourceManageBOImpl();
    {
        URL_MANAGE_MAP = Maps.newHashMap();
        URL_MANAGE_MAP.put("/user/create", getUserCreateUrlConnector());
        URL_MANAGE_MAP.put("/user/login", getUserLoginUrlConnector());
        URL_MANAGE_MAP.put("/user/list", getUserListUrlConnector());
    }

    public static UrlConnector getUrlConnector(String url) {
        return (UrlConnector) MapUtils.getObject(URL_MANAGE_MAP, url, getResourceUrlConnector());
    }

    private static UrlConnector getUserListUrlConnector() {
        return (parsingMap) -> userManageBO.list(parsingMap);
    }

    private static UrlConnector getUserCreateUrlConnector() {
        return (parsingMap) -> userManageBO.create(parsingMap);
    }

    private static UrlConnector getUserLoginUrlConnector() {
        return (parsingMap) -> userManageBO.login(parsingMap);
    }

    private static UrlConnector getResourceUrlConnector() {
        return (parsingMap) -> resourceManageBO.getResource(parsingMap);
    }
}
