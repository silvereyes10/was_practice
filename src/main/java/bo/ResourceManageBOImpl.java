package bo;

import model.HttpResponseEnum;
import model.ResponseData;
import org.apache.commons.collections.MapUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Map;

/**
 * Created by silvereyes10 on 2016-10-31.
 */
public class ResourceManageBOImpl {
    public ResponseData getResource(Map<String, String> parsingMap) throws IOException {
        String url = MapUtils.getString(parsingMap, "Url");
        ResponseData data = new ResponseData(url);

        byte[] body = Files.readAllBytes(new File("./webapp" + url).toPath());
        data.setResponseBody(new String(body));
        data.setResponseStatus(HttpResponseEnum.STATUS_200.name());

        return data;
    }
}
