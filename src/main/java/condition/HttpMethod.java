package condition;

import com.google.common.collect.Maps;

import java.util.Map;

/**
 * Created by silvereyes10 on 2016-11-28.
 */
public enum HttpMethod {
    GET,
    POST;

    private static Map<String, HttpMethod> httpMethodMap = Maps.newHashMap();

    static {
        httpMethodMap.put("GET", GET);
        httpMethodMap.put("POST", POST);
    }

    public boolean isPost() {
        return this == POST;
    }

    public boolean isGet() {
        return this == GET;
    }
}
