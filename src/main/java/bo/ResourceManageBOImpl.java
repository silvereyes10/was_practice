package bo;

import model.HttpResponse;
import model.ResponseData;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Map;

/**
 * Created by silvereyes10 on 2016-10-31.
 */
public class ResourceManageBOImpl implements RequestManageBO {
    @Override
    public ResponseData process(String url, Map<String, String> parsingMap) throws IOException {
        ResponseData data = new ResponseData(url);

        byte[] body = Files.readAllBytes(new File("./webapp" + url).toPath());
        data.setResponseBody(new String(body));
        data.setResponseStatus(HttpResponse.STATUS_200.name());

        return data;
    }
}
