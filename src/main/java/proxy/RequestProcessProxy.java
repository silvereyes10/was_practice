package proxy;

import bo.RequestManageBO;
import bo.ResourceManageBOImpl;
import bo.UserManageBOImpl;
import model.ResponseData;

import java.io.IOException;
import java.util.Map;

/**
 * Created by silvereyes10 on 2016-10-31.
 */
public class RequestProcessProxy {
    private RequestManageBO userManageBO;
    private RequestManageBO resourceManageBO;

    public RequestProcessProxy() {
        this.userManageBO = new UserManageBOImpl();
        this.resourceManageBO = new ResourceManageBOImpl();
    }

    public ResponseData process(String url, Map<String, String> parsingMap) throws IOException {
        ResponseData data;
        if (url.contains("/user")) {
            data = userManageBO.process(url, parsingMap);
        } else {
            data = resourceManageBO.process(url, parsingMap);
        }

        return data;
    }
}
