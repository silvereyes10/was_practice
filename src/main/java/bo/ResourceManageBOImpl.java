package bo;

import constant.HttpConstants;
import model.HttpRequest;
import model.ResponseData;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

/**
 * Created by silvereyes10 on 2016-10-31.
 */
public class ResourceManageBOImpl implements ResourceManageBO {
    @Override
    public ResponseData getResource(HttpRequest request) throws IOException {
        String requestUrl = request.getPath().equals("/") ? "/index.html" : request.getPath();
        ResponseData data = new ResponseData(requestUrl);

        byte[] body = Files.readAllBytes(new File("./webapp" + requestUrl).toPath());
        data.setResponseBody(new String(body));
        data.setContentLength(body.length);
        data.setResponseStatus(HttpConstants.HTTP_STATUS_200);

        return data;
    }
}
